package io.dubai.admin.modules.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.dubai.admin.modules.user.entity.UserWithdraw;
import io.dubai.admin.modules.user.entity.vo.UserWithdrawVo;
import io.dubai.common.utils.PageUtils;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 提现申请表
 *
 * @author mother fucker
 * @email admin@gmail.com
 * @date 2021-12-14 18:27:22
 */
public interface UserWithdrawService extends IService<UserWithdraw> {

    PageUtils queryPage(Map<String, Object> params);

    BigDecimal queryAmountSumTotal(Map<String, Object> params);

    UserWithdrawVo queryById(Long id);
}

