package io.dubai.admin.modules.goods.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.dubai.admin.modules.goods.entity.ShopGoodsLang;
import io.dubai.common.utils.PageUtils;

import java.util.Map;

/**
 * 商品标题
 *
 * @author mother fucker
 * @email admin@gmail.com
 * @date 2021-12-17 14:05:29
 */
public interface ShopGoodsLangService extends IService<ShopGoodsLang> {

    PageUtils queryPage(Map<String, Object> params);
}

