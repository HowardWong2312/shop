package io.dubai.modules.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.dubai.modules.user.entity.UserBalanceLog;
import io.dubai.modules.user.query.LogQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * 资金记录
 *
 * @author Howard
 * @email admin@gmail.com
 * @date 2021-10-07 16:28:10
 */
@Mapper
public interface UserBalanceLogDao extends BaseMapper<UserBalanceLog> {

    List<UserBalanceLog> queryPage(IPage page, @Param("query") LogQuery query);

    BigDecimal querySumAmountByUserIdType(@Param("userId") Long userId, @Param("type") Integer type);

    Integer queryCountByUserIdType(@Param("userId") Long userId, @Param("type") Integer type);

}
