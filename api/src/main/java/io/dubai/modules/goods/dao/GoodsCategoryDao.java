package io.dubai.modules.goods.dao;

import io.dubai.modules.goods.entity.GoodsCategory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.dubai.modules.goods.entity.vo.GoodsCategoryVo;
import io.dubai.modules.goods.entity.vo.GoodsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品分类
 * 
 * @author mother fucker
 * @email admin@gmail.com
 * @date 2021-10-15 18:18:50
 */
@Mapper
public interface GoodsCategoryDao extends BaseMapper<GoodsCategory> {

    List<GoodsCategoryVo> queryListByParentIdAndLanguageId(@Param("parentId") Long parentId, @Param("languageId") Long languageId);

    GoodsCategoryVo queryByIdAndLanguageId(@Param("id") Long id, @Param("languageId") Long languageId);

    List<Long> queryIdsByParentId(@Param("parentId") Long parentId);

}
