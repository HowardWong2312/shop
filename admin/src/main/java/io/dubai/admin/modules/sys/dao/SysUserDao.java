package io.dubai.admin.modules.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.dubai.admin.modules.sys.entity.SysUserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 系统用户
 *
 * @author howard
 */
@Mapper
public interface SysUserDao extends BaseMapper<SysUserEntity> {

    /**
     * 查询用户的所有权限
     *
     * @param userId 用户ID
     */
    List<String> queryAllPerms(Long userId);

    /**
     * 查询用户的所有菜单ID
     */
    List<Long> queryAllMenuId(Long userId);

    /**
     * 查询所有用户的集合
     */
    @Select("select user_id,username from sys_user where user_id!=1 order by user_id desc")
    List<SysUserEntity> queryAllForSelect();

    /**
     * 查询所有用户的集合
     */
    @Select("select user_id,username from sys_user where dept_id=#{deptId} and user_id!=1 order by user_id desc")
    List<SysUserEntity> queryByDeptIdForSelect(@Param("deptId") Long deptId);

}
