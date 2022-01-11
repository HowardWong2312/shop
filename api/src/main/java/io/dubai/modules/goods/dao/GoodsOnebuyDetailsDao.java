package io.dubai.modules.goods.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.dubai.modules.goods.entity.GoodsOnebuyDetails;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 一元购记录
 * 
 * @author mother fucker
 * @email admin@gmail.com
 * @date 2022-01-03 17:49:28
 */
@Mapper
public interface GoodsOnebuyDetailsDao extends BaseMapper<GoodsOnebuyDetails> {
	
}
