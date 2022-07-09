package cn.com.coderd.framework.starter.jasypt;

import com.ulisesbocchio.jasyptspringboot.encryptor.ByteEncryptorStringEncryptorDelegate;
import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * sm4 jasypt配置
 */
@Slf4j
@Configuration(proxyBeanMethods = false)
public class Sm4Configuration {

    /**
     * 替换jasypt默认加密bean
     *
     * @return
     */
    @Bean("jasyptStringEncryptor")
    public StringEncryptor stringEncryptor(ConfigurableEnvironment environment) {
        Sm4Config config = new Sm4Config();
        if (environment.containsProperty("sm4.sk")) {
            config.setSk(environment.resolveRequiredPlaceholders("${sm4.sk}"));
            config.setIv(environment.resolveRequiredPlaceholders("${sm4.iv}"));
            log.info("加载配置密钥和向量成功");
        } else if (environment.containsProperty("sm4.sk1")) {
            config.setSk1(environment.resolveRequiredPlaceholders("${sm4.sk1}"));
            config.setSk2(environment.resolveRequiredPlaceholders("${sm4.sk2}"));
            config.setIv1(environment.resolveRequiredPlaceholders("${sm4.iv1}"));
            config.setIv2(environment.resolveRequiredPlaceholders("${sm4.iv2}"));
            log.info("加载配置分段密钥和向量成功");
        } else {
            config.setSk("VAWYwsv7xzNfYXs8RfiDMw==");
            config.setIv("SjYwaDZuTDE5bU1aRXVEbA==");
            log.warn("正在使用默认配置密钥和向量,建议修改");
        }
        Sm4ByteEncryptor encryptor = new Sm4ByteEncryptor(config);
        return new ByteEncryptorStringEncryptorDelegate(encryptor);
    }
}
