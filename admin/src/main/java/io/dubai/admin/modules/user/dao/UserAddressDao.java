package io.dubai.admin.modules.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.dubai.admin.modules.user.entity.UserAddress;
import io.dubai.admin.modules.user.entity.vo.UserAddressVo;
import io.dubai.admin.modules.user.entity.vo.UserBankVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 收货地址
 *
 * @author mother fucker
 * @email admin@gmail.com
 * @date 2021-12-14 18:27:22
 */
@Mapper
public interface UserAddressDao extends BaseMapper<UserAddress> {

    List<UserAddressVo> queryPage(IPage page, Map<String, Object> params);

}
