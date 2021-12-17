package io.dubai.admin.modules.goods.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dubai.admin.modules.goods.dao.ShopGoodsRushDao;
import io.dubai.admin.modules.goods.entity.ShopGoodsRush;
import io.dubai.admin.modules.goods.entity.vo.ShopGoodsRushVo;
import io.dubai.admin.modules.goods.service.ShopGoodsRushService;
import io.dubai.common.utils.PageUtils;
import io.dubai.common.utils.Query;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("shopGoodsRushService")
public class ShopGoodsRushServiceImpl extends ServiceImpl<ShopGoodsRushDao, ShopGoodsRush> implements ShopGoodsRushService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ShopGoodsRushVo> page = new Query<ShopGoodsRushVo>().getPage(params);
        page.setRecords(baseMapper.queryPage(page, params));
        return new PageUtils(page);
    }

    @Override
    public ShopGoodsRushVo queryById(Long id) {
        return baseMapper.queryById(id);
    }

}
