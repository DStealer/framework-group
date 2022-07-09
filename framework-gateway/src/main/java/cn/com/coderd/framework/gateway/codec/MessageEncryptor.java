package cn.com.coderd.framework.gateway.codec;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.web.server.ServerWebExchange;

/**
 * 消息体编码转换
 */
public abstract class MessageEncryptor {
    /**
     * 是否支持该请求类型的消息
     *
     * @param headers
     * @return
     */
    public abstract boolean supportRequest(HttpHeaders headers);

    /**
     * 处理请求消息头
     *
     * @param headers
     * @return
     */
    public abstract HttpHeaders filterRequest(HttpHeaders headers);

    /**
     * 是否支持该响应类型的消息
     *
     * @param headers
     * @return
     */
    public abstract boolean supportResponse(HttpHeaders headers);

    /**
     * 处理响应消息头
     *
     * @param headers
     * @return
     */
    public abstract HttpHeaders filterResponse(HttpHeaders headers);

    /**
     * 编码消息内容
     *
     * @param exchange
     * @param buffer
     * @return
     */
    public abstract DataBuffer encrypt(ServerWebExchange exchange, DataBuffer buffer);

    /**
     * 解码消息内容
     *
     * @param exchange
     * @param buffer
     * @return
     */
    public abstract DataBuffer decrypt(ServerWebExchange exchange, DataBuffer buffer);

}
