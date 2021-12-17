package io.dubai.admin.modules.goods.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dubai.common.utils.PageUtils;
import io.dubai.common.utils.Query;

import io.dubai.admin.modules.goods.dao.ShopGoodsImgDao;
import io.dubai.admin.modules.goods.entity.ShopGoodsImg;
import io.dubai.admin.modules.goods.service.ShopGoodsImgService;


@Service("shopGoodsImgService")
public class ShopGoodsImgServiceImpl extends ServiceImpl<ShopGoodsImgDao, ShopGoodsImg> implements ShopGoodsImgService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ShopGoodsImg> page = this.page(
                new Query<ShopGoodsImg>().getPage(params),
                new QueryWrapper<ShopGoodsImg>()
        );

        return new PageUtils(page);
    }

}
