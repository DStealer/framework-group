package cn.com.coderd.framework.common.support;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class H3<T1, T2, T3> implements Serializable {
    private static final long serialVersionUID = -6267658469847698942L;
    private T1 t1;
    private T2 t2;
    private T3 t3;
}
