package cn.com.coderd.framework.common.support;

import lombok.Data;

import java.io.Serializable;

@Data
public class EnumVO implements Serializable {
    /**
     * 枚举类型编码
     */
    private String code;
    /**
     * 枚举类型描述
     */
    private String desc;
}
