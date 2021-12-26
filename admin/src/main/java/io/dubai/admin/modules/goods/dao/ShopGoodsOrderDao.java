package io.dubai.admin.modules.goods.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.dubai.admin.modules.goods.entity.ShopGoodsOrder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.dubai.admin.modules.goods.entity.vo.ShopGoodsOnebuyVo;
import io.dubai.admin.modules.goods.entity.vo.ShopGoodsOrderVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 商品订单
 * 
 * @author mother fucker
 * @email admin@gmail.com
 * @date 2021-12-25 18:14:19
 */
@Mapper
public interface ShopGoodsOrderDao extends BaseMapper<ShopGoodsOrder> {
    List<ShopGoodsOrderVo> queryPage(IPage page, Map<String, Object> params);

    ShopGoodsOrderVo queryById(String id);
}
