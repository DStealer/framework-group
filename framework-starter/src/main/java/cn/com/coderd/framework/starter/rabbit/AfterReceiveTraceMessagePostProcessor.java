package cn.com.coderd.framework.starter.rabbit;

import cn.com.coderd.framework.starter.constants.ConstVar;
import org.slf4j.MDC;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Correlation;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.core.PriorityOrdered;


/**
 * rabbit接收消息后处理器
 */
public class AfterReceiveTraceMessagePostProcessor implements MessagePostProcessor, PriorityOrdered {
    /**
     * 接收消息后,从消息头中取出标记设置到线程MDC中
     *
     * @param message
     * @return
     * @throws AmqpException
     */
    @Override
    public Message postProcessMessage(Message message) throws AmqpException {
        message.getMessageProperties().getHeaders().entrySet().stream()
                .filter(e -> e.getKey().startsWith(ConstVar.TRACE_PREFIX) && e.getValue() != null)
                .forEach(e -> MDC.put(e.getKey(), String.valueOf(e.getValue())));
        return message;
    }

    /**
     * 接收消息后,从消息头中取出标记设置到线程MDC中
     *
     * @param message
     * @param correlation
     * @return
     */
    @Override
    public Message postProcessMessage(Message message, Correlation correlation) {
        message.getMessageProperties().getHeaders().entrySet().stream()
                .filter(e -> e.getKey().startsWith(ConstVar.TRACE_PREFIX) && e.getValue() != null)
                .forEach(e -> MDC.put(e.getKey(), String.valueOf(e.getValue())));
        return message;
    }

    /**
     * 处理器顺序
     *
     * @return
     */
    @Override
    public int getOrder() {
        return -1000;
    }
}
