package cn.com.coderd.framework.provider.application.grpc;

import cn.com.coderd.framework.service.generated.GreeterGrpc;
import cn.com.coderd.framework.service.generated.HelloReply;
import cn.com.coderd.framework.service.generated.HelloReq;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

@Slf4j
@GrpcService
public class GreeterGrpcImpl extends GreeterGrpc.GreeterImplBase {
    @Override
    public void sayHello(HelloReq request, StreamObserver<HelloReply> responseObserver) {
        HelloReply reply = HelloReply.newBuilder().setMessage("hello also").build();
        log.info("someone say hello to: {} ,i reply :{}", request.getName(), reply.getMessage());
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }
}
