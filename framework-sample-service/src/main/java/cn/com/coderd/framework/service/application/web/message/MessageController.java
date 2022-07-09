package cn.com.coderd.framework.service.application.web.message;

import cn.com.coderd.framework.common.basic.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/message")
public interface MessageController {
    /**
     * 登陆短信
     *
     * @param request
     * @return
     */
    @PostMapping("/login")
    Result<LoginMessageResponse> loginMessage(@RequestBody LoginMessageRequest request);
}
