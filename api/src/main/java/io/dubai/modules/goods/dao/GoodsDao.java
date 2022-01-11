package io.dubai.modules.goods.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.dubai.modules.goods.entity.Goods;
import io.dubai.modules.goods.entity.vo.GoodsVo;
import io.dubai.modules.goods.query.GoodsQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 算力产品
 *
 * @author mother fucker
 * @email admin@gmail.com
 * @date 2021-10-07 16:25:36
 */
@Mapper
public interface GoodsDao extends BaseMapper<Goods> {

    List<GoodsVo> queryFavoriteList(@Param("query") GoodsQuery query);

    List<GoodsVo> queryPage(IPage page, @Param("query") GoodsQuery query);

    GoodsVo queryInfoByIdAndLanguageId(@Param("id") Long id,@Param("languageId") String languageId);

    List<GoodsVo> queryListByUserIdAndLanguageId(@Param("userId") Long userId,@Param("languageId") String languageId);

    List<GoodsVo> queryListIssueByUserIdAndLanguageId(@Param("userId") Long userId,@Param("languageId") String languageId);

}
