package io.dubai.admin.modules.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.dubai.admin.modules.user.entity.UserBank;
import io.dubai.common.utils.PageUtils;

import java.util.Map;

/**
 * 提现申请表
 *
 * @author mother fucker
 * @email admin@gmail.com
 * @date 2021-12-14 18:27:22
 */
public interface UserBankService extends IService<UserBank> {

    PageUtils queryPage(Map<String, Object> params);
}

