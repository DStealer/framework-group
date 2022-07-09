package cn.com.coderd.framework.service.application.web.login;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class LoginResponse implements Serializable {
    private String token;
}
