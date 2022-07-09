package cn.com.coderd.framework.consumer.application.web;

import cn.com.coderd.framework.common.basic.Result;
import cn.com.coderd.framework.service.application.web.gray.GrayFeign;
import cn.com.coderd.framework.service.pojo.Entity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RestController
@RequestMapping("/gray")
public class GrayController {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private GrayFeign grayFeign;

    @PostMapping("/rest")
    @SuppressWarnings("unchecked")
    public Result<String> rest(@RequestBody Entity entity) {
        log.info("灰度测试:{}", entity);
        return restTemplate.postForObject("http://framework-sample-provider/gray/echo", entity, Result.class);
    }

    @PostMapping("/fegin")
    public Result<String> fegin(@RequestBody Entity entity) {
        log.info("灰度测试:{}", entity);
        return grayFeign.echo(entity);
    }
}
