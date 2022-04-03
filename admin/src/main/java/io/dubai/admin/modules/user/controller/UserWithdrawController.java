package io.dubai.admin.modules.user.controller;

import com.cz.czUser.system.entity.UserInfo;
import io.dubai.admin.modules.sys.controller.AbstractController;
import io.dubai.admin.modules.user.entity.UserBalanceLog;
import io.dubai.admin.modules.user.entity.UserWithdraw;
import io.dubai.admin.modules.user.service.UserBalanceLogService;
import io.dubai.admin.modules.user.service.UserInfoService;
import io.dubai.admin.modules.user.service.UserWithdrawService;
import io.dubai.common.enums.LogTypeEnum;
import io.dubai.common.enums.UserBalanceLogStatusEnum;
import io.dubai.common.utils.Constant;
import io.dubai.common.utils.R;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
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
public class UserWithdrawController extends AbstractController {
    @Resource
    private UserWithdrawService userWithdrawService;

    @Resource
    private UserInfoService userInfoService;

    @Resource
    private UserBalanceLogService userBalanceLogService;

    @GetMapping("/list")
    @RequiresPermissions("user:userWithdraw:list")
    public R list(@RequestParam Map<String, Object> params) {
        if(getDeptId() != Constant.SUPER_DEPT){
            params.put("sysUserId",getUserId().toString());
        }
        return R.ok().put("page", userWithdrawService.queryPage(params)).put("totalAmount",userWithdrawService.queryAmountSumTotal(params));
    }


    @GetMapping("/info/{id}")
    @RequiresPermissions("user:userWithdraw:info")
    public R info(@PathVariable("id") Long id) {
        return R.ok().put("userWithdraw", userWithdrawService.queryById(id));
    }

    @PostMapping("/check")
    @RequiresPermissions("user:userWithdraw:check")
    public R save(@RequestBody UserWithdraw form) {
        UserWithdraw bean = userWithdrawService.getById(form.getId());
        bean.setStatus(form.getStatus());
        if(form.getRebate() != null){
            bean.setRebate(form.getRebate());
            bean.setRealAmount(bean.getRealAmount().subtract(form.getRebate().divide(BigDecimal.valueOf(100)).multiply(bean.getAmount())));
        }
        if(form.getTax() != null){
            bean.setTax(form.getTax());
            bean.setRealAmount(bean.getRealAmount().subtract(form.getTax().divide(BigDecimal.valueOf(100)).multiply(bean.getAmount())));
        }
        if(StringUtils.isNotBlank(form.getRemark())){
            bean.setRemark(form.getRemark());
        }
        if(bean.getStatus() == 2){
            UserInfo userInfo = userInfoService.getById(form.getUserId());
            userInfo.setBalance(userInfo.getBalance().add(form.getAmount()));
            userInfoService.update(userInfo);
            UserBalanceLog userBalanceLog = new UserBalanceLog();
            userBalanceLog.setType(LogTypeEnum.INCOME.code);
            userBalanceLog.setBalance(userInfo.getBalance());
            userBalanceLog.setAmount(form.getAmount());
            userBalanceLog.setUserId(userInfo.getUserId().longValue());
            userBalanceLog.setStatus(UserBalanceLogStatusEnum.FAIL_WITHDRAW.code);
            userBalanceLogService.save(userBalanceLog);
        }
        userWithdrawService.updateById(bean);
        return R.ok();
    }


}
