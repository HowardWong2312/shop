package io.dubai.admin.modules.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cz.czUser.system.entity.AppUser;
import com.cz.czUser.system.entity.UserInfo;
import io.dubai.admin.modules.sys.shiro.ShiroUtils;
import io.dubai.admin.modules.user.dao.AppUserDao;
import io.dubai.admin.modules.user.dao.UserInfoDao;
import io.dubai.admin.modules.user.entity.UserCreditsLog;
import io.dubai.admin.modules.user.entity.UserDeposit;
import io.dubai.admin.modules.user.entity.UserWithdraw;
import io.dubai.admin.modules.user.form.UserInfoForm;
import io.dubai.admin.modules.user.service.*;
import io.dubai.common.easemob.api.IMUserAPI;
import io.dubai.common.enums.LogTypeEnum;
import io.dubai.common.enums.UserCreditsLogStatusEnum;
import io.dubai.common.exception.RRException;
import io.dubai.common.sys.service.SysConfigService;
import io.dubai.common.utils.*;
import io.swagger.client.model.RegisterUsers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Service("userInfoService")
public class UserInfoServiceImpl extends ServiceImpl<UserInfoDao, UserInfo> implements UserInfoService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Resource
    private UserDepositService userDepositService;

    @Resource
    private UserWithdrawService userWithdrawService;

    @Resource
    private UserCreditsLogService userCreditsLogService;

    @Resource
    private UserBalanceLogService userBalanceLogService;

    @Resource
    private SysConfigService sysConfigService;

    @Resource
    private AppUserDao appUserDao;

    @Resource
    private IMUserAPI imUserAPI;

    @Resource
    private RedisUtils redisUtils;


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<UserInfo> page = new Query<UserInfo>().getPage(params);
        List<UserInfo> list = baseMapper.queryPage(page, params);
        list.forEach(s -> {
            //查直属和裂变用户人数
            int fissionCount = 0;
            List<UserInfo> directList = baseMapper.selectList(new QueryWrapper<UserInfo>().eq("fatherId", s.getUserId()));
            if (!directList.isEmpty()) {
                List<Integer> idList = directList.stream().map(UserInfo::getUserId).collect(Collectors.toList());
                List<UserInfo> secondList = baseMapper.selectList(new QueryWrapper<UserInfo>().in("fatherId", idList));
                fissionCount += secondList.size();
                if (!secondList.isEmpty()) {
                    idList = secondList.stream().map(UserInfo::getUserId).collect(Collectors.toList());
                    List<UserInfo> thirdList = baseMapper.selectList(new QueryWrapper<UserInfo>().in("fatherId", idList));
                    fissionCount += thirdList.size();
                }
            }
            //查电商收入
            Map<String, Object> map = new HashMap<>();
            map.put("type", 1);
            map.put("status", 5);
            map.put("userId", s.getUserId());
            BigDecimal amount = userBalanceLogService.queryAmountSum(map);
            Integer depositUserNumTotal = userBalanceLogService.queryDepositUserNumByKey(s.getUserId(), false);
            Integer depositUserNumCurMonth = userBalanceLogService.queryDepositUserNumByKey(s.getUserId(), true);
            if (amount == null) {
                amount = BigDecimal.ZERO;
            }
            s.setDepositUserNumTotal(depositUserNumTotal);
            s.setDepositUserNumCurMonth(depositUserNumCurMonth);
            s.setMerchantIncomeTotal(amount);
            s.setDirectCount(directList.size());
            s.setFissionCount(fissionCount);
        });
        page.setRecords(list);
        return new PageUtils(page);
    }

    @Override
    public UserInfo update(UserInfo userInfo) {
        AppUser appUser = appUserDao.selectById(userInfo.getUserId());
        List<AppUser> tempAppUserList = appUserDao.selectList(
                new QueryWrapper<AppUser>()
                        .eq("countryCode", userInfo.getCountryCode())
                        .eq("phone", userInfo.getPhone())
                        .ne("id", userInfo.getUserId())
        );
        if (!tempAppUserList.isEmpty()) {
            throw new RRException("手机号重复");
        }
        List<UserInfo> tempUserInfoList = baseMapper.selectList(
                new QueryWrapper<UserInfo>()
                        .eq("countryCode", userInfo.getCountryCode())
                        .eq("phone", userInfo.getPhone())
                        .ne("userId", userInfo.getUserId())
        );
        if (!tempUserInfoList.isEmpty()) {
            throw new RRException("手机号重复");
        }
        tempUserInfoList = baseMapper.selectList(
                new QueryWrapper<UserInfo>()
                        .eq("bibiCode", userInfo.getBibiCode())
                        .ne("userId", userInfo.getUserId())
        );
        if (!tempUserInfoList.isEmpty()) {
            throw new RRException("bibi号重复");
        }
        appUser.setCountryCode(userInfo.getCountryCode());
        appUser.setPhone(userInfo.getPhone());
        if (!StringUtils.isEmpty(userInfo.getLoginPassword())) {
            appUser.setPassword(CryptAES.AES_Encrypt(userInfo.getLoginPassword()));
        }
        appUserDao.updateById(appUser);
        try {
            userInfo.setECode(createEwm(userInfo.getUserId(), userInfo.getNickName(), userInfo.getInviteCode()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        baseMapper.updateById(userInfo);
        Object temp = redisTemplate.opsForValue().get(RedisKeys.userInfoKey + userInfo.getUserId());
        if (temp != null) {
            redisTemplate.opsForValue().set(RedisKeys.userInfoKey + userInfo.getUserId(), userInfo);
        }
        return userInfo;
    }

    @Override
    @Transactional
    public UserInfo save(UserInfoForm form) {
        UserInfo userInfo = new UserInfo();
        try {
            List<AppUser> tempAppUserList = appUserDao.selectList(
                    new QueryWrapper<AppUser>()
                            .eq("countryCode", form.getCountryCode())
                            .eq("phone", form.getPhone())
            );
            if (!tempAppUserList.isEmpty()) {
                throw new RRException("手机号重复");
            }
            List<UserInfo> tempUserInfoList = baseMapper.selectList(
                    new QueryWrapper<UserInfo>()
                            .eq("countryCode", form.getCountryCode())
                            .eq("phone", form.getPhone())
            );
            if (!tempUserInfoList.isEmpty()) {
                throw new RRException("手机号重复");
            }
            if (StringUtils.isEmpty(form.getBibiCode())) {
                form.setBibiCode(createBiBiCode());
            }
            tempUserInfoList = baseMapper.selectList(
                    new QueryWrapper<UserInfo>()
                            .eq("bibiCode", form.getBibiCode())
            );
            if (!tempUserInfoList.isEmpty()) {
                throw new RRException("bibi号重复");
            }
            if (form.getUserLevelId() == null) {
                form.setUserLevelId(1L);
            }
            if (form.getLotteryTimes() == null) {
                form.setLotteryTimes(0);
            }
            AppUser appUser = new AppUser();
            appUser.setCountryCode(form.getCountryCode());
            appUser.setPhone(form.getPhone());
            appUser.setPassword(CryptAES.AES_Encrypt(form.getLoginPassword()));
            appUserDao.insert(appUser);
            userInfo.setSex("女性");
            userInfo.setFatherId(form.getFatherId());
            userInfo.setSysUserId(form.getSysUserId());
            userInfo.setUserId(appUser.getId());
            userInfo.setPhone(appUser.getPhone()); //手机号
            userInfo.setCountryCode(appUser.getCountryCode());
            userInfo.setCreateTime(Timestamp.from(Instant.now()));  //注册时间
            userInfo.setDel(0);
            userInfo.setSex("");
            userInfo.setUserLevelId(form.getUserLevelId());
            userInfo.setBalance(BigDecimal.ZERO);
            userInfo.setIsMerchant(form.getIsMerchant());
            userInfo.setLotteryTimes(form.getLotteryTimes());
            userInfo.setIsLockCredits(0);
            userInfo.setBibiCode(form.getBibiCode());
            userInfo.setNickName(form.getNickName());
            userInfo.setHeader(form.getHeader());
            userInfo.setInviteCode(createInviteCode());
            BigDecimal credits = BigDecimal.ZERO;
            String temp = sysConfigService.getValue("NEW_USER_CREDITS");
            if (temp != null) {
                credits = new BigDecimal(temp);
            }
            userInfo.setAge(18);
            userInfo.setBirthday(new Date());
            userInfo.setCredits(credits);
            userInfo.setIncomeCredits(credits);
            userInfo.setWealth(new BigDecimal(0));
            userInfo.setWealthLevel(0);
            userInfo.setECode(createEwm(appUser.getId(), userInfo.getNickName(), userInfo.getInviteCode()));
            userInfo.setVerifyFriend(0);
            userInfo.setIsPhoneAddFriend(1);
            userInfo.setIsBiBiCodeAddFriend(1);
            userInfo.setIsGroupAddFriend(1);
            userInfo.setIsECodeAddFriend(1);
            userInfo.setIsApprove(0);
            userInfo.setLanguage("5");
            userInfo.setIsCanCreateGroup(1);
            userInfo.setSearchPhone(appUser.getCountryCode() + appUser.getPhone());
            //注册环信信息
            io.swagger.client.model.User imUser = new io.swagger.client.model.User();
            imUser.setUsername("u" + appUser.getId());
            imUser.setPassword(String.valueOf(appUser.getId()));
            RegisterUsers ru = new RegisterUsers();
            ru.add(imUser);
            imUserAPI.createNewIMUserSingle(ru);
            userInfo.setEasemobId(imUser.getUsername());
            userInfo.setEasemobPwd(imUser.getPassword());
            baseMapper.insert(userInfo);
            UserCreditsLog userCreditsLog = new UserCreditsLog();
            userCreditsLog.setUserId(userInfo.getUserId().longValue());
            userCreditsLog.setStatus(UserCreditsLogStatusEnum.NEW_USER.code);
            userCreditsLog.setType(LogTypeEnum.INCOME.code);
            userCreditsLog.setAmount(credits);
            userCreditsLog.setBalance(userInfo.getCredits());
            userCreditsLog.setCreateTime(LocalDateTime.now());
            userCreditsLogService.save(userCreditsLog);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RRException("系统错误");
        }
        return userInfo;
    }

    @Override
    public R getTodayFundsAndUserData(Long sysUserId) {
        HashMap<String, Object> map = new HashMap<>();
        QueryWrapper<UserInfo> userInfoQueryWrapper = new QueryWrapper<>();
        map.put("show", sysUserId == Constant.SUPER_ADMIN);
        if (sysUserId != Constant.SUPER_ADMIN) {
            userInfoQueryWrapper.eq("sysUserId", sysUserId);
        }
        List<UserInfo> userInfos = this.getBaseMapper().selectList(userInfoQueryWrapper);//借用userId取出用户总数
        if (userInfos == null) {
            userInfos = new ArrayList<>();
        }
        ArrayList<Integer> userIds = new ArrayList<>();
        if (sysUserId != Constant.SUPER_ADMIN) {
//            如果不是系统用户就根据查处的用户id为基础查询
            for (UserInfo u : userInfos) {
                userIds.add(u.getUserId());
            }
        }
        map.put("userCount", userInfos.size());
        getFatherIdByToDayAndNewUser(map, userInfoQueryWrapper, sysUserId);
        getFundingData(map, userIds);
        getCredits(map, userIds);
        HashSet userOnlineCount = redisUtils.get(RedisKeys.userOnlineKey, HashSet.class);
        if (userOnlineCount == null) {
            userOnlineCount = new HashSet();
        }
        map.put("userOnlineCount", userOnlineCount.size());
        return R.ok().put("data", map);
    }

    /**
     * 获取当日的新增用户
     *
     * @return
     */
    private HashMap<String, Long> getFatherIdByToDayAndNewUser(HashMap hashMap, QueryWrapper<UserInfo> userInfoQueryWrapper, Long sysUserId) {
        userInfoQueryWrapper.select("userId", "fatherId").apply("to_days(createTime) = TO_DAYS(now())");
        QueryWrapper<UserInfo> salesUserQuery = new QueryWrapper<UserInfo>().select("userId");
        if (sysUserId == Constant.SUPER_ADMIN) {
            salesUserQuery.eq("fatherId", 0);
        } else {
            salesUserQuery.eq("userId", sysUserId);
        }
        List<UserInfo> toDayNewUserFatherIds = this.getBaseMapper().selectList(userInfoQueryWrapper);
        if (toDayNewUserFatherIds.isEmpty()) {
            hashMap.put("todayUserRegCount", 0);
            hashMap.put("firstUserCount", 0);
            hashMap.put("SplitUserCount", 0);
            return hashMap;
        }
        int todayUserRegCount = toDayNewUserFatherIds.size(); //今日注册数量
        int toDaySalesRegCount = 0; //今日新增直属用户数量

        //直属业务员
        List<Object> salesUserIds = this.getBaseMapper().selectObjs(salesUserQuery);

        //查询归属业务员底下的今日新增直属用户的总数
        if (!salesUserIds.isEmpty()) {
            toDaySalesRegCount += this.getBaseMapper().selectCount(new QueryWrapper<UserInfo>().in("fatherId", salesUserIds).apply("to_days(createTime) = TO_DAYS(now())"));
        }

        List<UserCreditsLog> userCreditsLog = userCreditsLogService.list(
                new QueryWrapper<UserCreditsLog>()
                        .eq("status", UserCreditsLogStatusEnum.SIGNED.code)
                        .apply("to_days(createTime) = TO_DAYS(now())")
                        .groupBy("userId")
        );
        hashMap.put("todayUserRegCount", todayUserRegCount);
        hashMap.put("firstUserCount", toDaySalesRegCount);
        hashMap.put("SplitUserCount", todayUserRegCount - toDaySalesRegCount); //今日总注册 - 今日直属 = 今日分裂用户
        hashMap.put("signedUserCountToday", userCreditsLog.size());

        return hashMap;
    }

    /**
     * 获取资金相关的数据
     *
     * @param hashMap
     * @return
     */
    private HashMap<String, Long> getFundingData(HashMap hashMap, ArrayList<Integer> userIds) {
        QueryWrapper<UserDeposit> depositSumQuery = new QueryWrapper<UserDeposit>().select("IFNULL(sum(amount),0) amount").eq(" `status`", 1);
        QueryWrapper<UserDeposit> depositSumQueryForMonth = new QueryWrapper<UserDeposit>().select("IFNULL(sum(amount),0) amount").eq(" `status`", 1);
        QueryWrapper<UserWithdraw> withdrawSumQuery = new QueryWrapper<UserWithdraw>().select("IFNULL(sum(amount),0) amount").eq("`status`", 1);
        QueryWrapper<UserDeposit> userDepositQueryWrapper = new QueryWrapper<UserDeposit>().select("user_id").eq("`status`", 1);
        QueryWrapper<UserWithdraw> userWithdrawQueryWrapper = new QueryWrapper<UserWithdraw>().select("user_id").eq("`status`", 1).eq("to_days(create_time)", "TO_DAYS(now())");
        if (!userIds.isEmpty()) {
            depositSumQuery.in("user_id", userIds);
            withdrawSumQuery.in("user_id", userIds);
            depositSumQueryForMonth.in("user_id", userIds);
            userDepositQueryWrapper.in("user_id", userIds).groupBy("user_id");
            userWithdrawQueryWrapper.in("user_id", userIds).groupBy("user_id");
        }
        UserDeposit userDeposit = userDepositService.getBaseMapper().selectOne(depositSumQuery);
        UserWithdraw userWithdraw = userWithdrawService.getBaseMapper().selectOne(withdrawSumQuery);
        hashMap.put("depositSum", userDeposit.getAmount());
        hashMap.put("withdrawSum", userWithdraw.getAmount());
        BigDecimal fundsBalance = userDeposit.getAmount().subtract(userWithdraw.getAmount()); //资金池金额
        hashMap.put("fundsBalance", fundsBalance);
        depositSumQuery.apply("to_days(create_time) = TO_DAYS(now())");
        withdrawSumQuery.apply("to_days(create_time) = TO_DAYS(now())");
        depositSumQueryForMonth.apply(" DATE_FORMAT(NOW(), '%Y%m') = DATE_FORMAT(create_time, '%Y%m')");

        List<Object> toDayDepositSum = userDepositService.getBaseMapper().selectObjs(depositSumQuery);
        List<Object> theMonthDepositSum = userDepositService.getBaseMapper().selectObjs(depositSumQueryForMonth);
        List<Object> toDayWithdrawSum = userWithdrawService.getBaseMapper().selectObjs(withdrawSumQuery);
        hashMap.put("toDayDepositSum", toDayDepositSum.get(0)); //今日充值金额
        hashMap.put("theMonthDepositSum", theMonthDepositSum.get(0)); //当月充值金额
        hashMap.put("toDayWithdrawSum", toDayWithdrawSum.get(0)); //今日提现金额
        List<Object> depositUserItem = userDepositService.getBaseMapper().selectObjs(userDepositQueryWrapper);
        hashMap.put("depositCount", depositUserItem.size());//总充值用户


        userDepositQueryWrapper.apply("to_days(create_time) = TO_DAYS(now())");
        List<Object> todayDepositUserItem = userDepositService.getBaseMapper().selectObjs(userDepositQueryWrapper);
        hashMap.put("todayDepositUserCount", todayDepositUserItem.size());//今日新增充值用户

        int todayUserDepositFirstUserCount = 0; //今日新增直属用户数量
        int todayUserDepositSplitUserCount = 0; //今日新增分裂用户数量
        int todayUserDepositOtherUserCount = 0; //其他用户

        for (Object userId : todayDepositUserItem) {
            UserInfo userItemInfo = this.getBaseMapper().selectOne(new QueryWrapper<UserInfo>().eq("userId", userId));
            if (userItemInfo != null) {
                //排除==0的业务员充值
                if (userItemInfo.getFatherId() != null && !userItemInfo.getFatherId().equals(0)) {
                    UserInfo fatherUserInfo = this.getBaseMapper().selectOne(new QueryWrapper<UserInfo>().eq("userId", userItemInfo.getFatherId()));
                    //如果上级fatherId == 0
                    if (fatherUserInfo.getFatherId() != null && fatherUserInfo.getFatherId().equals(0)) {
                        //父类的fatherId 是0 则为直属用户
                        todayUserDepositFirstUserCount++;
                    } else {
                        //父类id不是0 则为分裂用户
                        todayUserDepositSplitUserCount++;
                    }
                } else {
                    //没有上级的用户或者业务员充值
                    todayUserDepositOtherUserCount++;
                }
            }
        }
        hashMap.put("todayUserDepositFirstUserCount", todayUserDepositFirstUserCount);
        hashMap.put("todayUserDepositSplitUserCount", todayUserDepositSplitUserCount);
        hashMap.put("todayUserDepositOtherUserCount", todayUserDepositOtherUserCount);
        List<Object> todayUserWithdrawItem = userWithdrawService.getBaseMapper().selectObjs(userWithdrawQueryWrapper);
        hashMap.put("todayUserWithdrawCount", todayUserWithdrawItem.size());
        return hashMap;
    }

    private HashMap<String, Long> getCredits(HashMap map, ArrayList<Integer> userIds) {

        QueryWrapper<UserCreditsLog> userCreditsLogQueryWrapper = new QueryWrapper<UserCreditsLog>().select("IFNULL(sum(amount),0) amount").eq("type", 1).in("status", 1, 2, 3, 4, 7, 8, 9, 10, 14);
        QueryWrapper<UserCreditsLog> userCreditsLogQueryWrapper2 = new QueryWrapper<UserCreditsLog>().select("IFNULL(sum(amount),0) amount");
        if (!userIds.isEmpty()) {
            userCreditsLogQueryWrapper.in("userId", userIds);
            userCreditsLogQueryWrapper2.in("userId", userIds);
        }
        BigDecimal userGetCredits = userCreditsLogService.getBaseMapper().selectOne(userCreditsLogQueryWrapper).getAmount();
        BigDecimal sysOutlay = userCreditsLogService.getBaseMapper().selectOne(userCreditsLogQueryWrapper2).getAmount();
        BigDecimal remainingCredits = BigDecimal.valueOf(Long.valueOf(sysConfigService.getValue("CREDITS_TOTAL"))).subtract(sysOutlay);//不在系统内的积分，就是还未兑换的积分总数

        userCreditsLogQueryWrapper.eq("to_days(createTime)", "TO_DAYS(now())");
        BigDecimal todayCredits = userCreditsLogService.getBaseMapper().selectOne(userCreditsLogQueryWrapper).getAmount();
        map.put("userGetCredits", userGetCredits); //用户自行获得的积分数
        map.put("sysOutlay", sysOutlay); //系统流出积分总数
        map.put("remainingCredits", remainingCredits); //未兑换总数
        map.put("todayCredits", todayCredits); //当日获取积分总数
        return map;
    }

    /**
     * 生成8位BB号
     */
    public static String createBiBiCode() {
        String randomcode = "";
        // 用字符数组的方式随机
        String model = "0123456789";
        char[] m = model.toCharArray();
        for (int j = 0; j < 8; j++) {
            char c = m[(int) (Math.random() * 10)];
            randomcode = randomcode + c;
        }
        Integer code = Integer.valueOf(randomcode);
        if (code <= 10000) {
            createBiBiCode();
        }
        return randomcode;
    }


    /**
     * 生成邀请码
     */
    public static String createInviteCode() {
        String randomcode = "";
        // 用字符数组的方式随机
        String model = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        char[] m = model.toCharArray();
        for (int j = 0; j < 6; j++) {
            char c = m[(int) (Math.random() * 36)];
            // 保证六位随机数之间没有重复的
            if (randomcode.contains(String.valueOf(c))) {
                j--;
                continue;
            }
            randomcode = randomcode + c;
        }
        return randomcode;
    }

    /**
     * 生成邀请码
     */
    public String createEwm(Integer userId, String userName, String inviteCode) throws Exception {
        String url = sysConfigService.getValue("BIBIMO_DOMAIN") + "/share/#/pages/tabbar/regist?inviteCode=" + inviteCode + "&inviteUserName=" + userName + "&userId=" + userId;
        String QRCode = QRCodeUtils.getQRCode(url);
        return QRCode;
    }
}
