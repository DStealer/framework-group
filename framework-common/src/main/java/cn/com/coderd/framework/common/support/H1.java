package cn.com.coderd.framework.common.support;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class H1<T> implements Serializable {
    private static final long serialVersionUID = -271961842894960256L;
    private T t1;
}
