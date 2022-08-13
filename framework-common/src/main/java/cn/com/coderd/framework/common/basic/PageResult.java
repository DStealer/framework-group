package cn.com.coderd.framework.common.basic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 包装分页返回集合
 *
 * @param <T>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class PageResult<T> implements Serializable {
    private static final long serialVersionUID = 4146983228477375811L;
    /**
     * 一般用于响应前端
     */
    private String code = "NA";
    /**
     * 响应消息
     */
    private String msg = "";
    /**
     * 当前页索引
     */
    private Long pageIndex;
    /**
     * 当前页大小
     */
    private Long pageSize;
    /**
     * 总页数
     */
    private Long totalPage;
    /**
     * 总条数
     */
    private Long totalSize;
    /**
     * 记录
     */
    private List<T> records;
    /**
     * 额外信息
     */
    private Map<String, Object> additional;

    /**
     * 通过分页参数控制
     *
     * @param pageInfo
     * @param <E>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <E> PageResult<E> withEmpty(PageInfo pageInfo) {
        return with(pageInfo.getPageIndex(), pageInfo.getSize(), 0L, Collections.EMPTY_LIST);
    }

    /**
     * 通过分页参数控制
     *
     * @param pageInfo
     * @param totalSize
     * @param data
     * @param <E>
     * @return
     */
    public static <E> PageResult<E> with(PageInfo pageInfo, long totalSize, List<E> data) {
        return with(pageInfo.getPageIndex(), pageInfo.getSize(), totalSize, data);
    }

    /**
     * 通过分页参数控制
     *
     * @param pageIndex
     * @param pageSize
     * @param totalSize
     * @param data
     * @param <E>
     * @return
     */
    public static <E> PageResult<E> with(long pageIndex, long pageSize, long totalSize, List<E> data) {
        PageResult<E> pageResult = new PageResult<>();
        pageResult.setRecords(data);
        pageResult.setPageIndex(pageIndex);
        pageResult.setPageSize(pageSize);
        pageResult.setTotalSize(totalSize);
        if (pageSize > 0) {
            pageResult.setTotalPage(totalSize / pageSize + (totalSize % pageSize == 0L ? 0L : 1L));
        } else {
            pageResult.setTotalPage(totalSize);
        }
        return pageResult;
    }

    /**
     * 获取第一个数据,快捷封装
     *
     * @return
     */
    public T one() {
        if (this.records == null || this.records.isEmpty()) {
            return null;
        }
        return this.records.get(0);
    }

    /**
     * 转换集合
     *
     * @param converter
     * @param <U>
     * @return
     */
    public <U> PageResult<U> map(Function<? super T, ? extends U> converter) {
        PageResult<U> pageResult = new PageResult<>();
        pageResult.setPageIndex(this.pageIndex);
        pageResult.setPageSize(this.pageSize);
        pageResult.setTotalPage(this.totalPage);
        pageResult.setTotalSize(this.totalSize);
        pageResult.setRecords(this.records.stream()
                .map(converter)
                .collect(Collectors.toList()));
        pageResult.setAdditional(this.additional);
        return pageResult;
    }
}
