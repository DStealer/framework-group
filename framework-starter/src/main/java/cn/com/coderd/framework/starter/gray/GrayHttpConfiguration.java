package cn.com.coderd.framework.starter.gray;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClientSpecification;
import org.springframework.cloud.loadbalancer.config.LoadBalancerAutoConfiguration;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置http灰度支持
 */
@Slf4j
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(ServiceInstanceListSupplier.class)
@AutoConfigureBefore(value = {LoadBalancerAutoConfiguration.class})
public class GrayHttpConfiguration {
    @Bean
    public LoadBalancerClientSpecification defaultGrayLoadBalancerClientSpecification() {
        log.info("增强配置灰度功能LoadBalancerClientSpecification");
        LoadBalancerClientSpecification specification = new LoadBalancerClientSpecification();
        specification.setName("default.GrayLoadBalancerClientSpecification");
        specification.setConfiguration(new Class[]{DefaultLoadBalancerClientSpecification.class});
        return specification;
    }

}
