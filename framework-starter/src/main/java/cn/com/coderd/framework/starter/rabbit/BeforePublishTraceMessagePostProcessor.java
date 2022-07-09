package cn.com.coderd.framework.starter.rabbit;

import cn.com.coderd.framework.starter.constants.ConstVar;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Correlation;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.core.PriorityOrdered;

import java.util.Map;
import java.util.Optional;

/**
 * rabbit发送消息增加来源和标记功能实现
 */
@Slf4j
public class BeforePublishTraceMessagePostProcessor implements MessagePostProcessor, PriorityOrdered {
    /**
     * 发送消息时从线程MDC中取出标记和来源信息设置到消息头
     * @param message
     * @return
     * @throws AmqpException
     */
    @Override
    public Message postProcessMessage(Message message) throws AmqpException {
        Map<String, Object> headers = message.getMessageProperties().getHeaders();
        Optional.ofNullable(MDC.getCopyOfContextMap())
                .ifPresent(content ->
                        content.entrySet()
                                .stream()
                                .filter(e -> e.getKey().startsWith(ConstVar.TRACE_PREFIX) && e.getValue() != null)
                                .forEach(e -> headers.put(e.getKey(), e.getValue())));
        return message;
    }

    /**
     * 发送消息时从线程MDC中取出标记和来源信息设置到消息头
     * @param message
     * @param correlation
     * @return
     */
    @Override
    public Message postProcessMessage(Message message, Correlation correlation) {
        Map<String, Object> headers = message.getMessageProperties().getHeaders();
        Optional.ofNullable(MDC.getCopyOfContextMap())
                .ifPresent(content ->
                        content.entrySet()
                                .stream()
                                .filter(e -> e.getKey().startsWith(ConstVar.TRACE_PREFIX) && e.getValue() != null)
                                .forEach(e -> headers.put(e.getKey(), e.getValue())));
        return message;
    }

    /**
     * 排序
     * @return
     */
    @Override
    public int getOrder() {
        return -1000;
    }
}
