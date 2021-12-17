package io.dubai.admin.modules.goods.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.dubai.common.utils.PageUtils;
import io.dubai.admin.modules.goods.entity.ShopGoodsImg;

import java.util.Map;

/**
 * 商品图片
 *
 * @author mother fucker
 * @email admin@gmail.com
 * @date 2021-12-17 14:05:29
 */
public interface ShopGoodsImgService extends IService<ShopGoodsImg> {

    PageUtils queryPage(Map<String, Object> params);
}

