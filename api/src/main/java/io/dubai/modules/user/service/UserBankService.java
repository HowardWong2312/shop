package io.dubai.modules.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.dubai.modules.user.entity.UserBank;

import java.util.List;

/**
 * 用户提现
 *
 * @author mother fucker
 * @email admin@gmail.com
 * @date 2021-10-07 16:28:10
 */
public interface UserBankService extends IService<UserBank> {

    List<UserBank> queryByUserIdAndLanguageId(Long userId,String languageId);

}

