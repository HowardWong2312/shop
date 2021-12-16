package io.dubai.admin.modules.sys.controller;


import io.dubai.admin.common.annotation.SysLog;
import io.dubai.admin.modules.sys.entity.SysUserEntity;
import io.dubai.admin.modules.sys.service.SysUserRoleService;
import io.dubai.admin.modules.sys.service.SysUserService;
import io.dubai.admin.modules.sys.shiro.ShiroUtils;
import io.dubai.common.utils.Constant;
import io.dubai.common.utils.PageUtils;
import io.dubai.common.utils.R;
import io.dubai.common.validator.Assert;
import io.dubai.common.validator.ValidatorUtils;
import io.dubai.common.validator.group.AddGroup;
import io.dubai.common.validator.group.UpdateGroup;
import org.apache.commons.lang.ArrayUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 系统用户
 *
 * @author mother fucker
 */

@RestController
@RequestMapping("/sys/user")
public class SysUserController extends AbstractController {
    @Resource
    private SysUserService sysUserService;
    @Resource
    private SysUserRoleService sysUserRoleService;

    /**
     * 所有用户列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("sys:user:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = sysUserService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
     * 查看所有用户，用于下拉列表
     */
    @RequestMapping("/listForSelect")
    public R listForSelect() {
        List<SysUserEntity> list = null;
        if (getUserId() == Constant.SUPER_ADMIN) {
            list = sysUserService.queryAllForSelect();
        }
        return R.ok().put("list", list);
    }

    /**
     * 根据部门ID查询用户，用于下拉列表
     */
    @RequestMapping("/listForSelectByDeptId/{deptId}")
    public R listForSelectByDeptId(@PathVariable("deptId") Long deptId) {
        List<SysUserEntity> list = null;
        if (getUserId() == Constant.SUPER_ADMIN) {
            list = sysUserService.queryByDeptIdForSelect(deptId);
        }
        return R.ok().put("list", list);
    }


    /**
     * 获取登录的用户信息
     */
    @RequestMapping("/info")
    public R info() {
        return R.ok().put("user", getUser());
    }

    /**
     * 修改登录用户密码
     */
    @SysLog("修改管理员密码")
    @RequestMapping("/password")
    public R password(String password, String newPassword) {
        Assert.isBlank(newPassword, "新密码不为能空");

        //原密码
        password = ShiroUtils.sha256(password, getUser().getSalt());
        //新密码
        newPassword = ShiroUtils.sha256(newPassword, getUser().getSalt());

        //更新密码
        boolean flag = sysUserService.updatePassword(getUserId(), password, newPassword);
        if (!flag) {
            return R.error("原密码不正确");
        }

        return R.ok();
    }

    /**
     * 用户信息
     */
    @RequestMapping("/info/{userId}")
    @RequiresPermissions("sys:user:info")
    public R info(@PathVariable("userId") Long userId) {
        SysUserEntity user = sysUserService.getById(userId);

        //获取用户所属的角色列表
        List<Long> roleIdList = sysUserRoleService.queryRoleIdList(userId);
        user.setRoleIdList(roleIdList);

        return R.ok().put("user", user);
    }

    /**
     * 保存用户
     */
    @SysLog("新增管理员账户")
    @RequestMapping("/save")
    @RequiresPermissions("sys:user:save")
    public R save(@RequestBody SysUserEntity user) {
        ValidatorUtils.validateEntity(user, AddGroup.class);

        sysUserService.saveUser(user);

        return R.ok();
    }

    /**
     * 修改用户
     */
    @SysLog("修改管理员账户")
    @RequestMapping("/update")
    @RequiresPermissions("sys:user:update")
    public R update(@RequestBody SysUserEntity user) {
        ValidatorUtils.validateEntity(user, UpdateGroup.class);

        sysUserService.update(user);

        return R.ok();
    }

    /**
     * 删除用户
     */
    @SysLog("删除管理员账户")
    @RequestMapping("/delete")
    @RequiresPermissions("sys:user:delete")
    public R delete(@RequestBody Long[] userIds) {
        if (ArrayUtils.contains(userIds, 1L)) {
            return R.error("系统管理员不能删除");
        }

        if (ArrayUtils.contains(userIds, getUserId())) {
            return R.error("当前用户不能删除");
        }

        sysUserService.removeByIds(Arrays.asList(userIds));

        return R.ok();
    }
}
