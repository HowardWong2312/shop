package io.dubai.admin.modules.sys.service;


import com.baomidou.mybatisplus.extension.service.IService;
import io.dubai.admin.modules.sys.entity.SysRoleEntity;
import io.dubai.common.utils.PageUtils;

import java.util.Map;


/**
 * 角色
 *
 * @author mother fucker
 */
public interface SysRoleService extends IService<SysRoleEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveRole(SysRoleEntity role);

    void update(SysRoleEntity role);

    void deleteBatch(Long[] roleIds);

}
