package io.dubai.admin.modules.user.controller;

import io.dubai.admin.modules.user.entity.UserBalanceLog;
import io.dubai.admin.modules.user.service.UserBalanceLogService;
import io.dubai.common.utils.PageUtils;
import io.dubai.common.utils.R;
import io.dubai.common.validator.ValidatorUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Map;


/**
 * 资金记录
 *
 * @author mother fucker
 * @name 资金记录
 * @date 2021-12-14 18:27:22
 */
@RestController
@RequestMapping("user/userBalanceLog")
public class UserBalanceLogController {
    @Resource
    private UserBalanceLogService userBalanceLogService;

    @GetMapping("/list")
    @RequiresPermissions("user:userBalanceLog:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = userBalanceLogService.queryPage(params);

        return R.ok().put("page", page);
    }


    @GetMapping("/info/{id}")
    @RequiresPermissions("user:userBalanceLog:info")
    public R info(@PathVariable("id") Long id) {
        UserBalanceLog userBalanceLog = userBalanceLogService.getById(id);

        return R.ok().put("userBalanceLog", userBalanceLog);
    }

    @PostMapping("/save")
    @RequiresPermissions("user:userBalanceLog:save")
    public R save(@RequestBody UserBalanceLog userBalanceLog) {
        userBalanceLogService.save(userBalanceLog);

        return R.ok();
    }

    @PostMapping("/update")
    @RequiresPermissions("user:userBalanceLog:update")
    public R update(@RequestBody UserBalanceLog userBalanceLog) {
        ValidatorUtils.validateEntity(userBalanceLog);
        userBalanceLogService.updateById(userBalanceLog);

        return R.ok();
    }

    @DeleteMapping("/delete")
    @RequiresPermissions("user:userBalanceLog:delete")
    public R delete(@RequestBody Long[] ids) {
        userBalanceLogService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
