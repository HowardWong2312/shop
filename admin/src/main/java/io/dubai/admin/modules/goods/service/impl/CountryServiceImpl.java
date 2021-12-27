package io.dubai.admin.modules.goods.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dubai.admin.modules.goods.dao.CountryDao;
import io.dubai.admin.modules.goods.entity.Country;
import io.dubai.admin.modules.goods.service.CountryService;
import io.dubai.common.utils.PageUtils;
import io.dubai.common.utils.Query;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("CountryService")
public class CountryServiceImpl extends ServiceImpl<CountryDao, Country> implements CountryService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<Country> page = this.page(
                new Query<Country>().getPage(params),
                new QueryWrapper<Country>()
        );

        return new PageUtils(page);
    }

}
