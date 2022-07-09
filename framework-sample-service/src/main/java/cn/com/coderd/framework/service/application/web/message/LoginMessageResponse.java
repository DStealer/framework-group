package cn.com.coderd.framework.service.application.web.message;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class LoginMessageResponse implements Serializable {
    private String content;
}
