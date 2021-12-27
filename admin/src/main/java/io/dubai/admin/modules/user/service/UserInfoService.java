package io.dubai.admin.modules.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cz.czUser.system.entity.UserInfo;
import io.dubai.common.utils.PageUtils;
import io.dubai.common.utils.R;

import java.util.Map;

/**
 * 用户信息表
 *
 * @author mother fucker
 * @email admin@gmail.com
 * @date 2021-12-14 18:27:22
 */
public interface UserInfoService extends IService<UserInfo> {

    PageUtils queryPage(Map<String, Object> params);

    UserInfo insert(UserInfo userInfo);

    UserInfo update(UserInfo userInfo);

    R getTodayFundsAndUserData();
}

