package io.dubai.modules.other.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dubai.common.utils.PageUtils;
import io.dubai.common.utils.Query;
import io.dubai.modules.other.dao.LanguageDao;
import io.dubai.modules.other.entity.Language;
import io.dubai.modules.other.service.LanguageService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("languageService")
public class LanguageServiceImpl extends ServiceImpl<LanguageDao, Language> implements LanguageService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<Language> page = this.page(
                new Query<Language>().getPage(params),
                new QueryWrapper<Language>()
        );

        return new PageUtils(page);
    }

    @Override
    @Cacheable(value = "language", key = "#name")
    public Language queryByName(String name) {
        return baseMapper.selectOne(new QueryWrapper<Language>().eq("name",name));
    }

}
