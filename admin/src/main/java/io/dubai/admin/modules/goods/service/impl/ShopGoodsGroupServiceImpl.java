package io.dubai.admin.modules.goods.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dubai.common.utils.PageUtils;
import io.dubai.common.utils.Query;

import io.dubai.admin.modules.goods.dao.ShopGoodsGroupDao;
import io.dubai.admin.modules.goods.entity.ShopGoodsGroup;
import io.dubai.admin.modules.goods.service.ShopGoodsGroupService;


@Service("shopGoodsGroupService")
public class ShopGoodsGroupServiceImpl extends ServiceImpl<ShopGoodsGroupDao, ShopGoodsGroup> implements ShopGoodsGroupService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ShopGoodsGroup> page = this.page(
                new Query<ShopGoodsGroup>().getPage(params),
                new QueryWrapper<ShopGoodsGroup>()
        );

        return new PageUtils(page);
    }

}
