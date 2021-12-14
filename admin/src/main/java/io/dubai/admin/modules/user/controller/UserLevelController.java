package io.dubai.admin.modules.user.controller;

import io.dubai.admin.modules.user.entity.UserLevel;
import io.dubai.admin.modules.user.service.UserLevelService;
import io.dubai.common.utils.PageUtils;
import io.dubai.common.utils.R;
import io.dubai.common.validator.ValidatorUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Map;


/**
 * 用户等级
 *
 * @author mother fucker
 * @name 用户等级
 * @date 2021-12-14 18:27:22
 */
@RestController
@RequestMapping("user/userLevel")
public class UserLevelController {
    @Resource
    private UserLevelService userLevelService;

    @GetMapping("/list")
    @RequiresPermissions("user:userLevel:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = userLevelService.queryPage(params);

        return R.ok().put("page", page);
    }


    @GetMapping("/info/{id}")
    @RequiresPermissions("user:userLevel:info")
    public R info(@PathVariable("id") Integer id) {
        UserLevel userLevel = userLevelService.getById(id);

        return R.ok().put("userLevel", userLevel);
    }

    @PostMapping("/save")
    @RequiresPermissions("user:userLevel:save")
    public R save(@RequestBody UserLevel userLevel) {
        userLevelService.save(userLevel);

        return R.ok();
    }

    @PostMapping("/update")
    @RequiresPermissions("user:userLevel:update")
    public R update(@RequestBody UserLevel userLevel) {
        ValidatorUtils.validateEntity(userLevel);
        userLevelService.updateById(userLevel);

        return R.ok();
    }

    @DeleteMapping("/delete")
    @RequiresPermissions("user:userLevel:delete")
    public R delete(@RequestBody Integer[] ids) {
        userLevelService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
