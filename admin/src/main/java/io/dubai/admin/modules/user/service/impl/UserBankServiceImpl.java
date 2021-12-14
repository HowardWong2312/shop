package io.dubai.admin.modules.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dubai.admin.modules.user.dao.UserBankDao;
import io.dubai.admin.modules.user.entity.UserBank;
import io.dubai.admin.modules.user.service.UserBankService;
import io.dubai.common.utils.PageUtils;
import io.dubai.common.utils.Query;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("userBankService")
public class UserBankServiceImpl extends ServiceImpl<UserBankDao, UserBank> implements UserBankService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<UserBank> page = this.page(
                new Query<UserBank>().getPage(params),
                new QueryWrapper<UserBank>()
        );

        return new PageUtils(page);
    }

}
