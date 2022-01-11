package io.dubai.admin.modules.other.service;


import com.baomidou.mybatisplus.extension.service.IService;
import io.dubai.common.utils.PageUtils;
import io.dubai.admin.modules.other.entity.NewsEntity;

import java.util.Map;

/**
 * 
 *
 * @author howard
 * @email admin@gmail.com
 * @date 2020-11-04 23:42:56
 */
public interface NewsService extends IService<NewsEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

