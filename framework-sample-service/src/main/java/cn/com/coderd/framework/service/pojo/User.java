package cn.com.coderd.framework.service.pojo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class User implements Serializable {
    private Long userId;
    private String userName;
    private Long orgId;
    private String orgName;
}
