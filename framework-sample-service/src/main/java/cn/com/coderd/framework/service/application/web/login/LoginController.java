package cn.com.coderd.framework.service.application.web.login;

import cn.com.coderd.framework.common.basic.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/login")
public interface LoginController {
    /**
     * Login
     *
     * @param request
     * @return
     */
    @PostMapping("/app/login")
    Result<LoginResponse> login(@RequestBody LoginRequest request);
}
