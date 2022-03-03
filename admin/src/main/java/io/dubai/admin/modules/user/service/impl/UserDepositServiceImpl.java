package io.dubai.admin.modules.user.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dubai.admin.modules.user.dao.UserDepositDao;
import io.dubai.admin.modules.user.entity.UserDeposit;
import io.dubai.admin.modules.user.entity.vo.UserDepositVo;
import io.dubai.admin.modules.user.service.UserDepositService;
import io.dubai.common.utils.PageUtils;
import io.dubai.common.utils.Query;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;


@Service("userDepositService")
public class UserDepositServiceImpl extends ServiceImpl<UserDepositDao, UserDeposit> implements UserDepositService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<UserDepositVo> page = new Query<UserDepositVo>().getPage(params);
        page.setRecords(baseMapper.queryPage(page,params));
        return new PageUtils(page);
    }

    @Override
    public BigDecimal queryAmountSumTotal(Map<String, Object> params) {
        return baseMapper.queryAmountSumTotal(params);
    }

}
