package cn.com.coderd.framework.service.application.web.hello;

import cn.com.coderd.framework.common.basic.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/login")
@Tag(name = "grpc示例", description = "grpc示例接口展示")
public interface HelloController {

    /**
     * hello
     *
     * @param request
     * @return
     */
    @PostMapping("/app/hello")
    @Operation(summary = "grpc示例接口 hello", description = "grpc示例接口展示")
    Result<HelloResponse> hello(HelloRequest request);
}
