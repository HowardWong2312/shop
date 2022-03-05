package io.dubai.admin.modules.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.dubai.admin.modules.user.entity.UserDeposit;
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
public interface UserDepositService extends IService<UserDeposit> {

    PageUtils queryPage(Map<String, Object> params);

    BigDecimal queryAmountSumTotal(Map<String, Object> params);
}

