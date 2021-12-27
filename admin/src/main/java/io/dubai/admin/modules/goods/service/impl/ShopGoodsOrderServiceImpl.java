package io.dubai.admin.modules.goods.service.impl;

import io.dubai.admin.modules.goods.entity.vo.ShopGoodsOrderVo;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dubai.common.utils.PageUtils;
import io.dubai.common.utils.Query;

import io.dubai.admin.modules.goods.dao.ShopGoodsOrderDao;
import io.dubai.admin.modules.goods.entity.ShopGoodsOrder;
import io.dubai.admin.modules.goods.service.ShopGoodsOrderService;


@Service("shopGoodsOrderService")
public class ShopGoodsOrderServiceImpl extends ServiceImpl<ShopGoodsOrderDao, ShopGoodsOrder> implements ShopGoodsOrderService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ShopGoodsOrderVo> page = new Query<ShopGoodsOrderVo>().getPage(params);
        page.setRecords(baseMapper.queryPage(page,params));
        return new PageUtils(page);
    }

    @Override
    public ShopGoodsOrderVo queryById(String id) {
        return baseMapper.queryById(id);
    }

}
