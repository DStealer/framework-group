package cn.com.coderd.framework.starter.constants;

/**
 * 全局常量定义
 */
public interface ConstVar {
    /**
     * 标记前缀
     */
    String TRACE_PREFIX = "t-";
    /**
     * 环境感知配置,主要目的是区分环境加载不同的bean,例如在测试环境某些场景需要增加规则校验,而生产环境因性能而不需要
     */
    String ENV_PRODUCT = "env.product";
    /**
     * 灰度标签
     */
    String TAG_GRAY_KEY = "t-gray";
    /**
     * 基线版本
     */
    String TAG_BASELINE = "baseline";

}
