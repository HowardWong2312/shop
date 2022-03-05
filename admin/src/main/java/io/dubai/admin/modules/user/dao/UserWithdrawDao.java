package io.dubai.admin.modules.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.dubai.admin.modules.user.entity.UserWithdraw;
import io.dubai.admin.modules.user.entity.vo.UserWithdrawVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 提现申请表
 *
 * @author mother fucker
 * @email admin@gmail.com
 * @date 2021-12-14 18:27:22
 */
@Mapper
public interface UserWithdrawDao extends BaseMapper<UserWithdraw> {

    List<UserWithdrawVo> queryPage(IPage page, Map<String, Object> params);

    BigDecimal queryAmountSumTotal(@Param("params") Map<String, Object> params);

    UserWithdrawVo queryById(Long id);

}
