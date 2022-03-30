package io.dubai.admin.modules.goods.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dubai.admin.modules.goods.dao.ShopGoodsDao;
import io.dubai.admin.modules.goods.entity.ShopGoods;
import io.dubai.admin.modules.goods.entity.ShopGoodsCategory;
import io.dubai.admin.modules.goods.service.ShopGoodsCategoryService;
import io.dubai.admin.modules.goods.service.ShopGoodsService;
import io.dubai.common.utils.PageUtils;
import io.dubai.common.utils.Query;
import io.dubai.common.utils.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;


@Service("shopGoodsService")
public class ShopGoodsServiceImpl extends ServiceImpl<ShopGoodsDao, ShopGoods> implements ShopGoodsService {

    @Resource
    ShopGoodsCategoryService shopGoodsCategoryService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
//        if (!StringUtils.isEmpty(params.get("categoryId")) && !params.get("categoryId").equals("-1")) {
//            //根据一级分类
//            List<Object> categoryIds = shopGoodsCategoryService.getBaseMapper().selectObjs(new QueryWrapper<ShopGoodsCategory>().select("id").eq("parent_id", params.get("categoryId")));
//            params.put("ids", categoryIds);
//        }
        if (params.get("key") != null && StringUtils.isNumeric(params.get("key").toString())) {
            Integer userId = Integer.valueOf(params.get("key").toString());
            params.remove("key");
            params.put("userId", userId);
        }
        IPage page = new Query().getPage(params);
        page.setRecords(baseMapper.shopGoodsList(page, params));

        return new PageUtils(page);
    }

}
