package cn.com.coderd.framework.gateway.trace;

import cn.com.coderd.framework.gateway.utils.ConstVar;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * 增加标记上下文支持
 */
@Slf4j
public class TraceGlobalFilter implements GlobalFilter, Ordered {
    public static final Integer FILTER_ORDER = Ordered.HIGHEST_PRECEDENCE + 1000;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //TODO 白名单
        Map<String, String> map = exchange.getRequest().getHeaders().entrySet().stream()
                .filter(e -> e.getKey() != null && e.getKey().startsWith(ConstVar.TRACE_PREFIX) && e.getValue() != null)
                .collect(Collectors.toMap(Map.Entry::getKey, v -> StringUtils.collectionToCommaDelimitedString(v.getValue())));
        return chain.filter(exchange).contextWrite(Context.of(map))
                .onErrorMap(e->{log.error("请求:[{}]异常",exchange.getRequest().getURI(),e);return e;});
    }

    @Override
    public int getOrder() {
        return FILTER_ORDER;
    }
}
