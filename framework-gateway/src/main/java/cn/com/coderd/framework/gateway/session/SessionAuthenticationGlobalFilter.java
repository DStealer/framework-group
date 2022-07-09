package cn.com.coderd.framework.gateway.session;

import cn.com.coderd.framework.common.basic.Result;
import cn.com.coderd.framework.gateway.utils.ComponentUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.session.Session;
import org.springframework.session.web.server.session.SpringSessionWebSessionStore;
import org.springframework.util.PatternMatchUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

/**
 * session鉴权拦截器
 */
@Slf4j
public class SessionAuthenticationGlobalFilter implements GlobalFilter, Ordered {
    public static final Integer FILTER_ORDER = Ordered.HIGHEST_PRECEDENCE + 2000;
    private final SpringSessionWebSessionStore<? extends Session> store;
    private final List<String> excludePatterns;

    public SessionAuthenticationGlobalFilter(
            SpringSessionWebSessionStore<? extends Session> store, List<String> excludePatterns) {
        this.store = store;
        this.excludePatterns = excludePatterns;
    }

    /**
     * 会话鉴权
     *
     * @param exchange
     * @param chain
     * @return
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        if (this.excludePatterns.stream()
                .anyMatch(p -> PatternMatchUtils.simpleMatch(p, path))) {
            return chain.filter(exchange);
        }
        String token = this.resolveSessionId(exchange);
        if (token == null || token.isEmpty()) {
            log.warn("request:{} token not found", exchange.getRequest().getURI().getPath());
            return ComponentUtils.writeJsonMono(exchange, Result.fail("GW101", "会话不存在"));
        }
        return this.store.retrieveSession(token)
                .filter(session -> session.isStarted() && !session.isExpired())
                .map(session -> session.getAttribute("user") != null ? Optional.of(session) : Optional.<WebSession>empty())
                .defaultIfEmpty(Optional.empty())
                .flatMap(sessionOptional -> {
                    if (sessionOptional.isPresent()) {
                        exchange.getResponse().beforeCommit(() -> sessionOptional.get().save());
                        return chain.filter(exchange);
                    } else {
                        log.warn("request:{} session:{} not found", exchange.getRequest().getURI().getPath(), token);
                        return ComponentUtils.writeJsonMono(exchange, Result.fail("GW101", "会话不存在"));
                    }
                });
    }


    /**
     * 获取session id
     *
     * @param exchange
     * @return
     */
    private String resolveSessionId(ServerWebExchange exchange) {
        return exchange.getRequest().getHeaders().getFirst("token");
    }

    @Override
    public int getOrder() {
        return FILTER_ORDER;
    }
}
