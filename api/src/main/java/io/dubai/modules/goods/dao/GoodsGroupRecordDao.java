package io.dubai.modules.goods.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.dubai.modules.goods.entity.GoodsGroupRecord;
import io.dubai.modules.goods.entity.vo.GoodsGroupRecordVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 拼团记录
 *
 * @author mother fucker
 * @email admin@gmail.com
 * @date 2021-10-07 16:25:36
 */
@Mapper
public interface GoodsGroupRecordDao extends BaseMapper<GoodsGroupRecord> {

    List<GoodsGroupRecordVo> queryListByGoodsId(@Param("goodsId") Long goodsId, @Param("limit") Integer limit);

    GoodsGroupRecordVo queryById(@Param("id") Long id);

}
