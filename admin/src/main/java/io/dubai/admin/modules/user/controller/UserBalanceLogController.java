package io.dubai.admin.modules.user.controller;

import io.dubai.admin.modules.sys.controller.AbstractController;
import io.dubai.admin.modules.user.entity.UserBalanceLog;
import io.dubai.admin.modules.user.service.UserBalanceLogService;
import io.dubai.common.utils.Constant;
import io.dubai.common.utils.PageUtils;
import io.dubai.common.utils.R;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
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
public class UserBalanceLogController extends AbstractController {
    @Resource
    private UserBalanceLogService userBalanceLogService;

    @GetMapping("/list")
    @RequiresPermissions("user:userBalanceLog:list")
    public R list(@RequestParam Map<String, Object> params) {
        if(getDeptId() != Constant.SUPER_DEPT){
            params.put("sysUserId",getUserId().toString());
        }
        PageUtils page = userBalanceLogService.queryPage(params);
        return R.ok().put("page", page).put("totalAmount",userBalanceLogService.queryAmountSumTotal(params));
    }


    @GetMapping("/info/{id}")
    @RequiresPermissions("user:userBalanceLog:info")
    public R info(@PathVariable("id") Long id) {
        UserBalanceLog userBalanceLog = userBalanceLogService.getById(id);

        return R.ok().put("userBalanceLog", userBalanceLog);
    }

}
