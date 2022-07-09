package cn.com.coderd.framework.service.application.dubbo.login;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class LoginDTO implements Serializable {
    private String username;
    private String password;
}
