package io.dubai.modules.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.dubai.modules.user.entity.UserAddress;
import org.apache.ibatis.annotations.Mapper;

/**
 * 收货地址
 *
 * @author mother fucker
 * @email admin@gmail.com
 * @date 2021-10-17 19:32:56
 */
@Mapper
public interface UserAddressDao extends BaseMapper<UserAddress> {

}
