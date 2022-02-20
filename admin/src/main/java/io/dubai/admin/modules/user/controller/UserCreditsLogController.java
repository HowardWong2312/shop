package io.dubai.admin.modules.user.controller;

import io.dubai.admin.modules.sys.controller.AbstractController;
import io.dubai.admin.modules.user.entity.UserCreditsLog;
import io.dubai.admin.modules.user.service.UserCreditsLogService;
import io.dubai.common.utils.Constant;
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
@RequestMapping("user/userCreditsLog")
public class UserCreditsLogController extends AbstractController {
    @Resource
    private UserCreditsLogService userCreditsLogService;

    @GetMapping("/list")
    @RequiresPermissions("user:userCreditsLog:list")
    public R list(@RequestParam Map<String, Object> params) {
        if(getUserId() != Constant.SUPER_ADMIN){
            params.put("sysUserId",getUserId().toString());
        }
        PageUtils page = userCreditsLogService.queryPage(params);
        return R.ok().put("page", page);
    }


    @GetMapping("/info/{id}")
    @RequiresPermissions("user:userCreditsLog:info")
    public R info(@PathVariable("id") Long id) {
        UserCreditsLog userCreditsLog = userCreditsLogService.getById(id);

        return R.ok().put("userCreditsLog", userCreditsLog);
    }

    @PostMapping("/save")
    @RequiresPermissions("user:userCreditsLog:save")
    public R save(@RequestBody UserCreditsLog userCreditsLog) {
        userCreditsLogService.save(userCreditsLog);

        return R.ok();
    }

    @PostMapping("/update")
    @RequiresPermissions("user:userCreditsLog:update")
    public R update(@RequestBody UserCreditsLog userCreditsLog) {
        ValidatorUtils.validateEntity(userCreditsLog);
        userCreditsLogService.updateById(userCreditsLog);

        return R.ok();
    }

    @DeleteMapping("/delete")
    @RequiresPermissions("user:userCreditsLog:delete")
    public R delete(@RequestBody Long[] ids) {
        userCreditsLogService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
