package io.dubai.admin.modules.goods.service.impl;

import io.dubai.admin.modules.goods.entity.ShopGoodsCategory;
import io.dubai.admin.modules.goods.service.ShopGoodsCategoryService;
import io.dubai.common.utils.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dubai.common.utils.PageUtils;
import io.dubai.common.utils.Query;

import io.dubai.admin.modules.goods.dao.ShopGoodsDao;
import io.dubai.admin.modules.goods.entity.ShopGoods;
import io.dubai.admin.modules.goods.service.ShopGoodsService;

import javax.annotation.Resource;


@Service("shopGoodsService")
public class ShopGoodsServiceImpl extends ServiceImpl<ShopGoodsDao, ShopGoods> implements ShopGoodsService {

    @Resource
    ShopGoodsCategoryService shopGoodsCategoryService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        if (!StringUtils.isEmpty(params.get("categoryId"))) {
            //根据一级分类
            List<Object> categoryIds = shopGoodsCategoryService.getBaseMapper().selectObjs(new QueryWrapper<ShopGoodsCategory>().select("id").eq("parent_id", params.get("categoryId")));
            params.put("ids", categoryIds);
        }
        IPage page = new Query().getPage(params);
        page.setRecords(baseMapper.shopGoodsList(page, params));

        return new PageUtils(page);
    }

}
