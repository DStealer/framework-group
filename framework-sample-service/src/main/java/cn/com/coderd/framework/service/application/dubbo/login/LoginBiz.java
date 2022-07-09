package cn.com.coderd.framework.service.application.dubbo.login;

import cn.com.coderd.framework.common.basic.Result;

/**
 * 登陆
 */
public interface LoginBiz {
    /**
     * 用户登陆
     *
     * @param dto
     * @return
     */
    Result<LoginDO> login(LoginDTO dto);
}
