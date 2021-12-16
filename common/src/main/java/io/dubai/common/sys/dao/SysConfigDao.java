package io.dubai.common.sys.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.dubai.common.sys.entity.SysConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 系统配置信息
 *
 * @author mother fucker
 */
@Mapper
public interface SysConfigDao extends BaseMapper<SysConfig> {

    /**
     * 根据key，查询value
     */
    SysConfig queryByKey(String paramKey);

    /**
     * 根据key，更新value
     */
    int updateValueByKey(@Param("paramKey") String paramKey, @Param("paramValue") String paramValue);

}
