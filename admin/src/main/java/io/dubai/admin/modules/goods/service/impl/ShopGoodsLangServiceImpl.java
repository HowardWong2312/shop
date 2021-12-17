package io.dubai.admin.modules.goods.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dubai.admin.modules.goods.dao.ShopGoodsLangDao;
import io.dubai.admin.modules.goods.entity.ShopGoodsLang;
import io.dubai.admin.modules.goods.service.ShopGoodsLangService;
import io.dubai.common.utils.PageUtils;
import io.dubai.common.utils.Query;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("shopGoodsLangService")
public class ShopGoodsLangServiceImpl extends ServiceImpl<ShopGoodsLangDao, ShopGoodsLang> implements ShopGoodsLangService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ShopGoodsLang> page = this.page(
                new Query<ShopGoodsLang>().getPage(params),
                new QueryWrapper<ShopGoodsLang>()
        );

        return new PageUtils(page);
    }

}
