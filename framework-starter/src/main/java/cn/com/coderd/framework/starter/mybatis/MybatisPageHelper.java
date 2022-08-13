package cn.com.coderd.framework.starter.mybatis;

import cn.com.coderd.framework.common.basic.PageData;
import cn.com.coderd.framework.common.basic.PageInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.experimental.UtilityClass;

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
    public static <T, P extends PageInfo<?>> IPage<T> toPage(P pageInfo) {
        if (pageInfo.getPageIndex() == null || pageInfo.getPageSize() == null) {
            Page<T> page = new Page<>();
            page.setCurrent(PageInfo.DEFAULT.getPageIndex());
            page.setSize(PageInfo.DEFAULT.getPageSize());
            page.setSearchCount(true);
            page.setOptimizeCountSql(true);
            return page;
        } else if (PageInfo.NO_LIMIT.getPageIndex().equals(pageInfo.getPageIndex())
                && PageInfo.NO_LIMIT.getPageSize().equals(pageInfo.getPageSize())) {
            Page<T> page = new Page<>();
            page.setCurrent(1L);
            page.setSize(Long.MAX_VALUE);
            page.setSearchCount(false);
            page.setOptimizeCountSql(false);
            return page;
        } else if (PageInfo.NO_DATA.getPageIndex().equals(pageInfo.getPageIndex())
                && PageInfo.NO_DATA.getPageSize().equals(pageInfo.getPageSize())) {
            Page<T> page = new Page<>();
            page.setCurrent(1L);
            page.setSize(0);
            page.setSearchCount(true);
            page.setOptimizeCountSql(true);
            return page;
        } else {
            Page<T> page = new Page<>();
            page.setCurrent(pageInfo.getPageIndex());
            page.setSize(pageInfo.getPageSize());
            page.setSearchCount(true);
            page.setOptimizeCountSql(true);
            return page;
        }
    }

    /**
     * 从mybatis转换
     *
     * @param page
     * @param <E>
     * @return
     */
    public static <E> PageData<E> withPageData(IPage<E> page) {
        PageData<E> pageData = new PageData<>();
        if (page.searchCount()) {
            pageData.setPageIndex(page.getCurrent());
            pageData.setPageSize(page.getSize());
            pageData.setTotalPage(page.getPages());
            pageData.setTotalSize(page.getTotal());
        } else {
            pageData.setPageIndex(1L);
            pageData.setPageSize((long) page.getRecords().size());
            pageData.setTotalPage(1L);
            pageData.setTotalSize((long) page.getRecords().size());
        }
        pageData.setRecords(page.getRecords());
        return pageData;
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
    public static <E, U> PageData<U> withPageData(IPage<E> page, Function<E, U> function) {
        PageData<U> pageData = new PageData<>();
        if (page.searchCount()) {
            pageData.setPageIndex(page.getCurrent());
            pageData.setPageSize(page.getSize());
            pageData.setTotalSize(page.getTotal());
            pageData.setTotalPage(page.getPages());
        } else {
            pageData.setPageIndex(1L);
            pageData.setPageSize((long) page.getRecords().size());
            pageData.setTotalSize((long) page.getRecords().size());
            pageData.setTotalPage(1L);
        }
        pageData.setRecords(page.getRecords().stream()
                .map(function)
                .collect(Collectors.toList()));
        return pageData;
    }
}
