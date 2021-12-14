package io.dubai.admin.modules.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dubai.admin.modules.user.dao.UserLevelDao;
import io.dubai.admin.modules.user.entity.UserLevel;
import io.dubai.admin.modules.user.service.UserLevelService;
import io.dubai.common.utils.PageUtils;
import io.dubai.common.utils.Query;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("userLevelService")
public class UserLevelServiceImpl extends ServiceImpl<UserLevelDao, UserLevel> implements UserLevelService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<UserLevel> page = this.page(
                new Query<UserLevel>().getPage(params),
                new QueryWrapper<UserLevel>()
        );

        return new PageUtils(page);
    }

}
