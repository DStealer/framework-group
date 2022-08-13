package cn.com.coderd.framework.consumer.application.web;

import cn.com.coderd.framework.common.basic.Result;
import cn.com.coderd.framework.service.application.dubbo.login.LoginBiz;
import cn.com.coderd.framework.service.application.dubbo.login.LoginDO;
import cn.com.coderd.framework.service.application.dubbo.login.LoginDTO;
import cn.com.coderd.framework.service.application.web.login.LoginController;
import cn.com.coderd.framework.service.application.web.login.LoginRequest;
import cn.com.coderd.framework.service.application.web.login.LoginResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.RestController;

/**
 * 登陆相关接口
 */
@Slf4j
@RestController
public class LoginControllerImpl implements LoginController {

    @DubboReference(group = "", version = "", check = false)
    private LoginBiz biz;

    /**
     * 登陆请求
     *
     * @param request
     * @return
     */
    @Override
    public Result<LoginResponse> login(LoginRequest request) {
        if (request.getUsername() == null || request.getUsername().isEmpty()
                || request.getPassword() == null || request.getPassword().isEmpty()) {
            return Result.fail("GA0001", "参数错误");
        }
        log.info("登陆请求:{}", request);
        Result<LoginDO> result = biz.login(new LoginDTO()
                .setUsername(request.getUsername())
                .setPassword(request.getPassword()));

        return result.mapResult(e -> new LoginResponse().setToken("abc"));
    }
}
