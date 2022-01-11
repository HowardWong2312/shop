package io.dubai.admin.modules.other.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dubai.common.utils.PageUtils;
import io.dubai.common.utils.Query;
import io.dubai.admin.modules.other.dao.NewsDao;
import io.dubai.admin.modules.other.entity.NewsEntity;
import io.dubai.admin.modules.other.service.NewsService;
import io.dubai.admin.modules.sys.entity.SysDictEntity;
import io.dubai.admin.modules.sys.service.SysDictService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;


@Service("newsService")
public class NewsServiceImpl extends ServiceImpl<NewsDao, NewsEntity> implements NewsService {

    @Resource
    private SysDictService sysDictService;


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
        for (NewsEntity bean : page.getRecords()) {
            List<SysDictEntity> sysDictEntityList = sysDictService.queryByTypeAndCode("news_type",bean.getNewsType().toString());
            if(null == sysDictEntityList || sysDictEntityList.isEmpty()){
                continue;
            }
            bean.setDict(sysDictEntityList.get(0));
        }
        return new PageUtils(page);
    }
}
