package io.dubai.modules.goods.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dubai.modules.goods.dao.GoodsGroupRecordDetailsDao;
import io.dubai.modules.goods.entity.GoodsGroupRecordDetails;
import io.dubai.modules.goods.entity.vo.GoodsGroupRecordDetailsVo;
import io.dubai.modules.goods.service.GoodsGroupRecordDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;


@Service("goodsGroupRecordDetailsService")
public class GoodsGroupRecordDetailsServiceImpl extends ServiceImpl<GoodsGroupRecordDetailsDao, GoodsGroupRecordDetails> implements GoodsGroupRecordDetailsService {

    @Override
    public List<GoodsGroupRecordDetailsVo> queryListByGoodsGroupRecordId(Long goodsGroupRecordId) {
        return baseMapper.queryListByGoodsGroupRecordId(goodsGroupRecordId);
    }

    @Override
    public int queryCashAwardCountByGoodsGroupId(Long goodsGroupId) {
        return baseMapper.queryCashAwardCountByGoodsGroupId(goodsGroupId);
    }

    @Override
    public GoodsGroupRecordDetails queryByGoodsOrderCode(String goodsOrderCode) {
        return baseMapper.queryByGoodsOrderCode(goodsOrderCode);
    }
}
