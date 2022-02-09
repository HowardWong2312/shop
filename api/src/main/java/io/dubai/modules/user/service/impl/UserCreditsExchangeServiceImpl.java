package io.dubai.modules.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dubai.common.utils.PageUtils;
import io.dubai.common.utils.Query;
import io.dubai.modules.user.dao.UserCreditsExchangeDao;
import io.dubai.modules.user.entity.UserCreditsExchange;
import io.dubai.modules.user.entity.vo.UserCreditsExchangeVo;
import io.dubai.modules.user.service.UserCreditsExchangeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service("userCreditsExchangeService")
public class UserCreditsExchangeServiceImpl extends ServiceImpl<UserCreditsExchangeDao, UserCreditsExchange> implements UserCreditsExchangeService {

}
