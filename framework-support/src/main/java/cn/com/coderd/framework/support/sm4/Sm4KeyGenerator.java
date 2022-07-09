package cn.com.coderd.framework.support.sm4;

import lombok.SneakyThrows;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.KeyGenerator;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Arrays;
import java.util.Base64;

public class Sm4KeyGenerator {
    static {
        if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
            Security.addProvider(new BouncyCastleProvider());
        }
    }

    /**
     * 生成密钥
     *
     * @param keySize
     * @return
     */
    @SneakyThrows
    public static String generateKey(int keySize) {
        KeyGenerator kg = KeyGenerator.getInstance("SM4", BouncyCastleProvider.PROVIDER_NAME);
        kg.init(keySize, new SecureRandom());
        return Base64.getEncoder().encodeToString(kg.generateKey().getEncoded());
    }

    /**
     * 生成密钥
     *
     * @param keySize
     * @return
     */
    @SneakyThrows
    public static String[] generateKeyPart(int keySize) {
        KeyGenerator kg = KeyGenerator.getInstance("SM4", BouncyCastleProvider.PROVIDER_NAME);
        kg.init(keySize, new SecureRandom());
        byte[] encoded = kg.generateKey().getEncoded();
        String[] rst = new String[2];
        rst[0] = Base64.getEncoder().encodeToString(Arrays.copyOfRange(encoded, 0, 8));
        rst[1] = Base64.getEncoder().encodeToString(Arrays.copyOfRange(encoded, 8, encoded.length));
        return rst;
    }

    /**
     * 生成向量
     *
     * @param len
     * @return
     */
    @SneakyThrows
    public static String generateIv(int len) {
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        final byte[] iv = new byte[len];
        random.nextBytes(iv);
        return Base64.getEncoder().encodeToString(iv);
    }

    /**
     * 生成向量
     *
     * @param len
     * @return
     */
    @SneakyThrows
    public static String[] generateIvPart(int len) {
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        byte[] iv = new byte[len];
        random.nextBytes(iv);
        String[] rst = new String[2];
        rst[0] = Base64.getEncoder().encodeToString(Arrays.copyOfRange(iv, 0, 8));
        rst[1] = Base64.getEncoder().encodeToString(Arrays.copyOfRange(iv, 8, iv.length));
        return rst;
    }

    /**
     * 合并密钥或向量
     *
     * @param pt1
     * @param pt2
     * @return
     */
    public static String mergeKeyOrIv(String pt1, String pt2) {
        byte[] bts1 = Base64.getDecoder().decode(pt1);
        byte[] bts2 = Base64.getDecoder().decode(pt2);
        byte[] result = new byte[bts1.length + bts2.length];
        System.arraycopy(bts1, 0, result, 0, bts1.length);
        System.arraycopy(bts2, 0, result, bts1.length, bts2.length);
        return Base64.getEncoder().encodeToString(result);
    }

    /**
     * 拆分密钥或向量
     *
     * @param pt
     * @return
     */
    public static String[] splitKeyOrIv(String pt) {
        byte[] bts = Base64.getDecoder().decode(pt);
        String[] rst = new String[2];
        rst[0] = Base64.getEncoder().encodeToString(Arrays.copyOfRange(bts, 0, 8));
        rst[1] = Base64.getEncoder().encodeToString(Arrays.copyOfRange(bts, 8, bts.length));
        return rst;
    }
}
