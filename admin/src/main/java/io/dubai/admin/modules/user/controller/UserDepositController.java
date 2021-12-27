package io.dubai.admin.modules.user.controller;

import io.dubai.admin.modules.user.entity.UserDeposit;
import io.dubai.admin.modules.user.service.UserDepositService;
import io.dubai.common.utils.PageUtils;
import io.dubai.common.utils.R;
import io.dubai.common.validator.ValidatorUtils;
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
@RequestMapping("user/userDeposit")
public class UserDepositController {
    @Resource
    private UserDepositService userDepositService;

    @GetMapping("/list")
    @RequiresPermissions("user:userDeposit:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = userDepositService.queryPage(params);
        return R.ok().put("page", page);
    }


    @GetMapping("/info/{orderCode}")
    @RequiresPermissions("user:userDeposit:info")
    public R info(@PathVariable("orderCode") String orderCode) {
        UserDeposit userDeposit = userDepositService.getById(orderCode);

        return R.ok().put("userDeposit", userDeposit);
    }

    @PostMapping("/save")
    @RequiresPermissions("user:userDeposit:save")
    public R save(@RequestBody UserDeposit userDeposit) {
        userDepositService.save(userDeposit);

        return R.ok();
    }

    @PostMapping("/update")
    @RequiresPermissions("user:userDeposit:update")
    public R update(@RequestBody UserDeposit userDeposit) {
        ValidatorUtils.validateEntity(userDeposit);
        userDepositService.updateById(userDeposit);

        return R.ok();
    }

    @DeleteMapping("/delete")
    @RequiresPermissions("user:userDeposit:delete")
    public R delete(@RequestBody String[] orderCodes) {
        userDepositService.removeByIds(Arrays.asList(orderCodes));

        return R.ok();
    }

}
