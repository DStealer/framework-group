package cn.com.coderd.framework.gateway.trace;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration(proxyBeanMethods = false)
public class TraceConfiguration {
    /**
     * 上下文增加标记支持
     *
     * @return
     */
    @Bean
    public TraceGlobalFilter traceGlobalFilter() {
        log.info("增强链路标记支持");
        return new TraceGlobalFilter();
    }
}
