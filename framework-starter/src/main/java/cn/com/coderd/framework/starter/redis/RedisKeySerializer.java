package cn.com.coderd.framework.starter.redis;

import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

/**
 * 带有规则校验 redis key serializer
 */
public class RedisKeySerializer extends StringRedisSerializer {
    private static final Pattern KEY_PATTERN = Pattern.compile("^[a-zA-Z0-9_]+:[a-zA-Z0-9_:#$()=\\-]+$");

    public RedisKeySerializer() {
        super(StandardCharsets.US_ASCII);
    }

    @Override
    public byte[] serialize(String string) {
        if (string == null || !KEY_PATTERN.asPredicate().test(string)) {
            throw new IllegalArgumentException("redis key invalid");
        }
        return super.serialize(string);
    }
}
