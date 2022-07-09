package cn.com.coderd.framework.starter.mybatis;

import com.baomidou.mybatisplus.core.toolkit.AES;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.type.*;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;

import java.sql.*;

/**
 * aes加解密处理
 */

@Data
@Slf4j
@Alias("AESTypeHandler")
@MappedTypes(String.class)
@MappedJdbcTypes(JdbcType.OTHER)
public class AESTypeHandler implements TypeHandler<String>, InitializingBean {
    //区别算法或密钥 A用户区别算法 0可以兼容后续密钥替换
    private static final String SUFFIX_A0 = "@A0";
    //用于密钥升级, 不使用请不要设置
    private static final String SUFFIX_A1 = "@A1";
    @Value("${spring.datasource.aes.a0:7awgNGMiK5mXs9Uf}")
    private transient String codeA0;
    @Value("${spring.datasource.aes.a1:}")
    private transient String codeA1;

    @Override
    public void afterPropertiesSet() throws Exception {
        if (this.codeA0 == null || this.codeA0.isEmpty()) {
            throw new RuntimeException("A0密钥错误");
        }
        if (this.codeA0.equals("7awgNGMiK5mXs9Uf")) {
            log.warn("正在使用默认密钥,请考虑替换密钥");
        }
    }

    @Override
    public void setParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType) throws SQLException {
        if (parameter == null) {
            ps.setNull(i, Types.VARCHAR);
        } else if (this.codeA1 != null && !this.codeA1.isEmpty()) {
            ps.setString(i, AES.encrypt(parameter, this.codeA1) + SUFFIX_A1);
        } else {
            ps.setString(i, AES.encrypt(parameter, this.codeA0) + SUFFIX_A0);
        }
    }

    @Override
    public String getResult(ResultSet rs, String columnName) throws SQLException {
        String rst = rs.getString(columnName);
        if (rst == null || rst.isEmpty()) {
            return rst;
        }
        if (rst.endsWith(SUFFIX_A0)) {
            return AES.decrypt(rst.substring(0, rst.length() - SUFFIX_A0.length()), this.codeA0);
        } else if (rst.endsWith(SUFFIX_A1)) {
            return AES.decrypt(rst.substring(0, rst.length() - SUFFIX_A1.length()), this.codeA1);
        } else {
            throw new SQLException("算法或密钥错误");
        }
    }

    @Override
    public String getResult(ResultSet rs, int columnIndex) throws SQLException {
        String rst = rs.getString(columnIndex);
        if (rst == null || rst.isEmpty()) {
            return rst;
        }
        if (rst.endsWith(SUFFIX_A0)) {
            return AES.decrypt(rst.substring(0, rst.length() - SUFFIX_A0.length()), this.codeA0);
        } else if (rst.endsWith(SUFFIX_A1)) {
            return AES.decrypt(rst.substring(0, rst.length() - SUFFIX_A1.length()), this.codeA1);
        } else {
            throw new SQLException("算法或密钥错误");
        }
    }

    @Override
    public String getResult(CallableStatement cs, int columnIndex) throws SQLException {
        String rst = cs.getString(columnIndex);
        if (rst == null || rst.isEmpty()) {
            return rst;
        }
        if (rst.endsWith(SUFFIX_A0)) {
            return AES.decrypt(rst.substring(0, rst.length() - 3), this.codeA0);
        } else if (rst.endsWith(SUFFIX_A1)) {
            return AES.decrypt(rst.substring(0, rst.length() - 3), this.codeA1);
        } else {
            throw new SQLException("算法或密钥错误");
        }
    }
}
