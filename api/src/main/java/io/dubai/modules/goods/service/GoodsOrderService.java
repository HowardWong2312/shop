package io.dubai.modules.goods.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.dubai.common.utils.PageUtils;
import io.dubai.modules.goods.entity.GoodsOrder;
import io.dubai.modules.goods.entity.vo.GoodsOrderVo;
import io.dubai.modules.goods.query.GoodsOrderQuery;

import java.util.List;
import java.util.Map;

/**
 * 商品订单
 *
 * @author Howard
 * @email admin@gmail.com
 * @date 2021-10-07 16:25:36
 */
public interface GoodsOrderService extends IService<GoodsOrder> {

    PageUtils queryPage(GoodsOrderQuery query);

    PageUtils queryPageForMerchant(GoodsOrderQuery query);

    GoodsOrderVo queryByOrderCodeAndLanguageId(String orderCode, Long languageId);

    GoodsOrderVo queryByOrderCodeAndLanguageIdForMerchant(String orderCode, Long languageId);

}

