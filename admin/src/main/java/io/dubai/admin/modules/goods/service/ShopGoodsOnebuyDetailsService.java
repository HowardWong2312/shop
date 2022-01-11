package io.dubai.admin.modules.goods.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.dubai.admin.modules.goods.entity.vo.ShopGoodsOnebuyDetailsVo;
import io.dubai.common.utils.PageUtils;
import io.dubai.admin.modules.goods.entity.ShopGoodsOnebuyDetails;

import java.util.List;
import java.util.Map;

/**
 * 一元购记录
 *
 * @author mother fucker
 * @email admin@gmail.com
 * @date 2022-01-03 17:49:28
 */
public interface ShopGoodsOnebuyDetailsService extends IService<ShopGoodsOnebuyDetails> {

    List<ShopGoodsOnebuyDetailsVo> queryList(Long goodsOnebuyId);
}

