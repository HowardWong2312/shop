package io.dubai.admin.modules.goods.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.dubai.admin.modules.goods.entity.Country;
import io.dubai.common.utils.PageUtils;

import java.util.Map;

/**
 * 国家表
 *
 * @author mother fucker
 * @email admin@gmail.com
 * @date 2021-12-19 14:07:37
 */
public interface CountryService extends IService<Country> {

    PageUtils queryPage(Map<String, Object> params);
}

