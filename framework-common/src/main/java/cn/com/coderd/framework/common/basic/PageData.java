package cn.com.coderd.framework.common.basic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
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
public class PageData<T> implements Serializable {
    private static final long serialVersionUID = 4146983228477375811L;
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
    private Map<String, Object> extras;

    /**
     * 通过分页参数控制
     *
     * @param pageInfo
     * @param <E>
     * @return
     */
    public static <E> PageData<E> with(PageInfo<?> pageInfo) {
        return with(pageInfo.getPageIndex(), pageInfo.getSize(), 0L, Collections.emptyList());
    }

    /**
     * 通过分页参数控制
     *
     * @param pageInfo
     * @param totalSize
     * @param records
     * @param <E>
     * @return
     */
    public static <E> PageData<E> with(PageInfo<?> pageInfo, Long totalSize, List<E> records) {
        return with(pageInfo.getPageIndex(), pageInfo.getSize(), totalSize, records);
    }

    /**
     * 通过分页参数控制
     *
     * @param pageIndex
     * @param pageSize
     * @param totalSize
     * @param records
     * @param <E>
     * @return
     */
    public static <E> PageData<E> with(Long pageIndex, Long pageSize, Long totalSize, List<E> records) {
        PageData<E> pageResult = new PageData<>();
        pageResult.setRecords(records);
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
     * 设置额外信息
     *
     * @param key
     * @param value
     * @return
     */
    public Map<String, Object> addExtra(String key, Object value) {
        if (this.extras == null) {
            this.extras = new HashMap<>();
        }
        this.extras.put(key, value);
        return this.extras;
    }

    /**
     * 获取第一个数据,快捷封装
     *
     * @return
     */
    public T first() {
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
    public <U> PageData<U> map(Function<? super T, ? extends U> converter) {
        PageData<U> pageResult = new PageData<>();
        pageResult.setPageIndex(this.pageIndex);
        pageResult.setPageSize(this.pageSize);
        pageResult.setTotalPage(this.totalPage);
        pageResult.setTotalSize(this.totalSize);
        pageResult.setRecords(this.records.stream()
                .map(converter)
                .collect(Collectors.toList()));
        pageResult.setExtras(this.extras);
        return pageResult;
    }
}
