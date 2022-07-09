package cn.com.coderd.framework.starter.sentinel;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * sentinel自动配置
 */
@Slf4j
@Configuration(proxyBeanMethods = false)
public class SentinelConfiguration {
    /**
     * web熔断降级处理bean
     *
     * @return
     */
    @Bean
    public WebBlockExceptionHandler webBlockExceptionHandler() {
        return new WebBlockExceptionHandler();
    }
}
