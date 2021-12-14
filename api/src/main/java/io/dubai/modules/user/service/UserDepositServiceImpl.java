package io.dubai.modules.user.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dubai.modules.user.dao.UserDepositDao;
import io.dubai.modules.user.dao.UserWithdrawDao;
import io.dubai.modules.user.entity.UserDeposit;
import io.dubai.modules.user.entity.UserWithdraw;
import org.springframework.stereotype.Service;


@Service("userDepositService")
public class UserDepositServiceImpl extends ServiceImpl<UserDepositDao, UserDeposit> implements UserDepositService {


}
