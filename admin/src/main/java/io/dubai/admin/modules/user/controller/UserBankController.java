package io.dubai.admin.modules.user.controller;

import io.dubai.admin.modules.sys.controller.AbstractController;
import io.dubai.admin.modules.user.entity.UserBank;
import io.dubai.admin.modules.user.service.UserBankService;
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
 * 提现申请表
 *
 * @author mother fucker
 * @name 提现申请表
 * @date 2021-12-14 18:27:22
 */
@RestController
@RequestMapping("user/userBank")
public class UserBankController extends AbstractController {
    @Resource
    private UserBankService userBankService;

    @GetMapping("/list")
    @RequiresPermissions("user:userBank:list")
    public R list(@RequestParam Map<String, Object> params) {
        if(getUserId() != Constant.SUPER_ADMIN){
            params.put("sysUserId",getUserId().toString());
        }
        PageUtils page = userBankService.queryPage(params);

        return R.ok().put("page", page);
    }


    @GetMapping("/info/{id}")
    @RequiresPermissions("user:userBank:info")
    public R info(@PathVariable("id") Long id) {
        UserBank userBank = userBankService.getById(id);

        return R.ok().put("userBank", userBank);
    }

    @PostMapping("/save")
    @RequiresPermissions("user:userBank:save")
    public R save(@RequestBody UserBank userBank) {
        userBankService.save(userBank);

        return R.ok();
    }

    @PostMapping("/update")
    @RequiresPermissions("user:userBank:update")
    public R update(@RequestBody UserBank userBank) {
        ValidatorUtils.validateEntity(userBank);
        userBankService.updateById(userBank);

        return R.ok();
    }

    @DeleteMapping("/delete")
    @RequiresPermissions("user:userBank:delete")
    public R delete(@RequestBody Long[] ids) {
        userBankService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
