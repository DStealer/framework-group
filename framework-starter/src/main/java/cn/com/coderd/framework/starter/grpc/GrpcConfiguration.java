package cn.com.coderd.framework.starter.grpc;

import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.interceptor.GlobalClientInterceptorConfigurer;
import net.devh.boot.grpc.server.interceptor.GlobalServerInterceptorConfigurer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * grpc 功能增强
 */
@Slf4j
@Configuration(proxyBeanMethods = false)
public class GrpcConfiguration {

    /**
     * 从当前线程MDC取出标记添加到客户端请求上下文中
     *
     * @return
     */
    @Bean
    @ConditionalOnClass(GlobalClientInterceptorConfigurer.class)
    public GlobalClientInterceptorConfigurer globalClientTraceInterceptorConfigurer() {
        log.info("添加Grpc客户端标记追踪功能");
        return interceptors -> interceptors.add(0, new ClientTraceInterceptor());
    }

    /**
     * 从当前请求上下文中取出标记添加到线程MDC
     *
     * @return
     */
    @Bean
    @ConditionalOnClass(GlobalServerInterceptorConfigurer.class)
    public GlobalServerInterceptorConfigurer globalServerTraceInterceptorConfigurer() {
        log.info("添加Grpc服务端标记追踪功能");
        return interceptors -> interceptors.add(0, new ServerTraceInterceptor());
    }

}
