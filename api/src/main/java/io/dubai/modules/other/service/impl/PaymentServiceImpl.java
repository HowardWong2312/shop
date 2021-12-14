package io.dubai.modules.other.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dubai.common.utils.PageUtils;
import io.dubai.common.utils.Query;
import io.dubai.modules.other.dao.PaymentDao;
import io.dubai.modules.other.entity.Payment;
import io.dubai.modules.other.service.PaymentService;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("paymentService")
public class PaymentServiceImpl extends ServiceImpl<PaymentDao, Payment> implements PaymentService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<Payment> page = this.page(
                new Query<Payment>().getPage(params),
                new QueryWrapper<Payment>()
        );

        return new PageUtils(page);
    }

}
