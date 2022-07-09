package cn.com.coderd.framework.gateway.codec;

import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.NettyWriteResponseFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

/**
 * 消息编码转换过滤器实现
 */
@Slf4j
public class MessageCodeGlobalFilter implements GlobalFilter, Ordered {
    public static final Integer FILTER_ORDER = NettyWriteResponseFilter.WRITE_RESPONSE_FILTER_ORDER - 1000;
    private final List<MessageEncryptor> messageEncryptors;

    public MessageCodeGlobalFilter(List<MessageEncryptor> messageEncryptors) {
        this.messageEncryptors = messageEncryptors;
    }

    /**
     * 消息转换
     *
     * @param exchange
     * @param chain
     * @return
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        Optional<MessageEncryptor> encryptorOptional = this.messageEncryptors.stream()
                .filter(e -> e.supportRequest(exchange.getRequest().getHeaders()))
                .findFirst();
        return chain.filter(exchange.mutate()
                .request(new ServerHttpRequestDecorator(exchange.getRequest()) {
                    private HttpHeaders headers;

                    @Override
                    public HttpHeaders getHeaders() {
                        if (this.headers == null) {
                            this.headers = encryptorOptional
                                    .map(e -> e.filterRequest(super.getHeaders()))
                                    .orElse(super.getHeaders());
                        }
                        return this.headers;
                    }

                    @Override
                    public Flux<DataBuffer> getBody() {
                        return encryptorOptional.map(e -> DataBufferUtils.join(super.getBody())
                                .map(buffer -> e.decrypt(exchange, buffer)).flux()).orElseGet(super::getBody);
                    }
                }).response(new ServerHttpResponseDecorator(exchange.getResponse()) {
                    private HttpHeaders headers;

                    @Override
                    public HttpHeaders getHeaders() {
                        if (this.headers == null) {
                            this.headers = encryptorOptional.filter(e -> e.supportResponse(super.getHeaders()))
                                    .map(e -> e.filterResponse(super.getHeaders()))
                                    .orElse(super.getHeaders());
                        }
                        return this.headers;
                    }

                    @Override
                    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                        return encryptorOptional.filter(e -> e.supportResponse(super.getHeaders()))
                                .map(e -> super.writeWith(DataBufferUtils.join(body)
                                        .map(buffer -> e.encrypt(exchange, buffer))))
                                .orElseGet(() -> super.writeWith(body));
                    }

                    @Override
                    public Mono<Void> writeAndFlushWith(Publisher<? extends Publisher<? extends DataBuffer>> body) {
                        return encryptorOptional.filter(e -> e.supportResponse(super.getHeaders()))
                                .map(e -> super.writeAndFlushWith(Flux.from(body)
                                        .map(pub -> DataBufferUtils.join(pub)
                                                .map(buffer -> e.encrypt(exchange, buffer)))))
                                .orElseGet(() -> super.writeAndFlushWith(body));
                    }
                }).build());
    }

    @Override
    public int getOrder() {
        return FILTER_ORDER;
    }
}
