package cn.com.coderd.framework.common.support;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class H2<T1, T2> implements Serializable {
    private static final long serialVersionUID = -8250376693811524616L;
    private T1 t1;
    private T2 t2;
}
