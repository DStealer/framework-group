package cn.com.coderd.framework.starter.web;

import cn.com.coderd.framework.common.basic.Result;
import cn.com.coderd.framework.common.exception.ApplicationException;
import cn.com.coderd.framework.common.exception.ApplicationRuntimeException;
import cn.com.coderd.framework.common.exception.ResultException;
import cn.com.coderd.framework.common.exception.ResultRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 全局异常处理。
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 处理基础异常
     *
     * @param ex Exception
     * @return Response
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler({ResultException.class})
    public Result<Object> handleResultException(ResultException ex) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        log.error("Handle ResultException on :{}", requestAttributes.getRequest().getRequestURI(), ex);
        return Result.custom(ex.getStatusCode(), ex.getStatusMessage(), null, null);
    }

    /**
     * 处理基础异常
     *
     * @param ex Exception
     * @return Response
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler({ResultRuntimeException.class})
    public Result<Object> handleResultRuntimeException(ResultRuntimeException ex) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        log.error("Handle ResultRuntimeException on :{}", requestAttributes.getRequest().getRequestURI(), ex);
        return Result.custom(ex.getStatusCode(), ex.getStatusMessage(), null, null);
    }

    /**
     * 处理应用异常
     *
     * @param ex Exception
     * @return Response
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler({ApplicationException.class})
    public Result<Object> handleResultRuntimeException(ApplicationException ex) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        log.error("Handle ApplicationException on :{}", requestAttributes.getRequest().getRequestURI(), ex);
        return Result.custom("GA000002", "应用异常请稍后重试", null, null);
    }

    /**
     * 处理应用异常
     *
     * @param ex Exception
     * @return Response
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler({ApplicationRuntimeException.class})
    public Result<Object> handleResultRuntimeException(ApplicationRuntimeException ex) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        log.error("Handle ApplicationRuntimeException on :{}", requestAttributes.getRequest().getRequestURI(), ex);
        return Result.custom("GA000003", "应用异常请稍后重试", null, null);
    }

    /**
     * 处理参数异常,使用 @org.springframework.validation.annotation.Validated
     *
     * @param ex BindException
     * @return Response
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler({BindException.class})
    public Result<Object> handleBindException(BindException ex) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        String errorMessage = ex.getBindingResult()
                .getAllErrors().stream()
                .findFirst()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .orElse("数据校验错误");
        log.error("Handle method argument not valid exception on :{}-{}", requestAttributes.getRequest().getRequestURI(), errorMessage);
        return Result.custom("GA000001", errorMessage, null, null);
    }

    /**
     * 处理参数异常 处理使用 Assert等判断的异常
     *
     * @param ex IllegalArgumentException
     * @return Response
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler({IllegalArgumentException.class})
    public Result<Object> handleIllegalArgumentException(IllegalArgumentException ex) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        log.error("Handle illegal argument exception on :{}-{}", requestAttributes.getRequest().getRequestURI(), ex.getMessage());
        return Result.custom("GA000001", ex.getMessage(), null, null);
    }

    /**
     * 处理异常
     *
     * @param ex Exception
     * @return Response
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler({Exception.class})
    public Result<Object> handleUnknownException(Exception ex) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        ResultException rex = ExceptionUtils.throwableOfType(ex, ResultException.class);
        if (rex != null) {
            log.error("Handle BizException on :{}, exception: {}", requestAttributes.getRequest().getRequestURI(), ex);
            return Result.custom(rex.getStatusCode(), rex.getStatusMessage(), null, null);
        }
        log.error("Handle unknown exception on :{} exception: {}", requestAttributes.getRequest().getRequestURI(), ex);
        return Result.custom("GA999999", "未预期异常,请联系项目管理人员", null, null);
    }
}