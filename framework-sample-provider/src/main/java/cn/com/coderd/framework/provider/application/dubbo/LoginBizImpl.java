package cn.com.coderd.framework.provider.application.dubbo;

import cn.com.coderd.framework.common.basic.Result;
import cn.com.coderd.framework.provider.domain.entity.TUser;
import cn.com.coderd.framework.provider.domain.service.ITUserService;
import cn.com.coderd.framework.service.application.dubbo.login.LoginBiz;
import cn.com.coderd.framework.service.application.dubbo.login.LoginDO;
import cn.com.coderd.framework.service.application.dubbo.login.LoginDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 用户登陆实现
 */
@Slf4j
@DubboService(group = "", version = "")
public class LoginBizImpl implements LoginBiz {
    @Autowired
    private ITUserService userService;

    @Override
    public Result<LoginDO> login(LoginDTO dto) {
        if (dto == null || dto.getUsername() == null || dto.getUsername().isEmpty()
                || dto.getPassword() == null || dto.getPassword().isEmpty()) {
            return Result.fail("GA0001", "参数错误");
        }
        log.info("用户登陆:{}", dto);
        TUser one = userService.lambdaQuery().eq(TUser::getMobile, dto.getUsername())
                .one();
        if (one == null) {
            return Result.fail("SP0001", "用户不存在");
        }
        if (!one.getPassword().equals(dto.getPassword())) {
            return Result.fail("SP0001", "密码错误");
        }
        log.info("用户:{}登陆成功", one);
        return Result.ok(new LoginDO());
    }
}
