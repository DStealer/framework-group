package cn.com.coderd.framework.starter.redis;

import cn.com.coderd.framework.starter.constants.ConstVar;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * redis 自动配置类
 */
@Slf4j
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(RedisOperations.class)
@AutoConfigureBefore(RedisAutoConfiguration.class)
public class RedisConfiguration {
    /**
     * 设置默认的redisTemplate
     * @param redisConnectionFactory
     * @param keySerializer
     * @return
     */
    @Bean
    @ConditionalOnBean(RedisConnectionFactory.class)
    @ConditionalOnSingleCandidate(RedisConnectionFactory.class)
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory, RedisSerializer<?> keySerializer) {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setEnableDefaultSerializer(true);
        template.setKeySerializer(keySerializer);
        template.setDefaultSerializer(RedisSerializer.json());
        template.afterPropertiesSet();
        log.info("初始化redisTemplate完成:[{}]", template);
        return template;
    }
    /**
     * 设置默认的stringRedisTemplate
     * @param redisConnectionFactory
     * @param keySerializer
     * @return
     */
    @Bean
    @ConditionalOnBean(RedisConnectionFactory.class)
    @ConditionalOnSingleCandidate(RedisConnectionFactory.class)
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory, RedisSerializer<?> keySerializer) {
        StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(redisConnectionFactory);
        template.setEnableDefaultSerializer(true);
        template.setKeySerializer(keySerializer);
        template.setDefaultSerializer(RedisSerializer.json());
        template.afterPropertiesSet();
        log.info("初始化stringRedisTemplate完成:[{}]", template);
        return template;
    }

    /**
     * 设置生产环境使用的 redis key serializer
     * @return
     */
    @Bean
    @ConditionalOnProperty(name = ConstVar.ENV_PRODUCT, havingValue = "*")
    public RedisSerializer<?> proRedisKeySerializer() {
        log.info("配置 pro redis key serializer 完毕");
        return RedisSerializer.string();
    }
    /**
     * 设置测试环境使用的 redis key serializer 附有规则校验功能,所以不应
     *
     * @return
     */
    @Bean
    @ConditionalOnProperty(name = ConstVar.ENV_PRODUCT, havingValue = "false", matchIfMissing = true)
    public RedisSerializer<?> devRedisKeySerializer() {
        log.info("配置 dev redis key serializer 完毕");
        return new RedisKeySerializer();
    }
}
