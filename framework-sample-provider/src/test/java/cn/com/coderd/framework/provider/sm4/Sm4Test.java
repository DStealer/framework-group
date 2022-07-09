package cn.com.coderd.framework.provider.sm4;

import cn.com.coderd.framework.support.sm4.Sm4Codec;
import cn.com.coderd.framework.support.sm4.Sm4KeyGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Sm4Test {

    @Test
    void testSm4() {
        String key = Sm4KeyGenerator.generateKey(128);
        String iv = Sm4KeyGenerator.generateIv(16);
        String plainText = "abc12321312312312312";
        String encrypt = Sm4Codec.encrypt(key, iv, plainText);
        String decryptText = Sm4Codec.decrypt(key, iv, encrypt);
        Assertions.assertEquals(plainText, decryptText);

        String[] splitKey = Sm4KeyGenerator.splitKeyOrIv(key);
        String mergeKey = Sm4KeyGenerator.mergeKeyOrIv(splitKey[0], splitKey[1]);
        Assertions.assertEquals(key, mergeKey);
        String[] splitIv = Sm4KeyGenerator.splitKeyOrIv(iv);
        String mergeIv = Sm4KeyGenerator.mergeKeyOrIv(splitIv[0], splitIv[1]);
        Assertions.assertEquals(iv, mergeIv);
    }
}
