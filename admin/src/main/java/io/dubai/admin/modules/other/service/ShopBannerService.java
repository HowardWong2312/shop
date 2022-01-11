package io.dubai.admin.modules.other.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.dubai.common.utils.PageUtils;
import io.dubai.admin.modules.other.entity.ShopBanner;

import java.util.Map;

/**
 * 轮播图
 *
 * @author mother fucker
 * @email admin@gmail.com
 * @date 2022-01-04 19:01:44
 */
public interface ShopBannerService extends IService<ShopBanner> {

    PageUtils queryPage(Map<String, Object> params);
}

