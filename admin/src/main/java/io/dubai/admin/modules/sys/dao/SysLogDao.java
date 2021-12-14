package io.dubai.admin.modules.sys.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.dubai.admin.modules.sys.entity.SysLogEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 系统日志
 *
 * @author howard
 */
@Mapper
public interface SysLogDao extends BaseMapper<SysLogEntity> {

}
