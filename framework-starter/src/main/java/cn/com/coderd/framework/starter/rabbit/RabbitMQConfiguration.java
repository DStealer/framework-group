package cn.com.coderd.framework.starter.rabbit;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.DeclarableCustomizer;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.ContentTypeDelegatingMessageConverter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * rabbit自动配置
 */
@Slf4j
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({RabbitTemplate.class, Channel.class})
public class RabbitMQConfiguration {
    /**
     * 配置rabbit消息转换器
     *
     * @return
     */
    @Bean
    public MessageConverter messageConverter() {
        Jackson2JsonMessageConverter jsonMessageConverter = new Jackson2JsonMessageConverter();
        ContentTypeDelegatingMessageConverter converter = new ContentTypeDelegatingMessageConverter(jsonMessageConverter);
        converter.addDelegate(MessageProperties.CONTENT_TYPE_JSON, jsonMessageConverter);
        SimpleMessageConverter simpleMessageConverter = new SimpleMessageConverter();
        converter.addDelegate(MessageProperties.CONTENT_TYPE_TEXT_PLAIN, simpleMessageConverter);
        converter.addDelegate(MessageProperties.CONTENT_TYPE_SERIALIZED_OBJECT, simpleMessageConverter);
        return converter;
    }

    /**
     * 在Rabbit投递消息时增加消息来源和标记
     *
     * @param environment
     * @return
     */
    @Bean
    public BeanPostProcessor rabbitTemplateBeanPostProcessor(ConfigurableEnvironment environment) {
        return new BeanPostProcessor() {
            @Override
            public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
                if (bean instanceof RabbitTemplate) {
                    log.info("增强rabbitTemplate标记追踪功能:{}-{}",beanName,bean);
                    ((RabbitTemplate) bean).addBeforePublishPostProcessors(new BeforePublishTraceMessagePostProcessor());
                    ((RabbitTemplate) bean).addAfterReceivePostProcessors(new AfterReceiveTraceMessagePostProcessor());
                }
                return bean;
            }
        };
    }

    /**
     * 打印需要自动声明的 queue topic binding
     *
     * @return
     */
    @Bean
    @ConditionalOnClass(DeclarableCustomizer.class)
    public DeclarableCustomizer logDeclarableCustomizer() {
        return declarable -> {
            log.info("rabbit mq declare :{}", declarable);
            return declarable;
        };
    }
}
