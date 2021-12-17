package io.dubai.admin.modules.goods.service.impl;

import io.dubai.admin.modules.goods.entity.vo.ShopGoodsRushVo;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dubai.common.utils.PageUtils;
import io.dubai.common.utils.Query;

import io.dubai.admin.modules.goods.dao.ShopGoodsRushDao;
import io.dubai.admin.modules.goods.entity.ShopGoodsRush;
import io.dubai.admin.modules.goods.service.ShopGoodsRushService;


@Service("shopGoodsRushService")
public class ShopGoodsRushServiceImpl extends ServiceImpl<ShopGoodsRushDao, ShopGoodsRush> implements ShopGoodsRushService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ShopGoodsRushVo> page = new Query<ShopGoodsRushVo>().getPage(params);
        page.setRecords(baseMapper.queryPage(page,params));
        return new PageUtils(page);
    }

    @Override
    public ShopGoodsRushVo queryById(Long id) {
        return baseMapper.queryById(id);
    }

}
