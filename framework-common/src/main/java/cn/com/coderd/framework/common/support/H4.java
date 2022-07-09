package cn.com.coderd.framework.common.support;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class H4<T1, T2, T3, T4> implements Serializable {
    private static final long serialVersionUID = -1239262926434162736L;
    private T1 t1;
    private T2 t2;
    private T3 t3;
    private T4 t4;

}
