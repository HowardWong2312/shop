package io.dubai.admin.modules.goods.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.dubai.common.utils.PageUtils;
import io.dubai.admin.modules.goods.entity.TProvince;

import java.util.Map;

/**
 * 省级表
 *
 * @author mother fucker
 * @email admin@gmail.com
 * @date 2022-01-14 18:51:41
 */
public interface TProvinceService extends IService<TProvince> {

    PageUtils queryPage(Map<String, Object> params);
}

