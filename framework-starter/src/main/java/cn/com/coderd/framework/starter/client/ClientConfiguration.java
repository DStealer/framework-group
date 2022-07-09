package cn.com.coderd.framework.starter.client;

import cn.com.coderd.framework.starter.sentinel.SentinelBlockExceptionHandler;
import com.alibaba.cloud.sentinel.annotation.SentinelRestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.FeignBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.web.client.RestTemplate;

/**
 * 主要配置Feign和RestTemplate等客户端增强功能
 */
@Slf4j
@Configuration(proxyBeanMethods = false)
public class ClientConfiguration {
    /**
     * 默认restTemplate
     *
     * @return
     */
    @Bean
    @LoadBalanced
    @SentinelRestTemplate(blockHandler = "handleBlock", blockHandlerClass = SentinelBlockExceptionHandler.class,
            fallback = "handleFallback", fallbackClass = SentinelBlockExceptionHandler.class)
    public RestTemplate restTemplate() {
        log.info("配置默认restTemplate负载均衡和服务降级熔断客户端");
        return new RestTemplateBuilder()
                .build();
    }

    /**
     * RestTemplate客户端增加携带标记信息功能
     *
     * @param <T>
     * @return
     */
    @Bean
    public <T> TraceRequestCustomizer<ClientHttpRequest> traceRequestCustomizer() {
        log.info("添加RestTemplate标记追踪功能");
        return new TraceRequestCustomizer<>();
    }

    /**
     * Feign客户端增加携带标记信息功能
     *
     * @return
     */
    @Bean
    public FeignBuilderCustomizer traceFeignBuilderCustomizer() {
        log.info("添加Feign标记追踪功能");
        return builder -> builder.requestInterceptor(new TraceRequestInterceptor());
    }
}
