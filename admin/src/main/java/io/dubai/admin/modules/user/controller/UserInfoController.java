package io.dubai.admin.modules.user.controller;

import io.dubai.admin.modules.user.entity.UserInfo;
import io.dubai.admin.modules.user.service.UserInfoService;
import io.dubai.common.utils.PageUtils;
import io.dubai.common.utils.R;
import io.dubai.common.validator.ValidatorUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
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

    @GetMapping("/list")
    @RequiresPermissions("user:userInfo:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = userInfoService.queryPage(params);
        return R.ok().put("page", page);
    }


    @GetMapping("/info/{userid}")
    @RequiresPermissions("user:userInfo:info")
    public R info(@PathVariable("userid") Integer userid) {
        UserInfo userInfo = userInfoService.getById(userid);

        return R.ok().put("userInfo", userInfo);
    }

    @PostMapping("/save")
    @RequiresPermissions("user:userInfo:save")
    public R save(@RequestBody UserInfo userInfo) {
        userInfoService.save(userInfo);

        return R.ok();
    }

    @PostMapping("/update")
    @RequiresPermissions("user:userInfo:update")
    public R update(@RequestBody UserInfo userInfo) {
        ValidatorUtils.validateEntity(userInfo);
        userInfoService.update(userInfo);
        return R.ok();
    }

    @DeleteMapping("/delete")
    @RequiresPermissions("user:userInfo:delete")
    public R delete(@RequestBody Integer[] userids) {
        userInfoService.removeByIds(Arrays.asList(userids));

        return R.ok();
    }

}
