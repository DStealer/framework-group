package cn.com.coderd.framework.consumer.application.web;

import cn.com.coderd.framework.common.basic.Result;
import cn.com.coderd.framework.service.application.web.hello.HelloController;
import cn.com.coderd.framework.service.application.web.hello.HelloRequest;
import cn.com.coderd.framework.service.application.web.hello.HelloResponse;
import cn.com.coderd.framework.service.generated.GreeterGrpc;
import cn.com.coderd.framework.service.generated.HelloReply;
import cn.com.coderd.framework.service.generated.HelloReq;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class GrpcController implements HelloController {
    @GrpcClient("framework-sample-provider")
    private GreeterGrpc.GreeterBlockingStub stub;


    @Override
    public Result<HelloResponse> hello(HelloRequest request) {
        log.info("some one say hello to:{}", request.getName());
        HelloReply reply = stub.sayHello(HelloReq.newBuilder()
                .setName(request.getName()).build());
        log.info("backend reply:{}", reply.getMessage());
        return Result.ok(new HelloResponse()
                .setMessage(reply.getMessage()), null);
    }
}
