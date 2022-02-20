package io.dubai.modules.user.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cz.czUser.system.entity.UserInfo;
import io.dubai.common.annotation.LoginUser;
import io.dubai.common.enums.LogTypeEnum;
import io.dubai.common.enums.ResponseStatusEnum;
import io.dubai.common.enums.UserBalanceLogStatusEnum;
import io.dubai.common.enums.UserCreditsLogStatusEnum;
import io.dubai.common.utils.PageUtils;
import io.dubai.common.utils.R;
import io.dubai.common.validator.ValidatorUtils;
import io.dubai.modules.user.entity.UserBalanceLog;
import io.dubai.modules.user.entity.UserCreditsExchange;
import io.dubai.modules.user.entity.UserCreditsLog;
import io.dubai.modules.user.form.BuyCreditsForm;
import io.dubai.modules.user.form.CancelSellCreditsForm;
import io.dubai.modules.user.form.SellCreditsForm;
import io.dubai.modules.user.service.UserBalanceLogService;
import io.dubai.modules.user.service.UserCreditsExchangeService;
import io.dubai.modules.user.service.UserCreditsLogService;
import io.dubai.modules.user.service.UserInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * 积分交易
 *
 * @author mother fucker
 * @name 积分交易
 * @date 2022-01-12 19:15:13
 */
@RestController
@RequestMapping("exchange")
@Api(tags = "积分交易")
public class ExchangeController {

    @Resource
    private UserCreditsExchangeService userCreditsExchangeService;

    @Resource
    private UserInfoService userInfoService;

    @Resource
    private UserBalanceLogService userBalanceLogService;

    @Resource
    private UserCreditsLogService userCreditsLogService;


    @GetMapping("/list")
    @ApiOperation("获取出售列表")
    public R list(@ApiIgnore @LoginUser UserInfo userInfo) {
        List<UserCreditsExchange> list = userCreditsExchangeService.list(
                new QueryWrapper<UserCreditsExchange>()
                        .eq("status",0)
                        .ne("user_id",userInfo.getUserId())
        );
        return R.ok().put("list", list);
    }

    @GetMapping("/list/{userId}")
    @ApiOperation("根据用户ID获取出售列表")
    public R list(@PathVariable String userId) {
        List<UserCreditsExchange> list = userCreditsExchangeService.list(
                new QueryWrapper<UserCreditsExchange>()
                .eq("user_id",userId)
        );
        return R.ok().put("list", list);
    }


    @PostMapping("/buy")
    @Transactional
    public R buy(@RequestBody BuyCreditsForm form,@ApiIgnore @LoginUser UserInfo userInfo) {
        if(userInfo.getPassword() == null || "".equalsIgnoreCase(userInfo.getPassword())){
            return R.ok(ResponseStatusEnum.PAY_PASSWORD_NOT_EXIST);
        }
        if(!userInfo.getPassword().equalsIgnoreCase(form.getPassword())){
            return R.ok(ResponseStatusEnum.PAY_PASSWORD_ERROR);
        }
        UserCreditsExchange bean = userCreditsExchangeService.getById(form.getUserCreditsExchangeId());
        if(bean == null || bean.getStatus().intValue() != 0){
            return R.ok(ResponseStatusEnum.CREDITS_SOLD);
        }
        BigDecimal amount = bean.getQuantity().multiply(bean.getPrice());
        if(userInfo.getBalance().compareTo(amount) == -1){
            return R.ok(ResponseStatusEnum.BALANCE_INSUFFICIENT);
        }
        //增加卖家账户余额
        UserInfo seller = userInfoService.queryByUserId(bean.getUserId());
        if(seller != null){
            seller.setBalance(seller.getBalance().add(amount));
            userInfoService.update(seller);
            //卖家增加资金变动记录
            UserBalanceLog userBalanceLog = new UserBalanceLog();
            userBalanceLog.setType(LogTypeEnum.INCOME.code);
            userBalanceLog.setBalance(userInfo.getBalance());
            userBalanceLog.setAmount(amount);
            userBalanceLog.setUserId(userInfo.getUserId().longValue());
            userBalanceLog.setStatus(UserBalanceLogStatusEnum.CREDITS_SOLD.code);
            userBalanceLogService.save(userBalanceLog);
            //卖家增加积分记录，无需减卖家积分，因为发布卖单时已经扣过
            UserCreditsLog userCreditsLog = new UserCreditsLog();
            userCreditsLog.setUserId(userInfo.getUserId().longValue());
            userCreditsLog.setAmount(bean.getQuantity().negate());
            userCreditsLog.setType(LogTypeEnum.OUTLAY.code);
            userCreditsLog.setStatus(UserCreditsLogStatusEnum.CREDITS_SOLD.code);
            userCreditsLog.setBalance(userInfo.getCredits());
            userCreditsLogService.save(userCreditsLog);
        }
        //修改交易信息
        bean.setBuyerId(userInfo.getUserId().longValue());
        bean.setStatus(1);
        userCreditsExchangeService.updateById(bean);
        //扣除买家账户余额，增加买家积分，
        userInfo.setCredits(userInfo.getCredits().add(bean.getQuantity()));
        userInfo.setBalance(userInfo.getBalance().subtract(amount));
        userInfoService.update(userInfo);
        //增加账单记录
        UserBalanceLog userBalanceLog = new UserBalanceLog();
        userBalanceLog.setType(LogTypeEnum.OUTLAY.code);
        userBalanceLog.setBalance(userInfo.getBalance());
        userBalanceLog.setAmount(amount.negate());
        userBalanceLog.setUserId(userInfo.getUserId().longValue());
        userBalanceLog.setStatus(UserBalanceLogStatusEnum.CREDITS_BOUGHT.code);
        userBalanceLogService.save(userBalanceLog);
        //增加积分记录
        UserCreditsLog userCreditsLog = new UserCreditsLog();
        userCreditsLog.setUserId(userInfo.getUserId().longValue());
        userCreditsLog.setAmount(bean.getQuantity());
        userCreditsLog.setType(LogTypeEnum.INCOME.code);
        userCreditsLog.setStatus(UserCreditsLogStatusEnum.CREDITS_BOUGHT.code);
        userCreditsLog.setBalance(userInfo.getCredits());
        userCreditsLogService.save(userCreditsLog);
        return R.ok();
    }

    @PostMapping("/sell")
    @Transactional
    public R sell(@RequestBody SellCreditsForm form, @ApiIgnore @LoginUser UserInfo userInfo) {
        if(userInfo.getPassword() == null || "".equalsIgnoreCase(userInfo.getPassword())){
            return R.ok(ResponseStatusEnum.PAY_PASSWORD_NOT_EXIST);
        }
        if(!userInfo.getPassword().equalsIgnoreCase(form.getPassword())){
            return R.ok(ResponseStatusEnum.PAY_PASSWORD_ERROR);
        }
        if(userInfo.getCredits().compareTo(form.getQuantity()) == -1){
            return R.ok(ResponseStatusEnum.CREDITS_INSUFFICIENT);
        }
        UserCreditsExchange bean = new UserCreditsExchange();
        bean.setUserId(userInfo.getUserId().longValue());
        bean.setStatus(0);
        bean.setQuantity(form.getQuantity());
        bean.setPrice(form.getPrice());
        userCreditsExchangeService.save(bean);
        //扣除账户积分余额
        userInfo.setCredits(userInfo.getCredits().subtract(bean.getQuantity()));
        userInfoService.update(userInfo);
        return R.ok();
    }

    @PostMapping("/cancel")
    @Transactional
    public R cancel(@RequestBody CancelSellCreditsForm form, @ApiIgnore @LoginUser UserInfo userInfo) {
        UserCreditsExchange bean = userCreditsExchangeService.getById(form.getUserCreditsExchangeId());
        if(bean != null){
            bean.setStatus(2);
            userCreditsExchangeService.updateById(bean);
            userInfo.setCredits(userInfo.getCredits().add(bean.getQuantity()));
            userInfoService.update(userInfo);
        }
        return R.ok();
    }

}
