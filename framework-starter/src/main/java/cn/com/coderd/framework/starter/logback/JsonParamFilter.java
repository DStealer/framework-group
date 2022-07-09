package cn.com.coderd.framework.starter.logback;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

/**
 * logback日志增强
 * 使用:
 * <appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
 * <filter class="cn.com.coderd.framework.starter.logback.JsonParamFilter"/>
 * <encoder>
 * <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{50} - %msg%n</pattern>
 * </encoder>
 * </appender>
 */

public class JsonParamFilter extends Filter<ILoggingEvent> {
    private static final ObjectMapper MAPPER = JsonMapper.builder()
            .serializationInclusion(JsonInclude.Include.NON_NULL)
            .build();

    public JsonParamFilter() {
        this.setName("JsonParamFilter");
    }

    @Override
    public FilterReply decide(ILoggingEvent event) {
        Object[] argumentArray = event.getArgumentArray();
        if (argumentArray == null) {
            return FilterReply.NEUTRAL;
        }
        if (!event.getClass().isAssignableFrom(LoggingEvent.class)) {
            return FilterReply.NEUTRAL;
        }
        try {
            for (int i = 0; i < argumentArray.length; i++) {
                Object obj = argumentArray[i];
                if (obj != null && !obj.getClass().getCanonicalName().startsWith("java.")) {
                    argumentArray[i] = MAPPER.writeValueAsString(obj);
                }
            }
        } catch (Exception e) {
            //ignore
        }
        return FilterReply.NEUTRAL;
    }
}
