package cn.com.coderd.framework.starter.sentinel;

import cn.com.coderd.framework.common.basic.Result;
import cn.com.coderd.framework.starter.utils.CompositeUtil;
import com.alibaba.cloud.sentinel.rest.SentinelClientHttpResponse;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpResponse;

/**
 * sentinel 阻塞处理类
 */
@Slf4j
public class SentinelBlockExceptionHandler {
    private static final ObjectMapper MAPPER = JsonMapper.builder()
            .configure(JsonGenerator.Feature.IGNORE_UNKNOWN, true)
            .build();

    /**
     * 处理阻塞异常
     *
     * @param request
     * @param body
     * @param execution
     * @param exception
     * @return
     */
    public static ClientHttpResponse handleBlock(HttpRequest request, byte[] body,
                                                 ClientHttpRequestExecution execution, BlockException exception) {
        log.warn("请求阻塞:{}<=>{}", request.getURI(), CompositeUtil.formatHeader(request), exception);
        try {
            return new SentinelClientHttpResponse(MAPPER.writeValueAsString(Result.fail("GA401", "请求阻塞")));
        } catch (JsonProcessingException e) {
            log.error("处理json异常", e);
            return new SentinelClientHttpResponse("未知异常");
        }
    }

    /**
     * 处理降级异常
     *
     * @param request
     * @param body
     * @param execution
     * @param exception
     * @return
     */
    public static ClientHttpResponse handleFallback(HttpRequest request, byte[] body,
                                                    ClientHttpRequestExecution execution, BlockException exception) {
        log.warn("请求降级:{}<=>{}", request.getURI(), CompositeUtil.formatHeader(request), exception);
        try {
            return new SentinelClientHttpResponse(MAPPER.writeValueAsString(Result.fail("GA402", "请求降级")));
        } catch (JsonProcessingException e) {
            log.error("处理json异常", e);
            return new SentinelClientHttpResponse("未知异常");
        }
    }
}
