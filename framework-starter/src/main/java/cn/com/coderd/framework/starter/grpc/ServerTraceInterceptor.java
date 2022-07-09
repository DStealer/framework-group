package cn.com.coderd.framework.starter.grpc;

import cn.com.coderd.framework.starter.constants.ConstVar;
import io.grpc.*;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import java.util.HashMap;
import java.util.Map;

/**
 * 添加grpc server标记功能
 */
@Slf4j
public class ServerTraceInterceptor implements ServerInterceptor {
    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> serverCall, Metadata metadata, ServerCallHandler<ReqT, RespT> serverCallHandler) {
        Map<String, String> map = new HashMap<>();
        metadata.keys().stream()
                .filter(e -> e.startsWith(ConstVar.TRACE_PREFIX))
                .forEach(e -> map.put(e, metadata.get(Metadata.Key.of(e, Metadata.ASCII_STRING_MARSHALLER))));
        return new ForwardingServerCallListener.SimpleForwardingServerCallListener<ReqT>(serverCallHandler.startCall(serverCall, metadata)) {

            @Override
            public void onReady() {
                map.forEach(MDC::put);
                super.onReady();
            }

            @Override
            public void onCancel() {
                MDC.clear();
                super.onCancel();
            }

            @Override
            public void onComplete() {
                MDC.clear();
                super.onComplete();
            }
        };
    }
}