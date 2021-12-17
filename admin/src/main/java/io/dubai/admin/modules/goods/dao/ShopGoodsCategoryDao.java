package io.dubai.admin.modules.goods.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.dubai.admin.modules.goods.entity.ShopGoodsCategory;
import io.dubai.admin.modules.goods.entity.vo.ShopGoodsCategoryVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 商品分类
 *
 * @author mother fucker
 * @email admin@gmail.com
 * @date 2021-12-15 15:03:45
 */
@Mapper
public interface ShopGoodsCategoryDao extends BaseMapper<ShopGoodsCategory> {

    List<ShopGoodsCategoryVo> queryList(IPage page, @Param("params") Map<String, Object> params);

}
