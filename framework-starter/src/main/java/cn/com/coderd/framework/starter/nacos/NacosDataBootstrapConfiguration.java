package cn.com.coderd.framework.starter.nacos;

import com.alibaba.cloud.nacos.NacosConfigAutoConfiguration;
import com.alibaba.cloud.nacos.NacosConfigBootstrapConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;

import java.util.Map;

/**
 * nacos自动配置
 */
@Slf4j
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(name = "spring.cloud.nacos.data.enable", matchIfMissing = true)
@AutoConfigureBefore({NacosConfigBootstrapConfiguration.class, NacosConfigAutoConfiguration.class})
public class NacosDataBootstrapConfiguration implements InitializingBean {
    @Autowired
    private ConfigurableEnvironment configurableEnvironment;

    /**
     * 这里规划nacos文件路径
     * 日志路径优先级  SystemProperties (JM.LOG.PATH) > 环境变量(JM.LOG.PATH)
     * >环境变量(JM_ROOT,如配置规则 ${JM_ROOT}/${spring.application.name}/log) > 默认配置
     * 日志路径优先级  SystemProperties (JM.SNAPSHOT.PATH) >环境变量(JM.SNAPSHOT.PATH)
     * >环境变量(JM_ROOT,如配置规则 ${JM_ROOT}/${spring.application.name}/snapshot) > 默认配置
     *
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, Object> properties = configurableEnvironment.getSystemProperties();
        Map<String, Object> environment = configurableEnvironment.getSystemEnvironment();
        if (properties.containsKey("JM.LOG.PATH")) {
            log.info("nacos JM.LOG.PATH set to:[{}] use SystemProperties", properties.get("JM.LOG.PATH"));
        } else if (environment.containsKey("JM.LOG.PATH")) {
            properties.put("JM.LOG.PATH", environment.get("JM.LOG.PATH"));
            log.info("nacos JM.LOG.PATH will from environment:[{}]", environment.get("JM.LOG.PATH"));
        } else if (environment.containsKey("JM_ROOT")) {
            String path = configurableEnvironment.resolveRequiredPlaceholders("${JM_ROOT}/${spring.application.name}/log");
            properties.put("JM.LOG.PATH", path);
            log.info("nacos JM.LOG.PATH resolve from environment:[{}]", path);
        } else {
            log.warn("nacos JM.LOG.PATH will default location");
        }

        if (properties.containsKey("JM.SNAPSHOT.PATH")) {
            log.info("nacos JM.SNAPSHOT.PATH will from system.properties:[{}]", properties.get("JM.SNAPSHOT.PATH"));
        } else if (environment.containsKey("JM.SNAPSHOT.PATH")) {
            properties.put("JM.SNAPSHOT.PATH", environment.get("JM.SNAPSHOT.PATH"));
            log.info("nacos JM.SNAPSHOT.PATH will from environment:[{}]", environment.get("JM.SNAPSHOT.PATH"));
        } else if (environment.containsKey("JM_ROOT")) {
            String path = configurableEnvironment.resolveRequiredPlaceholders("${JM_ROOT}/${spring.application.name}/snapshot");
            properties.put("JM.SNAPSHOT.PATH", path);
            log.info("nacos JM.SNAPSHOT.PATH resolve from environment:[{}]", path);
        } else {
            log.warn("nacos JM.SNAPSHOT.PATH will default location");
        }
    }


}
