package cn.com.coderd.framework.starter.client;

import cn.com.coderd.framework.starter.constants.ConstVar;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import java.util.Optional;

/**
 * Feign客户端增加携带标记信息功能实现类
 */
@Slf4j
public class TraceRequestInterceptor implements RequestInterceptor {
    /**
     * 从当前线程MDC取出标记添加到请求头中
     *
     * @param requestTemplate
     */
    @Override
    public void apply(RequestTemplate requestTemplate) {
        Optional.ofNullable(MDC.getCopyOfContextMap())
                .ifPresent(content ->
                        content.entrySet()
                                .stream()
                                .filter(e -> e.getKey().startsWith(ConstVar.TRACE_PREFIX) && e.getValue() != null)
                                .forEach(e -> requestTemplate.header(e.getKey(), e.getValue())));
    }
}
