package cn.com.coderd.framework.provider.domain.service.impl;

import cn.com.coderd.framework.provider.domain.entity.TUser;
import cn.com.coderd.framework.provider.domain.mapper.TUserMapper;
import cn.com.coderd.framework.provider.domain.service.ITUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户基础信息表 服务实现类
 * </p>
 *
 * @since 2022-01-05
 */
@Service
public class TUserServiceImpl extends ServiceImpl<TUserMapper, TUser> implements ITUserService {

}
