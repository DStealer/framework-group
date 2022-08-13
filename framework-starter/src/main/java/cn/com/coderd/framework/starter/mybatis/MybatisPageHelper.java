package cn.com.coderd.framework.starter.mybatis;

import cn.com.coderd.framework.common.basic.PageInfo;
import cn.com.coderd.framework.common.basic.PageResult;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * mybatis plus 分页转换工具类
 */
@UtilityClass
public class MybatisPageHelper {

    /**
     * 转换Mybatis-plus分页
     *
     * @param <T>
     * @return
     */
    public static <T> IPage<T> toPage(PageInfo pageInfo) {
        Page<T> page = new Page<>();
        page.setCurrent(pageInfo.getPageIndex());
        page.setSize(pageInfo.getPageSize());
        page.setSearchCount(pageInfo.getPageIndex().compareTo(1L) != 0
                || pageInfo.getPageSize().compareTo(Long.MAX_VALUE) != 0);
        page.setOptimizeCountSql(true);
        return page;
    }

    /**
     * 从mybatis转换
     *
     * @param page
     * @param <E>
     * @return
     */
    public static <E> PageResult<E> with(IPage<E> page) {
        PageResult<E> pageResult = new PageResult<>();
        if (page.searchCount()) {
            pageResult.setPageIndex(page.getCurrent());
            pageResult.setPageSize(page.getSize());
            pageResult.setTotalSize(page.getTotal());
            pageResult.setTotalPage(page.getPages());
        } else {
            pageResult.setPageIndex(1L);
            pageResult.setPageSize((long) page.getRecords().size());
            pageResult.setTotalSize((long) page.getRecords().size());
            pageResult.setTotalPage(1L);
        }
        pageResult.setRecords(page.getRecords());
        return pageResult;
    }

    /**
     * 从mybatis转换
     *
     * @param page
     * @param <E>
     * @return
     */
    public static <E> PageResult<E> with(IPage<E> page, List<E> data) {
        PageResult<E> pageResult = new PageResult<>();
        if (page.searchCount()) {
            pageResult.setPageIndex(page.getCurrent());
            pageResult.setPageSize(page.getSize());
            pageResult.setTotalSize(page.getTotal());
            pageResult.setTotalPage(page.getPages());
        } else {
            pageResult.setPageIndex(1L);
            pageResult.setPageSize((long) data.size());
            pageResult.setTotalSize((long) data.size());
            pageResult.setTotalPage(1L);
        }
        pageResult.setRecords(data);
        return pageResult;
    }

    /**
     * 从mybatis转换
     *
     * @param page
     * @param function
     * @param <E>
     * @param <U>
     * @return
     */
    public static <E, U> PageResult<U> with(IPage<E> page, List<E> data, Function<E, U> function) {
        PageResult<U> pageResult = new PageResult<>();
        if (page.searchCount()) {
            pageResult.setPageIndex(page.getCurrent());
            pageResult.setPageSize(page.getSize());
            pageResult.setTotalSize(page.getTotal());
            pageResult.setTotalPage(page.getPages());
        } else {
            pageResult.setPageIndex(1L);
            pageResult.setPageSize((long) data.size());
            pageResult.setTotalSize((long) data.size());
            pageResult.setTotalPage(1L);
        }
        pageResult.setRecords(data.stream()
                .map(function)
                .collect(Collectors.toList()));
        return pageResult;
    }
}
