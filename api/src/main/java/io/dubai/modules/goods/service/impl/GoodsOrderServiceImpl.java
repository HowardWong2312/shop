package io.dubai.modules.goods.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dubai.common.utils.PageUtils;
import io.dubai.common.utils.Query;
import io.dubai.modules.goods.dao.GoodsOrderDao;
import io.dubai.modules.goods.entity.GoodsOrder;
import io.dubai.modules.goods.entity.vo.GoodsOrderVo;
import io.dubai.modules.goods.query.GoodsOrderQuery;
import io.dubai.modules.goods.service.GoodsOrderService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


@Service("goodsOrderService")
public class GoodsOrderServiceImpl extends ServiceImpl<GoodsOrderDao, GoodsOrder> implements GoodsOrderService {

    @Override
    public PageUtils queryPage(GoodsOrderQuery query) {
        IPage<GoodsOrderVo> page = query.getPage();
        page.setRecords(baseMapper.queryPage(page,query));
        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPageForMerchant(GoodsOrderQuery query) {
        IPage<GoodsOrderVo> page = query.getPage();
        page.setRecords(baseMapper.queryPageForMerchant(page,query));
        return new PageUtils(page);
    }

    @Override
    public BigDecimal querySumPendingAmountByMerchantId(Integer merchantId) {
        return baseMapper.querySumPendingAmountByMerchantId(merchantId);
    }

    @Override
    public List<GoodsOrder> queryPendingOrderListByMerchantId(Integer merchantId) {
        return baseMapper.queryPendingOrderListByMerchantId(merchantId);
    }

    @Override
    public GoodsOrderVo queryByOrderCodeAndLanguageId(String orderCode, String languageId) {
        return baseMapper.queryByOrderCodeAndLanguageId(orderCode,languageId);
    }

    @Override
    public GoodsOrderVo queryByOrderCodeAndLanguageIdForMerchant(String orderCode, String languageId) {
        return baseMapper.queryByOrderCodeAndLanguageIdForMerchant(orderCode, languageId);
    }


}
