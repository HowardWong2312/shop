package io.dubai.admin.modules.goods.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dubai.common.utils.PageUtils;
import io.dubai.common.utils.Query;

import io.dubai.admin.modules.goods.dao.TCityDao;
import io.dubai.admin.modules.goods.entity.TCity;
import io.dubai.admin.modules.goods.service.TCityService;


@Service("tCityService")
public class TCityServiceImpl extends ServiceImpl<TCityDao, TCity> implements TCityService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<TCity> page = this.page(
                new Query<TCity>().getPage(params),
                new QueryWrapper<TCity>()
        );

        return new PageUtils(page);
    }

}
