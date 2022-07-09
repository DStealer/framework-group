package cn.com.coderd.framework.common.support;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class H9<T1, T2, T3, T4, T5, T6, T7, T8, T9> implements Serializable {
    private static final long serialVersionUID = 3355111521142724804L;
    private T1 t1;
    private T2 t2;
    private T3 t3;
    private T4 t4;
    private T5 t5;
    private T6 t6;
    private T7 t7;
    private T8 t8;
    private T9 t9;
}
