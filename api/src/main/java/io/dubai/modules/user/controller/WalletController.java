package io.dubai.modules.user.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cz.czUser.system.entity.UserInfo;
import io.dubai.common.annotation.Login;
import io.dubai.common.annotation.LoginUser;
import io.dubai.common.enums.LogTypeEnum;
import io.dubai.common.enums.ResponseStatusEnum;
import io.dubai.common.enums.UserBalanceLogStatusEnum;
import io.dubai.common.enums.UserCreditsLogStatusEnum;
import io.dubai.common.exception.RRException;
import io.dubai.common.sys.service.SysConfigService;
import io.dubai.common.utils.HttpUtils;
import io.dubai.common.utils.R;
import io.dubai.common.utils.StringUtils;
import io.dubai.common.utils.Utils;
import io.dubai.modules.user.entity.*;
import io.dubai.modules.user.entity.vo.CreditsTaskVo;
import io.dubai.modules.user.form.*;
import io.dubai.modules.user.query.LogQuery;
import io.dubai.modules.user.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 用户账户
 *
 * @author mother fucker
 * @email admin@gmail.com
 * @date 2021-10-07 16:28:10
 */
@RestController
@RequestMapping("wallet")
@Api(tags = "用户钱包")
public class WalletController {

    @Resource
    private UserInfoService userInfoService;

    @Resource
    private UserBalanceLogService userBalanceLogService;

    @Resource
    private UserCreditsLogService userCreditsLogService;

    @Resource
    private UserWithdrawService userWithdrawService;

    @Resource
    private UserDepositService userDepositService;

    @Resource
    private UserLevelService userLevelService;

    @Resource
    private UserBankService userBankService;

    @Resource
    private SysConfigService sysConfigService;


    @Login
    @ApiOperation("获取商家总收入")
    @GetMapping("/getMerchantTotalIncome")
    public R getMerchantInfo(@ApiIgnore @LoginUser UserInfo userInfo) {
        BigDecimal income = userBalanceLogService.querySumAmountByUserIdType(userInfo.getUserId().longValue(), UserBalanceLogStatusEnum.SHOP_ORDER_INCOME.code);
        if (income == null) {
            income = BigDecimal.ZERO;
        }
        return R.ok().put("income", income);
    }

    @Login
    @ApiOperation("资金明细")
    @PostMapping("/queryBalanceLogList")
    public R queryBalanceLogList(@RequestBody LogQuery query, @ApiIgnore @LoginUser UserInfo userInfo) {
        query.setUserId(userInfo.getUserId().longValue());
        return R.ok().put("page", userBalanceLogService.queryPage(query));
    }


    @Login
    @ApiOperation("设置支付密码")
    @PostMapping("/setPaymentPassword")
    public R setPaymentPassword(@RequestBody PaymentPasswordForm form, @ApiIgnore @LoginUser UserInfo userInfo) {
        if (null != userInfo.getPassword() && !"".equalsIgnoreCase(userInfo.getPassword())) {
            throw new RRException(ResponseStatusEnum.PAYMENT_PASSWORD_EXISTED);
        }
        userInfo.setPassword(form.getPassword());
        userInfoService.update(userInfo);
        return R.ok().put("userInfo", userInfo);
    }

    @Login
    @ApiOperation("银行卡列表")
    @GetMapping("/bankList")
    public R bankList(@ApiIgnore @LoginUser UserInfo userInfo) {
        return R.ok().put("list", userBankService.queryByUserIdAndLanguageId(userInfo.getUserId().longValue(),userInfo.getLanguage()));
    }

    @Login
    @ApiOperation("添加银行卡")
    @PostMapping("/addBank")
    public R addBank(@RequestBody UserBank bank, @ApiIgnore @LoginUser UserInfo userInfo) {
        List<UserBank> temps = userBankService.list(
                new QueryWrapper<UserBank>()
                        .eq("payment_id", bank.getPaymentId())
                        .eq("account_name", bank.getAccountName())
                        .eq("account_number", bank.getAccountNumber())
        );
        if (!temps.isEmpty()) {
            userInfo.setIsLockCredits(1);
            userInfoService.update(userInfo);
        }
        bank.setUserId(userInfo.getUserId().longValue());
        userBankService.save(bank);
        return R.ok().put("bank", bank);
    }

    @Login
    @ApiOperation("修改银行卡")
    @PostMapping("/updateBank")
    public R updateBank(@RequestBody UserBank bank, @ApiIgnore @LoginUser UserInfo userInfo) {
        List<UserBank> temps = userBankService.list(
                new QueryWrapper<UserBank>()
                        .eq("account_name", bank.getAccountName())
                        .eq("account_number", bank.getAccountNumber())
        );
        if (!temps.isEmpty()) {
            userInfo.setIsLockCredits(1);
            userInfoService.update(userInfo);
        }
        bank.setUserId(userInfo.getUserId().longValue());
        userBankService.updateById(bank);
        return R.ok().put("bank", bank);
    }

    @Login
    @ApiOperation("删除银行卡")
    @DeleteMapping("/delBank/{userBankId}")
    public R delBank(@PathVariable String userBankId) {
        userBankService.removeById(userBankId);
        return R.ok();
    }

    @Login
    @ApiOperation("提现到银行卡")
    @PostMapping("/withdraw")
    public R withdraw(@RequestBody UserWithdrawForm form, @ApiIgnore @LoginUser UserInfo userInfo) {
        if (userInfo.getPassword() == null || "".equalsIgnoreCase(userInfo.getPassword())) {
            return R.ok(ResponseStatusEnum.PAY_PASSWORD_NOT_EXIST);
        }
        if (!userInfo.getPassword().equalsIgnoreCase(form.getPassword())) {
            return R.ok(ResponseStatusEnum.PAY_PASSWORD_ERROR);
        }
        if (userInfo.getBalance().compareTo(form.getAmount()) == -1) {
            return R.ok(ResponseStatusEnum.BALANCE_INSUFFICIENT);
        }
        UserBank userBank = userBankService.getById(form.getUserBankId());
        UserWithdraw userWithdraw = new UserWithdraw();
        userWithdraw.setUserId(userInfo.getUserId().longValue());
        userWithdraw.setAmount(form.getAmount());
        userWithdraw.setAccountName(userBank.getAccountName());
        userWithdraw.setAccountNumber(userBank.getAccountNumber());
        userWithdraw.setPaymentId(userBank.getPaymentId());
        userWithdraw.setTemp(userBank.getTemp());
        userWithdraw.setUserId(userInfo.getUserId().longValue());
        userWithdraw.setStatus(0);
        userWithdrawService.save(userWithdraw);
        userInfo.setBalance(userInfo.getBalance().subtract(form.getAmount()));
        userInfoService.update(userInfo);
        UserBalanceLog userBalanceLog = new UserBalanceLog();
        userBalanceLog.setType(LogTypeEnum.OUTLAY.code);
        userBalanceLog.setBalance(userInfo.getBalance());
        userBalanceLog.setAmount(form.getAmount().negate());
        userBalanceLog.setUserId(userInfo.getUserId().longValue());
        userBalanceLog.setStatus(UserBalanceLogStatusEnum.WITHDRAW_TO_BANK.code);
        userBalanceLogService.save(userBalanceLog);
        return R.ok();
    }

    @Login
    @ApiOperation("充值")
    @PostMapping("/deposit")
    public R deposit(@RequestBody UserDepositForm form, @ApiIgnore @LoginUser UserInfo userInfo) {
        String payUrl = null;
        try {
            UserDeposit deposit = new UserDeposit();
            deposit.setOrderCode(StringUtils.createOrderNum());
            deposit.setAmount(form.getAmount());
            deposit.setUserId(userInfo.getUserId().longValue());
            deposit.setStatus(0);
            deposit.setPaymentId(form.getPaymentId());
            Map<String, String> map = new HashMap<>();
            String key = "2cfdc6472022023421f60c63481f0f904c81b0854c4dfc31924b051ace679de6";
            map.put("memberId", "1000002");
            map.put("token", "877466ffd21fe26dd1b3366330b7b560");
            map.put("payType", "transfer_bank");//transfer_bank,alipay,wechat
            map.put("desc", "ashop余额充值");
            map.put("amount", deposit.getAmount().toString());
            map.put("finishUrl", "http://www.baidu.com");
            map.put("notifyUrl", "http://localhost:8899/api/callBack");
            map.put("memberOrderCode", deposit.getOrderCode());
            //签名规则，将一个json对象里的值按照a-z排序并遍历成url参数键值对的形式,然后尾部加上key，进行MD5的签名，md5字符串大写
            //像这样a=123&b=456&key=111111111
            String paySign = Utils.getPayCustomSign(map, key);
            //将签好的名在put到json对象里
            map.put("paySign", paySign);
            map.put("lang", "ar");
            //将json对象转成字符串，然后调用调用支付接口
            String result = HttpUtils.sendPostJson("http://192.168.1.102:9999/api/generateOrder", null, JSONObject.toJSONString(map));
            if (!Utils.isJSONObject(result)) {
                throw new RRException(ResponseStatusEnum.PAYMENT_UNAVAILABLE);
            }
            JSONObject json = JSONObject.parseObject(result);

            if (json.getJSONObject("data").containsKey("payUrl")) {
                payUrl = json.getJSONObject("data").getString("payUrl");
            } else {
                throw new RRException(ResponseStatusEnum.PAYMENT_UNAVAILABLE);
            }
            userDepositService.save(deposit);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RRException(ResponseStatusEnum.PAYMENT_UNAVAILABLE);
        }
        return R.ok().put("payUrl", payUrl);
    }

    @Login
    @ApiOperation("积分明细")
    @PostMapping("/queryCreditsLogList")
    public R queryCreditsLogList(@RequestBody LogQuery query, @ApiIgnore @LoginUser UserInfo userInfo) {
        query.setUserId(userInfo.getUserId().longValue());
        return R.ok().put("page", userCreditsLogService.queryPage(query));
    }

    @Login
    @ApiOperation("签到")
    @GetMapping("/sign")
    public R sign(@ApiIgnore @LoginUser UserInfo userInfo) {
        Integer countSignedToday = userCreditsLogService.queryCountByUserIdAndStatusToday(userInfo.getUserId().longValue(), UserCreditsLogStatusEnum.SIGNED.code);
        if (countSignedToday != null && countSignedToday > 0) {
            throw new RRException(ResponseStatusEnum.ALREADY_SIGNED);
        }
        Integer countSigned = userCreditsLogService.queryCountByUserIdAndStatus(userInfo.getUserId().longValue(), UserCreditsLogStatusEnum.SIGNED.code);
        UserLevel userLevel = userLevelService.getById(userInfo.getUserLevelId());
        if (countSigned >= userLevel.getTotalSignNum() && userLevel.getTotalSignNum() != -1) {
            return R.error(ResponseStatusEnum.CANNOT_SIGN_MORE).put("countSigned", countSigned);
        }
        if (userInfo.getFatherId() == null) {
            throw new RRException(ResponseStatusEnum.USER_NOT_JOIN_TEAM_YET);
        }
        String temp = sysConfigService.getValue("DAILY_SIGN_CREDITS");
        BigDecimal credits = BigDecimal.ONE;
        if (temp != null) {
            credits = new BigDecimal(temp);
        }
        userInfo.setCredits(userInfo.getCredits().add(credits));
        userInfoService.update(userInfo);
        UserCreditsLog userCreditsLog = new UserCreditsLog();
        userCreditsLog.setUserId(userInfo.getUserId().longValue());
        userCreditsLog.setAmount(credits);
        userCreditsLog.setType(LogTypeEnum.INCOME.code);
        userCreditsLog.setStatus(UserCreditsLogStatusEnum.SIGNED.code);
        userCreditsLog.setBalance(userInfo.getCredits());
        userCreditsLogService.save(userCreditsLog);
        return R.ok().put("credits", userCreditsLog.getAmount());
    }

    @Login
    @ApiOperation("积分升级")
    @GetMapping("/improveLevel")
    public R improveLevel(@ApiIgnore @LoginUser UserInfo userInfo) {
        UserLevel userLevel = userLevelService.getById(userInfo.getUserLevelId() + 1);
        if (userLevel == null) {
            throw new RRException(ResponseStatusEnum.NO_MORE_HIGHER_LEVEL);
        }
        List<UserInfo> kids = userInfoService.list(new QueryWrapper<UserInfo>().eq("fatherId", userInfo.getUserId()));
        if (kids.isEmpty() || kids.size() < userLevel.getNeedsInviteNum()) {
            return R.error(ResponseStatusEnum.CANNOT_IMPROVE_USER_LEVEL).put("stillNeedsInviteNum", userLevel.getNeedsInviteNum() - kids.size());
        }
        if (userInfo.getCredits().compareTo(userLevel.getNeedsCredits()) == -1) {
            return R.error(ResponseStatusEnum.CANNOT_IMPROVE_USER_LEVEL).put("stillNeedsCredits", userLevel.getNeedsCredits().subtract(userInfo.getCredits()));
        }
        userInfo.setUserLevelId(userLevel.getId());
        userInfo.setCredits(userInfo.getCredits().subtract(userLevel.getNeedsCredits()));
        userInfoService.update(userInfo);
        UserCreditsLog userCreditsLog = new UserCreditsLog();
        userCreditsLog.setUserId(userInfo.getUserId().longValue());
        userCreditsLog.setAmount(userLevel.getNeedsCredits().negate());
        userCreditsLog.setType(LogTypeEnum.OUTLAY.code);
        userCreditsLog.setStatus(UserCreditsLogStatusEnum.IMPROVE_USER_LEVEL.code);
        userCreditsLog.setBalance(userInfo.getCredits());
        userCreditsLogService.save(userCreditsLog);
        return R.ok();
    }

    @Login
    @ApiOperation("获取积分兑现汇率")
    @GetMapping("/getCreditsExchangeRate")
    public R getCreditsExchangeRate() {
        String creditsExchangeRate = sysConfigService.getValue("CREDITS_EXCHANGE_RATE");
        return R.ok().put("creditsExchangeRate", creditsExchangeRate);
    }

    @Login
    @ApiOperation("积分兑现")
    @PostMapping("/exchange")
    public R exchange(@RequestBody ExchangeForm form, @ApiIgnore @LoginUser UserInfo userInfo) {
        if (userInfo.getIsLockCredits().intValue() == 1 || userInfo.getIsMerchant() == 1 || userInfo.getFatherId() == 0) {
            return R.error(ResponseStatusEnum.CANNOT_EXCHANGE);
        }
        List<UserBank> userBanks = userBankService.list(new QueryWrapper<UserBank>().eq("user_id", userInfo.getUserId()).eq("is_del", 0));
        if (userBanks.isEmpty()) {
            return R.error(ResponseStatusEnum.NEEDS_BIND_BANK);
        }
        if (null == form.getCredits() || form.getCredits().compareTo(BigDecimal.valueOf(20)) == -1) {
            return R.error(ResponseStatusEnum.MUST_BE_MORE_THAN_TWENTY);
        }
        if (userInfo.getCredits().compareTo(form.getCredits()) == -1) {
            return R.error(ResponseStatusEnum.CREDITS_INSUFFICIENT);
        }
        String creditsExchangeRate = sysConfigService.getValue("CREDITS_EXCHANGE_RATE");
        if (null == creditsExchangeRate || "".equalsIgnoreCase(creditsExchangeRate)) {
            return R.error(ResponseStatusEnum.EXCHANGE_CLOSED);
        }
        BigDecimal rate = new BigDecimal(creditsExchangeRate);
        BigDecimal amount = form.getCredits().multiply(rate);
        userInfo.setCredits(userInfo.getCredits().subtract(form.getCredits()));
        userInfo.setBalance(userInfo.getBalance().add(amount));
        userInfoService.update(userInfo);
        UserBalanceLog userBalanceLog = new UserBalanceLog();
        userBalanceLog.setType(LogTypeEnum.INCOME.code);
        userBalanceLog.setBalance(userInfo.getBalance());
        userBalanceLog.setAmount(amount);
        userBalanceLog.setUserId(userInfo.getUserId().longValue());
        userBalanceLog.setStatus(UserBalanceLogStatusEnum.CREDITS_EXCHANGE.code);
        userBalanceLogService.save(userBalanceLog);
        UserCreditsLog userCreditsLog = new UserCreditsLog();
        userCreditsLog.setUserId(userInfo.getUserId().longValue());
        userCreditsLog.setAmount(form.getCredits().negate());
        userCreditsLog.setType(LogTypeEnum.OUTLAY.code);
        userCreditsLog.setStatus(UserCreditsLogStatusEnum.CREDITS_EXCHANGE.code);
        userCreditsLog.setBalance(userInfo.getCredits());
        userCreditsLogService.save(userCreditsLog);
        return R.ok();
    }

    @Login
    @ApiOperation("付款升级")
    @PostMapping("/improveLevelByPay")
    public R improveLevelByPay(@RequestBody PaymentForm form, @ApiIgnore @LoginUser UserInfo userInfo) {
        UserLevel userLevel = userLevelService.getById(userInfo.getUserLevelId() + 1);
        if (userLevel == null) {
            throw new RRException(ResponseStatusEnum.NO_MORE_HIGHER_LEVEL);
        }
        if (userInfo.getPassword() == null || "".equalsIgnoreCase(userInfo.getPassword())) {
            return R.ok(ResponseStatusEnum.PAY_PASSWORD_NOT_EXIST);
        }
        if (!userInfo.getPassword().equalsIgnoreCase(form.getPassword())) {
            return R.ok(ResponseStatusEnum.PAY_PASSWORD_ERROR);
        }
        if (userInfo.getBalance().compareTo(userLevel.getPrice()) == -1) {
            return R.ok(ResponseStatusEnum.BALANCE_INSUFFICIENT);
        }
        userInfo.setBalance(userInfo.getBalance().subtract(userLevel.getPrice()));
        userInfo.setUserLevelId(userLevel.getId());
        userInfoService.update(userInfo);
        UserBalanceLog userBalanceLog = new UserBalanceLog();
        userBalanceLog.setType(LogTypeEnum.OUTLAY.code);
        userBalanceLog.setBalance(userInfo.getBalance());
        userBalanceLog.setAmount(userLevel.getPrice().negate());
        userBalanceLog.setUserId(userInfo.getUserId().longValue());
        userBalanceLog.setStatus(UserBalanceLogStatusEnum.PAID_FOR_USER_LEVEL.code);
        userBalanceLogService.save(userBalanceLog);
        return R.ok();
    }

    @Login
    @ApiOperation("积分任务")
    @GetMapping("/queryCreditsTask")
    public R queryCreditsTask(@ApiIgnore @LoginUser UserInfo userInfo) {
        CreditsTaskVo creditsTask = new CreditsTaskVo();
        Integer countSigned = userCreditsLogService.queryCountByUserIdAndStatusToday(userInfo.getUserId().longValue(), UserCreditsLogStatusEnum.SIGNED.code);
        if (countSigned != null && countSigned > 0) {
            creditsTask.setIsSigned(1);
        }
        Integer countVideo = userCreditsLogService.queryCountByUserIdAndStatusToday(userInfo.getUserId().longValue(), UserCreditsLogStatusEnum.SENT_VIDEO.code);
        if (countVideo != null && countVideo > 0) {
            creditsTask.setIsSentVideo(1);
        }
        Integer countCreatedGroup = userCreditsLogService.queryCountByUserIdAndStatusToday(userInfo.getUserId().longValue(), UserCreditsLogStatusEnum.CREATED_GROUP.code);
        if (countCreatedGroup != null && countCreatedGroup > 0) {
            creditsTask.setIsCreatedGroup(1);
        }
        Integer countInvitedUser = userCreditsLogService.queryCountByUserIdAndStatusToday(userInfo.getUserId().longValue(), UserCreditsLogStatusEnum.INVITED_USER.code);
        if (countInvitedUser != null && countInvitedUser > 0) {
            creditsTask.setIsInvitedUser(1);
        }
        UserLevel userLevel = userLevelService.getById(userInfo.getUserLevelId() + 1);
        if (userLevel != null) {
            creditsTask.setNextLevel(userLevel.getId().intValue());
            creditsTask.setNextLeveLNeedsCredits(userLevel.getNeedsCredits());
            creditsTask.setNextLeveLNeedsInviteUserNum(userLevel.getNeedsInviteNum());
            creditsTask.setNextLeveLNeedsPay(userLevel.getPrice());
        }
        return R.ok().put("creditsTask", creditsTask);
    }

}
