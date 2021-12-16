package io.dubai.admin.modules.goods.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dubai.admin.modules.goods.dao.ShopGoodsCategoryDao;
import io.dubai.admin.modules.goods.entity.ShopGoodsCategory;
import io.dubai.admin.modules.goods.entity.ShopGoodsCategoryFrom;
import io.dubai.admin.modules.goods.entity.ShopGoodsCategoryLang;
import io.dubai.admin.modules.goods.service.ShopGoodsCategoryLangService;
import io.dubai.admin.modules.goods.service.ShopGoodsCategoryService;
import io.dubai.common.exception.RRException;
import io.dubai.common.utils.PageUtils;
import io.dubai.common.utils.Query;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;


@Service("shopGoodsCategoryService")
public class ShopGoodsCategoryServiceImpl extends ServiceImpl<ShopGoodsCategoryDao, ShopGoodsCategory> implements ShopGoodsCategoryService {

    @Resource
    private ShopGoodsCategoryLangService shopGoodsCategoryLangService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage page = new Query().getPage(params);
        page.setRecords(baseMapper.queryList(page, params));
        return new PageUtils(page);
    }

    @Override
    public Integer save(ShopGoodsCategoryFrom shopGoodsCategoryFrom) {

        ShopGoodsCategory shopGoodsCategory = new ShopGoodsCategory();
        ShopGoodsCategoryLang shopGoodsCategoryLang = new ShopGoodsCategoryLang();


        //一级分类必须带有的参数 默认标题 语言拓展标题 语言id
        shopGoodsCategory.setTitle(shopGoodsCategoryFrom.getDefaultTitle());
        shopGoodsCategory.setOrderNum(shopGoodsCategoryFrom.getOrderNum());
        shopGoodsCategory.setParentId(shopGoodsCategoryFrom.getParentId());
        shopGoodsCategory.setIconUrl(shopGoodsCategoryFrom.getDefaultIconUrl());
        baseMapper.insert(shopGoodsCategory);

        //增加语言包
        shopGoodsCategoryLang.setGoodsCategoryId(shopGoodsCategory.getId());
        shopGoodsCategoryLang.setLanguageId(shopGoodsCategoryFrom.getLanguageId());
        shopGoodsCategoryLang.setTitle(shopGoodsCategoryFrom.getLanguageTitle());
        shopGoodsCategoryLang.setIconUrl(shopGoodsCategoryFrom.getLanguageIconUrl());


        return shopGoodsCategoryLangService.getBaseMapper().insert(shopGoodsCategoryLang);
    }

    @Override
    public Integer addShopCategoryLang(ShopGoodsCategoryFrom shopGoodsCategoryFrom) {
        if (shopGoodsCategoryFrom.getGoodsCategoryId() == null) {
            throw new RRException("id不存在");
        }

        if (shopGoodsCategoryLangService.getBaseMapper().selectOne(new QueryWrapper<ShopGoodsCategoryLang>().eq("goods_category_id", shopGoodsCategoryFrom.getGoodsCategoryId()).eq("language_id", shopGoodsCategoryFrom.getLanguageId())) != null) {
            throw new RRException("语言包已存在");
        }

        ShopGoodsCategoryLang shopGoodsCategoryLang = new ShopGoodsCategoryLang();
        shopGoodsCategoryLang.setLanguageId(shopGoodsCategoryFrom.getLanguageId());
        shopGoodsCategoryLang.setTitle(shopGoodsCategoryFrom.getLanguageTitle());
        shopGoodsCategoryLang.setIconUrl(shopGoodsCategoryFrom.getLanguageIconUrl());
        shopGoodsCategoryLang.setGoodsCategoryId(shopGoodsCategoryFrom.getGoodsCategoryId());
        return shopGoodsCategoryLangService.getBaseMapper().insert(shopGoodsCategoryLang);
    }
}
