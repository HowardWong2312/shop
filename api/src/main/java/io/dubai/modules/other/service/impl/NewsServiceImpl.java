package io.dubai.modules.other.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dubai.common.utils.PageUtils;
import io.dubai.common.utils.Query;
import io.dubai.modules.other.dao.NewsDao;
import io.dubai.modules.other.entity.NewsEntity;
import io.dubai.modules.other.service.NewsService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("newsService")
public class NewsServiceImpl extends ServiceImpl<NewsDao, NewsEntity> implements NewsService {


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String title = (String)params.get("title");
        String newsType = (String)params.get("newsType");
        IPage<NewsEntity> page = this.page(
                new Query<NewsEntity>().getPage(params),
                new QueryWrapper<NewsEntity>()
                        .like(StringUtils.isNotBlank(title),"title",title)
                        .eq(StringUtils.isNotBlank(newsType),"news_type",newsType)
        );
        return new PageUtils(page);
    }
}
