package io.dubai.modules.goods.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.dubai.modules.goods.entity.GoodsOrder;
import io.dubai.modules.goods.entity.UserFavoriteGoods;
import io.dubai.modules.goods.entity.vo.GoodsOrderVo;
import io.dubai.modules.goods.query.GoodsOrderQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品订单
 *
 * @author Howard
 * @email admin@gmail.com
 * @date 2021-10-07 16:25:36
 */
@Mapper
public interface UserFavoriteGoodsDao extends BaseMapper<UserFavoriteGoods> {


}
