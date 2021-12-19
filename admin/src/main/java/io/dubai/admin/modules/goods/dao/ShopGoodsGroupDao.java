package io.dubai.admin.modules.goods.dao;

import io.dubai.admin.modules.goods.entity.ShopGoodsGroup;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 拼团商品
 * 
 * @author mother fucker
 * @email admin@gmail.com
 * @date 2021-12-19 20:21:24
 */
@Mapper
public interface ShopGoodsGroupDao extends BaseMapper<ShopGoodsGroup> {
	
}
