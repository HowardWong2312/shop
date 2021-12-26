package io.dubai.modules.goods.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cz.czUser.system.entity.UserInfo;
import io.dubai.common.annotation.Login;
import io.dubai.common.annotation.LoginUser;
import io.dubai.common.enums.ResponseStatusEnum;
import io.dubai.common.enums.UserBalanceLogStatusEnum;
import io.dubai.common.enums.LogTypeEnum;
import io.dubai.common.enums.UserCreditsLogStatusEnum;
import io.dubai.common.exception.RRException;
import io.dubai.common.sys.service.SysConfigService;
import io.dubai.common.utils.PageUtils;
import io.dubai.common.utils.R;
import io.dubai.common.utils.StringUtils;
import io.dubai.modules.goods.entity.*;
import io.dubai.modules.goods.entity.vo.GoodsGroupRecordDetailsVo;
import io.dubai.modules.goods.entity.vo.GoodsGroupRecordVo;
import io.dubai.modules.goods.entity.vo.GoodsOrderVo;
import io.dubai.modules.goods.entity.vo.GoodsVo;
import io.dubai.modules.goods.form.CheckReturnDeliveryForm;
import io.dubai.modules.goods.form.OrderDeliveryForm;
import io.dubai.modules.goods.form.PaymentOrderForm;
import io.dubai.modules.goods.form.ReturnDeliveryForm;
import io.dubai.modules.goods.query.GoodsOrderQuery;
import io.dubai.modules.goods.service.*;
import io.dubai.modules.other.entity.Language;
import io.dubai.modules.other.service.LanguageService;
import io.dubai.modules.user.entity.UserBalanceLog;
import io.dubai.modules.user.entity.UserCreditsLog;
import io.dubai.modules.user.entity.UserLevel;
import io.dubai.modules.user.service.UserBalanceLogService;
import io.dubai.modules.user.service.UserCreditsLogService;
import io.dubai.modules.user.service.UserInfoService;
import io.dubai.modules.user.service.UserLevelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.ZoneOffset;
import java.util.List;


/**
 * 商品订单
 *
 * @author mother fucker
 * @email admin@gmail.com
 * @date 2021-10-07 16:25:36
 */
@RestController
@RequestMapping("goods/order")
@Api(tags = "商品订单")
public class GoodsOrderController {

    @Resource
    private GoodsOrderService goodsOrderService;

    @Resource
    private GoodsGroupRecordService goodsGroupRecordService;

    @Resource
    private GoodsGroupRecordDetailsService goodsGroupRecordDetailsService;

    @Resource
    private GoodsGroupService goodsGroupService;

    @Resource
    private LanguageService languageService;

    @Resource
    private UserInfoService userInfoService;

    @Resource
    private GoodsService goodsService;

    @Resource
    private UserBalanceLogService userBalanceLogService;

    @Resource
    private UserCreditsLogService userCreditsLogService;

    @Resource
    private UserLevelService userLevelService;

    @Resource
    private SysConfigService sysConfigService;

    @Login
    @ApiOperation("商家订单列表")
    @PostMapping("/listForMerchant")
    public R queryMerchantOrderList(@RequestBody GoodsOrderQuery query,@ApiIgnore @LoginUser UserInfo userInfo) {
        Long languageId = 1L;
        Language language = languageService.queryByName(userInfo.getLanguage());
        if(language != null){
            languageId = language.getId();
        }
        query.setLanguageId(languageId);
        query.setUserId(userInfo.getUserId().longValue());
        return R.ok().put("list", goodsOrderService.queryPageForMerchant(query));
    }

    @Login
    @ApiOperation("商家订单详情")
    @GetMapping("/infoForMerchant/{orderCode}")
    public R infoForMerchant(@PathVariable String orderCode,@ApiIgnore @LoginUser UserInfo userInfo) {
        Long languageId = 1L;
        Language language = languageService.queryByName(userInfo.getLanguage());
        if(language != null){
            languageId = language.getId();
        }
        GoodsOrderVo goodsOrderVo = goodsOrderService.queryByOrderCodeAndLanguageIdForMerchant(orderCode,languageId);
        return R.ok().put("order", goodsOrderVo);
    }

    @Login
    @ApiOperation("用户订单列表")
    @PostMapping("/list")
    public R list(@RequestBody GoodsOrderQuery query,@ApiIgnore @LoginUser UserInfo userInfo) {
        Long languageId = 1L;
        Language language = languageService.queryByName(userInfo.getLanguage());
        if(language != null){
            languageId = language.getId();
        }
        query.setLanguageId(languageId);
        query.setUserId(userInfo.getUserId().longValue());
        PageUtils page = goodsOrderService.queryPage(query);
        return R.ok().put("page", page);
    }

    @Login
    @ApiOperation("用户订单详情")
    @GetMapping("/info/{orderCode}")
    public R info(@PathVariable String orderCode,@ApiIgnore @LoginUser UserInfo userInfo) {
        Long languageId = 1L;
        Language language = languageService.queryByName(userInfo.getLanguage());
        if(language != null){
            languageId = language.getId();
        }
        GoodsOrderVo goodsOrderVo = goodsOrderService.queryByOrderCodeAndLanguageId(orderCode,languageId);
        GoodsGroupRecordDetails goodsGroupRecordDetails = goodsGroupRecordDetailsService.getOne(
                new QueryWrapper<GoodsGroupRecordDetails>()
                .eq("goods_order_code",orderCode)
                .last("limit 1")
        );
        if(goodsGroupRecordDetails != null){
            List<GoodsGroupRecordDetailsVo> goodsGroupRecordDetailsList = goodsGroupRecordDetailsService.queryListByGoodsGroupRecordId(goodsGroupRecordDetails.getGoodsGroupRecordId());
            goodsOrderVo.setGoodsGroupRecordDetailsList(goodsGroupRecordDetailsList);
            GoodsGroupRecordVo goodsGroupRecordVo = goodsGroupRecordService.queryById(goodsGroupRecordDetails.getGoodsGroupRecordId());
            long expiredTime = goodsGroupRecordVo.getCreateTime().plusHours(goodsGroupRecordVo.getPeriod()).toInstant(ZoneOffset.of("+8")).toEpochMilli();
            goodsGroupRecordVo.setPeriodTime(expiredTime-System.currentTimeMillis());
            goodsOrderVo.setGoodsGroupRecord(goodsGroupRecordVo);
        }
        long payPeriodTime = goodsOrderVo.getCreateTime().plusHours(1).toInstant(ZoneOffset.of("+8")).toEpochMilli();
        goodsOrderVo.setPayPeriodTime(payPeriodTime-System.currentTimeMillis());
        return R.ok().put("order", goodsOrderVo);
    }

    @Login
    @ApiOperation("订单付款")
    @PostMapping("/payment")
    public R payment(@RequestBody PaymentOrderForm form, @ApiIgnore @LoginUser UserInfo userInfo) {
        GoodsOrder goodsOrder = goodsOrderService.getById(form.getOrderCode());
        if(goodsOrder.getStatus() != 0){
            return R.ok(ResponseStatusEnum.ORDER_HAS_BEEN_UPDATED).put("orderCode",goodsOrder.getOrderCode());
        }
        if(userInfo.getPassword() == null || "".equalsIgnoreCase(userInfo.getPassword())){
            return R.ok(ResponseStatusEnum.PAY_PASSWORD_NOT_EXIST).put("orderCode",goodsOrder.getOrderCode());
        }
        if(!userInfo.getPassword().equalsIgnoreCase(form.getPassword())){
            return R.ok(ResponseStatusEnum.PAY_PASSWORD_ERROR).put("orderCode",goodsOrder.getOrderCode());
        }
        if(userInfo.getBalance().compareTo(goodsOrder.getAmount()) == -1){
            return R.ok(ResponseStatusEnum.BALANCE_INSUFFICIENT).put("orderCode",goodsOrder.getOrderCode());
        }
        goodsOrder.setStatus(1);
        GoodsGroupRecordDetails goodsGroupRecordDetails = goodsGroupRecordDetailsService.queryByGoodsOrderCode(goodsOrder.getOrderCode());
        if(goodsGroupRecordDetails != null){
            goodsGroupRecordDetails.setStatus(1);
            GoodsGroupRecord goodsGroupRecord = goodsGroupRecordService.queryById(goodsGroupRecordDetails.getGoodsGroupRecordId());
            if(goodsGroupRecordDetails.getUserId().equals(goodsGroupRecord.getUserId())){
                //如果是团长
                goodsGroupRecord.setStatus(1);
            }else{
                goodsGroupRecord.setStillNeed(goodsGroupRecord.getStillNeed()-1);
                if(goodsGroupRecord.getStillNeed() == 0) {
                    goodsGroupRecord.setStatus(2);
                }
            }
            goodsGroupRecordService.updateById(goodsGroupRecord);
            goodsGroupRecordDetailsService.updateById(goodsGroupRecordDetails);
        }
        goodsOrderService.updateById(goodsOrder);
        //扣除账户余额
        userInfo.setBalance(userInfo.getBalance().subtract(goodsOrder.getAmount()));
        userInfoService.update(userInfo);
        //增加账单记录
        UserBalanceLog userBalanceLog = new UserBalanceLog();
        userBalanceLog.setType(LogTypeEnum.OUTLAY.code);
        userBalanceLog.setBalance(userInfo.getBalance());
        userBalanceLog.setAmount(goodsOrder.getAmount().negate());
        userBalanceLog.setUserId(userInfo.getUserId().longValue());
        userBalanceLog.setStatus(UserBalanceLogStatusEnum.PAID_FOR_SHOP_ORDER.code);
        userBalanceLogService.save(userBalanceLog);
        return R.ok().put("orderCode", goodsOrder.getOrderCode());
    }

    @Login
    @ApiOperation("取消订单")
    @GetMapping("/cancel/{orderCode}")
    @Transactional
    public R cancel(@PathVariable String orderCode, @ApiIgnore @LoginUser UserInfo userInfo) {
        GoodsOrder goodsOrder = goodsOrderService.getById(orderCode);
        if(goodsOrder.getStatus() != 0 && goodsOrder.getStatus() != 1 && goodsOrder.getStatus() != 2){
            return R.ok(ResponseStatusEnum.ORDER_HAS_BEEN_UPDATED);
        }
        GoodsGroupRecordDetails goodsGroupRecordDetails = goodsGroupRecordDetailsService.queryByGoodsOrderCode(goodsOrder.getOrderCode());
        if(goodsOrder.getStatus() == 0){
            //待付款订单取消
            goodsGroupRecordDetails.setStatus(2);
        }
        if(goodsOrder.getStatus() == 1){
            Long languageId = 1L;
            Language language = languageService.queryByName(userInfo.getLanguage());
            if(language != null){
                languageId = language.getId();
            }
            GoodsVo goodsVo = goodsService.queryInfoByIdAndLanguageId(goodsOrder.getGoodsId(),languageId);
            //待分享订单取消，只返还金额
            userInfo.setBalance(userInfo.getBalance().add(goodsOrder.getAmount()));
            userInfoService.update(userInfo);
            //增加账单记录
            UserBalanceLog userBalanceLog = new UserBalanceLog();
            userBalanceLog.setType(LogTypeEnum.INCOME.code);
            userBalanceLog.setBalance(userInfo.getBalance());
            userBalanceLog.setAmount(goodsOrder.getAmount());
            userBalanceLog.setUserId(userInfo.getUserId().longValue());
            userBalanceLog.setStatus(UserBalanceLogStatusEnum.SHOP_ORDER_CANCELED.code);
            if(StringUtils.isEmpty(goodsVo.getLanguageTitle())){
                userBalanceLog.setDesc(goodsVo.getLanguageTitle());
            }else{
                userBalanceLog.setDesc(goodsVo.getTitle());
            }
            userBalanceLogService.save(userBalanceLog);
            goodsGroupRecordDetails.setStatus(2);
        }
        if(goodsOrder.getStatus() == 2){
            //待确认订单取消，返还余额并奖励积分
            GoodsGroupRecord goodsGroupRecord = goodsGroupRecordService.getById(goodsGroupRecordDetails.getGoodsGroupRecordId());
            GoodsGroup goodsGroup = goodsGroupService.getById(goodsGroupRecord.getGoodsGroupId());
            if(goodsGroup.getAwardNum() > 1 && userInfo.getFatherId() != null){
                BigDecimal credits = goodsGroup.getAward().multiply(BigDecimal.valueOf(goodsOrder.getQuantity()));
                //判断是否符合返积分条件
                Integer countToday = userCreditsLogService.queryCountByUserIdAndStatusToday(userInfo.getUserId().longValue(), UserCreditsLogStatusEnum.GROUP_AWARD.code);
                UserLevel userLevel = userLevelService.getById(userInfo.getUserLevelId());
                if(countToday < userLevel.getGroupNum()){
                    Integer countTotal = userCreditsLogService.queryCountByUserIdAndStatus(userInfo.getUserId().longValue(), UserCreditsLogStatusEnum.GROUP_AWARD.code);
                    if(countTotal < userLevel.getTotalGroupNum() || userLevel.getTotalGroupNum() == -1){
                        goodsGroupRecordDetails.setStatus(3);
                        goodsGroup.setAwardNum(goodsGroup.getAwardNum()-1);
                        userInfo.setCredits(userInfo.getCredits().add(credits));
                        for (int i = 0; i < goodsOrder.getQuantity(); i++) {
                            //增加积分记录
                            UserCreditsLog userCreditsLog = new UserCreditsLog();
                            userCreditsLog.setUserId(userInfo.getUserId().longValue());
                            userCreditsLog.setAmount(credits);
                            userCreditsLog.setType(LogTypeEnum.INCOME.code);
                            userCreditsLog.setStatus(UserCreditsLogStatusEnum.GROUP_AWARD.code);
                            userCreditsLog.setBalance(userInfo.getCredits());
                            userCreditsLogService.save(userCreditsLog);
                        }
                        UserInfo direct = userInfoService.queryByUserId(userInfo.getFatherId().longValue());
                        if(direct != null){
                            credits = goodsGroup.getAward().multiply(new BigDecimal(sysConfigService.getValue("DIRECT_INCOME_RATE")));
                            direct.setCredits(direct.getCredits().add(credits));
                            userInfoService.update(direct);
                            UserCreditsLog userCreditsLog = new UserCreditsLog();
                            userCreditsLog.setUserId(direct.getUserId().longValue());
                            userCreditsLog.setAmount(credits);
                            userCreditsLog.setType(LogTypeEnum.INCOME.code);
                            userCreditsLog.setStatus(UserCreditsLogStatusEnum.COMMISSION.code);
                            userCreditsLog.setBalance(direct.getCredits());
                            userCreditsLogService.save(userCreditsLog);
                            UserInfo second = userInfoService.queryByUserId(direct.getFatherId().longValue());
                            if(second != null){
                                credits = goodsGroup.getAward().multiply(new BigDecimal(sysConfigService.getValue("SECOND_INCOME_RATE")));
                                second.setCredits(second.getCredits().add(credits));
                                userInfoService.update(second);
                                userCreditsLog = new UserCreditsLog();
                                userCreditsLog.setUserId(second.getUserId().longValue());
                                userCreditsLog.setAmount(credits);
                                userCreditsLog.setType(LogTypeEnum.INCOME.code);
                                userCreditsLog.setStatus(UserCreditsLogStatusEnum.COMMISSION.code);
                                userCreditsLog.setBalance(second.getCredits());
                                userCreditsLogService.save(userCreditsLog);
                                UserInfo third = userInfoService.queryByUserId(second.getFatherId().longValue());
                                if(third != null){
                                    credits = goodsGroup.getAward().multiply(new BigDecimal(sysConfigService.getValue("THIRD_INCOME_RATE")));
                                    third.setCredits(third.getCredits().add(credits));
                                    userInfoService.update(third);
                                    userCreditsLog = new UserCreditsLog();
                                    userCreditsLog.setUserId(third.getUserId().longValue());
                                    userCreditsLog.setAmount(credits);
                                    userCreditsLog.setType(LogTypeEnum.INCOME.code);
                                    userCreditsLog.setStatus(UserCreditsLogStatusEnum.COMMISSION.code);
                                    userCreditsLog.setBalance(third.getCredits());
                                    userCreditsLogService.save(userCreditsLog);
                                }
                            }
                        }
                    }
                }

            }
            userInfo.setBalance(userInfo.getBalance().add(goodsOrder.getAmount()));
            userInfoService.update(userInfo);
            //增加账单记录
            UserBalanceLog userBalanceLog = new UserBalanceLog();
            userBalanceLog.setType(LogTypeEnum.INCOME.code);
            userBalanceLog.setBalance(userInfo.getBalance());
            userBalanceLog.setAmount(goodsOrder.getAmount());
            userBalanceLog.setUserId(userInfo.getUserId().longValue());
            userBalanceLog.setStatus(UserBalanceLogStatusEnum.SHOP_ORDER_CANCELED.code);
            userBalanceLogService.save(userBalanceLog);
        }
        goodsGroupRecordDetailsService.updateById(goodsGroupRecordDetails);
        goodsOrder.setStatus(6);
        goodsOrderService.updateById(goodsOrder);
        return R.ok();
    }

    @Login
    @ApiOperation("确认此订单需要发货")
    @GetMapping("/confirm/{orderCode}")
    public R confirm(@PathVariable String orderCode, @ApiIgnore @LoginUser UserInfo userInfo) {
        GoodsOrder goodsOrder = goodsOrderService.getById(orderCode);
        if(goodsOrder.getStatus() == 0){
            return R.ok(ResponseStatusEnum.ORDER_NOT_YET_PAY);
        }
        if(goodsOrder.getStatus() == 1){
            return R.ok(ResponseStatusEnum.GROUP_ORDER_NOT_YET_CONFIRM);
        }
        if(goodsOrder.getStatus() != 2){
            return R.ok(ResponseStatusEnum.ORDER_HAS_BEEN_UPDATED);
        }
        GoodsGroupRecordDetails goodsGroupRecordDetails = goodsGroupRecordDetailsService.queryByGoodsOrderCode(goodsOrder.getOrderCode());
        GoodsGroupRecord goodsGroupRecord = goodsGroupRecordService.getById(goodsGroupRecordDetails.getGoodsGroupRecordId());
        GoodsGroup goodsGroup = goodsGroupService.getById(goodsGroupRecord.getGoodsGroupId());
        if(goodsGroup.getAwardNum() > 1 && userInfo.getFatherId() != null){
            BigDecimal credits = goodsGroup.getAward();
            //判断是否符合返积分条件
            Integer countToday = userCreditsLogService.queryCountByUserIdAndStatusToday(userInfo.getUserId().longValue(), UserCreditsLogStatusEnum.GROUP_AWARD.code);
            UserLevel userLevel = userLevelService.getById(userInfo.getUserLevelId());
            if(countToday < userLevel.getGroupNum()){
                Integer countTotal = userCreditsLogService.queryCountByUserIdAndStatus(userInfo.getUserId().longValue(), UserCreditsLogStatusEnum.GROUP_AWARD.code);
                if(countTotal < userLevel.getTotalGroupNum() || userLevel.getTotalGroupNum() == -1){
                    goodsGroupRecordDetails.setStatus(3);
                    goodsGroupRecordDetailsService.updateById(goodsGroupRecordDetails);
                    goodsGroup.setAwardNum(goodsGroup.getAwardNum()-1);
                    userInfo.setCredits(userInfo.getCredits().add(credits));
                    userInfoService.update(userInfo);
                    UserCreditsLog userCreditsLog = new UserCreditsLog();
                    userCreditsLog.setUserId(userInfo.getUserId().longValue());
                    userCreditsLog.setAmount(credits);
                    userCreditsLog.setType(LogTypeEnum.INCOME.code);
                    userCreditsLog.setStatus(UserCreditsLogStatusEnum.GROUP_AWARD.code);
                    userCreditsLog.setBalance(userInfo.getCredits());
                    userCreditsLogService.save(userCreditsLog);
                    UserInfo direct = userInfoService.queryByUserId(userInfo.getFatherId().longValue());
                    if(direct != null){
                        credits = goodsGroup.getAward().multiply(new BigDecimal(sysConfigService.getValue("DIRECT_INCOME_RATE")));
                        direct.setCredits(direct.getCredits().add(credits));
                        userInfoService.update(direct);
                        userCreditsLog = new UserCreditsLog();
                        userCreditsLog.setUserId(direct.getUserId().longValue());
                        userCreditsLog.setAmount(credits);
                        userCreditsLog.setType(LogTypeEnum.INCOME.code);
                        userCreditsLog.setStatus(UserCreditsLogStatusEnum.COMMISSION.code);
                        userCreditsLog.setBalance(direct.getCredits());
                        userCreditsLogService.save(userCreditsLog);
                        UserInfo second = userInfoService.queryByUserId(direct.getFatherId().longValue());
                        if(second != null){
                            credits = goodsGroup.getAward().multiply(new BigDecimal(sysConfigService.getValue("SECOND_INCOME_RATE")));
                            second.setCredits(second.getCredits().add(credits));
                            userInfoService.update(second);
                            userCreditsLog = new UserCreditsLog();
                            userCreditsLog.setUserId(second.getUserId().longValue());
                            userCreditsLog.setAmount(credits);
                            userCreditsLog.setType(LogTypeEnum.INCOME.code);
                            userCreditsLog.setStatus(UserCreditsLogStatusEnum.COMMISSION.code);
                            userCreditsLog.setBalance(second.getCredits());
                            userCreditsLogService.save(userCreditsLog);
                            UserInfo third = userInfoService.queryByUserId(second.getFatherId().longValue());
                            if(third != null){
                                credits = goodsGroup.getAward().multiply(new BigDecimal(sysConfigService.getValue("THIRD_INCOME_RATE")));
                                third.setCredits(third.getCredits().add(credits));
                                userInfoService.update(third);
                                userCreditsLog = new UserCreditsLog();
                                userCreditsLog.setUserId(third.getUserId().longValue());
                                userCreditsLog.setAmount(credits);
                                userCreditsLog.setType(LogTypeEnum.INCOME.code);
                                userCreditsLog.setStatus(UserCreditsLogStatusEnum.COMMISSION.code);
                                userCreditsLog.setBalance(third.getCredits());
                                userCreditsLogService.save(userCreditsLog);
                            }
                        }
                    }
                }
            }
        }
        goodsOrder.setStatus(3);
        goodsOrderService.updateById(goodsOrder);
        return R.ok();
    }

    @Login
    @ApiOperation("用户确认收货")
    @GetMapping("/confirmDelivery/{orderCode}")
    public R confirmDelivery(@PathVariable String orderCode) {
        GoodsOrder goodsOrder = goodsOrderService.getById(orderCode);
        if(goodsOrder.getStatus() != 4){
            return R.ok(ResponseStatusEnum.ORDER_NOT_YET_DELIVERY);
        }
        goodsOrder.setStatus(5);
        goodsOrderService.updateById(goodsOrder);
        Goods goods = goodsService.getById(goodsOrder.getGoodsId());
        UserInfo merchant = userInfoService.queryByUserId(goods.getUserId());
        //将该订单的金额加到商家的用户余额里。
        merchant.setBalance(merchant.getBalance().add(goodsOrder.getAmount()));
        userInfoService.update(merchant);
        //增加余额变动记录
        UserBalanceLog userBalanceLog = new UserBalanceLog();
        userBalanceLog.setUserId(goods.getUserId());
        userBalanceLog.setAmount(goodsOrder.getAmount());
        userBalanceLog.setBalance(merchant.getBalance());
        userBalanceLog.setStatus(UserBalanceLogStatusEnum.SHOP_ORDER_INCOME.code);
        userBalanceLog.setType(LogTypeEnum.INCOME.code);
        userBalanceLogService.save(userBalanceLog);
        return R.ok();
    }

    @Login
    @ApiOperation("商家发货")
    @PostMapping("/delivery")
    public R delivery(@RequestBody OrderDeliveryForm form) {
        GoodsOrder goodsOrder = goodsOrderService.getById(form.getOrderCode());
        if(goodsOrder.getStatus() != 3){
            return R.ok(ResponseStatusEnum.ORDER_NOT_YET_CONFIRM_TO_DELIVERY);
        }
        goodsOrder.setLogistics(form.getLogistics());
        goodsOrder.setLogisticsCode(form.getLogisticsCode());
        goodsOrder.setStatus(4);
        goodsOrderService.updateById(goodsOrder);
        return R.ok();
    }

    @Login
    @ApiOperation("用户申请退货")
    @PostMapping("/returnDelivery")
    public R returnDelivery(@RequestBody ReturnDeliveryForm form) {
        GoodsOrder goodsOrder = goodsOrderService.getById(form.getOrderCode());
        if(goodsOrder.getStatus() != 4){
            return R.ok(ResponseStatusEnum.ORDER_NOT_YET_DELIVERY);
        }
        goodsOrder.setReturnReason(form.getReturnReason());
        goodsOrder.setStatus(7);
        goodsOrderService.updateById(goodsOrder);
        return R.ok();
    }

    @Login
    @ApiOperation("商家处理退货")
    @PostMapping("/checkReturnDelivery")
    public R checkReturnDelivery(@RequestBody CheckReturnDeliveryForm form) {
        GoodsOrder goodsOrder = goodsOrderService.getById(form.getOrderCode());
        if(goodsOrder.getStatus() != 7){
            return R.ok(ResponseStatusEnum.ORDER_HAS_BEEN_UPDATED);
        }
        if(form.getType() == null){
            throw new RRException(ResponseStatusEnum.PARAMS_IS_ERROR);
        }
        if(form.getType().intValue() == 1){
            UserInfo userInfo = userInfoService.queryByUserId(goodsOrder.getUserId());
            userInfo.setBalance(userInfo.getBalance().add(goodsOrder.getAmount()));
            userInfoService.update(userInfo);
            //增加余额变动记录
            UserBalanceLog userBalanceLog = new UserBalanceLog();
            userBalanceLog.setUserId(goodsOrder.getUserId());
            userBalanceLog.setAmount(goodsOrder.getAmount());
            userBalanceLog.setBalance(userInfo.getBalance());
            userBalanceLog.setStatus(UserBalanceLogStatusEnum.RETURN_DELIVERY.code);
            userBalanceLog.setType(LogTypeEnum.INCOME.code);
            userBalanceLogService.save(userBalanceLog);
            goodsOrder.setStatus(8);
        }
        if(form.getType().intValue() == 2){
            goodsOrder.setRemark(form.getRemark());
            goodsOrder.setStatus(9);
        }
        goodsOrderService.updateById(goodsOrder);
        return R.ok();
    }


}
