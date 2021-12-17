package io.dubai.admin.modules.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dubai.admin.modules.user.dao.UserBalanceLogDao;
import io.dubai.admin.modules.user.entity.UserBalanceLog;
import io.dubai.admin.modules.user.entity.vo.UserBalanceLogVo;
import io.dubai.admin.modules.user.service.UserBalanceLogService;
import io.dubai.common.utils.PageUtils;
import io.dubai.common.utils.Query;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("userBalanceLogService")
public class UserBalanceLogServiceImpl extends ServiceImpl<UserBalanceLogDao, UserBalanceLog> implements UserBalanceLogService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<UserBalanceLogVo> page = new Query<UserBalanceLogVo>().getPage(params);
        page.setRecords(baseMapper.queryPage(page,params));
        return new PageUtils(page);
    }

}
