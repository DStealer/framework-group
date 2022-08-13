package cn.com.coderd.framework.common.basic;

import cn.com.coderd.framework.common.exception.ResultRuntimeException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 封装返回值
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Result<T> implements Serializable {

    private static final long serialVersionUID = -8728907194461430786L;
    /**
     * 一般用于响应前端
     */
    private String code;
    /**
     * 响应消息
     */
    private String msg;

    /**
     * 操作返回结果
     */
    private T data;

    /**
     * 失败响应结果
     *
     * @param code 响应码
     * @param msg  响应消息
     * @return
     */
    public static <T> Result<T> fail(String code, String msg) {
        return new Result<>(code, msg, null);
    }

    /**
     * 自定义响应
     *
     * @param code
     * @param <T>
     * @return
     */
    public static <T> Result<T> custom(String code, String msg, T data) {
        return new Result<>(code, msg, data);
    }

    /**
     * 成功响应结果
     *
     * @param data 响应数据
     * @param <T>  响应内容实体
     * @return
     */
    public static <T> Result<T> ok(T data) {
        return new Result<>("NA", "操作成功", data);
    }
    /**
     * 成功响应结果
     *
     * @param msg  响应消息
     * @param data 响应数据
     * @param <T>  响应内容实体
     * @return
     */
    public static <T> Result<T> ok(String msg, T data) {
        return new Result<>("NA", msg, data);
    }

    /**
     * 判断请求是否成功
     *
     * @return
     */
    public boolean ok() {
        return this.code != null && this.code.equals("NA");
    }

    /**
     * 泛化类型,仅保留状态码和消息
     *
     * @param <E>
     * @return
     */
    public <E> Result<E> toGeneric() {
        return new Result<>(this.code, this.msg, null);
    }

    /**
     * 进行数据类型转换
     *
     * @param mapper
     * @param <E>
     * @return
     */
    public <E> E map(Function<T, E> mapper) {
        return this.data == null ? null : mapper.apply(this.data);
    }

    /**
     * 如果是成功响应,则进行类型转换,否则置空data并返回自身
     *
     * @param mapper
     * @param <E>
     * @return
     */
    @SuppressWarnings("unchecked")
    public <E> Result<E> mapResultIfOk(Function<T, E> mapper) {
        if (this.ok()) {
            return new Result<>(this.code, this.msg, mapper.apply(this.data));
        } else {
            this.data = null;//置空data防止运行错误
            return (Result<E>) this;
        }
    }

    /**
     * 如果成功响应则返回自身,否则使用other替换
     *
     * @param other
     * @return
     */
    public T orElseGet(Supplier<T> other) {
        return this.ok() ? this.getData() : other.get();
    }

    /**
     * 抛出异常或者返回自身
     *
     * @return
     */
    public T orElseThrow() {
        if (this.ok()) {
            return this.getData();
        } else {
            throw new ResultRuntimeException(this.code, this.msg, (Object[]) null);
        }
    }

    /**
     * 抛出异常或者返回自身
     *
     * @return
     */
    public T orElseThrow(Supplier<? extends RuntimeException> supplier) {
        if (this.ok()) {
            return this.getData();
        } else {
            throw supplier.get();
        }
    }

    /**
     * 抛出异常或者返回自身
     *
     * @return
     */
    public T orElseThrow(Function<Result<T>, ? extends RuntimeException> mapper) {
        if (this.ok()) {
            return this.getData();
        } else {
            throw mapper.apply(this);
        }
    }
}
