package cn.com.coderd.framework.starter.sentinel;

import cn.com.coderd.framework.common.basic.Result;
import cn.com.coderd.framework.starter.utils.CompositeUtil;
import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;

/**
 * sentinel 阻塞处理类
 */
@Slf4j
public class WebBlockExceptionHandler implements BlockExceptionHandler {
    private static final ObjectMapper MAPPER = JsonMapper.builder()
            .configure(JsonGenerator.Feature.IGNORE_UNKNOWN, true)
            .build();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, BlockException e) throws Exception {
        log.warn("request blocked :[{}]<=>[{}]", request.getRequestURI(), CompositeUtil.formatHeader(request), e);
        response.setStatus(429);
        response.setContentType(MediaType.APPLICATION_JSON.toString());
        try (OutputStream writer = response.getOutputStream()) {
            MAPPER.writeValue(writer, Result.fail("GA429", "请求阻塞"));
        }
    }
}