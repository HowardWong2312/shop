package io.dubai.modules.other.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.dubai.modules.other.entity.Payment;
import io.dubai.modules.other.entity.vo.PaymentVo;
import io.dubai.modules.other.query.PaymentQuery;

import java.util.List;
import java.util.Map;

/**
 * 支付方式
 *
 * @author mother fucker
 * @email admin@gmail.com
 * @date 2021-10-07 16:29:29
 */
public interface PaymentService extends IService<Payment> {

    List<PaymentVo> queryList(PaymentQuery query);
}

