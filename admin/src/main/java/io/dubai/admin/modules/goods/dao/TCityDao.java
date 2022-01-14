package io.dubai.admin.modules.goods.dao;

import io.dubai.admin.modules.goods.entity.TCity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 城市表
 * 
 * @author mother fucker
 * @email admin@gmail.com
 * @date 2022-01-14 18:50:25
 */
@Mapper
public interface TCityDao extends BaseMapper<TCity> {
	
}
