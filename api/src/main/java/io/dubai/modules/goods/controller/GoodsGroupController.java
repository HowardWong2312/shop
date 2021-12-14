package io.dubai.modules.goods.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cz.czUser.system.entity.UserInfo;
import io.dubai.common.annotation.Login;
import io.dubai.common.annotation.LoginUser;
import io.dubai.common.enums.ResponseStatusEnum;
import io.dubai.common.enums.UserBalanceLogStatusEnum;
import io.dubai.common.enums.LogTypeEnum;
import io.dubai.common.exception.RRException;
import io.dubai.common.utils.R;
import io.dubai.common.utils.StringUtils;
import io.dubai.modules.goods.entity.*;
import io.dubai.modules.goods.entity.vo.GoodsGroupRecordDetailsVo;
import io.dubai.modules.goods.entity.vo.GoodsGroupRecordVo;
import io.dubai.modules.goods.form.CreateGroupForm;
import io.dubai.modules.goods.form.JoinGroupForm;
import io.dubai.modules.goods.service.GoodsGroupRecordDetailsService;
import io.dubai.modules.goods.service.GoodsGroupRecordService;
import io.dubai.modules.goods.service.GoodsGroupService;
import io.dubai.modules.goods.service.GoodsOrderService;
import io.dubai.modules.user.entity.UserAddress;
import io.dubai.modules.user.entity.UserBalanceLog;
import io.dubai.modules.user.service.UserAddressService;
import io.dubai.modules.user.service.UserBalanceLogService;
import io.dubai.modules.user.service.UserInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * 拼团商品
 *
 * @author Howard
 * @email admin@gmail.com
 * @date 2021-10-07 16:25:36
 */
@RestController
@RequestMapping("goods/group")
@Api(tags = "拼团商品")
public class GoodsGroupController {

    @Resource
    private GoodsGroupService goodsGroupService;

    @Resource
    private UserInfoService userInfoService;

    @Resource
    private GoodsGroupRecordService goodsGroupRecordService;

    @Resource
    private GoodsGroupRecordDetailsService goodsGroupRecordDetailsService;

    @Resource
    private GoodsOrderService goodsOrderService;

    @Resource
    private UserBalanceLogService userBalanceLogService;

    @Resource
    private UserAddressService userAddressService;

    @Login
    @ApiOperation("根据商品ID查拼团列表")
    @GetMapping("/goodsGroupRecordList/{goodsId}")
    public R goodsGroupRecordList(@PathVariable Long goodsId) {
        return R.ok().put("list", goodsGroupRecordService.queryListByGoodsId(goodsId,null));
    }

    @Login
    @ApiOperation("根据拼团ID查询参团用户")
    @GetMapping("/goodsGroupRecordDetailsList/{goodsGroupRecordId}")
    public R goodsGroupRecordDetailsList(@PathVariable Long goodsGroupRecordId) {
        List<GoodsGroupRecordDetailsVo> list = goodsGroupRecordDetailsService.queryListByGoodsGroupRecordId(goodsGroupRecordId);
        GoodsGroupRecordVo goodsGroupRecordVo = goodsGroupRecordService.queryById(goodsGroupRecordId);
        return R.ok().put("list", list).put("goodsGroupRecord",goodsGroupRecordVo);
    }

    @Login
    @ApiOperation("发起拼团")
    @PostMapping("/createGroup")
    public R createGroup(@RequestBody CreateGroupForm form, @ApiIgnore @LoginUser UserInfo userInfo) {
        GoodsGroup goodsGroup = goodsGroupService.getById(form.getGoodsGroupId());
        if(goodsGroup == null){
            throw new RRException(ResponseStatusEnum.GROUP_NOT_EXIST);
        }
        if(userInfo.getPassword() == null || "".equalsIgnoreCase(userInfo.getPassword())){
            return R.ok(ResponseStatusEnum.PAY_PASSWORD_NOT_EXIST);
        }
        if(!userInfo.getPassword().equalsIgnoreCase(form.getPassword())){
            return R.ok(ResponseStatusEnum.PAY_PASSWORD_ERROR);
        }
        BigDecimal amount = goodsGroup.getPrice().multiply(BigDecimal.valueOf(form.getQuantity()));
        UserAddress userAddress = userAddressService.getById(form.getUserAddressId());
        //创建订单
        GoodsOrder goodsOrder = new GoodsOrder();
        goodsOrder.setReceiverName(userAddress.getName());
        goodsOrder.setReceiverPhone(userAddress.getPhone());
        goodsOrder.setReceiverProvince(userAddress.getProvince());
        goodsOrder.setReceiverCity(userAddress.getCity());
        goodsOrder.setReceiverArea(userAddress.getArea());
        goodsOrder.setReceiverAddress(userAddress.getAddress());
        goodsOrder.setQuantity(form.getQuantity());
        goodsOrder.setStatus(0);
        goodsOrder.setOrderCode(StringUtils.createOrderNum());
        goodsOrder.setGoodsId(goodsGroup.getGoodsId());
        goodsOrder.setAmount(amount);
        goodsOrder.setUserId(userInfo.getUserId().longValue());
        goodsOrder.setPaymentId(form.getPaymentId());
        goodsOrderService.save(goodsOrder);
        //创建团
        GoodsGroupRecord goodsGroupRecord = new GoodsGroupRecord();
        goodsGroupRecord.setGoodsGroupId(goodsGroup.getId());
        goodsGroupRecord.setUserId(userInfo.getUserId().longValue());
        goodsGroupRecord.setUserNum(goodsGroup.getUserNum());
        goodsGroupRecord.setStillNeed(goodsGroup.getUserNum());
        goodsGroupRecord.setStatus(0);
        goodsGroupRecordService.save(goodsGroupRecord);
        //添加参团记录
        GoodsGroupRecordDetails goodsGroupRecordDetails = new GoodsGroupRecordDetails();
        goodsGroupRecordDetails.setGoodsGroupRecordId(goodsGroupRecord.getId());
        goodsGroupRecordDetails.setUserId(userInfo.getUserId().longValue());
        goodsGroupRecordDetails.setGoodsOrderCode(goodsOrder.getOrderCode());
        goodsGroupRecordDetails.setStatus(0);
        goodsGroupRecordDetailsService.save(goodsGroupRecordDetails);
        if(userInfo.getBalance().compareTo(goodsOrder.getAmount()) == -1){
            return R.ok(ResponseStatusEnum.BALANCE_INSUFFICIENT).put("orderCode",goodsOrder.getOrderCode());
        }
        goodsOrder.setStatus(1);
        goodsOrderService.updateById(goodsOrder);
        goodsGroupRecord.setStillNeed(goodsGroupRecord.getStillNeed()-1);
        goodsGroupRecord.setStatus(1);
        if(goodsGroupRecord.getStillNeed() == 0){
            goodsGroupRecord.setStatus(2);
        }
        goodsGroupRecordService.updateById(goodsGroupRecord);
        goodsGroupRecordDetails.setStatus(1);
        goodsGroupRecordDetailsService.updateById(goodsGroupRecordDetails);
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
        return R.ok().put("orderCode",goodsOrder.getOrderCode());
    }

    @Login
    @ApiOperation("参与拼团")
    @PostMapping("/joinGroup")
    public R joinGroup(@RequestBody JoinGroupForm form, @ApiIgnore @LoginUser UserInfo userInfo) {
        //判断当前用户是否已参过此团
        List<GoodsGroupRecordDetails> goodsGroupRecordDetailsList = goodsGroupRecordDetailsService.list(
                new QueryWrapper<GoodsGroupRecordDetails>()
                .eq("goods_group_record_id",form.getGoodsGroupRecordId())
                .eq("user_id",userInfo.getUserId())
        );
        if(!goodsGroupRecordDetailsList.isEmpty()){
            throw new RRException(ResponseStatusEnum.USER_HAS_BEEN_JOINED_GROUP);
        }
        GoodsGroupRecord goodsGroupRecord = goodsGroupRecordService.getById(form.getGoodsGroupRecordId());
        if(goodsGroupRecord == null){
            throw new RRException(ResponseStatusEnum.GROUP_RECORD_NOT_EXIST);
        }
        if(goodsGroupRecord.getStatus() == 2){
            throw new RRException(ResponseStatusEnum.GROUP_RECORD_IS_DONE);
        }
        GoodsGroup goodsGroup = goodsGroupService.getById(goodsGroupRecord.getGoodsGroupId());
        if(goodsGroup == null){
            throw new RRException(ResponseStatusEnum.GROUP_RECORD_NOT_EXIST);
        }
        if(userInfo.getPassword() == null || "".equalsIgnoreCase(userInfo.getPassword())){
            return R.ok(ResponseStatusEnum.PAY_PASSWORD_NOT_EXIST);
        }
        if(!userInfo.getPassword().equalsIgnoreCase(form.getPassword())){
            return R.ok(ResponseStatusEnum.PAY_PASSWORD_ERROR);
        }
        UserAddress userAddress = userAddressService.getById(form.getUserAddressId());
        GoodsOrder goodsOrder = new GoodsOrder();
        goodsOrder.setReceiverName(userAddress.getName());
        goodsOrder.setReceiverPhone(userAddress.getPhone());
        goodsOrder.setReceiverProvince(userAddress.getProvince());
        goodsOrder.setReceiverCity(userAddress.getCity());
        goodsOrder.setReceiverArea(userAddress.getArea());
        goodsOrder.setReceiverAddress(userAddress.getAddress());
        goodsOrder.setQuantity(form.getQuantity());
        goodsOrder.setStatus(0);
        goodsOrder.setOrderCode(StringUtils.createOrderNum());
        goodsOrder.setGoodsId(goodsGroup.getGoodsId());
        goodsOrder.setAmount(goodsGroup.getPrice().multiply(BigDecimal.valueOf(form.getQuantity())));
        goodsOrder.setUserId(userInfo.getUserId().longValue());
        goodsOrder.setPaymentId(form.getPaymentId());
        goodsOrderService.save(goodsOrder);
        GoodsGroupRecordDetails goodsGroupRecordDetails = new GoodsGroupRecordDetails();
        goodsGroupRecordDetails.setGoodsGroupRecordId(goodsGroupRecord.getId());
        goodsGroupRecordDetails.setUserId(userInfo.getUserId().longValue());
        goodsGroupRecordDetails.setGoodsOrderCode(goodsOrder.getOrderCode());
        goodsGroupRecordDetails.setStatus(0);
        goodsGroupRecordDetailsService.save(goodsGroupRecordDetails);
        if(userInfo.getBalance().compareTo(goodsOrder.getAmount()) == -1){
            return R.ok(ResponseStatusEnum.BALANCE_INSUFFICIENT).put("orderCode",goodsOrder.getOrderCode());
        }
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
        goodsOrder.setStatus(1);
        goodsGroupRecordDetails.setStatus(1);
        goodsGroupRecord.setStillNeed(goodsGroupRecord.getStillNeed()-1);
        if(goodsGroupRecord.getStillNeed() == 0) {
            goodsGroupRecord.setStatus(2);
            //把该团的所有订单设为待确认
        }
        goodsGroupRecordService.updateById(goodsGroupRecord);
        goodsOrderService.updateById(goodsOrder);
        goodsGroupRecordDetailsService.updateById(goodsGroupRecordDetails);
        return R.ok().put("orderCode",goodsOrder.getOrderCode());
    }

//    @Login
//    @ApiOperation("领取返现奖励")
//    @GetMapping("/getCashAward/{orderCode}")
//    public R getCashAward(@PathVariable String orderCode, @ApiIgnore @LoginUser UserInfo userInfo) {
//        GoodsOrder goodsOrder = goodsOrderService.getById(orderCode);
//        if(goodsOrder == null){
//            throw new RRException(ResponseStatusEnum.PARAMS_IS_ERROR);
//        }
//        if(goodsOrder.getStatus() == 0){
//            throw new RRException(ResponseStatusEnum.ORDER_NOT_YET_PAY);
//        }
//        GoodsGroupRecordDetails goodsGroupRecordDetails = goodsGroupRecordDetailsService.getOne(
//                new QueryWrapper<GoodsGroupRecordDetails>()
//                .eq("goods_order_code",orderCode)
//        );
//        if(goodsGroupRecordDetails.getStatus() == 0){
//            throw new RRException(ResponseStatusEnum.ORDER_NOT_YET_PAY);
//        }
//        if(goodsGroupRecordDetails.getStatus() == 2){
//            throw new RRException(ResponseStatusEnum.ORDER_EXPIRED);
//        }
//        if(goodsGroupRecordDetails.getStatus() == 3){
//            throw new RRException(ResponseStatusEnum.ALREADY_TAKE_CASH_AWARD);
//        }
//        GoodsGroupRecord goodsGroupRecord = goodsGroupRecordService.getById(goodsGroupRecordDetails.getGoodsGroupRecordId());
//        if(goodsGroupRecord == null){
//            throw new RRException(ResponseStatusEnum.PARAMS_IS_ERROR);
//        }
//        GoodsGroup goodsGroup = goodsGroupService.getById(goodsGroupRecord.getGoodsGroupId());
//        if(goodsGroup == null){
//            throw new RRException(ResponseStatusEnum.PARAMS_IS_ERROR);
//        }
//        if(goodsGroup.getCashAward().compareTo(BigDecimal.ZERO) < 1){
//            throw new RRException(ResponseStatusEnum.THERE_IS_NOT_CASH_AWARD);
//        }
//        int cashAwardCount = goodsGroupRecordDetailsService.queryCashAwardCountByGoodsGroupId(goodsGroup.getId());
//        if(goodsGroup.getCashAwardNum() >= cashAwardCount){
//            throw new RRException(ResponseStatusEnum.THERE_IS_NOT_MORE_CASH_AWARD);
//        }
//        BigDecimal cashAward = goodsGroup.getCashAward().multiply(BigDecimal.valueOf(goodsOrder.getQuantity()));
//        goodsGroupRecordDetails.setStatus(3);
//        goodsGroupRecordDetailsService.updateById(goodsGroupRecordDetails);
//        userAccount.setBalance(userAccount.getBalance().add(cashAward));
//        userInfoService.update(userAccount);
//        return R.ok();
//    }

}
