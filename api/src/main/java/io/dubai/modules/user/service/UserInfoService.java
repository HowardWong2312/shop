package io.dubai.modules.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cz.czUser.system.entity.UserInfo;

/**
 * 用户账户
 *
 * @author mother fucker
 * @email admin@gmail.com
 * @date 2021-10-07 16:28:10
 */
public interface UserInfoService extends IService<UserInfo> {

    UserInfo queryByUserId(Long userId);

    UserInfo insert(UserInfo userInfo);

    UserInfo update(UserInfo userInfo);
}

