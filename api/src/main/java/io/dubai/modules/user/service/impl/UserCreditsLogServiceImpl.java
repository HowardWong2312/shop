package io.dubai.modules.user.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dubai.common.utils.PageUtils;
import io.dubai.modules.user.dao.UserCreditsLogDao;
import io.dubai.modules.user.entity.UserCreditsLog;
import io.dubai.modules.user.query.LogQuery;
import io.dubai.modules.user.service.UserCreditsLogService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


@Service("userCreditsLogService")
public class UserCreditsLogServiceImpl extends ServiceImpl<UserCreditsLogDao, UserCreditsLog> implements UserCreditsLogService {

    @Override
    public PageUtils queryPage(LogQuery query) {
        if ("create_time".equalsIgnoreCase(query.getOrderField())) {
            query.setOrderField("createTime");
        }
        IPage<UserCreditsLog> page = query.getPage();
        page.setRecords(baseMapper.queryPage(page, query));
        return new PageUtils(page);
    }

    @Override
    public Integer queryCountByUserIdAndStatusToday(Long userId, Integer status) {
        return baseMapper.queryCountByUserIdAndStatusToday(userId, status);
    }

    @Override
    public Integer queryCountByUserIdAndStatus(Long userId, Integer status) {
        return baseMapper.queryCountByUserIdAndStatus(userId, status);
    }

    @Override
    public BigDecimal queryAmountSumByUserIdAndStatus(Long userId, Integer status) {
        return baseMapper.queryAmountSumByUserIdAndStatus(userId, status);
    }

}
