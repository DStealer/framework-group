package cn.com.coderd.framework.starter.gray;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration(proxyBeanMethods = false)
public class DefaultLoadBalancerClientSpecification {
    /**
     * 灰度 BeanPostProcessor
     *
     * @return
     */
    @Bean
    public BeanPostProcessor graySupportPostProcessor() {
        return new BeanPostProcessor() {
            @Override
            public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
                if (bean instanceof ServiceInstanceListSupplier) {
                    log.info("配置灰度功能:{}-{}", beanName, bean.toString());
                    return new GrayHttpServiceInstanceListSupplier((ServiceInstanceListSupplier) bean);

                }
                return bean;
            }
        };
    }
}
