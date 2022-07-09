package cn.com.coderd.framework.starter.mybatis;

import cn.com.coderd.framework.common.basic.PageInfo;
import cn.com.coderd.framework.common.basic.PageList;
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
    public static <E> PageList<E> with(IPage<E> page) {
        PageList<E> pageList = new PageList<>();
        if (page.searchCount()) {
            pageList.setPageIndex(page.getCurrent());
            pageList.setPageSize(page.getSize());
            pageList.setTotalSize(page.getTotal());
            pageList.setTotalPage(page.getPages());
        } else {
            pageList.setPageIndex(1L);
            pageList.setPageSize(page.getRecords().size());
            pageList.setTotalSize(page.getRecords().size());
            pageList.setTotalPage(1L);
        }
        pageList.setList(page.getRecords());
        return pageList;
    }

    /**
     * 从mybatis转换
     *
     * @param page
     * @param <E>
     * @return
     */
    public static <E> PageList<E> with(IPage<E> page, List<E> data) {
        PageList<E> pageList = new PageList<>();
        if (page.searchCount()) {
            pageList.setPageIndex(page.getCurrent());
            pageList.setPageSize(page.getSize());
            pageList.setTotalSize(page.getTotal());
            pageList.setTotalPage(page.getPages());
        } else {
            pageList.setPageIndex(1L);
            pageList.setPageSize(data.size());
            pageList.setTotalSize(data.size());
            pageList.setTotalPage(1L);
        }
        pageList.setList(data);
        return pageList;
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
    public static <E, U> PageList<U> with(IPage<E> page, List<E> data, Function<E, U> function) {
        PageList<U> pageList = new PageList<>();
        if (page.searchCount()) {
            pageList.setPageIndex(page.getCurrent());
            pageList.setPageSize(page.getSize());
            pageList.setTotalSize(page.getTotal());
            pageList.setTotalPage(page.getPages());
        } else {
            pageList.setPageIndex(1L);
            pageList.setPageSize(data.size());
            pageList.setTotalSize(data.size());
            pageList.setTotalPage(1L);
        }
        pageList.setList(data.stream()
                .map(function)
                .collect(Collectors.toList()));
        return pageList;
    }
}
