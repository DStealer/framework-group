package cn.com.coderd.framework.gateway.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

/**
 * 项目公共工具类
 */
@UtilityClass
public class ComponentUtils {

    private static final ObjectMapper MAPPER = JsonMapper.builder()
            .serializationInclusion(JsonInclude.Include.NON_NULL)
            .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
            .build();

    private static final char[] CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890".toCharArray();

    /**
     * 生成随机字符串
     *
     * @param len
     * @return
     */
    public static String randomString(int len) {
        StringBuilder builder = new StringBuilder();
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < len; i++) {
            builder.append(CHARS[random.nextInt(CHARS.length)]);
        }
        return builder.toString();
    }

    /**
     * json格式化
     *
     * @param obj
     * @return
     */
    @SneakyThrows
    public static String json(Object obj) {
        return MAPPER.writeValueAsString(obj);
    }

    /**
     * 获取默认字符编码
     *
     * @param headers
     * @return
     */
    public static Charset getCharsetOrUTF8(HttpHeaders headers) {
        MediaType mediaType = headers.getContentType();
        return mediaType == null ? StandardCharsets.UTF_8 : mediaType.getCharset() == null
                ? StandardCharsets.UTF_8 : mediaType.getCharset();
    }

    /**
     * 响应json格式
     *
     * @param exchange
     * @param obj
     * @return
     */
    @SneakyThrows
    public static Mono<Void> writeJsonMono(ServerWebExchange exchange, Object obj) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.OK);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        return response.writeWith(Mono.just(response.bufferFactory().wrap(MAPPER.writeValueAsBytes(obj))));
    }
}
