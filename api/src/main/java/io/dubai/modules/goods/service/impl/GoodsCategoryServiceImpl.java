package io.dubai.modules.goods.service.impl;

import io.dubai.modules.goods.entity.vo.GoodsCategoryVo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dubai.common.utils.PageUtils;
import io.dubai.common.utils.Query;

import io.dubai.modules.goods.dao.GoodsCategoryDao;
import io.dubai.modules.goods.entity.GoodsCategory;
import io.dubai.modules.goods.service.GoodsCategoryService;


@Service("goodsCategoryService")
public class GoodsCategoryServiceImpl extends ServiceImpl<GoodsCategoryDao, GoodsCategory> implements GoodsCategoryService {

    @Override
    public List<GoodsCategoryVo> queryListByParentIdAndLanguageId(Long parentId, Long languageId) {
        return baseMapper.queryListByParentIdAndLanguageId(parentId,languageId);
    }

    @Override
    public GoodsCategoryVo queryByIdAndLanguageId(Long id, Long languageId) {
        return baseMapper.queryByIdAndLanguageId(id,languageId);
    }

    @Override
    public List<Long> queryIterativeIdsByParentId(Long parentId) {
        List<Long> list = baseMapper.queryIdsByParentId(parentId);
        for (int i = 0; i < list.size(); i++) {
            List<Long> temp = queryIterativeIdsByParentId(list.get(i));
            list.addAll(temp);
        }
        return list;
    }
}
