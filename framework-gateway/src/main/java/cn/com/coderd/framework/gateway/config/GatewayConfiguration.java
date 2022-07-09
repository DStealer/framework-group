package cn.com.coderd.framework.gateway.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 网关自动配置
 */
@Slf4j
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(GatewayTenantProperties.class)
public class GatewayConfiguration {
}