package cn.com.coderd.framework.openapi;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Hello world!
 */
@SpringBootApplication
@EnableDiscoveryClient
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
