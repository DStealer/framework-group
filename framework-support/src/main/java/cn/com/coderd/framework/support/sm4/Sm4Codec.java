package cn.com.coderd.framework.support.sm4;

import lombok.SneakyThrows;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Security;
import java.util.Base64;

public class Sm4Codec {

    private static final String ALGORITHM = "SM4/CBC/PKCS5PADDING";

    static {
        if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
            Security.addProvider(new BouncyCastleProvider());
        }
    }

    /**
     * sm4 加密
     *
     * @param key
     * @param iv
     * @param plainText
     * @return
     */
    @SneakyThrows
    public static String encrypt(String key, String iv, String plainText) {
        Cipher sm4 = Cipher.getInstance(ALGORITHM, BouncyCastleProvider.PROVIDER_NAME);
        sm4.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(Base64.getDecoder().decode(key), "SM4"),
                new IvParameterSpec(Base64.getDecoder().decode(iv)));
        return Base64.getEncoder().encodeToString(sm4.doFinal(plainText.getBytes(StandardCharsets.UTF_8)));
    }

    /**
     * sm4解密
     *
     * @param key
     * @param iv
     * @param cryptText
     * @return
     */
    @SneakyThrows
    public static String decrypt(String key, String iv, String cryptText) {
        Cipher sm4 = Cipher.getInstance(ALGORITHM, BouncyCastleProvider.PROVIDER_NAME);
        sm4.init(Cipher.DECRYPT_MODE, new SecretKeySpec(Base64.getDecoder().decode(key), "SM4"),
                new IvParameterSpec(Base64.getDecoder().decode(iv)));
        return new String(sm4.doFinal(Base64.getDecoder().decode(cryptText)));
    }
}
