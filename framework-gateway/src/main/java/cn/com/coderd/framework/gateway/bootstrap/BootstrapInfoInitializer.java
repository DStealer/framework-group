package cn.com.coderd.framework.gateway.bootstrap;

import cn.com.coderd.framework.gateway.utils.ComponentUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.InputStream;
import java.util.Properties;

/**
 * 该类职责为:
 * 1. 查询 classpath:git.properties 并打印其内容,主要是确认该打包时的git版本信息 配合 framework-parent 项目 git-commit-id-plugin插件使用
 */
@Slf4j
public class BootstrapInfoInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        ApplicationContext parent = applicationContext.getParent();
        if (parent == null) {
            ResourceLoader loader = new DefaultResourceLoader();
            Resource resource = loader.getResource("classpath:git.properties");
            try (InputStream inputStream = resource.getInputStream()) {
                Properties properties = new Properties();
                properties.load(inputStream);
                log.info("当前应用版本信息:{}", ComponentUtils.json(properties));

            } catch (Exception e) {
                log.warn("版本信息加载失败!");
            }
        }
    }
}
