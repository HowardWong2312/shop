package io.dubai.modules.goods.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dubai.common.utils.PageUtils;
import io.dubai.common.utils.Query;
import io.dubai.modules.goods.dao.GoodsGroupRecordDao;
import io.dubai.modules.goods.entity.GoodsGroupRecord;
import io.dubai.modules.goods.entity.vo.GoodsGroupRecordVo;
import io.dubai.modules.goods.service.GoodsGroupRecordService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service("goodsGroupRecordService")
public class GoodsGroupRecordServiceImpl extends ServiceImpl<GoodsGroupRecordDao, GoodsGroupRecord> implements GoodsGroupRecordService {


    @Override
    public List<GoodsGroupRecordVo> queryListByGoodsId(Long goodsId, Integer limit) {
        List<GoodsGroupRecordVo> temp = baseMapper.queryListByGoodsId(goodsId,limit);
        List<GoodsGroupRecordVo> list = new ArrayList<>();
        for (int i = 0; i < temp.size(); i++) {
            long expiredTime = temp.get(i).getCreateTime().plusHours(temp.get(i).getPeriod()).toInstant(ZoneOffset.of("+2")).toEpochMilli();
            long err = expiredTime-System.currentTimeMillis();
            temp.get(i).setPeriodTime(err);
            if(err > 0){
                list.add(temp.get(i));
            }
        }
        return list;
    }

    @Override
    public GoodsGroupRecordVo queryById(Long id) {
        return baseMapper.queryById(id);
    }

}
