package io.dubai.modules.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dubai.modules.user.dao.UserWithdrawDao;
import io.dubai.modules.user.entity.UserWithdraw;
import io.dubai.modules.user.service.UserWithdrawService;
import org.springframework.stereotype.Service;


@Service("userWithdrawService")
public class UserWithdrawServiceImpl extends ServiceImpl<UserWithdrawDao, UserWithdraw> implements UserWithdrawService {


}
