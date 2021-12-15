package io.dubai.admin.modules.goods.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dubai.common.utils.PageUtils;
import io.dubai.common.utils.Query;

import io.dubai.admin.modules.goods.dao.ShopGoodsCategoryLangDao;
import io.dubai.admin.modules.goods.entity.ShopGoodsCategoryLang;
import io.dubai.admin.modules.goods.service.ShopGoodsCategoryLangService;


@Service("shopGoodsCategoryLangService")
public class ShopGoodsCategoryLangServiceImpl extends ServiceImpl<ShopGoodsCategoryLangDao, ShopGoodsCategoryLang> implements ShopGoodsCategoryLangService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ShopGoodsCategoryLang> page = this.page(
                new Query<ShopGoodsCategoryLang>().getPage(params),
                new QueryWrapper<ShopGoodsCategoryLang>()
        );

        return new PageUtils(page);
    }

}
