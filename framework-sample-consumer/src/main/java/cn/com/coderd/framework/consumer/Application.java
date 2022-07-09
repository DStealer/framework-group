package cn.com.coderd.framework.consumer;

import cn.com.coderd.framework.service.App;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Hello world!
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableScheduling
@EnableFeignClients(basePackageClasses = App.class)
@EnableDubbo
public class Application {
    public static void main(String[] args) throws Exception {
        new SpringApplicationBuilder()
                .sources(Application.class)
                .headless(true)
                .web(WebApplicationType.SERVLET)
                .run(args);
        Thread.currentThread().join();
    }
}