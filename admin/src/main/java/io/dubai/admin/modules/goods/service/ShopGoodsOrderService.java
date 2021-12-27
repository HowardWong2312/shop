package io.dubai.admin.modules.goods.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.dubai.admin.modules.goods.entity.ShopGoodsOrder;
import io.dubai.admin.modules.goods.entity.vo.ShopGoodsOrderVo;
import io.dubai.common.utils.PageUtils;

import java.util.Map;

/**
 * 商品订单
 *
 * @author mother fucker
 * @email admin@gmail.com
 * @date 2021-12-25 18:14:19
 */
public interface ShopGoodsOrderService extends IService<ShopGoodsOrder> {

    PageUtils queryPage(Map<String, Object> params);

    ShopGoodsOrderVo queryById(String id);
}

