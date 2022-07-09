package cn.com.coderd.framework.provider.infrastructure.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration(proxyBeanMethods = false)
@OpenAPIDefinition(info = @Info(title = "生产者示例", version = "0.0.1"))
public class OpenApi3Configuration {
}
