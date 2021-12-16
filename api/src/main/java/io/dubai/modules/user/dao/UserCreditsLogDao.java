package io.dubai.modules.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.dubai.modules.user.entity.UserCreditsLog;
import io.dubai.modules.user.query.LogQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * 资金记录
 *
 * @author mother fucker
 * @email admin@gmail.com
 * @date 2021-10-07 16:28:10
 */
@Mapper
public interface UserCreditsLogDao extends BaseMapper<UserCreditsLog> {

    List<UserCreditsLog> queryPage(IPage page, @Param("query") LogQuery query);

    Integer queryCountByUserIdAndStatusToday(@Param("userId") Long userId, @Param("status") Integer status);

    Integer queryCountByUserIdAndStatus(@Param("userId") Long userId, @Param("status") Integer status);

    BigDecimal queryAmountSumByUserIdAndStatus(@Param("userId") Long userId, @Param("status") Integer status);

}
