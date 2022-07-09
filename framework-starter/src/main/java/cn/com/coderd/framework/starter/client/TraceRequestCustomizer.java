package cn.com.coderd.framework.starter.client;

import cn.com.coderd.framework.starter.constants.ConstVar;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.boot.web.client.RestTemplateRequestCustomizer;
import org.springframework.http.client.ClientHttpRequest;

import java.util.Optional;

/**
 * RestTemplate客户端增加携带标记信息功能实现类
 *
 * @param <T>
 */
@Slf4j
public class TraceRequestCustomizer<T extends ClientHttpRequest> implements RestTemplateRequestCustomizer<T> {
    /**
     * 从当前线程MDC取出标记添加到请求头中
     *
     * @param request
     */
    @Override
    public void customize(T request) {
        Optional.ofNullable(MDC.getCopyOfContextMap())
                .ifPresent(content ->
                        content.entrySet()
                                .stream()
                                .filter(e -> e.getKey().startsWith(ConstVar.TRACE_PREFIX) && e.getValue() != null)
                                .forEach(e -> request.getHeaders().set(e.getKey(), e.getValue())));
    }
}