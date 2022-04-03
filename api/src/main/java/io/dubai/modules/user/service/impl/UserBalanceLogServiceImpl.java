package io.dubai.modules.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dubai.common.enums.LogTypeEnum;
import io.dubai.common.enums.UserBalanceLogStatusEnum;
import io.dubai.common.utils.PageUtils;
import io.dubai.modules.goods.entity.GoodsOrder;
import io.dubai.modules.goods.service.GoodsOrderService;
import io.dubai.modules.user.dao.UserBalanceLogDao;
import io.dubai.modules.user.entity.UserBalanceLog;
import io.dubai.modules.user.entity.UserDeposit;
import io.dubai.modules.user.entity.UserWithdraw;
import io.dubai.modules.user.query.LogQuery;
import io.dubai.modules.user.service.UserBalanceLogService;
import io.dubai.modules.user.service.UserDepositService;
import io.dubai.modules.user.service.UserWithdrawService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Service("userBalanceLogService")
public class UserBalanceLogServiceImpl extends ServiceImpl<UserBalanceLogDao, UserBalanceLog> implements UserBalanceLogService {

    @Resource
    private UserDepositService userDepositService;

    @Resource
    private UserWithdrawService userWithdrawService;

    @Resource
    private GoodsOrderService goodsOrderService;

    @Override
    public PageUtils queryPage(LogQuery query) {
        IPage<UserBalanceLog> page = query.getPage();
        List<UserBalanceLog> list = new ArrayList<>();
        List<UserWithdraw> userWithdrawList = userWithdrawService.list(
                new QueryWrapper<UserWithdraw>()
                        .eq("user_id",query.getUserId())
                        .eq("status",0)
        );
        for (int i = 0; i < userWithdrawList.size(); i++) {
            UserBalanceLog log = new UserBalanceLog();
            log.setUserId(query.getUserId());
            log.setAmount(userWithdrawList.get(i).getAmount());
            log.setStatus(UserBalanceLogStatusEnum.WITHDRAW_WAITING.code);
            log.setType(LogTypeEnum.OUTLAY.code);
            log.setCreateTime(userWithdrawList.get(i).getCreateTime());
            list.add(log);
        }
        List<UserDeposit> userDepositList = userDepositService.list(
                new QueryWrapper<UserDeposit>()
                        .eq("user_id",query.getUserId())
                        .eq("status",0)
        );
        for (int i = 0; i < userDepositList.size(); i++) {
            UserBalanceLog log = new UserBalanceLog();
            log.setUserId(query.getUserId());
            log.setAmount(userDepositList.get(i).getAmount());
            log.setStatus(UserBalanceLogStatusEnum.DEPOSIT_WAITING.code);
            log.setType(LogTypeEnum.INCOME.code);
            log.setCreateTime(userDepositList.get(i).getCreateTime());
            log.setDesc(userDepositList.get(i).getOrderCode());
            list.add(log);
        }
        List<GoodsOrder> orderList = goodsOrderService.queryPendingOrderListByMerchantId(query.getUserId().intValue());
        for (int i = 0; i < orderList.size(); i++) {
            UserBalanceLog log = new UserBalanceLog();
            log.setUserId(query.getUserId());
            log.setAmount(orderList.get(i).getAmount());
            log.setStatus(UserBalanceLogStatusEnum.SHOP_ORDER_PENDING_INCOME.code);
            log.setType(LogTypeEnum.INCOME.code);
            log.setCreateTime(orderList.get(i).getCreateTime());
            list.add(log);
        }
        list.addAll(baseMapper.queryPage(page, query));
        page.setRecords(list);
        return new PageUtils(page);
    }

    @Override
    public BigDecimal querySumAmountByUserIdAndStatus(Long userId, Integer status) {
        return baseMapper.querySumAmountByUserIdAndStatus(userId, status);
    }


}
