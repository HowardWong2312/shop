package io.dubai.admin.modules.goods.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.dubai.admin.modules.goods.entity.ShopGoodsRush;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.dubai.admin.modules.goods.entity.vo.ShopGoodsOnebuyVo;
import io.dubai.admin.modules.goods.entity.vo.ShopGoodsRushVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 免费抢活动
 * 
 * @author mother fucker
 * @email admin@gmail.com
 * @date 2021-12-16 18:16:04
 */
@Mapper
public interface ShopGoodsRushDao extends BaseMapper<ShopGoodsRush> {

    List<ShopGoodsRushVo> queryPage(IPage page, Map<String, Object> params);

    ShopGoodsRushVo queryById(Long id);
	
}
