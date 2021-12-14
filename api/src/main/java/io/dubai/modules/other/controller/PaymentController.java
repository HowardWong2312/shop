package io.dubai.modules.other.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.dubai.common.utils.PageUtils;
import io.dubai.common.utils.R;
import io.dubai.common.validator.ValidatorUtils;
import io.dubai.modules.other.entity.Payment;
import io.dubai.modules.other.service.PaymentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Map;


/**
 * 付款方式
 *
 * @author Howard
 * @email admin@gmail.com
 * @date 2021-10-07 16:29:29
 */
@RestController
@RequestMapping("other/payment")
@Api(tags = "付款方式")
public class PaymentController {
    @Resource
    private PaymentService paymentService;

    @ApiOperation("支付方式")
    @GetMapping("/list")
    public R list() {
        return R.ok().put("list", paymentService.list(new QueryWrapper<Payment>().eq("status",1)));
    }

    @ApiOperation("充值方式")
    @GetMapping("/depositList")
    public R depositList() {
        return R.ok().put("list", paymentService.list(new QueryWrapper<Payment>().eq("status",1).eq("is_deposit",1)));
    }


}
