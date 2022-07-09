package cn.com.coderd.framework.starter.message;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.config.GlobalChannelInterceptor;
import org.springframework.messaging.support.ChannelInterceptor;

/**
 * spring message功能
 */
@Slf4j
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(ChannelInterceptor.class)
public class MessageConfiguration {
    /**
     * 在投递消息时增加消息来源和标记
     *
     * @return
     */
    @Bean
    @GlobalChannelInterceptor()
    public ChannelInterceptor sourceAwareGlobalChannelInterceptor() {
        log.info("增强MessageQueue标记追踪功能");
        return new TraceChannelInterceptor();
    }
}
