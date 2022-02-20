package io.dubai.modules.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.dubai.modules.user.entity.UserCreditsExchange;
import io.dubai.modules.user.entity.vo.UserCreditsExchangeVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 积分交易
 *
 * @author mother fucker
 * @email admin@gmail.com
 * @date 2022-01-12 19:15:13
 */
@Mapper
public interface UserCreditsExchangeDao extends BaseMapper<UserCreditsExchange> {


}
