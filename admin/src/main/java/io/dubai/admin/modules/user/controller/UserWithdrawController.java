package io.dubai.admin.modules.user.controller;

import io.dubai.admin.modules.user.entity.UserWithdraw;
import io.dubai.admin.modules.user.service.UserWithdrawService;
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
@RequestMapping("user/userWithdraw")
public class UserWithdrawController {
    @Resource
    private UserWithdrawService userWithdrawService;

    @GetMapping("/list")
    @RequiresPermissions("user:userWithdraw:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = userWithdrawService.queryPage(params);

        return R.ok().put("page", page);
    }


    @GetMapping("/info/{id}")
    @RequiresPermissions("user:userWithdraw:info")
    public R info(@PathVariable("id") Long id) {
        UserWithdraw userWithdraw = userWithdrawService.getById(id);

        return R.ok().put("userWithdraw", userWithdraw);
    }

    @PostMapping("/save")
    @RequiresPermissions("user:userWithdraw:save")
    public R save(@RequestBody UserWithdraw userWithdraw) {
        userWithdrawService.save(userWithdraw);

        return R.ok();
    }

    @PostMapping("/update")
    @RequiresPermissions("user:userWithdraw:update")
    public R update(@RequestBody UserWithdraw userWithdraw) {
        ValidatorUtils.validateEntity(userWithdraw);
        userWithdrawService.updateById(userWithdraw);

        return R.ok();
    }

    @DeleteMapping("/delete")
    @RequiresPermissions("user:userWithdraw:delete")
    public R delete(@RequestBody Long[] ids) {
        userWithdrawService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
