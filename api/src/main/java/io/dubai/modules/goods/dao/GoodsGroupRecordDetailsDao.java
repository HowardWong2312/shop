package io.dubai.modules.goods.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.dubai.modules.goods.entity.GoodsGroupRecordDetails;
import io.dubai.modules.goods.entity.vo.GoodsGroupRecordDetailsVo;
import io.dubai.modules.goods.entity.vo.GoodsGroupRecordVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 参团记录
 *
 * @author mother fucker
 * @email admin@gmail.com
 * @date 2021-10-07 16:25:36
 */
@Mapper
public interface GoodsGroupRecordDetailsDao extends BaseMapper<GoodsGroupRecordDetails> {

    List<GoodsGroupRecordDetailsVo> queryListByGoodsGroupRecordId(@Param("goodsGroupRecordId") Long goodsGroupRecordId);

    int queryCashAwardCountByGoodsGroupId(@Param("goodsGroupId") Long goodsGroupId);

    GoodsGroupRecordDetails queryByGoodsOrderCode(@Param("goodsOrderCode") String goodsOrderCode);

}
