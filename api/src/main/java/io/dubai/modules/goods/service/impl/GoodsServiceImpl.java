package io.dubai.modules.goods.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dubai.common.utils.PageUtils;
import io.dubai.modules.goods.dao.GoodsDao;
import io.dubai.modules.goods.entity.Goods;
import io.dubai.modules.goods.entity.GoodsGroup;
import io.dubai.modules.goods.entity.vo.GoodsGroupRecordVo;
import io.dubai.modules.goods.entity.vo.GoodsVo;
import io.dubai.modules.goods.query.GoodsQuery;
import io.dubai.modules.goods.service.GoodsGroupService;
import io.dubai.modules.goods.service.GoodsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


@Service("goodsService")
public class GoodsServiceImpl extends ServiceImpl<GoodsDao, Goods> implements GoodsService {

    @Resource
    private GoodsGroupService goodsGroupService;

    @Override
    public List<GoodsVo> queryFavoriteList(GoodsQuery query) {
        return baseMapper.queryFavoriteList(query);
    }

    @Override
    public PageUtils queryPage(GoodsQuery query) {
        IPage<GoodsVo> page = query.getPage();
        page.setRecords(baseMapper.queryPage(page, query));
        page.getRecords().forEach(s->{
            if(s.getIsGroup() == 1){
                GoodsGroup goodsGroup = goodsGroupService.getOne(
                        new QueryWrapper<GoodsGroup>()
                                .eq("goods_id",s.getId())
                                .eq("status",1)
                );
                if(goodsGroup != null){
                    s.setGoodsGroupPrice(goodsGroup.getPrice());
                }
            }
        });
        return new PageUtils(page);
    }

    @Override
    public GoodsVo queryInfoByIdAndLanguageId(Long id,String languageId) {
        GoodsVo goodsVo = baseMapper.queryInfoByIdAndLanguageId(id,languageId);
        return goodsVo;
    }

    @Override
    public List<GoodsVo> queryListByUserIdAndLanguageId(Long userId, String languageId) {
        return baseMapper.queryListByUserIdAndLanguageId(userId,languageId);
    }

    @Override
    public List<GoodsVo> queryListIssueByUserIdAndLanguageId(Long userId, String languageId) {
        return baseMapper.queryListIssueByUserIdAndLanguageId(userId,languageId);
    }

}
