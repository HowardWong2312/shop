package io.dubai.admin.modules.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.dubai.admin.modules.user.entity.UserLevel;
import io.dubai.common.utils.PageUtils;

import java.util.Map;

/**
 * 用户等级
 *
 * @author mother fucker
 * @email admin@gmail.com
 * @date 2021-12-14 18:27:22
 */
public interface UserLevelService extends IService<UserLevel> {

    PageUtils queryPage(Map<String, Object> params);
}

