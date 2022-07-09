package cn.com.coderd.framework.gateway.codec;

import cn.com.coderd.framework.gateway.config.KeySpec;
import cn.com.coderd.framework.gateway.constants.MediaTypeConstants;
import cn.com.coderd.framework.gateway.utils.ComponentUtils;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;

import javax.crypto.Cipher;
import java.nio.charset.Charset;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * jsonx消息转换实现,适用场景 使用非对称加密,适用用于请求和响应数据量不大的情况,典型场景 app端请求
 */
@Slf4j
public class JsonxMessageEncryptor extends MessageEncryptor {
    private static final String ALGO = "RSA";
    private transient final Map<String, KeyPair> keySpecs;

    public JsonxMessageEncryptor(Map<String, KeySpec> keySpecs) {
        Assert.notNull(keySpecs, "密钥不可为null");
        this.keySpecs = this.init(keySpecs);
    }

    private Map<String, KeyPair> init(Map<String, KeySpec> keySpecs) {
        Map<String, KeyPair> keyPairMap = new HashMap<>(keySpecs.size());
        try {
            for (Map.Entry<String, KeySpec> entry : keySpecs.entrySet()) {
                KeyFactory keyFactory = KeyFactory.getInstance(ALGO);
                X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(Base64.decode(entry.getValue().getPublicKey()));
                PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
                PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(Base64.decode(entry.getValue().getPrivateKey()));
                PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);
                keyPairMap.put(entry.getKey(), new KeyPair(publicKey, privateKey));
            }
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        } finally {
            keySpecs.clear();
        }
        return Collections.unmodifiableMap(keyPairMap);
    }

    @Override
    public boolean supportRequest(HttpHeaders headers) {
        return MediaTypeConstants.MEDIA_TYPE_JSONX.isCompatibleWith(headers.getContentType());
    }

    @Override
    public HttpHeaders filterRequest(HttpHeaders headers) {
        HttpHeaders filtered = new HttpHeaders();
        headers.entrySet().stream()
                .filter(e -> !HttpHeaders.CONTENT_LENGTH.equals(e.getKey()))
                .filter(e -> !HttpHeaders.CONTENT_TYPE.equals(e.getKey()))
                .forEach(e -> filtered.put(e.getKey(), e.getValue()));
        filtered.setContentType(MediaType.APPLICATION_JSON);
        return filtered;
    }

    @Override
    public boolean supportResponse(HttpHeaders headers) {
        return MediaType.APPLICATION_JSON.isCompatibleWith(headers.getContentType());
    }

    @Override
    public HttpHeaders filterResponse(HttpHeaders headers) {
        HttpHeaders filtered = new HttpHeaders();
        headers.entrySet().stream()
                .filter(e -> !HttpHeaders.CONTENT_LENGTH.equals(e.getKey()))
                .filter(e -> !HttpHeaders.CONTENT_TYPE.equals(e.getKey()))
                .forEach(e -> filtered.put(e.getKey(), e.getValue()));
        filtered.setContentType(MediaTypeConstants.MEDIA_TYPE_JSONM);
        return filtered;
    }

    @Override
    public DataBuffer encrypt(ServerWebExchange exchange, DataBuffer buffer) {
        try {
            String message = buffer.toString(ComponentUtils.getCharsetOrUTF8(exchange.getResponse().getHeaders()));
            byte[] plainTextData = message.getBytes(ComponentUtils.getCharsetOrUTF8(exchange.getResponse().getHeaders()));
            log.info("write:{} token:{} body:{}", exchange.getRequest().getURI().getPath(), exchange.getLogPrefix(), message);
            // 这里可以根据客户端请特征选择密钥的逻辑,典型场景,例如密钥更换,多账号等逻辑
            PublicKey publicKey = keySpecs.get("default").getPublic();
            Cipher cipher = Cipher.getInstance(ALGO);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            DataBuffer dataBuffer = exchange.getResponse().bufferFactory().allocateBuffer();
            dataBuffer.write(cipher.doFinal(plainTextData));
            return dataBuffer;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "加密失败", e);
        } finally {
            DataBufferUtils.release(buffer);
        }
    }

    @Override
    public DataBuffer decrypt(ServerWebExchange exchange, DataBuffer buffer) {
        try {
            Charset charset = ComponentUtils.getCharsetOrUTF8(exchange.getRequest().getHeaders());
            String msg = buffer.toString(charset);
            byte[] plainTextData = msg.getBytes(charset);
            Cipher cipher = Cipher.getInstance(ALGO);
            PrivateKey privateKey = keySpecs.get("default").getPrivate();
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            DataBuffer dataBuffer = exchange.getResponse().bufferFactory().allocateBuffer();
            dataBuffer.write(cipher.doFinal(plainTextData));
            DataBuffer retainedSlice = dataBuffer.retainedSlice(0, dataBuffer.readableByteCount());
            log.info("read:{} token:{} body:{}", exchange.getRequest().getURI().getPath(),
                    exchange.getLogPrefix(), retainedSlice.toString(charset));
            DataBufferUtils.release(retainedSlice);
            return dataBuffer;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "解密失败", e);
        } finally {
            DataBufferUtils.release(buffer);
        }
    }
}
