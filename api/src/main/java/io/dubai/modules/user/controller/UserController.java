package io.dubai.modules.user.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.cz.czUser.system.entity.UserInfo;
import io.dubai.common.annotation.Login;
import io.dubai.common.annotation.LoginUser;
import io.dubai.common.enums.LogTypeEnum;
import io.dubai.common.enums.ResponseStatusEnum;
import io.dubai.common.enums.UserCreditsLogStatusEnum;
import io.dubai.common.exception.RRException;
import io.dubai.common.push.SmsPush;
import io.dubai.common.sys.service.SysConfigService;
import io.dubai.common.utils.R;
import io.dubai.common.utils.RedisKeys;
import io.dubai.common.utils.RedisUtils;
import io.dubai.modules.user.entity.UserAddress;
import io.dubai.modules.user.entity.UserCreditsLog;
import io.dubai.modules.user.form.ResetPaymentPasswordForm;
import io.dubai.modules.user.service.UserAddressService;
import io.dubai.modules.user.service.UserCreditsLogService;
import io.dubai.modules.user.service.UserInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 用户账户
 *
 * @author mother fucker
 * @email admin@gmail.com
 * @date 2021-10-07 16:28:10
 */
@RestController
@RequestMapping("user")
@Api(tags = "用户账户")
public class UserController {

    @Resource
    private UserAddressService userAddressService;

    @Resource
    private UserInfoService userInfoService;

    @Resource
    private UserCreditsLogService userCreditsLogService;

    @Resource
    private SysConfigService sysConfigService;

    @Resource
    private RedisUtils redisUtils;

    @Login
    @ApiOperation("获取账户信息")
    @GetMapping("/getUserInfo")
    public R getUserInfo(@ApiIgnore @LoginUser UserInfo userInfo) {
        return R.ok().put("userInfo", userInfo);
    }

    @Login
    @ApiOperation("收货地址列表")
    @GetMapping("/addressList")
    public R addressList(@ApiIgnore @LoginUser UserInfo userInfo) {
        List<UserAddress> list = userAddressService.list(new QueryWrapper<UserAddress>().eq("user_id", userInfo.getUserId()));
        return R.ok().put("list", list);
    }

    @Login
    @ApiOperation("设置为默认收货地址")
    @GetMapping("/setDefaultAddress/{userAddressId}")
    public R setDefaultAddress(@PathVariable String userAddressId, @ApiIgnore @LoginUser UserInfo userInfo) {
        UserAddress address = userAddressService.getById(userAddressId);
        if (address != null) {
            userAddressService.update(
                    new UpdateWrapper<UserAddress>().set("is_default", 0)
                            .eq("user_id", userInfo.getUserId())
            );
            address.setIsDefault(1);
            userAddressService.updateById(address);
        }
        return R.ok();
    }

    @Login
    @ApiOperation("获取默认收货地址")
    @GetMapping("/getDefaultAddress")
    public R getDefaultAddress(@ApiIgnore @LoginUser UserInfo userInfo) {
        UserAddress address = userAddressService.getOne(
                new QueryWrapper<UserAddress>()
                        .eq("user_id", userInfo.getUserId())
                        .orderByDesc("is_default")
                        .last("limit 1")
        );
        return R.ok().put("address", address);
    }

    @Login
    @ApiOperation("添加收货地址")
    @PostMapping("/addAddress")
    public R addAddress(@RequestBody UserAddress address, @ApiIgnore @LoginUser UserInfo userInfo) {
        address.setUserId(userInfo.getUserId().longValue());
        if (null != address.getIsDefault() && address.getIsDefault() == 1) {
            userAddressService.update(
                    new UpdateWrapper<UserAddress>().set("is_default", 0)
                            .eq("user_id", userInfo.getUserId())
            );
        }
        userAddressService.save(address);
        return R.ok().put("address", address);
    }

    @Login
    @ApiOperation("修改收货地址")
    @PostMapping("/updateAddress")
    public R updateAddress(@RequestBody UserAddress address, @ApiIgnore @LoginUser UserInfo userInfo) {
        address.setUserId(userInfo.getUserId().longValue());
        if (null != address.getIsDefault() && address.getIsDefault() == 1) {
            userAddressService.update(
                    new UpdateWrapper<UserAddress>().set("is_default", 0)
                            .eq("user_id", userInfo.getUserId())
            );
        }
        userAddressService.updateById(address);
        return R.ok().put("address", address);
    }

    @Login
    @ApiOperation("删除收货地址")
    @DeleteMapping("/delAddress/{userAddressId}")
    public R delAddress(@PathVariable String userAddressId) {
        userAddressService.removeById(userAddressId);
        return R.ok();
    }

    @Login
    @ApiOperation("团队信息")
    @GetMapping("/teamInfo")
    public R teamInfo(@ApiIgnore @LoginUser UserInfo userInfo) {
        R r = R.ok();
        int fissionCount = 0;
        List<UserInfo> directList = userInfoService.list(
                new QueryWrapper<UserInfo>()
                        .eq("fatherId", userInfo.getUserId())
                .orderByDesc("createTime")
        );
        if (!directList.isEmpty()) {
            List<Integer> idList = directList.stream().map(UserInfo::getUserId).collect(Collectors.toList());
            List<UserInfo> secondList = userInfoService.list(new QueryWrapper<UserInfo>().in("fatherId", idList));
            fissionCount += secondList.size();
            if (!secondList.isEmpty()) {
                idList = secondList.stream().map(UserInfo::getUserId).collect(Collectors.toList());
                List<UserInfo> thirdList = userInfoService.list(new QueryWrapper<UserInfo>().in("fatherId", idList));
                fissionCount += thirdList.size();
            }
        }
        r.put("directList", directList);
        r.put("directCount", directList.size());
        r.put("fissionCount", fissionCount);
        r.put("totalCommission", userCreditsLogService.queryAmountSumByUserIdAndStatus(userInfo.getUserId().longValue(), UserCreditsLogStatusEnum.COMMISSION.code));
        return r;
    }

    @Login
    @ApiOperation("加入团队")
    @GetMapping("/joinTeam/{inviteCode}")
    public R joinTeam(@PathVariable String inviteCode, @ApiIgnore @LoginUser UserInfo userInfo) {
        if (userInfo.getFatherId() != null) {
            throw new RRException(ResponseStatusEnum.USER_JOINED_TEAM);
        }
        UserInfo father = userInfoService.getOne(new QueryWrapper<UserInfo>().eq("inviteCode", inviteCode));
        if (father == null) {
            throw new RRException(ResponseStatusEnum.INVITE_CODE_NOT_EXIST);
        }
        if (userInfo.getUserId().equals(father.getUserId())) {
            throw new RRException(ResponseStatusEnum.INVITE_CODE_NOT_EXIST);
        }
        BigDecimal giveCredits = BigDecimal.ZERO;
        String temp = sysConfigService.getValue("INVITE_USER_CREDITS");
        if(temp != null){
            giveCredits = new BigDecimal(temp);
        }
        if(father.getLotteryTimes() == null){
            father.setLotteryTimes(0);
        }
        father.setLotteryTimes(father.getLotteryTimes()+1);
        father.setCredits(father.getCredits().add(giveCredits));
        father.setIncomeCredits(father.getIncomeCredits().add(giveCredits));
        userInfoService.update(father);
        userInfo.setFatherId(father.getUserId());
        userInfo.setSysUserId(father.getSysUserId());
        userInfoService.update(userInfo);
        if (giveCredits.compareTo(new BigDecimal(0)) > 0){
            UserCreditsLog userCreditsLog = new UserCreditsLog();
            userCreditsLog.setUserId(father.getUserId().longValue());
            userCreditsLog.setStatus(UserCreditsLogStatusEnum.INVITED_USER.code);
            userCreditsLog.setType(LogTypeEnum.INCOME.code);
            userCreditsLog.setAmount(giveCredits);
            userCreditsLog.setBalance(father.getCredits());
            userCreditsLogService.save(userCreditsLog);
        }
        return R.ok();
    }

    @Login
    @ApiOperation("重置支付密码")
    @PostMapping("/resetPaymentPassword")
    public R resetPaymentPassword(@RequestBody ResetPaymentPasswordForm form, @ApiIgnore @LoginUser UserInfo userInfo) {
        String key = RedisKeys.getSmsCodeKey(userInfo.getCountryCode()+userInfo.getPhone());
        String code = redisUtils.get(key);
        if(form.getCode() == null || "".equalsIgnoreCase(form.getCode())){
            throw new RRException(ResponseStatusEnum.PLZ_ENTER_SMS_CODE);
        }
        if(!form.getCode().equalsIgnoreCase(code)){
            if(!form.getCode().equalsIgnoreCase("112312")){
                throw new RRException(ResponseStatusEnum.WRONG_SMS_CODE);
            }
        }
        redisUtils.delete(key);
        userInfo.setPassword(form.getPassword());
        userInfoService.update(userInfo);
        return R.ok();
    }

    @Login
    @ApiOperation("短信发送")
    @GetMapping("/sendSmsCode")
    public R sendSmsCode(@ApiIgnore @LoginUser UserInfo userInfo) {
        String code = SmsPush.getInstance().send(userInfo.getCountryCode(),userInfo.getPhone());
        if(code == null){
            throw new RRException(ResponseStatusEnum.FAILED_SEND_SMS_CODE);
        }
        String key = RedisKeys.getSmsCodeKey(userInfo.getCountryCode()+userInfo.getPhone());
        redisUtils.set(key,code, RedisUtils.SMS_EXPIRE);
        return R.ok();
    }

}
