package io.dubai.modules.user.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dubai.common.utils.PageUtils;
import io.dubai.modules.user.dao.UserBalanceLogDao;
import io.dubai.modules.user.entity.UserBalanceLog;
import io.dubai.modules.user.query.LogQuery;
import io.dubai.modules.user.service.UserBalanceLogService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


@Service("userBalanceLogService")
public class UserBalanceLogServiceImpl extends ServiceImpl<UserBalanceLogDao, UserBalanceLog> implements UserBalanceLogService {

    @Override
    public PageUtils queryPage(LogQuery query) {
        IPage<UserBalanceLog> page = query.getPage();
        page.setRecords(baseMapper.queryPage(page,query));
        return new PageUtils(page);
    }

    @Override
    public BigDecimal querySumAmountByUserIdType(Long userId, Integer type) {
        return baseMapper.querySumAmountByUserIdType(userId, type);
    }

    @Override
    public Integer queryCountByUserIdType(Long userId, Integer type) {
        return baseMapper.queryCountByUserIdType(userId, type);
    }

}
