package cn.com.coderd.framework.starter.mybatis;

import org.apache.ibatis.logging.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * mybatis日志桥接到slf4j日志实现
 */
public final class MybatisLog implements Log {
    private final Logger logger;

    public MybatisLog(String logId) {
        if (logId.contains(".dao.") || logId.contains("DAO") || logId.contains("Dao")) {
            this.logger = LoggerFactory.getLogger(MybatisLog.this.abbreviate(logId));
        } else {
            this.logger = LoggerFactory.getLogger(logId);
        }
    }

    @Override
    public boolean isDebugEnabled() {
        return logger.isDebugEnabled();
    }

    @Override
    public boolean isTraceEnabled() {
        return logger.isTraceEnabled();
    }

    @Override
    public void error(String s, Throwable e) {
        logger.error(s, e);
    }


    @Override
    public void error(String s) {
        logger.error(s);
    }

    @Override
    public void debug(String s) {
        logger.debug(s);
    }

    @Override
    public void trace(String s) {
        logger.trace(s);
    }

    @Override
    public void warn(String s) {
        logger.warn(s);
    }

    /**
     * 压缩路径,保留最后两位
     *
     * @param fqn
     * @return
     */
    private String abbreviate(String fqn) {
        if (fqn == null) {
            throw new IllegalArgumentException("Class name may not be null");
        }
        String[] split = fqn.split("\\.");
        if (split.length <= 2) {
            return fqn;
        }
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < split.length - 2; i++) {
            buf.append(split[i], 0, 1).append(".");
        }
        buf.append(split[split.length - 2])
                .append(".")
                .append(split[split.length - 1]);
        return buf.toString();
    }
}
