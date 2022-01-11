package io.dubai.admin.modules.goods.service.impl;

import io.dubai.admin.modules.goods.entity.vo.ShopGoodsOnebuyDetailsVo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dubai.common.utils.PageUtils;
import io.dubai.common.utils.Query;

import io.dubai.admin.modules.goods.dao.ShopGoodsOnebuyDetailsDao;
import io.dubai.admin.modules.goods.entity.ShopGoodsOnebuyDetails;
import io.dubai.admin.modules.goods.service.ShopGoodsOnebuyDetailsService;


@Service("shopGoodsOnebuyDetailsService")
public class ShopGoodsOnebuyDetailsServiceImpl extends ServiceImpl<ShopGoodsOnebuyDetailsDao, ShopGoodsOnebuyDetails> implements ShopGoodsOnebuyDetailsService {

    @Override
    public List<ShopGoodsOnebuyDetailsVo> queryList(Long goodsOnebuyId) {
        return baseMapper.queryList(goodsOnebuyId);
    }

}
