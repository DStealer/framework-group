package cn.com.coderd.framework.gateway.security;

import cn.com.coderd.framework.common.basic.Result;
import cn.com.coderd.framework.gateway.codec.MessageCodeGlobalFilter;
import cn.com.coderd.framework.gateway.utils.ComponentUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 安全过滤器
 */
@Slf4j
public class SecurityGlobalFilter implements GlobalFilter, Ordered {
    public static final Integer FILTER_ORDER = MessageCodeGlobalFilter.FILTER_ORDER + 1000;
    private final SecurityChecker checker;

    public SecurityGlobalFilter(SecurityChecker checker) {
        this.checker = checker;
    }


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        if (!MediaType.APPLICATION_JSON.isCompatibleWith(exchange.getRequest().getHeaders().getContentType())) {
            return chain.filter(exchange);
        }
        return DataBufferUtils.join(exchange.getRequest().getBody()).flatMap(dataBuffer -> {
            String content = dataBuffer.toString(ComponentUtils.getCharsetOrUTF8(exchange.getRequest().getHeaders()));
            Result<Void> checkJsonResult = this.checker.checkJson(exchange.getRequest(), content);
            if (!checkJsonResult.ok()) {
                DataBufferUtils.release(dataBuffer);
                return ComponentUtils.writeJsonMono(exchange, checkJsonResult);
            } else {
                return chain.filter(exchange.mutate().request(new ServerHttpRequestDecorator(exchange.getRequest()) {
                    @Override
                    public Flux<DataBuffer> getBody() {
                        return Flux.just(dataBuffer);
                    }
                }).build());
            }
        });
    }


    @Override
    public int getOrder() {
        return FILTER_ORDER;
    }
}
