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
     * 操作返分页结果
     */
    private PageData<T> pageData;

    /**
     * 失败响应结果
     *
     * @param code 响应码
     * @param msg  响应消息
     * @return
     */
    public static <T> Result<T> fail(String code, String msg) {
        return new Result<>(code, msg, null, null);
    }

    /**
     * 成功响应结果
     *
     * @param data 响应数据
     * @param <T>  响应内容实体
     * @return
     */
    public static <T> Result<T> ok(T data, PageData<T> pageData) {
        return new Result<>("NA", "操作成功", data, pageData);
    }

    /**
     * 自定义响应
     *
     * @param code
     * @param <T>
     * @return
     */
    public static <T> Result<T> custom(String code, String msg, T data, PageData<T> pageData) {
        return new Result<>(code, msg, data, pageData);
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
        return new Result<>(this.code, this.msg, null, null);
    }

    /**
     * 进行数据类型转换
     *
     * @param mapper
     * @param <E>
     * @return
     */
    public <E> E mapData(Function<T, E> mapper) {
        return this.data == null ? null : mapper.apply(this.data);
    }

    /**
     * 进行数据类型转换
     *
     * @param mapper
     * @param <E>
     * @return
     */
    public <E> PageData<E> mapPageData(Function<T, E> mapper) {
        return this.pageData == null ? null : this.pageData.map(mapper);
    }

    /**
     * 进行类型转换
     *
     * @param mapper
     * @param <E>
     * @return
     */
    @SuppressWarnings("unchecked")
    public <E> Result<E> mapResult(Function<T, E> mapper) {
        E data = this.data == null ? null : mapper.apply(this.data);
        PageData<E> pageData = this.pageData == null ? null : this.pageData.map(mapper);
        return new Result<>(this.code, this.msg, data, pageData);
    }

    /**
     * 如果成功响应则返回数据,否则使用other替换
     *
     * @param other
     * @return
     */
    public T orElseGetData(Supplier<T> other) {
        return this.ok() ? this.data : other.get();
    }

    /**
     * 如果成功响应则返回数据,否则使用other替换
     *
     * @param other
     * @return
     */
    public PageData<T> orElseGetPageData(Supplier<PageData<T>> other) {
        return this.ok() ? this.pageData : other.get();
    }

    /**
     * 如果成功响应则返回数据,否则使用other替换
     *
     * @param other
     * @return
     */
    public Result<T> orElseGetResult(Supplier<Result<T>> other) {
        return this.ok() ? this : other.get();
    }


    /**
     * 抛出异常或者返回数据
     *
     * @return
     */
    public T orElseThrowData() {
        if (this.ok()) {
            return this.data;
        } else {
            throw new ResultRuntimeException(this.code, this.msg, (Object[]) null);
        }
    }

    /**
     * 抛出异常或者返回数据
     *
     * @return
     */
    public PageData<T> orElseThrowPageData() {
        if (this.ok()) {
            return this.pageData;
        } else {
            throw new ResultRuntimeException(this.code, this.msg, (Object[]) null);
        }
    }

    /**
     * 抛出异常或者返回数据
     *
     * @return
     */
    public T orElseThrowData(Supplier<? extends RuntimeException> supplier) {
        if (this.ok()) {
            return this.data;
        } else {
            throw supplier.get();
        }
    }

    /**
     * 抛出异常或者返回数据
     *
     * @return
     */
    public PageData<T> orElseThrowPageData(Supplier<? extends RuntimeException> supplier) {
        if (this.ok()) {
            return this.pageData;
        } else {
            throw supplier.get();
        }
    }
}
