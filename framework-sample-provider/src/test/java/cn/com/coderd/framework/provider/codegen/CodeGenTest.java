package cn.com.coderd.framework.provider.codegen;

import cn.com.coderd.framework.support.mybatis.CodeGen;
import com.mysql.cj.jdbc.Driver;
import org.junit.jupiter.api.Test;

public class CodeGenTest {
    @Test
    void genCode() throws Exception {
        new CodeGen()
                .setDriverName(Driver.class.getCanonicalName())
                .setUrl("jdbc:mysql://127.0.0.1:3306/coderd?useUnicode=true&characterEncoding=utf-8&useSSL=false")
                .setUsername("root")
                .setPassword("root@123")
                .setPackageName("cn.com.coderd.framework.provider")
                .setTableNameLike("user")
                .execute();
    }
}
