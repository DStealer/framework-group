package cn.com.coderd.framework.gateway.config;

import lombok.Data;

import java.io.Serializable;

/**
 * 密钥对实体
 */
@Data
public class KeySpec implements Serializable {
    private transient String privateKey;
    private transient String publicKey;
}
