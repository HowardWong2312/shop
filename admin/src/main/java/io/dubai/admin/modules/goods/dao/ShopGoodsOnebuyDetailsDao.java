package io.dubai.admin.modules.goods.dao;

import io.dubai.admin.modules.goods.entity.ShopGoodsOnebuyDetails;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.dubai.admin.modules.goods.entity.vo.ShopGoodsOnebuyDetailsVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 一元购记录
 * 
 * @author mother fucker
 * @email admin@gmail.com
 * @date 2022-01-03 17:49:28
 */
@Mapper
public interface ShopGoodsOnebuyDetailsDao extends BaseMapper<ShopGoodsOnebuyDetails> {

    List<ShopGoodsOnebuyDetailsVo> queryList(Long goodsOnebuyId);
}
