package io.dubai.modules.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.dubai.common.utils.PageUtils;
import io.dubai.modules.user.entity.UserCreditsLog;
import io.dubai.modules.user.query.LogQuery;

import java.math.BigDecimal;

/**
 * 资金记录
 *
 * @author Howard
 * @email admin@gmail.com
 * @date 2021-10-07 16:28:10
 */
public interface UserCreditsLogService extends IService<UserCreditsLog> {

    PageUtils queryPage(LogQuery query);

    Integer queryCountByUserIdAndStatusToday(Long userId, Integer status);

    Integer queryCountByUserIdAndStatus(Long userId, Integer status);

    BigDecimal queryAmountSumByUserIdAndStatus(Long userId, Integer status);

}

