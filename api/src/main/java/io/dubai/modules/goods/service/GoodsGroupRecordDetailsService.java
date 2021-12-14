package io.dubai.modules.goods.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.dubai.modules.goods.entity.GoodsGroupRecordDetails;
import io.dubai.modules.goods.entity.vo.GoodsGroupRecordDetailsVo;

import java.util.List;

/**
 * 参团记录
 *
 * @author Howard
 * @email admin@gmail.com
 * @date 2021-10-07 16:25:36
 */
public interface GoodsGroupRecordDetailsService extends IService<GoodsGroupRecordDetails> {

    List<GoodsGroupRecordDetailsVo> queryListByGoodsGroupRecordId(Long goodsGroupRecordId);

    int queryCashAwardCountByGoodsGroupId(Long goodsGroupId);

    GoodsGroupRecordDetails queryByGoodsOrderCode(String goodsOrderCode);
}

