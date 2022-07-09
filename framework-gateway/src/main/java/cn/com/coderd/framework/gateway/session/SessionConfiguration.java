package cn.com.coderd.framework.gateway.session;

import cn.com.coderd.framework.gateway.config.GatewayTenantProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.session.Session;
import org.springframework.session.data.redis.ReactiveRedisSessionRepository;
import org.springframework.session.data.redis.config.annotation.web.server.EnableRedisWebSession;
import org.springframework.session.web.server.session.SpringSessionWebSessionStore;

/**
 * session认证处理
 */

@Slf4j
@EnableRedisWebSession
@Configuration(proxyBeanMethods = false)
public class SessionConfiguration {
    /**
     * springSessionDefaultRedisSerializer序类化bean
     *
     * @return
     */
    @Bean("springSessionDefaultRedisSerializer")
    public RedisSerializer<Object> springSessionDefaultRedisSerializer() {
        return RedisSerializer.json();
    }

    /**
     * 使用session后端存储
     *
     * @param sessionRepository
     * @return
     */
    @Bean
    public SpringSessionWebSessionStore<? extends Session> springSessionWebSessionStore(
            ReactiveRedisSessionRepository sessionRepository) {
        return new SpringSessionWebSessionStore<>(sessionRepository);
    }

    /**
     * session会话认证过滤器
     *
     * @param store
     * @param gatewayTenantProperties
     * @return
     */
    @Bean
    public SessionAuthenticationGlobalFilter sessionAuthenticationGlobalFilter(
            SpringSessionWebSessionStore<? extends Session> store, GatewayTenantProperties gatewayTenantProperties) {
        return new SessionAuthenticationGlobalFilter(store, gatewayTenantProperties.getExcludePatterns());
    }

}
