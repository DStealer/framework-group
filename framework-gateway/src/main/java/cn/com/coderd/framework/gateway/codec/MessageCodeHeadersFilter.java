package cn.com.coderd.framework.gateway.codec;

import org.springframework.cloud.gateway.filter.headers.HttpHeadersFilter;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.web.server.ServerWebExchange;

/**
 * 自定义消息头信息过滤规则实现类
 */
public class MessageCodeHeadersFilter implements HttpHeadersFilter {
    @Override
    public boolean supports(Type type) {
        return type.equals(Type.REQUEST) || type.equals(Type.RESPONSE);
    }

    /**
     * 过滤规则
     *
     * @param input
     * @param exchange
     * @return
     */
    @Override
    public HttpHeaders filter(HttpHeaders input, ServerWebExchange exchange) {
        HttpHeaders filtered = new HttpHeaders();
        if (exchange.getAttribute(ServerWebExchangeUtils.CLIENT_RESPONSE_ATTR) == null) { //请求头处理
            input.forEach(filtered::put);
        } else { //响应头处理
            input.forEach(filtered::put);
        }
        return filtered;
    }
}
