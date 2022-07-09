package cn.com.coderd.framework.service.application.web.gray;

import cn.com.coderd.framework.common.basic.Result;
import cn.com.coderd.framework.service.pojo.Entity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/gray")
@FeignClient(name = "framework-sample-provider")
public interface GrayFeign {

    /**
     * 回声测试
     *
     * @param entity
     * @return
     */
    @PostMapping("/echo")
    Result<String> echo(@RequestBody Entity entity);
}
