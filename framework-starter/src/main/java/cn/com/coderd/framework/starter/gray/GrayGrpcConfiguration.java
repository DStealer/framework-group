package cn.com.coderd.framework.starter.gray;

import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.autoconfigure.GrpcDiscoveryClientAutoConfiguration;
import net.devh.boot.grpc.client.nameresolver.DiscoveryClientResolverFactory;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * grpc灰度支持配置类
 */
@Slf4j
@Configuration(proxyBeanMethods = false)
@AutoConfigureBefore(GrpcDiscoveryClientAutoConfiguration.class)
public class GrayGrpcConfiguration {
    @Bean
    DiscoveryClientResolverFactory grpcDiscoveryClientResolverFactory(DiscoveryClient client) {
        log.info("增强grpc灰度支持");
        return new GrpcDiscoveryClientResolverFactory(client);
    }
}
