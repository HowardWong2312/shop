package io.dubai.admin.modules.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dubai.admin.modules.user.dao.UserCreditsLogDao;
import io.dubai.admin.modules.user.entity.UserCreditsLog;
import io.dubai.admin.modules.user.service.UserCreditsLogService;
import io.dubai.common.utils.PageUtils;
import io.dubai.common.utils.Query;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("userCreditsLogService")
public class UserCreditsLogServiceImpl extends ServiceImpl<UserCreditsLogDao, UserCreditsLog> implements UserCreditsLogService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<UserCreditsLog> page = this.page(
                new Query<UserCreditsLog>().getPage(params),
                new QueryWrapper<UserCreditsLog>()
        );

        return new PageUtils(page);
    }

}