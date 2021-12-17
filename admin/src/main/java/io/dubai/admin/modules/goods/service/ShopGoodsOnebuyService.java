package io.dubai.admin.modules.goods.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.dubai.admin.modules.goods.entity.ShopGoodsOnebuy;
import io.dubai.admin.modules.goods.entity.vo.ShopGoodsOnebuyVo;
import io.dubai.common.utils.PageUtils;

import java.util.Map;

/**
 * 一元购活动
 *
 * @author mother fucker
 * @email admin@gmail.com
 * @date 2021-12-16 18:16:04
 */
public interface ShopGoodsOnebuyService extends IService<ShopGoodsOnebuy> {

    PageUtils queryPage(Map<String, Object> params);

    ShopGoodsOnebuyVo queryById(Long id);
}

