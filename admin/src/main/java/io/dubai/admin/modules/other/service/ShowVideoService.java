package io.dubai.admin.modules.other.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.dubai.admin.modules.other.entity.ShowVideo;
import io.dubai.common.utils.PageUtils;

import java.util.Map;

/**
 * 我的视频
 *
 * @author mother fucker
 * @email admin@gmail.com
 * @date 2021-12-25 19:13:36
 */
public interface ShowVideoService extends IService<ShowVideo> {

    PageUtils queryPage(Map<String, Object> params);
}

