package io.dubai.common.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cz.czUser.system.entity.UserInfo;
import io.dubai.common.enums.UserBalanceLogStatusEnum;
import io.dubai.modules.goods.entity.GoodsGroup;
import io.dubai.modules.goods.entity.GoodsGroupRecord;
import io.dubai.modules.goods.entity.GoodsGroupRecordDetails;
import io.dubai.modules.goods.entity.GoodsOrder;
import io.dubai.modules.goods.service.GoodsGroupRecordDetailsService;
import io.dubai.modules.goods.service.GoodsGroupRecordService;
import io.dubai.modules.goods.service.GoodsGroupService;
import io.dubai.modules.goods.service.GoodsOrderService;
import io.dubai.modules.user.entity.UserBalanceLog;
import io.dubai.modules.user.service.UserBalanceLogService;
import io.dubai.modules.user.service.UserInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Configuration
@EnableScheduling
public class GoodsOrderAndGroupTask {

    private static final Logger logger = LoggerFactory.getLogger(GoodsOrderAndGroupTask.class);
    private static final int payExpiredHours = 1;

    @Resource
    private GoodsOrderService goodsOrderService;

    @Resource
    private GoodsGroupRecordDetailsService goodsGroupRecordDetailsService;

    @Resource
    private GoodsGroupRecordService goodsGroupRecordService;

    @Resource
    private GoodsGroupService goodsGroupService;

    @Resource
    private UserBalanceLogService userBalanceLogService;

    @Resource
    private UserInfoService userInfoService;

    public static void main(String[] args) {
        LocalDateTime time = LocalDateTime.now().plusHours(-1);
        System.out.println(time);
        System.out.println(time.isAfter(LocalDateTime.now()));
    }

    //检测待退款账单
    @Scheduled(cron = "0 0/1 * * * ?")
    private void pendingRefundTask() {
        logger.warn("开始检测所有检测待退款账单，定时任务正在执行");
        int count = 0;
        List<UserBalanceLog> userBalanceLogList = userBalanceLogService.list(
                new QueryWrapper<UserBalanceLog>().eq("status", UserBalanceLogStatusEnum.PENDING_REFUND.code)
        );
        for (int i = 0; i < userBalanceLogList.size(); i++) {
            UserBalanceLog userBalanceLog = userBalanceLogList.get(i);
            if(userBalanceLog.getCreateTime().plusDays(1).isAfter(LocalDateTime.now())){
                ++count;
                UserInfo userInfo = userInfoService.queryByUserId(userBalanceLog.getUserId());
                userInfo.setBalance(userInfo.getBalance().add(userBalanceLog.getAmount()));
                userInfoService.update(userInfo);
                userBalanceLog.setBalance(userInfo.getBalance());
                userBalanceLog.setStatus(UserBalanceLogStatusEnum.SHOP_ORDER_CANCELED.code);
                userBalanceLogService.updateById(userBalanceLog);
            }
        }
        logger.warn("共检测到" + count + "个待退款账单,状态已设置为关闭");
    }

    //拼团订单支付超时检测
    @Scheduled(cron = "0 0/1 * * * ?")
    private void goodsOrderTask() {
        logger.warn("开始检测所有超时的订单，定时任务正在执行");
        int count = 0;
        List<GoodsOrder> goodsOrderList = goodsOrderService.list(
                new QueryWrapper<GoodsOrder>().eq("status",0)
        );
        for (int i = 0; i < goodsOrderList.size(); i++) {
            GoodsOrder order = goodsOrderList.get(i);
            GoodsGroupRecordDetails goodsGroupRecordDetails = goodsGroupRecordDetailsService.queryByGoodsOrderCode(order.getOrderCode());
            if(goodsGroupRecordDetails != null){
                LocalDateTime payExpiredTime = goodsGroupRecordDetails.getCreateTime().plusHours(payExpiredHours);
                LocalDateTime now = LocalDateTime.now();
                if(now.isAfter(payExpiredTime)){
                    count++;
                    goodsGroupRecordDetails.setStatus(2);
                    order.setStatus(6);
                    goodsGroupRecordDetailsService.updateById(goodsGroupRecordDetails);
                    goodsOrderService.updateById(order);
                }
            }else{
                LocalDateTime payExpiredTime = order.getCreateTime().plusHours(payExpiredHours);
                LocalDateTime now = LocalDateTime.now();
                if(now.isAfter(payExpiredTime)){
                    count++;
                    order.setStatus(6);
                    goodsOrderService.updateById(order);
                }
            }

        }
        logger.warn("共检测到" + count + "个超时订单,状态已设置为关闭");
    }

    //拼团超时检测
    @Scheduled(cron = "0 0/1 * * * ?")
    private void goodsGroupRecordTask() {
        int count = 0;
        logger.warn("开始检测所有超时的拼团，定时任务正在执行");
        List<GoodsGroupRecord> goodsGroupRecordList = goodsGroupRecordService.list(new QueryWrapper<GoodsGroupRecord>().in("status",0,1));
        for (int i = 0; i < goodsGroupRecordList.size(); i++) {
            GoodsGroupRecord goodsGroupRecord = goodsGroupRecordList.get(i);
            GoodsGroup goodsGroup = goodsGroupService.getById(goodsGroupRecord.getGoodsGroupId());
            if(goodsGroup == null || goodsGroup.getStatus() == 0){
                //拼团活动结束
                goodsGroupRecord.setStatus(4);
                goodsGroupRecordService.updateById(goodsGroupRecord);
                continue;
            }
            if(goodsGroupRecord.getStatus() == 0){
                //判断拼团是否已超时未支付过期
                GoodsGroupRecordDetails goodsGroupRecordDetails = goodsGroupRecordDetailsService.getOne(
                        new QueryWrapper<GoodsGroupRecordDetails>()
                        .eq("user_id",goodsGroupRecord.getUserId())
                        .eq("goods_group_record_id",goodsGroupRecord.getId())
                );
                LocalDateTime expiredTime = goodsGroupRecordDetails.getCreateTime().plusHours(payExpiredHours);
                LocalDateTime now = LocalDateTime.now();
                if(now.isAfter(expiredTime)){
                    goodsGroupRecord.setStatus(4);
                    goodsGroupRecordService.updateById(goodsGroupRecord);
                    goodsGroupRecordDetails.setStatus(2);
                    goodsGroupRecordDetailsService.updateById(goodsGroupRecordDetails);
                    GoodsOrder goodsOrder = goodsOrderService.getById(goodsGroupRecordDetails.getGoodsOrderCode());
                    goodsOrder.setStatus(6);
                    goodsOrderService.updateById(goodsOrder);
                }
            }else{
                //判断拼团是否结束
                LocalDateTime expiredTime = goodsGroupRecord.getCreateTime().plusHours(goodsGroup.getPeriod());
                LocalDateTime now = LocalDateTime.now();
                if(now.isAfter(expiredTime)){
                    //如果拼团到期且已开团，则无条件成团
                    goodsGroupRecord.setStatus(2);
                    goodsGroupRecordService.updateById(goodsGroupRecord);
                }
            }

        }
        logger.warn("共检测到" + count + "个超时拼团,状态已设置为结束");
    }

    //拼团超时检测
    @Scheduled(cron = "0 0/1 * * * ?")
    private void goodsGroupRecordConfirmTask() {
        int count = 0;
        logger.warn("开始检测所有待确认拼团....");
        List<GoodsGroupRecord> goodsGroupRecordList = goodsGroupRecordService.list(new QueryWrapper<GoodsGroupRecord>().in("status",2));
        for (int i = 0; i < goodsGroupRecordList.size(); i++) {
            GoodsGroupRecord goodsGroupRecord = goodsGroupRecordList.get(i);
            GoodsGroup goodsGroup = goodsGroupService.getById(goodsGroupRecord.getGoodsGroupId());
            if(goodsGroup == null || goodsGroup.getStatus() == 0){
                //拼团活动结束
                goodsGroupRecord.setStatus(4);
                goodsGroupRecordService.updateById(goodsGroupRecord);
                continue;
            }
            List<GoodsGroupRecordDetails> goodsGroupRecordDetailsList = goodsGroupRecordDetailsService.list(
                    new QueryWrapper<GoodsGroupRecordDetails>()
                            .eq("goods_group_record_id",goodsGroupRecord.getId())
            );
            for (int j = 0; j < goodsGroupRecordDetailsList.size(); j++) {
                GoodsGroupRecordDetails goodsGroupRecordDetails = goodsGroupRecordDetailsList.get(j);
                GoodsOrder goodsOrder = goodsOrderService.getById(goodsGroupRecordDetails.getGoodsOrderCode());
                if(goodsOrder == null){
                    continue;
                }
                if(goodsOrder.getStatus() == 1 ){
                    goodsOrder.setStatus(2);
                }else{
                    goodsOrder.setStatus(6);
                    goodsGroupRecordDetails.setStatus(2);
                    goodsGroupRecordDetailsService.updateById(goodsGroupRecordDetails);
                }
                goodsOrderService.updateById(goodsOrder);
            }
            goodsGroupRecord.setStatus(3);
            goodsGroupRecordService.updateById(goodsGroupRecord);
        }
        logger.warn("共检测到" + count + "个待确认拼团");
    }


}
