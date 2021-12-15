package io.dubai.admin.modules.goods.service.impl;

import org.springframework.stereotype.Service;

import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dubai.common.utils.PageUtils;
import io.dubai.common.utils.Query;

import io.dubai.admin.modules.goods.dao.ShopGoodsCategoryDao;
import io.dubai.admin.modules.goods.entity.ShopGoodsCategory;
import io.dubai.admin.modules.goods.service.ShopGoodsCategoryService;


@Service("shopGoodsCategoryService")
public class ShopGoodsCategoryServiceImpl extends ServiceImpl<ShopGoodsCategoryDao, ShopGoodsCategory> implements ShopGoodsCategoryService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage page = new Query().getPage(params);
        page.setRecords(baseMapper.queryList(page, params));
        return new PageUtils(page);
    }

}
