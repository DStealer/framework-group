package cn.com.coderd.framework.common.basic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 分页控制参数,页数从1开始
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class PageInfo implements Serializable {
    /**
     * 不查询数据,只返回符合条件的数据数量
     */
    public static final PageInfo NO_DATA = new PageInfo(1L, 0L);
    /**
     * 一次查询所有数据
     */
    public static final PageInfo NO_LIMIT = new PageInfo(1L, Long.MAX_VALUE);
    /**
     * 默认值,每页查询20条数据
     */
    public static final PageInfo DEFAULT = new PageInfo(1L, 20L);

    private static final long serialVersionUID = 1847140040968172308L;
    /**
     * 当前码
     */
    private Long pageIndex = 1L;
    /**
     * 页容量
     */
    private Long pageSize = 20L;

    /**
     * 起始索引
     *
     * @return
     */
    public Long getOffset() {
        return this.pageIndex < 1L ? 0L : (this.pageIndex - 1L) * this.pageSize;
    }

    /**
     * 条数
     *
     * @return
     */
    public Long getSize() {
        return this.pageSize;
    }
}
