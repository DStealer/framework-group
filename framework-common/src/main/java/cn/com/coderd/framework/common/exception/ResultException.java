package cn.com.coderd.framework.common.exception;


import cn.com.coderd.framework.common.basic.Result;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 业务异常 使用该类如果包含cause并且直接返回消费端,请确保该异常类型存在于消费端代码中
 */
@Data
@Accessors(chain = true)
public class ResultException extends Exception {
    private static final long serialVersionUID = -4510219829111679958L;
    private String statusCode;
    private String statusMessage;
    private String debugInfo;

    public ResultException() {

    }

    public ResultException(String statusCode, String statusMessage, Object... args) {
        super(String.format("[%s]-[%s]", statusCode, String.format(statusMessage, args)));
        this.statusCode = statusCode;
        this.statusMessage = String.format(statusMessage, args);
    }

    public ResultException(Throwable cause, String statusCode, String statusMessage, Object... args) {
        super(String.format("[%s]-[%s]", statusCode, String.format(statusMessage, args)), cause, false, true);
        this.statusCode = statusCode;
        this.statusMessage = String.format(statusMessage, args);
    }

    public ResultException(Throwable cause, Boolean writableStackTrace, String statusCode, String statusMessage, Object... args) {
        super(String.format("[%s]-[%s]", statusCode, String.format(statusMessage, args)), cause, false, writableStackTrace);
        this.statusCode = statusCode;
        this.statusMessage = String.format(statusMessage, args);
    }

    public Result<?> toResult() {
        return new Result<>(this.statusCode, this.statusMessage, null, null);
    }

}
