package cn.com.coderd.framework.service.pojo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class Entity implements Serializable {
    private String message;
}
