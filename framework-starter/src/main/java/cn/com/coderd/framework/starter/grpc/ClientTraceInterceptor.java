package cn.com.coderd.framework.starter.grpc;

import cn.com.coderd.framework.starter.constants.ConstVar;
import io.grpc.*;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import java.util.Map;

import static io.grpc.Metadata.ASCII_STRING_MARSHALLER;

/**
 * 添加grpc client 标记功能
 */
@Slf4j
public class ClientTraceInterceptor implements ClientInterceptor {
    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> methodDescriptor, CallOptions callOptions, Channel channel) {
        Map<String, String> map = MDC.getCopyOfContextMap();

        return new ForwardingClientCall.SimpleForwardingClientCall<ReqT, RespT>(channel.newCall(methodDescriptor, callOptions)) {
            @Override
            public void start(Listener<RespT> responseListener, Metadata headers) {
                if (map != null && !map.isEmpty()) {
                    for (Map.Entry<String, String> entry : map.entrySet()) {
                        if (entry.getKey().startsWith(ConstVar.TRACE_PREFIX) && entry.getValue() != null) {
                            headers.put(Metadata.Key.of(entry.getKey(), ASCII_STRING_MARSHALLER), entry.getValue());
                        }
                    }
                }
                super.start(responseListener, headers);
            }
        };
    }
}
