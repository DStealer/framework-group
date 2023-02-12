package cn.com.coderd.framework.gateway.codec;

import cn.com.coderd.framework.gateway.config.GatewayTenantProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * 消息编码自动配置
 */
@Slf4j
@Configuration(proxyBeanMethods = false)
public class MessageCodecConfiguration {
    /**
     * 自定义消息转换器
     *
     * @param properties
     * @return
     */
    @Bean
    public MessageCodeGlobalFilter cryptGlobalFilter(GatewayTenantProperties properties) {
        List<MessageEncryptor> encryptors = new ArrayList<>();
        encryptors.add(new JsonXMessageEncryptor(properties.getJsonxSpecs()));
        log.info("启用的自定义消息转换器:{}", encryptors);
        return new MessageCodeGlobalFilter(encryptors);
    }

    /**
     * 自定义消息头信息过滤规则
     *
     * @return
     */
    @Bean
    public MessageCodeHeadersFilter jsonHttpHeadersFilter() {
        return new MessageCodeHeadersFilter();
    }
}
