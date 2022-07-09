package cn.com.coderd.framework.service.application.message.message;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class LoginMessageDTO implements Serializable {
    private String mobile;
    private String content;
}
