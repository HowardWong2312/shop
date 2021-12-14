package io.dubai.modules.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dubai.modules.user.dao.UserBankDao;
import io.dubai.modules.user.dao.UserWithdrawDao;
import io.dubai.modules.user.entity.UserBank;
import io.dubai.modules.user.entity.UserWithdraw;
import io.dubai.modules.user.service.UserBankService;
import io.dubai.modules.user.service.UserWithdrawService;
import org.springframework.stereotype.Service;


@Service("userBankService")
public class UserBankServiceImpl extends ServiceImpl<UserBankDao, UserBank> implements UserBankService {


}
