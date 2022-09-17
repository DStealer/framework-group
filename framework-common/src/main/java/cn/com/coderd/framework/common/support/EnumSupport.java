package cn.com.coderd.framework.common.support;

public interface EnumSupport<K, T extends Enum<T>> {
    /**
     * 标识符
     *
     * @return
     */
    K code();

    /**
     * 描述
     *
     * @return
     */
    String desc();

    /**
     * 比较
     *
     * @param code
     * @return
     */
    default boolean compare(K code) {
        return this.code().equals(code);
    }

    /**
     * 比较
     *
     * @param obj
     * @return
     */
    default boolean compare(T obj) {
        return this.equals(obj);
    }
}
