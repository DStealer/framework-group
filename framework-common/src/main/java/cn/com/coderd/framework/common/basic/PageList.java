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
public class PageList<T> implements Serializable {
    private static final long serialVersionUID = 4146983228477375811L;
    private List<T> list;
    private long pageIndex;
    private long totalPage;
    private long pageSize;
    private long totalSize;
    private Map<String, Object> additional;

    /**
     * 通过分页参数控制
     *
     * @param pageInfo
     * @param <E>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <E> PageList<E> withEmpty(PageInfo pageInfo) {
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
    public static <E> PageList<E> with(PageInfo pageInfo, long totalSize, List<E> data) {
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
    public static <E> PageList<E> with(long pageIndex, long pageSize, long totalSize, List<E> data) {
        PageList<E> pageList = new PageList<>();
        pageList.setList(data);
        pageList.setPageIndex(pageIndex);
        pageList.setPageSize(pageSize);
        pageList.setTotalSize(totalSize);
        if (pageSize > 0) {
            pageList.setTotalPage(totalSize / pageSize + (totalSize % pageSize == 0L ? 0L : 1L));
        } else {
            pageList.setTotalPage(totalSize);
        }
        return pageList;
    }

    /**
     * 获取第一个数据,快捷封装
     *
     * @return
     */
    public T one() {
        if (this.list == null || this.list.isEmpty()) {
            return null;
        }
        return this.list.get(0);
    }

    /**
     * 转换集合
     *
     * @param converter
     * @param <U>
     * @return
     */
    public <U> PageList<U> map(Function<? super T, ? extends U> converter) {
        PageList<U> pageList = new PageList<>();
        pageList.setPageIndex(this.pageIndex);
        pageList.setPageSize(this.pageSize);
        pageList.setTotalPage(this.totalPage);
        pageList.setTotalSize(this.totalSize);
        pageList.setList(this.list.stream()
                .map(converter)
                .collect(Collectors.toList()));
        pageList.setAdditional(this.additional);
        return pageList;
    }
}
