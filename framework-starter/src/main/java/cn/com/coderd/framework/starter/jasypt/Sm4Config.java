package cn.com.coderd.framework.starter.jasypt;

import lombok.Data;
import org.jasypt.commons.CommonUtils;
import org.springframework.util.Base64Utils;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

@Data
public class Sm4Config {

    private String sk;
    private String iv;

    private String sk1;
    private String sk2;

    private String iv1;
    private String iv2;

    public SecretKey loadSecretKey() {
        if (this.sk != null && !this.sk.isEmpty()) {
            return new SecretKeySpec(Base64Utils.decodeFromString(sk), "SM4");
        } else {
            return new SecretKeySpec(CommonUtils.appendArrays(Base64Utils.decodeFromString(this.sk1),
                    Base64Utils.decodeFromString(this.sk2)), "SM4");
        }
    }

    public IvParameterSpec loadIvParameter() {
        if (this.iv != null && this.iv.isEmpty()) {
            return new IvParameterSpec(Base64Utils.decodeFromString(this.iv));
        } else {
            return new IvParameterSpec(CommonUtils.appendArrays(Base64Utils.decodeFromString(this.iv1),
                    Base64Utils.decodeFromString(this.iv2)));
        }
    }
}
