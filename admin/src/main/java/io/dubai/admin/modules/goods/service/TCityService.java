package io.dubai.admin.modules.goods.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.dubai.common.utils.PageUtils;
import io.dubai.admin.modules.goods.entity.TCity;

import java.util.Map;

/**
 * 城市表
 *
 * @author mother fucker
 * @email admin@gmail.com
 * @date 2022-01-14 18:50:25
 */
public interface TCityService extends IService<TCity> {

    PageUtils queryPage(Map<String, Object> params);
}

