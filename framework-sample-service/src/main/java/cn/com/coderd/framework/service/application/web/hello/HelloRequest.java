package cn.com.coderd.framework.service.application.web.hello;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@Schema(description = "请求参数示例")
public class HelloRequest implements Serializable {
    @Schema(description = "姓名")
    private String name;
}
