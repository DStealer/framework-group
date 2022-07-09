package cn.com.coderd.framework.provider.domain.service;

import cn.com.coderd.framework.common.basic.PageInfo;
import cn.com.coderd.framework.provider.domain.entity.TUser;
import cn.com.coderd.framework.starter.mybatis.MybatisPageHelper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
class ITUserServiceTest {
    @Autowired
    private ITUserService userService;

    @Test
    void testGet() {
        IPage<TUser> page = userService.lambdaQuery()
                .page(MybatisPageHelper.toPage(PageInfo.NO_LIMIT));
        page.getRecords().forEach(System.out::println);
    }

    @Test
    void testInsert() {
        userService.save(new TUser().setMobile("12321203475")
                .setPassword("123").setCreateDatetime(LocalDateTime.now())
                .setUpdateDatetime(LocalDateTime.now()));
    }
}