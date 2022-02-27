package io.dubai.modules.other.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.dubai.modules.other.entity.Payment;
import io.dubai.modules.other.entity.vo.PaymentVo;
import io.dubai.modules.other.query.PaymentQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 支付方式
 *
 * @author mother fucker
 * @email admin@gmail.com
 * @date 2021-10-07 16:29:29
 */
@Mapper
public interface PaymentDao extends BaseMapper<Payment> {

    List<PaymentVo> queryList(@Param("query") PaymentQuery query);

}
