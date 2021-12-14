package io.dubai.modules.goods.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.dubai.common.utils.PageUtils;
import io.dubai.modules.goods.entity.GoodsLang;

import java.util.Map;

/**
 * 商品标题
 *
 * @author Howard
 * @email admin@gmail.com
 * @date 2021-10-07 16:25:36
 */
public interface GoodsLangService extends IService<GoodsLang> {

    PageUtils queryPage(Map<String, Object> params);
}

