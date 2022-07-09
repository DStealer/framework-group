package cn.com.coderd.framework.starter.dubbo;

import cn.com.coderd.framework.starter.constants.ConstVar;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;
import org.slf4j.MDC;

import java.util.Optional;

/**
 * dubbo消费端增加携带标记信息功能实现类
 */
@Slf4j
@Activate(group = CommonConstants.CONSUMER)
public class ConsumerTraceFilter implements Filter {
    public ConsumerTraceFilter() {
        log.info("配置dubbo consumer trace filter 完成");
    }

    /**
     * 从当前线程MDC取出标记添加到请求上下文中
     * @param invoker
     * @param invocation
     * @return
     * @throws RpcException
     */
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        Optional.ofNullable(MDC.getCopyOfContextMap())
                .ifPresent(content ->
                        content.entrySet()
                                .stream()
                                .filter(e -> e.getKey().startsWith(ConstVar.TRACE_PREFIX) && e.getValue() != null)
                                .forEach(e -> invocation.setAttachment(e.getKey(), e.getValue())));
        return invoker.invoke(invocation);
    }
}
