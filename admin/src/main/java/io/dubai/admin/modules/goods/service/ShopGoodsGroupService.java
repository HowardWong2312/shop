package io.dubai.admin.modules.goods.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.dubai.admin.modules.goods.entity.ShopGoodsGroup;
import io.dubai.common.utils.PageUtils;

import java.util.Map;

/**
 * 拼团商品
 *
 * @author mother fucker
 * @email admin@gmail.com
 * @date 2021-12-19 20:21:24
 */
public interface ShopGoodsGroupService extends IService<ShopGoodsGroup> {

    PageUtils queryPage(Map<String, Object> params);
}

