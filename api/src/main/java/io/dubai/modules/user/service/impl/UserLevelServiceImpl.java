package io.dubai.modules.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dubai.modules.user.dao.UserLevelDao;
import io.dubai.modules.user.entity.UserLevel;
import io.dubai.modules.user.service.UserLevelService;
import org.springframework.stereotype.Service;


@Service("userLevelService")
public class UserLevelServiceImpl extends ServiceImpl<UserLevelDao, UserLevel> implements UserLevelService {


}
