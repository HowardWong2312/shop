package io.dubai.admin.modules.other.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dubai.common.utils.PageUtils;
import io.dubai.common.utils.Query;

import io.dubai.admin.modules.other.dao.ShopBannerDao;
import io.dubai.admin.modules.other.entity.ShopBanner;
import io.dubai.admin.modules.other.service.ShopBannerService;


@Service("shopBannerService")
public class ShopBannerServiceImpl extends ServiceImpl<ShopBannerDao, ShopBanner> implements ShopBannerService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ShopBanner> page = this.page(
                new Query<ShopBanner>().getPage(params),
                new QueryWrapper<ShopBanner>()
        );

        return new PageUtils(page);
    }

}
