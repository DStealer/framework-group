package cn.com.coderd.framework.gateway.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 安全过滤自动配置
 */
@Slf4j
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(SecurityPatternsProperties.class)
public class SecurityConfiguration {

    /**
     * 安全过滤检查器
     * @return
     */
    @Bean
    public SecurityChecker securityChecker(){
        return new SecurityChecker();
    }

    /**
     * 安全过滤器
     *
     * @return
     */
    @Bean
    @ConditionalOnBean(SecurityChecker.class)
    public SecurityGlobalFilter securityGlobalFilter(SecurityChecker checker) {
        log.info("配置安全过滤器");
        return new SecurityGlobalFilter(checker);
    }
}
