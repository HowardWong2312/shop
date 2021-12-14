package io.dubai.admin.modules.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.dubai.admin.modules.sys.entity.SysRoleEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 角色管理
 *
 * @author howard
 */
@Mapper
public interface SysRoleDao extends BaseMapper<SysRoleEntity> {


}
