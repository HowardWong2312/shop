package io.dubai.admin.modules.goods.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dubai.common.utils.PageUtils;
import io.dubai.common.utils.Query;

import io.dubai.admin.modules.goods.dao.TProvinceDao;
import io.dubai.admin.modules.goods.entity.TProvince;
import io.dubai.admin.modules.goods.service.TProvinceService;


@Service("tProvinceService")
public class TProvinceServiceImpl extends ServiceImpl<TProvinceDao, TProvince> implements TProvinceService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<TProvince> page = this.page(
                new Query<TProvince>().getPage(params),
                new QueryWrapper<TProvince>()
        );

        return new PageUtils(page);
    }

}
