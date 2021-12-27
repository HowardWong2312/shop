package io.dubai.admin.modules.user.controller;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import io.dubai.admin.modules.goods.entity.ShopGoods;
import io.dubai.admin.modules.goods.service.ShopGoodsService;
import io.dubai.admin.modules.user.entity.UserBalanceLog;
import io.dubai.admin.modules.user.entity.UserCreditsLog;
import io.dubai.admin.modules.user.entity.UserInfo;
import io.dubai.admin.modules.user.form.RechargeForm;
import io.dubai.admin.modules.user.service.UserBalanceLogService;
import io.dubai.admin.modules.user.service.UserCreditsLogService;
import io.dubai.admin.modules.user.service.UserInfoService;
import io.dubai.common.enums.LogTypeEnum;
import io.dubai.common.enums.UserBalanceLogStatusEnum;
import io.dubai.common.enums.UserCreditsLogStatusEnum;
import io.dubai.common.utils.PageUtils;
import io.dubai.common.utils.R;
import io.dubai.common.validator.ValidatorUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Map;


/**
 * 用户信息表
 *
 * @author mother fucker
 * @name 用户信息表
 * @date 2021-12-14 18:27:22
 */
@RestController
@RequestMapping("user/userInfo")
public class UserInfoController {
    @Resource
    private UserInfoService userInfoService;

    @Resource
    private ShopGoodsService shopGoodsService;

    @Resource
    private UserCreditsLogService userCreditsLogService;

    @Resource
    private UserBalanceLogService userBalanceLogService;



    @GetMapping("/list")
    @RequiresPermissions("user:userInfo:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = userInfoService.queryPage(params);
        return R.ok().put("page", page);
    }


    @GetMapping("/info/{userId}")
    @RequiresPermissions("user:userInfo:info")
    public R info(@PathVariable("userId") Integer userId) {
        UserInfo userInfo = userInfoService.getById(userId);

        return R.ok().put("userInfo", userInfo);
    }

    @PostMapping("/rechargeCredits")
    @RequiresPermissions("user:userInfo:rechargeCredits")
    public R rechargeCredits(@RequestBody RechargeForm form) {
        form.getIds().forEach(s->{
            UserInfo userInfo = userInfoService.getById(s);
            userInfo.setCredits(userInfo.getCredits().add(form.getAmount()));
            userInfoService.update(userInfo);
            //增加积分记录
            UserCreditsLog userCreditsLog = new UserCreditsLog();
            userCreditsLog.setUserId(userInfo.getUserId().longValue());
            userCreditsLog.setAmount(form.getAmount());
            if(form.getAmount().compareTo(BigDecimal.ZERO) == -1){
                userCreditsLog.setType(LogTypeEnum.OUTLAY.code);
                userCreditsLog.setStatus(UserCreditsLogStatusEnum.MANUAL_DEDUCTION.code);
            }else{
                userCreditsLog.setType(LogTypeEnum.INCOME.code);
                userCreditsLog.setStatus(UserCreditsLogStatusEnum.MANUAL_RECHARGE.code);
            }
            userCreditsLog.setBalance(userInfo.getCredits());
            userCreditsLogService.save(userCreditsLog);
        });
        return R.ok();
    }

    @PostMapping("/rechargeBalance")
    @RequiresPermissions("user:userInfo:rechargeBalance")
    public R rechargeBalance(@RequestBody RechargeForm form) {
        form.getIds().forEach(s->{
            UserInfo userInfo = userInfoService.getById(s);
            userInfo.setBalance(userInfo.getBalance().add(form.getAmount()));
            userInfoService.update(userInfo);
            //增加资金记录
            UserBalanceLog userBalanceLog = new UserBalanceLog();
            userBalanceLog.setUserId(userInfo.getUserId().longValue());
            userBalanceLog.setAmount(form.getAmount());
            if(form.getAmount().compareTo(BigDecimal.ZERO) == -1){
                userBalanceLog.setType(LogTypeEnum.OUTLAY.code);
                userBalanceLog.setStatus(UserBalanceLogStatusEnum.MANUAL_DEDUCTION.code);
            }else{
                userBalanceLog.setType(LogTypeEnum.INCOME.code);
                userBalanceLog.setStatus(UserBalanceLogStatusEnum.MANUAL_RECHARGE.code);
            }
            userBalanceLog.setBalance(userInfo.getBalance());
            userBalanceLogService.save(userBalanceLog);
        });
        return R.ok();
    }

    @PostMapping("/update")
    @RequiresPermissions("user:userInfo:update")
    public R update(@RequestBody UserInfo userInfo) {
        ValidatorUtils.validateEntity(userInfo);
        if(userInfo.getIsMerchant() == 1){
            shopGoodsService.update(
                    new UpdateWrapper<ShopGoods>()
                    .set("link_url",null)
                    .eq("user_id",userInfo.getUserId())
            );
        }
        userInfoService.update(userInfo);
        return R.ok();
    }

    @GetMapping("/getTodayFundsAndUserData")
    @RequiresPermissions("user:userInfo:count")
    public R getTodayFundsAndUserData(){
        return userInfoService.getTodayFundsAndUserData();
    }

}
