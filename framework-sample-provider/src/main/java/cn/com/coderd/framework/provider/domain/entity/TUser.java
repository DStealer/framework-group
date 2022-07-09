package cn.com.coderd.framework.provider.domain.entity;

import cn.com.coderd.framework.starter.mybatis.AESTypeHandler;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 用户基础信息表
 * </p>
 *
 * @since 2022-01-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value = "t_user", autoResultMap = true)
public class TUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 手机
     */
    private String mobile;

    /**
     * 密码
     */
    @TableField(typeHandler = AESTypeHandler.class)
    private String password;


    private LocalDateTime createDatetime;

    private LocalDateTime updateDatetime;


}
