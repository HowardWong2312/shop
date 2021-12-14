package io.dubai.admin.modules.user.controller;

import io.dubai.admin.modules.user.entity.UserAddress;
import io.dubai.admin.modules.user.service.UserAddressService;
import io.dubai.common.utils.PageUtils;
import io.dubai.common.utils.R;
import io.dubai.common.validator.ValidatorUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Map;


/**
 * 收货地址
 *
 * @author mother fucker
 * @name 收货地址
 * @date 2021-12-14 18:27:22
 */
@RestController
@RequestMapping("user/userAddress")
public class UserAddressController {
    @Resource
    private UserAddressService userAddressService;

    @GetMapping("/list")
    @RequiresPermissions("user:userAddress:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = userAddressService.queryPage(params);

        return R.ok().put("page", page);
    }


    @GetMapping("/info/{id}")
    @RequiresPermissions("user:userAddress:info")
    public R info(@PathVariable("id") Long id) {
        UserAddress userAddress = userAddressService.getById(id);

        return R.ok().put("userAddress", userAddress);
    }

    @PostMapping("/save")
    @RequiresPermissions("user:userAddress:save")
    public R save(@RequestBody UserAddress userAddress) {
        userAddressService.save(userAddress);

        return R.ok();
    }

    @PostMapping("/update")
    @RequiresPermissions("user:userAddress:update")
    public R update(@RequestBody UserAddress userAddress) {
        ValidatorUtils.validateEntity(userAddress);
        userAddressService.updateById(userAddress);

        return R.ok();
    }

    @DeleteMapping("/delete")
    @RequiresPermissions("user:userAddress:delete")
    public R delete(@RequestBody Long[] ids) {
        userAddressService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
