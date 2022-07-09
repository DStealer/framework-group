package cn.com.coderd.framework.starter.jasypt;

import com.ulisesbocchio.jasyptspringboot.util.Singleton;
import lombok.SneakyThrows;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.jasypt.encryption.ByteEncryptor;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.security.Security;

/**
 * jasypt sm4实现
 */
public class Sm4ByteEncryptor implements ByteEncryptor {

    private static final String ALGORITHM = "SM4/CBC/PKCS5PADDING";

    static {
        if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
            Security.addProvider(new BouncyCastleProvider());
        }
    }

    private final Singleton<SecretKey> key;
    private final Singleton<IvParameterSpec> ivParameter;

    public Sm4ByteEncryptor(Sm4Config sm4Config) {
        this.key = Singleton.from(sm4Config::loadSecretKey);
        this.ivParameter = Singleton.from(sm4Config::loadIvParameter);
    }

    @SneakyThrows
    @Override
    public byte[] encrypt(byte[] bytes) {
        Cipher sm4 = Cipher.getInstance(ALGORITHM, BouncyCastleProvider.PROVIDER_NAME);
        sm4.init(Cipher.ENCRYPT_MODE, key.get(), ivParameter.get());
        return sm4.doFinal(bytes);
    }

    @SneakyThrows
    @Override
    public byte[] decrypt(byte[] bytes) {
        Cipher sm4 = Cipher.getInstance(ALGORITHM, BouncyCastleProvider.PROVIDER_NAME);
        sm4.init(Cipher.DECRYPT_MODE, key.get(), ivParameter.get());
        return sm4.doFinal(bytes);
    }
}
