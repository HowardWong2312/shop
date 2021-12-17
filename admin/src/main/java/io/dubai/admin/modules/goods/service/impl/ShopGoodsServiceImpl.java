package io.dubai.admin.modules.goods.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dubai.common.utils.PageUtils;
import io.dubai.common.utils.Query;

import io.dubai.admin.modules.goods.dao.ShopGoodsDao;
import io.dubai.admin.modules.goods.entity.ShopGoods;
import io.dubai.admin.modules.goods.service.ShopGoodsService;


@Service("shopGoodsService")
public class ShopGoodsServiceImpl extends ServiceImpl<ShopGoodsDao, ShopGoods> implements ShopGoodsService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ShopGoods> page = this.page(
                new Query<ShopGoods>().getPage(params),
                new QueryWrapper<ShopGoods>()
        );

        return new PageUtils(page);
    }

}
