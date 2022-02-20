package io.dubai.modules.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cz.czUser.system.entity.UserInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户账户
 *
 * @author mother fucker
 * @email admin@gmail.com
 * @date 2021-10-07 16:28:10
 */
@Mapper
public interface UserInfoDao extends BaseMapper<UserInfo> {


}
