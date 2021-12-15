package io.dubai.admin.modules.goods.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.dubai.common.utils.PageUtils;
import io.dubai.admin.modules.goods.entity.ShopGoodsCategory;

import java.util.Map;

/**
 * 商品分类
 *
 * @author mother fucker
 * @email admin@gmail.com
 * @date 2021-12-15 15:03:45
 */
public interface ShopGoodsCategoryService extends IService<ShopGoodsCategory> {

    PageUtils queryPage(Map<String, Object> params);
}

