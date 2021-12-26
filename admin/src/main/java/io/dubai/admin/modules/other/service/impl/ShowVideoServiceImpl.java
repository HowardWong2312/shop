package io.dubai.admin.modules.other.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dubai.admin.modules.other.dao.ShowVideoDao;
import io.dubai.admin.modules.other.entity.ShowVideo;
import io.dubai.admin.modules.other.service.ShowVideoService;
import io.dubai.common.utils.PageUtils;
import io.dubai.common.utils.Query;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("showVideoService")
public class ShowVideoServiceImpl extends ServiceImpl<ShowVideoDao, ShowVideo> implements ShowVideoService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ShowVideo> page = new Query<ShowVideo>().getPage(params);
        page.setRecords(baseMapper.queryPage(page,params));
        return new PageUtils(page);
    }

}
