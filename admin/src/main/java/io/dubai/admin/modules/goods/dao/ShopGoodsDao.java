package io.dubai.admin.modules.goods.dao;

import io.dubai.admin.modules.goods.entity.ShopGoods;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 商品
 * 
 * @author mother fucker
 * @email admin@gmail.com
 * @date 2021-12-17 14:05:29
 */
@Mapper
public interface ShopGoodsDao extends BaseMapper<ShopGoods> {
	
}
