package cn.com.coderd.framework.gateway.codec;

import cn.com.coderd.framework.gateway.config.KeySpec;
import cn.com.coderd.framework.gateway.constants.MediaTypeConstants;
import cn.com.coderd.framework.gateway.utils.ComponentUtils;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
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
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * jsonm消息转换实现,该加密使用对称加密加密消息体,使用非对称加密加密密钥,适用请求和响应体较大的情况,典型场景 web端请求
 */
@Slf4j
public class JsonmMessageEncryptor extends MessageEncryptor {
    private static final String AES_ALGO = "AES";
    private static final String RSA_ALGO = "RSA";
    private final ObjectMapper mapper = JsonMapper.builder()
            .serializationInclusion(JsonInclude.Include.NON_NULL)
            .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
            .build();
    private transient final Map<String, KeyPair> keySpecs;

    public JsonmMessageEncryptor(Map<String, KeySpec> keySpecs) {
        Assert.notNull(keySpecs, "密钥不可为null");
        this.keySpecs = this.init(keySpecs);
    }

    private Map<String, KeyPair> init(Map<String, KeySpec> keySpecs) {
        Map<String, KeyPair> keyPairMap = new HashMap<>(keySpecs.size());
        try {
            for (Map.Entry<String, KeySpec> entry : keySpecs.entrySet()) {
                KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGO);
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
        return MediaTypeConstants.MEDIA_TYPE_JSONM.isCompatibleWith(headers.getContentType());
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
            Charset charset = ComponentUtils.getCharsetOrUTF8(exchange.getResponse().getHeaders());
            String message = buffer.toString(charset);
            log.info("write:{} token:{} body:{}", exchange.getRequest().getURI().getPath(), exchange.getLogPrefix(), message);
            byte[] aesKeyBytes = ComponentUtils.randomString(16).getBytes(StandardCharsets.UTF_8);
            Cipher aesCipher = Cipher.getInstance(AES_ALGO);
            SecretKey secretKey = new SecretKeySpec(aesKeyBytes, 0, aesKeyBytes.length, AES_ALGO);
            aesCipher.init(Cipher.ENCRYPT_MODE, secretKey);
            String data = Base64.toBase64String(aesCipher.doFinal(message.getBytes(charset)));

            Cipher rsaCipher = Cipher.getInstance(RSA_ALGO);
            rsaCipher.init(Cipher.ENCRYPT_MODE, this.keySpecs.get("default").getPublic());
            String sign = Base64.toBase64String(rsaCipher.doFinal(aesKeyBytes));

            ObjectNode node = this.mapper.createObjectNode().put("data", data).put("sign", sign);
            return exchange.getResponse().bufferFactory().wrap(mapper.writeValueAsString(node).getBytes(charset));
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
            JsonNode node = this.mapper.readTree(msg);
            Cipher rsaCipher = Cipher.getInstance(RSA_ALGO);
            rsaCipher.init(Cipher.DECRYPT_MODE, this.keySpecs.get("default").getPrivate());
            byte[] aesKeyBytes = rsaCipher.doFinal(Base64.decode(node.required("sign").asText()));
            Cipher cipher = Cipher.getInstance(AES_ALGO);
            SecretKey secretKey = new SecretKeySpec(aesKeyBytes, 0, aesKeyBytes.length, AES_ALGO);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] bytes = cipher.doFinal(Base64.decode(node.required("data").asText()));
            log.info("read:{} token:{} body:{}", exchange.getRequest().getURI().getPath(), exchange.getLogPrefix(),
                    new String(bytes, charset));
            return exchange.getResponse().bufferFactory().wrap(bytes);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "解密失败", e);
        } finally {
            DataBufferUtils.release(buffer);
        }
    }
}
