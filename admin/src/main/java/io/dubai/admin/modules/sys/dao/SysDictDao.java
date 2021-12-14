package io.dubai.admin.modules.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.dubai.admin.modules.sys.entity.SysDictEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 数据字典
 *
 * @author howard
 */
@Mapper
public interface SysDictDao extends BaseMapper<SysDictEntity> {

}
