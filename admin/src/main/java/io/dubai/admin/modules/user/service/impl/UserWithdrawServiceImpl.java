package io.dubai.admin.modules.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dubai.admin.modules.user.dao.UserWithdrawDao;
import io.dubai.admin.modules.user.entity.UserWithdraw;
import io.dubai.admin.modules.user.service.UserWithdrawService;
import io.dubai.common.utils.PageUtils;
import io.dubai.common.utils.Query;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("userWithdrawService")
public class UserWithdrawServiceImpl extends ServiceImpl<UserWithdrawDao, UserWithdraw> implements UserWithdrawService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<UserWithdraw> page = this.page(
                new Query<UserWithdraw>().getPage(params),
                new QueryWrapper<UserWithdraw>()
        );

        return new PageUtils(page);
    }

}
