package cn.com.coderd.framework.starter.mybatis;

import cn.com.coderd.framework.starter.constants.ConstVar;
import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusProperties;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * Mybatis Plus 功能自动配置类
 */
@Slf4j
@Configuration
@ConditionalOnClass({SqlSessionFactory.class, SqlSessionFactoryBean.class, MybatisPlusProperties.class})
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
@ConditionalOnBean(DataSource.class)
public class MybatisPlusConfiguration {
    /**
     * 加解密处理
     *
     * @return
     */
    @Bean
    public AESTypeHandler aesTypeHandler() {
        return new AESTypeHandler();
    }

    /**
     * mybatisLogCustomize
     *
     * @return
     */
    @Bean
    public ConfigurationCustomizer mybatisLogCustomize() {
        return configuration -> {
            log.info("配置Mybatis日志打印组件");
            configuration.setLogImpl(MybatisLog.class);
            configuration.setMapUnderscoreToCamelCase(false);
            configuration.getTypeAliasRegistry()
                    .registerAlias(AESTypeHandler.class);
        };
    }

    /**
     * 配置mysql plus增强拦截器(测试环境)
     *
     * @return
     */
    @Bean
    @ConditionalOnProperty(name = ConstVar.ENV_PRODUCT, havingValue = "false", matchIfMissing = true)
    public MybatisPlusInterceptor devMybatisPlusInterceptor() {
        log.info("配置测试环境MybatisPlus拦截器");
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        interceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());
        return interceptor;
    }

    /**
     * 配置mysql plus增强拦截器(生产环境)
     *
     * @return
     */
    @Bean
    @ConditionalOnProperty(name = ConstVar.ENV_PRODUCT, havingValue = "*")
    public MybatisPlusInterceptor proMybatisPlusInterceptor() {
        log.info("配置生产环境MybatisPlus拦截器");
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        return interceptor;
    }
}
