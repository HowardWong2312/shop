package io.dubai.modules.goods.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.dubai.modules.goods.entity.GoodsOrder;
import io.dubai.modules.goods.entity.vo.GoodsOrderVo;
import io.dubai.modules.goods.query.GoodsOrderQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品订单
 *
 * @author mother fucker
 * @email admin@gmail.com
 * @date 2021-10-07 16:25:36
 */
@Mapper
public interface GoodsOrderDao extends BaseMapper<GoodsOrder> {

    List<GoodsOrderVo> queryPage(IPage page, @Param("query") GoodsOrderQuery query);

    GoodsOrderVo queryByOrderCodeAndLanguageId(@Param("orderCode") String orderCode, @Param("languageId") Long languageId);

    GoodsOrderVo queryByOrderCodeAndLanguageIdForMerchant(@Param("orderCode") String orderCode, @Param("languageId") Long languageId);

    List<GoodsOrderVo> queryPageForMerchant(IPage page, @Param("query") GoodsOrderQuery query);


}
