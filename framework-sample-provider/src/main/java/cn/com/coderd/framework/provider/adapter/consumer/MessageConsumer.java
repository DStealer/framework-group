package cn.com.coderd.framework.provider.adapter.consumer;

import cn.com.coderd.framework.service.application.message.message.LoginMessageDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Slf4j
@Service
public class MessageConsumer {

    /**
     * 消费者
     *
     * @return
     */
    @Bean
    public Consumer<Message<LoginMessageDTO>> consumeLoginMessage() {
        return (message) -> log.info("登陆短信息接收:{}", message);
    }
}
