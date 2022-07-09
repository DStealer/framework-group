package cn.com.coderd.framework.starter.dubbo;

import cn.com.coderd.framework.starter.constants.ConstVar;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;
import org.slf4j.MDC;

import java.util.Map;

/**
 * dubbo提供端增加携带标记信息功能实现类
 */
@Slf4j
@Activate(group = CommonConstants.PROVIDER)
public class ProviderTraceFilter implements Filter {
    public ProviderTraceFilter() {
        log.info("配置dubbo provider trace filter 完成");
    }

    /**
     * 从当前请求上下文中取出标记设置到线程MDC中
     * @param invoker
     * @param invocation
     * @return
     * @throws RpcException
     */
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        Map<String, String> attachments = invocation.getAttachments();
        if (attachments != null) {
            attachments.entrySet().stream()
                    .filter(e -> e.getKey().startsWith(ConstVar.TRACE_PREFIX) && e.getValue() != null)
                    .forEach(e -> MDC.put(e.getKey(), e.getValue()));
        }
        try {
            return invoker.invoke(invocation);
        } finally {
            if (attachments != null) {
                attachments.keySet().stream()
                        .filter(e -> e.startsWith(ConstVar.TRACE_PREFIX))
                        .forEach(MDC::remove);
            }
        }
    }
}
