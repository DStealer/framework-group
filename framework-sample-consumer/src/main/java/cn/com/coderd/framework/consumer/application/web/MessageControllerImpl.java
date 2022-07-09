package cn.com.coderd.framework.consumer.application.web;

import cn.com.coderd.framework.common.basic.Result;
import cn.com.coderd.framework.service.application.message.message.LoginMessageDTO;
import cn.com.coderd.framework.service.application.web.message.LoginMessageRequest;
import cn.com.coderd.framework.service.application.web.message.LoginMessageResponse;
import cn.com.coderd.framework.service.application.web.message.MessageController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@RestController
public class MessageControllerImpl implements MessageController {
    @Autowired
    private StreamBridge streamBridge;

    @Override
    public Result<LoginMessageResponse> loginMessage(LoginMessageRequest request) {
        if (request == null || request.getMobile() == null || request.getMobile().isEmpty()) {
            return Result.fail("GA0001", "参说错误");
        }
        LoginMessageDTO dto = new LoginMessageDTO()
                .setMobile(request.getMobile());
        dto.setContent("您的验证码是:" + ThreadLocalRandom.current().nextInt());
        boolean send = streamBridge.send("login-message", dto);
        if (send) {
            log.info("消息发送成功:{}", dto);
            return Result.ok(new LoginMessageResponse().setContent(dto.getContent()));
        } else {
            log.info("消息发送失败:{}", dto);
            return Result.fail("GA0002", "发送失败");
        }
    }
}
