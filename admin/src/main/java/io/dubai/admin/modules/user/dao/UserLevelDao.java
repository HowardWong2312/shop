package io.dubai.admin.modules.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.dubai.admin.modules.user.entity.UserLevel;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户等级
 *
 * @author mother fucker
 * @email admin@gmail.com
 * @date 2021-12-14 18:27:22
 */
@Mapper
public interface UserLevelDao extends BaseMapper<UserLevel> {

}
