package io.dubai.modules.other.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.dubai.common.utils.PageUtils;
import io.dubai.modules.other.entity.Payment;

import java.util.Map;

/**
 * 支付方式
 *
 * @author mother fucker
 * @email admin@gmail.com
 * @date 2021-10-07 16:29:29
 */
public interface PaymentService extends IService<Payment> {

    PageUtils queryPage(Map<String, Object> params);
}

