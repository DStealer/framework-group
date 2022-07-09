package cn.com.coderd.framework.starter.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * spring mvc自动配置
 */
@Slf4j
@Configuration(proxyBeanMethods = false)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class WebMvcConfiguration implements WebMvcConfigurer {

    /**
     * 拓展消息转换器
     *
     * @param converters
     */
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        log.info("拓展http消息转换器");
        converters.add(new InputStreamMessageConverter());
    }

    /**
     * 添加http filter增加追踪信息
     *
     * @return
     */
    @Bean
    public FilterRegistrationBean<TraceSupportFilter> traceSupportFilterRegistrationBean() {
        FilterRegistrationBean<TraceSupportFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new TraceSupportFilter());
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE + 1000);
        log.info("配置mdc追踪filter");
        return registrationBean;
    }
}
