package io.dubai.admin.modules.goods.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.dubai.admin.modules.goods.entity.ShopGoodsRush;
import io.dubai.admin.modules.goods.entity.vo.ShopGoodsRushVo;
import io.dubai.common.utils.PageUtils;

import java.util.Map;

/**
 * 免费抢活动
 *
 * @author mother fucker
 * @email admin@gmail.com
 * @date 2021-12-16 18:16:04
 */
public interface ShopGoodsRushService extends IService<ShopGoodsRush> {

    PageUtils queryPage(Map<String, Object> params);

    ShopGoodsRushVo queryById(Long id);
}

