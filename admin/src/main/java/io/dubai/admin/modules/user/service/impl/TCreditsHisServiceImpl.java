package io.dubai.admin.modules.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dubai.admin.modules.user.dao.TCreditsHisDao;
import io.dubai.admin.modules.user.entity.TCreditsHis;
import io.dubai.admin.modules.user.service.TCreditsHisService;
import io.dubai.common.utils.PageUtils;
import io.dubai.common.utils.Query;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("tCreditsHisService")
public class TCreditsHisServiceImpl extends ServiceImpl<TCreditsHisDao, TCreditsHis> implements TCreditsHisService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<TCreditsHis> page = this.page(
                new Query<TCreditsHis>().getPage(params),
                new QueryWrapper<TCreditsHis>()
        );

        return new PageUtils(page);
    }

}
