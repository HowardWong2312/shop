package io.dubai.modules.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.dubai.common.utils.PageUtils;
import io.dubai.modules.user.entity.UserBalanceLog;
import io.dubai.modules.user.query.LogQuery;

import java.math.BigDecimal;

/**
 * 资金记录
 *
 * @author mother fucker
 * @email admin@gmail.com
 * @date 2021-10-07 16:28:10
 */
public interface UserBalanceLogService extends IService<UserBalanceLog> {

    PageUtils queryPage(LogQuery query);

    BigDecimal querySumAmountByUserIdType(Long userId, Integer type);

    Integer queryCountByUserIdType(Long userId, Integer type);

}

