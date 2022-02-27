package io.dubai.modules.other.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cz.czUser.system.entity.UserInfo;
import io.dubai.common.annotation.LoginUser;
import io.dubai.common.utils.PageUtils;
import io.dubai.common.utils.R;
import io.dubai.common.validator.ValidatorUtils;
import io.dubai.modules.other.entity.Payment;
import io.dubai.modules.other.query.PaymentQuery;
import io.dubai.modules.other.service.PaymentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Map;


/**
 * 付款方式
 *
 * @author mother fucker
 * @email admin@gmail.com
 * @date 2021-10-07 16:29:29
 */
@RestController
@RequestMapping("other/payment")
@Api(tags = "支付方式")
public class PaymentController {
    @Resource
    private PaymentService paymentService;

    @ApiOperation("收款方式列表")
    @GetMapping("/withdrawList")
    public R withdrawList(@ApiIgnore @LoginUser UserInfo userInfo) {
        PaymentQuery query = new PaymentQuery();
        query.setIsWithdraw(1);
        query.setStatus(1);
        query.setLanguageId(userInfo.getLanguage());
        return R.ok().put("list", paymentService.queryList(query));
    }

    @ApiOperation("充值方式列表")
    @GetMapping("/depositList")
    public R depositList(@ApiIgnore @LoginUser UserInfo userInfo) {
        PaymentQuery query = new PaymentQuery();
        query.setIsDeposit(1);
        query.setLanguageId(userInfo.getLanguage());
        query.setStatus(1);
        return R.ok().put("list", paymentService.queryList(query));
    }


}
