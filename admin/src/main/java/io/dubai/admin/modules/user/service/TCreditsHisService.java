package io.dubai.admin.modules.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.dubai.admin.modules.user.entity.TCreditsHis;
import io.dubai.common.utils.PageUtils;

import java.util.Map;

/**
 * 积分明细
 *
 * @author mother fucker
 * @email admin@gmail.com
 * @date 2021-12-25 14:12:53
 */
public interface TCreditsHisService extends IService<TCreditsHis> {

    PageUtils queryPage(Map<String, Object> params);
}

