package cn.com.coderd.framework.openapi.application.web;

import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

/**
 * openapi接口
 */
@RestController
public class OpenApiController {

    @Autowired
    private RestTemplate restTemplate;

    /**
     * openapi接口转换
     *
     * @param name
     * @return
     */
    @GetMapping("/v3/api-docs-ext/{name}")
    public OpenAPI apiDocsExt(@PathVariable("name") String name) {
        OpenAPI api = restTemplate.getForObject(String.format("http://%s/v3/api-docs", name), OpenAPI.class);
        List<Server> servers = api.getServers();
        if (servers == null) {
            api.setServers(Collections.singletonList(new Server()
                    .url(String.format("/%s", name))
                    .description("Generated gateway url")));
        } else {
            servers.add(0, new Server()
                    .url(String.format("/%s", name))
                    .description("Generated gateway url"));
        }
        Paths paths = api.getPaths();
        if (paths != null) {
            paths.values().forEach(e -> {
                Parameter token = new Parameter()
                        .in(ParameterIn.HEADER.toString())
                        .schema(new StringSchema())
                        .name("token")
                        .description("认证信息");
                e.addParametersItem(token);
                Parameter gray = new Parameter()
                        .in(ParameterIn.HEADER.toString())
                        .schema(new StringSchema())
                        .name("t-gray")
                        .description("灰度版本");
                e.addParametersItem(gray);
            });
        }
        return api;
    }
}
