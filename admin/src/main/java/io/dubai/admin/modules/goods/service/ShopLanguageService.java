package io.dubai.admin.modules.goods.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.dubai.common.utils.PageUtils;
import io.dubai.admin.modules.goods.entity.ShopLanguage;

import java.util.Map;

/**
 * 语言
 *
 * @author mother fucker
 * @email admin@gmail.com
 * @date 2021-12-15 20:01:22
 */
public interface ShopLanguageService extends IService<ShopLanguage> {

    PageUtils queryPage(Map<String, Object> params);
}

