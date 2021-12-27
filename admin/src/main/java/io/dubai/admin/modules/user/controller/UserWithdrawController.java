package io.dubai.admin.modules.user.controller;

import io.dubai.admin.modules.goods.entity.ShopGoods;
import io.dubai.admin.modules.goods.entity.ShopGoodsOnebuy;
import io.dubai.admin.modules.goods.entity.vo.ShopGoodsOnebuyVo;
import io.dubai.admin.modules.user.entity.UserBalanceLog;
import io.dubai.admin.modules.user.entity.UserInfo;
import io.dubai.admin.modules.user.entity.UserWithdraw;
import io.dubai.admin.modules.user.entity.vo.UserWithdrawVo;
import io.dubai.admin.modules.user.service.UserBalanceLogService;
import io.dubai.admin.modules.user.service.UserInfoService;
import io.dubai.admin.modules.user.service.UserWithdrawService;
import io.dubai.common.enums.LogTypeEnum;
import io.dubai.common.enums.UserBalanceLogStatusEnum;
import io.dubai.common.utils.PageUtils;
import io.dubai.common.utils.R;
import io.dubai.common.validator.ValidatorUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Map;


/**
 * 提现申请表
 *
 * @author mother fucker
 * @name 提现申请表
 * @date 2021-12-14 18:27:22
 */
@RestController
@RequestMapping("user/userWithdraw")
public class UserWithdrawController {
    @Resource
    private UserWithdrawService userWithdrawService;

    @Resource
    private UserInfoService userInfoService;

    @Resource
    private UserBalanceLogService userBalanceLogService;

    @GetMapping("/list")
    @RequiresPermissions("user:userWithdraw:list")
    public R list(@RequestParam Map<String, Object> params) {
        return R.ok().put("page", userWithdrawService.queryPage(params));
    }


    @GetMapping("/info/{id}")
    @RequiresPermissions("user:userWithdraw:info")
    public R info(@PathVariable("id") Long id) {
        return R.ok().put("userWithdraw", userWithdrawService.queryById(id));
    }

    @PostMapping("/check")
    @RequiresPermissions("user:userWithdraw:check")
    public R save(@RequestBody UserWithdraw userWithdraw) {
        UserWithdraw bean = userWithdrawService.getById(userWithdraw.getId());
        bean.setStatus(userWithdraw.getStatus());
        if(StringUtils.isNotBlank(userWithdraw.getRemark())){
            bean.setRemark(userWithdraw.getRemark());
        }
        if(bean.getStatus() == 2){
            UserInfo userInfo = userInfoService.getById(userWithdraw.getUserId());
            userInfo.setBalance(userInfo.getBalance().add(userWithdraw.getAmount()));
            userInfoService.update(userInfo);
            UserBalanceLog userBalanceLog = new UserBalanceLog();
            userBalanceLog.setType(LogTypeEnum.INCOME.code);
            userBalanceLog.setBalance(userInfo.getBalance());
            userBalanceLog.setAmount(userWithdraw.getAmount());
            userBalanceLog.setUserId(userInfo.getUserId().longValue());
            userBalanceLog.setStatus(UserBalanceLogStatusEnum.FAIL_WITHDRAW.code);
            userBalanceLogService.save(userBalanceLog);
        }
        userWithdrawService.updateById(bean);
        return R.ok();
    }


}
