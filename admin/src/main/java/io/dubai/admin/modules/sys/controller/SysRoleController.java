package io.dubai.admin.modules.sys.controller;

import io.dubai.admin.common.annotation.SysLog;
import io.dubai.admin.modules.sys.entity.SysRoleEntity;
import io.dubai.admin.modules.sys.service.SysRoleDeptService;
import io.dubai.admin.modules.sys.service.SysRoleMenuService;
import io.dubai.admin.modules.sys.service.SysRoleService;
import io.dubai.common.utils.PageUtils;
import io.dubai.common.utils.R;
import io.dubai.common.validator.ValidatorUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 角色管理
 *
 * @author mother fucker
 */
@RestController
@RequestMapping("/sys/role")
public class SysRoleController extends AbstractController {
    @Resource
    private SysRoleService sysRoleService;
    @Resource
    private SysRoleMenuService sysRoleMenuService;
    @Resource
    private SysRoleDeptService sysRoleDeptService;

    /**
     * 角色列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("sys:role:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = sysRoleService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
     * 角色列表
     */
    @RequestMapping("/select")
    @RequiresPermissions("sys:role:select")
    public R select() {
        List<SysRoleEntity> list = sysRoleService.list();

        return R.ok().put("list", list);
    }

    /**
     * 角色信息
     */
    @RequestMapping("/info/{roleId}")
    @RequiresPermissions("sys:role:info")
    public R info(@PathVariable("roleId") Long roleId) {
        SysRoleEntity role = sysRoleService.getById(roleId);

        //查询角色对应的菜单
        List<Long> menuIdList = sysRoleMenuService.queryMenuIdList(roleId);
        role.setMenuIdList(menuIdList);

        //查询角色对应的部门
        List<Long> deptIdList = sysRoleDeptService.queryDeptIdList(new Long[]{roleId});
        role.setDeptIdList(deptIdList);

        return R.ok().put("role", role);
    }

    /**
     * 保存角色
     */
    @SysLog("保存角色")
    @RequestMapping("/save")
    @RequiresPermissions("sys:role:save")
    public R save(@RequestBody SysRoleEntity role) {
        ValidatorUtils.validateEntity(role);

        sysRoleService.saveRole(role);

        return R.ok();
    }

    /**
     * 修改角色
     */
    @SysLog("修改角色")
    @RequestMapping("/update")
    @RequiresPermissions("sys:role:update")
    public R update(@RequestBody SysRoleEntity role) {
        ValidatorUtils.validateEntity(role);

        sysRoleService.update(role);

        return R.ok();
    }

    /**
     * 删除角色
     */
    @SysLog("删除角色")
    @RequestMapping("/delete")
    @RequiresPermissions("sys:role:delete")
    public R delete(@RequestBody Long[] roleIds) {
        sysRoleService.deleteBatch(roleIds);

        return R.ok();
    }
}
