package io.dubai.admin.modules.goods.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.dubai.admin.modules.goods.entity.ShopGoodsOnebuy;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.dubai.admin.modules.goods.entity.vo.ShopGoodsOnebuyVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 一元购活动
 * 
 * @author mother fucker
 * @email admin@gmail.com
 * @date 2021-12-16 18:16:04
 */
@Mapper
public interface ShopGoodsOnebuyDao extends BaseMapper<ShopGoodsOnebuy> {

    List<ShopGoodsOnebuyVo> queryPage(IPage page, Map<String, Object> params);

    ShopGoodsOnebuyVo queryById(Long id);

}
