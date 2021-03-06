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
 * ????????????
 *
 * @author mother fucker
 * @email admin@gmail.com
 * @date 2021-10-07 16:25:36
 */
@RestController
@RequestMapping("goods/order")
@Api(tags = "????????????")
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
    @ApiOperation("??????????????????")
    @PostMapping("/listForMerchant")
    public R queryMerchantOrderList(@RequestBody GoodsOrderQuery query,@ApiIgnore @LoginUser UserInfo userInfo) {
        query.setLanguageId(userInfo.getLanguage());
        query.setUserId(userInfo.getUserId().longValue());
        return R.ok().put("list", goodsOrderService.queryPageForMerchant(query));
    }

    @Login
    @ApiOperation("??????????????????")
    @GetMapping("/infoForMerchant/{orderCode}")
    public R infoForMerchant(@PathVariable String orderCode,@ApiIgnore @LoginUser UserInfo userInfo) {
        GoodsOrderVo goodsOrderVo = goodsOrderService.queryByOrderCodeAndLanguageIdForMerchant(orderCode,userInfo.getLanguage());
        return R.ok().put("order", goodsOrderVo);
    }

    @Login
    @ApiOperation("??????????????????")
    @PostMapping("/list")
    public R list(@RequestBody GoodsOrderQuery query,@ApiIgnore @LoginUser UserInfo userInfo) {
        query.setLanguageId(userInfo.getLanguage());
        query.setUserId(userInfo.getUserId().longValue());
        PageUtils page = goodsOrderService.queryPage(query);
        return R.ok().put("page", page);
    }

    @Login
    @ApiOperation("??????????????????")
    @GetMapping("/info/{orderCode}")
    public R info(@PathVariable String orderCode,@ApiIgnore @LoginUser UserInfo userInfo) {
        GoodsOrderVo goodsOrderVo = goodsOrderService.queryByOrderCodeAndLanguageId(orderCode,userInfo.getLanguage());
        GoodsGroupRecordDetails goodsGroupRecordDetails = goodsGroupRecordDetailsService.getOne(
                new QueryWrapper<GoodsGroupRecordDetails>()
                .eq("goods_order_code",orderCode)
                .last("limit 1")
        );
        if(goodsGroupRecordDetails != null){
            List<GoodsGroupRecordDetailsVo> goodsGroupRecordDetailsList = goodsGroupRecordDetailsService.queryListByGoodsGroupRecordId(goodsGroupRecordDetails.getGoodsGroupRecordId());
            goodsOrderVo.setGoodsGroupRecordDetailsList(goodsGroupRecordDetailsList);
            GoodsGroupRecordVo goodsGroupRecordVo = goodsGroupRecordService.queryById(goodsGroupRecordDetails.getGoodsGroupRecordId());
            long expiredTime = goodsGroupRecordVo.getCreateTime().plusHours(goodsGroupRecordVo.getPeriod()).toInstant(ZoneOffset.of("+2")).toEpochMilli();
            goodsGroupRecordVo.setPeriodTime(expiredTime-System.currentTimeMillis());
            goodsOrderVo.setGoodsGroupRecord(goodsGroupRecordVo);
        }
        long payPeriodTime = goodsOrderVo.getCreateTime().plusHours(1).toInstant(ZoneOffset.of("+2")).toEpochMilli();
        goodsOrderVo.setPayPeriodTime(payPeriodTime-System.currentTimeMillis());
        return R.ok().put("order", goodsOrderVo);
    }

    @Login
    @ApiOperation("????????????")
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
                //???????????????
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
        //??????????????????
        userInfo.setBalance(userInfo.getBalance().subtract(goodsOrder.getAmount()));
        userInfoService.update(userInfo);
        //??????????????????
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
    @ApiOperation("??????????????????")
    @GetMapping("/cancelForMerchant/{orderCode}")
    @Transactional
    public R cancelForMerchant(@PathVariable String orderCode, @ApiIgnore @LoginUser UserInfo userInfo) {
        GoodsOrder goodsOrder = goodsOrderService.getById(orderCode);
        if(goodsOrder.getStatus() != 3){
            return R.ok(ResponseStatusEnum.ORDER_HAS_BEEN_UPDATED);
        }
        goodsOrder.setStatus(6);
        goodsOrderService.updateById(goodsOrder);
        //??????????????????
        UserBalanceLog userBalanceLog = new UserBalanceLog();
        userBalanceLog.setType(LogTypeEnum.INCOME.code);
        userBalanceLog.setAmount(goodsOrder.getAmount());
        userBalanceLog.setUserId(userInfo.getUserId().longValue());
        userBalanceLog.setStatus(UserBalanceLogStatusEnum.PENDING_REFUND.code);
        userBalanceLog.setDesc(goodsOrder.getOrderCode());
        userBalanceLogService.save(userBalanceLog);
        return R.ok();
    }

    @Login
    @ApiOperation("??????????????????")
    @GetMapping("/cancel/{orderCode}")
    @Transactional
    public R cancel(@PathVariable String orderCode, @ApiIgnore @LoginUser UserInfo userInfo) {
        GoodsOrder goodsOrder = goodsOrderService.getById(orderCode);
        if(goodsOrder.getStatus() != 0 && goodsOrder.getStatus() != 1 && goodsOrder.getStatus() != 2){
            return R.ok(ResponseStatusEnum.ORDER_HAS_BEEN_UPDATED);
        }
        GoodsGroupRecordDetails goodsGroupRecordDetails = goodsGroupRecordDetailsService.queryByGoodsOrderCode(goodsOrder.getOrderCode());
        if(goodsOrder.getStatus() == 0){
            //?????????????????????
            goodsGroupRecordDetails.setStatus(2);
        }else{
            //???????????????????????????
            UserBalanceLog userBalanceLog = new UserBalanceLog();
            userBalanceLog.setType(LogTypeEnum.INCOME.code);
            userBalanceLog.setAmount(goodsOrder.getAmount());
            userBalanceLog.setUserId(userInfo.getUserId().longValue());
            userBalanceLog.setStatus(UserBalanceLogStatusEnum.PENDING_REFUND.code);
            userBalanceLogService.save(userBalanceLog);
        }
        if(goodsOrder.getStatus() == 1){
            goodsGroupRecordDetails.setStatus(2);
        }
        if(goodsOrder.getStatus() == 2){
            //????????????????????????????????????
            GoodsGroupRecord goodsGroupRecord = goodsGroupRecordService.getById(goodsGroupRecordDetails.getGoodsGroupRecordId());
            GoodsGroup goodsGroup = goodsGroupService.getById(goodsGroupRecord.getGoodsGroupId());
            if(goodsGroup.getAwardNum() > 1 && userInfo.getFatherId() != null){
                BigDecimal credits = goodsGroup.getAward().multiply(BigDecimal.valueOf(goodsOrder.getQuantity()));
                //?????????????????????????????????
                Integer countToday = userCreditsLogService.queryCountByUserIdAndStatusToday(userInfo.getUserId().longValue(), UserCreditsLogStatusEnum.GROUP_AWARD.code);
                UserLevel userLevel = userLevelService.getById(userInfo.getUserLevelId());
                if(countToday < userLevel.getGroupNum()){
                    Integer countTotal = userCreditsLogService.queryCountByUserIdAndStatus(userInfo.getUserId().longValue(), UserCreditsLogStatusEnum.GROUP_AWARD.code);
                    if(countTotal < userLevel.getTotalGroupNum() || userLevel.getTotalGroupNum() == -1){
                        goodsGroupRecordDetails.setStatus(3);
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
                        UserInfo direct = null;
                        if(userInfo.getFatherId() != null && userInfo.getFatherId() != 0){
                            direct = userInfoService.queryByUserId(userInfo.getFatherId().longValue());
                        }
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
                            UserInfo second = null;
                            if(direct.getFatherId() != null && direct.getFatherId() != 0){
                                second = userInfoService.queryByUserId(direct.getFatherId().longValue());
                            }
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
                                UserInfo third = null;
                                if(second.getFatherId() != null && second.getFatherId() != 0){
                                    third = userInfoService.queryByUserId(second.getFatherId().longValue());
                                }
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
        }
        goodsGroupRecordDetailsService.updateById(goodsGroupRecordDetails);
        goodsOrder.setStatus(6);
        goodsOrderService.updateById(goodsOrder);
        return R.ok();
    }

    @Login
    @ApiOperation("??????????????????????????????")
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
            //?????????????????????????????????
            Integer countToday = userCreditsLogService.queryCountByUserIdAndStatusToday(userInfo.getUserId().longValue(), UserCreditsLogStatusEnum.GROUP_AWARD.code);
            UserLevel userLevel = userLevelService.getById(userInfo.getUserLevelId());
            if(countToday < userLevel.getGroupNum()){
                Integer countTotal = userCreditsLogService.queryCountByUserIdAndStatus(userInfo.getUserId().longValue(), UserCreditsLogStatusEnum.GROUP_AWARD.code);
                if(countTotal < userLevel.getTotalGroupNum() || userLevel.getTotalGroupNum() == -1){
                    goodsGroupRecordDetails.setStatus(3);
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
                    UserInfo direct = null;
                    if(userInfo.getFatherId() != null && userInfo.getFatherId() != 0){
                        direct = userInfoService.queryByUserId(userInfo.getFatherId().longValue());
                    }
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
                        UserInfo second = null;
                        if(direct.getFatherId() != null && direct.getFatherId() != 0){
                            second = userInfoService.queryByUserId(direct.getFatherId().longValue());
                        }
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
                            UserInfo third = null;
                            if(second.getFatherId() != null && second.getFatherId() != 0){
                                third = userInfoService.queryByUserId(second.getFatherId().longValue());
                            }
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
        goodsGroupRecordDetailsService.updateById(goodsGroupRecordDetails);
        goodsOrder.setStatus(3);
        goodsOrderService.updateById(goodsOrder);
        return R.ok();
    }

    @Login
    @ApiOperation("??????????????????-????????????")
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
        //??????????????????????????????????????????????????????
        merchant.setBalance(merchant.getBalance().add(goodsOrder.getAmount()));
        userInfoService.update(merchant);
        //????????????????????????
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
    @ApiOperation("????????????")
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
    @ApiOperation("??????????????????")
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
    @ApiOperation("??????????????????")
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
            //????????????????????????
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
