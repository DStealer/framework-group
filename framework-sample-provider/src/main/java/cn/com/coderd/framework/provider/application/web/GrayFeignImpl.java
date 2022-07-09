package cn.com.coderd.framework.provider.application.web;

import cn.com.coderd.framework.common.basic.Result;
import cn.com.coderd.framework.service.application.web.gray.GrayFeign;
import cn.com.coderd.framework.service.pojo.Entity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class GrayFeignImpl implements GrayFeign {

    @Override
    public Result<String> echo(Entity entity) {
        log.info("echo:{}", entity);
        return Result.ok(entity.getMessage());
    }
}
