package io.dubai.modules.goods.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.dubai.modules.goods.entity.GoodsGroupRecord;
import io.dubai.modules.goods.entity.vo.GoodsGroupRecordVo;

import java.util.List;

/**
 * 拼团记录
 *
 * @author Howard
 * @email admin@gmail.com
 * @date 2021-10-07 16:25:36
 */
public interface GoodsGroupRecordService extends IService<GoodsGroupRecord> {

    List<GoodsGroupRecordVo> queryListByGoodsId(Long goodsId, Integer limit);

    GoodsGroupRecordVo queryById(Long id);

}

