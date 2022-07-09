package cn.com.coderd.framework.starter.message;

import cn.com.coderd.framework.starter.constants.ConstVar;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageBuilder;

import java.util.Optional;

/**
 * 消息增加来源以及标记追踪功能实现类
 */
@Slf4j
public class TraceChannelInterceptor implements ChannelInterceptor {

    private static final InheritableThreadLocal<Boolean> SET_BY_ME = new InheritableThreadLocal<>();

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        if (MDC.getCopyOfContextMap() == null
                || MDC.getCopyOfContextMap().keySet().stream().noneMatch(e -> e.startsWith(ConstVar.TRACE_PREFIX))) {
            message.getHeaders().entrySet().stream()
                    .filter(e -> e.getKey().startsWith(ConstVar.TRACE_PREFIX) && e.getValue() != null)
                    .forEach(e -> MDC.put(e.getKey(), String.valueOf(e.getValue())));
            SET_BY_ME.set(Boolean.TRUE);
            return message;
        }
        if (message.getHeaders().keySet().stream().noneMatch(e -> e.startsWith(ConstVar.TRACE_PREFIX))) {
            MessageBuilder<?> builder = MessageBuilder.fromMessage(message);
            MDC.getCopyOfContextMap().entrySet()
                    .stream()
                    .filter(e -> e.getKey().startsWith(ConstVar.TRACE_PREFIX) && e.getValue() != null)
                    .forEach(e -> builder.setHeader(e.getKey(), e.getValue()));
            return builder.build();
        }
        return message;
    }

    @Override
    public void afterSendCompletion(Message<?> message, MessageChannel channel, boolean sent, Exception ex) {
        if (Boolean.TRUE.equals(SET_BY_ME.get())) {
            Optional.ofNullable(MDC.getCopyOfContextMap()).ifPresent(c -> c.keySet()
                    .stream()
                    .filter(e -> e.startsWith(ConstVar.TRACE_PREFIX)).forEach(MDC::remove));
            SET_BY_ME.remove();
        }
    }

    @Override
    public Message<?> postReceive(Message<?> message, MessageChannel channel) {
        if (MDC.getCopyOfContextMap() == null
                || MDC.getCopyOfContextMap().keySet().stream().noneMatch(e -> e.startsWith(ConstVar.TRACE_PREFIX))) {
            message.getHeaders().entrySet().stream()
                    .filter(e -> e.getKey().startsWith(ConstVar.TRACE_PREFIX) && e.getValue() != null)
                    .forEach(e -> MDC.put(e.getKey(), String.valueOf(e.getValue())));
            SET_BY_ME.set(Boolean.TRUE);
            return message;
        }
        return message;
    }

    @Override
    public void afterReceiveCompletion(Message<?> message, MessageChannel channel, Exception ex) {
        if (Boolean.TRUE.equals(SET_BY_ME.get())) {
            Optional.ofNullable(MDC.getCopyOfContextMap()).ifPresent(c -> c.keySet()
                    .stream()
                    .filter(e -> e.startsWith(ConstVar.TRACE_PREFIX)).forEach(MDC::remove));
            SET_BY_ME.remove();
        }
    }
}
