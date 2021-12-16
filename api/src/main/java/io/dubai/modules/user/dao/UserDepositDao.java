package io.dubai.modules.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.dubai.modules.user.entity.UserDeposit;
import io.dubai.modules.user.entity.UserWithdraw;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户提现
 *
 * @author mother fucker
 * @email admin@gmail.com
 * @date 2021-10-07 16:28:10
 */
@Mapper
public interface UserDepositDao extends BaseMapper<UserDeposit> {


}
