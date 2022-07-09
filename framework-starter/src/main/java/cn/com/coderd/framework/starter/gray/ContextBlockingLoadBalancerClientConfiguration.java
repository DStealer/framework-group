package cn.com.coderd.framework.starter.gray;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerProperties;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 负载均衡上下文传递
 */
@Slf4j
@Configuration(proxyBeanMethods = false)
public class ContextBlockingLoadBalancerClientConfiguration {
    @Bean
    public LoadBalancerClient blockingLoadBalancerClient(LoadBalancerClientFactory loadBalancerClientFactory,
                                                         LoadBalancerProperties properties) {
        log.info("增强配置灰度功能LoadBalancerClient");
        return new ContextBlockingLoadBalancerClient(loadBalancerClientFactory, properties);
    }
}
