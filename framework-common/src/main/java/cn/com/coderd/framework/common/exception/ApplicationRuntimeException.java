package cn.com.coderd.framework.common.exception;

/**
 * 应用异常,使用该类如果包含cause并且直接返回消费端,请确保该异常类型存在于消费端代码中
 */
public class ApplicationRuntimeException extends RuntimeException {

    private static final long serialVersionUID = 3853936259550847144L;

    public ApplicationRuntimeException(String message) {
        super(message);
    }

    public ApplicationRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApplicationRuntimeException(String message, Object... args) {
        super(String.format(message, args));
    }

    public ApplicationRuntimeException(Throwable cause, String message, Object... args) {
        super(String.format(message, args), cause);
    }
}
