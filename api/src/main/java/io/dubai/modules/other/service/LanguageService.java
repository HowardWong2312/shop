package io.dubai.modules.other.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.dubai.common.utils.PageUtils;
import io.dubai.modules.other.entity.Language;

import java.util.Map;

/**
 * 语言
 *
 * @author mother fucker
 * @email admin@gmail.com
 * @date 2021-10-11 20:00:11
 */
public interface LanguageService extends IService<Language> {

    PageUtils queryPage(Map<String, Object> params);

    Language queryByName(String name);
}

