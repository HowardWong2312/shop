package io.dubai.admin.modules.goods.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.dubai.admin.modules.goods.entity.ShopGoods;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 商品
 *
 * @author mother fucker
 * @email admin@gmail.com
 * @date 2021-12-17 14:05:29
 */
@Mapper
public interface ShopGoodsDao extends BaseMapper<ShopGoods> {

    List<ShopGoods> shopGoodsList(IPage page, @Param("params") Map<String, Object> params);

}
