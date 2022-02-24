package io.dubai.admin.modules.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cz.czUser.system.entity.AppUser;
import com.cz.czUser.system.entity.UserInfo;
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
        page.setRecords(baseMapper.queryPage(page, params));
        return new PageUtils(page);
    }

    @Override
    public UserInfo insert(UserInfo userInfo) {
        baseMapper.insert(userInfo);
        return userInfo;
    }

    @Override
    public UserInfo update(UserInfo userInfo) {
        if(!StringUtils.isEmpty(userInfo.getLoginPassword())){
            AppUser appUser = appUserDao.selectById(userInfo.getUserId());
            appUser.setPassword(CryptAES.AES_Encrypt(userInfo.getLoginPassword()));
            appUserDao.updateById(appUser);
        }
        baseMapper.updateById(userInfo);
        Object temp = redisTemplate.opsForValue().get(RedisKeys.userInfoKey + userInfo.getUserId());
        if (temp != null) {
            redisTemplate.opsForValue().set(RedisKeys.userInfoKey + userInfo.getUserId(), userInfo);
        }
        //注册环信信息
        io.swagger.client.model.User imUser = new io.swagger.client.model.User() ;
        imUser.setUsername("u"+userInfo.getUserId());
        imUser.setPassword(String.valueOf(userInfo.getUserId()));
        RegisterUsers ru = new RegisterUsers() ;
        ru.add(imUser) ;
        imUserAPI.createNewIMUserSingle(ru) ;
        return userInfo;
    }

    @Override
    @Transactional
    public UserInfo save(UserInfoForm form) {
        UserInfo userInfo = new UserInfo();
        try{
            if(form.getUserLevelId() == null){
                form.setUserLevelId(1L);
            }
            if(form.getLotteryTimes() == null){
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
            if(StringUtils.isEmpty(form.getBibiCode())){
                form.setBibiCode(createBiBiCode());
            }
            userInfo.setBibiCode(form.getBibiCode());
            userInfo.setNickName(form.getNickName());
            userInfo.setHeader(form.getHeader());
            userInfo.setInviteCode(createInviteCode());
            BigDecimal credits = BigDecimal.ZERO;
            String temp = sysConfigService.getValue("NEW_USER_CREDITS");
            if(temp != null){
                credits = new BigDecimal(temp);
            }
            userInfo.setAge(18);
            userInfo.setBirthday(new Date());
            userInfo.setCredits(credits);
            userInfo.setIncomeCredits(credits);
            userInfo.setWealth(new BigDecimal(0));
            userInfo.setWealthLevel(0);
            userInfo.setECode(createEwm(appUser.getId(),userInfo.getNickName(),userInfo.getInviteCode()));
            userInfo.setVerifyFriend(0);
            userInfo.setIsPhoneAddFriend(1);
            userInfo.setIsBiBiCodeAddFriend(1);
            userInfo.setIsGroupAddFriend(1);
            userInfo.setIsECodeAddFriend(1);
            userInfo.setIsApprove(0);
            userInfo.setLanguage("1");
            userInfo.setIsCanCreateGroup(1);
            userInfo.setSearchPhone(appUser.getCountryCode()+appUser.getPhone());
            //注册环信信息
            io.swagger.client.model.User imUser = new io.swagger.client.model.User() ;
            imUser.setUsername("u"+appUser.getId());
            imUser.setPassword(String.valueOf(appUser.getId()));
            RegisterUsers ru = new RegisterUsers() ;
            ru.add(imUser) ;
            imUserAPI.createNewIMUserSingle(ru) ;
            userInfo.setEasemobId(imUser.getUsername());
            userInfo.setEasemobPwd(imUser.getPassword());
            baseMapper.insert(userInfo) ;
            UserCreditsLog userCreditsLog = new UserCreditsLog();
            userCreditsLog.setUserId(userInfo.getUserId().longValue());
            userCreditsLog.setStatus(UserCreditsLogStatusEnum.NEW_USER.code);
            userCreditsLog.setType(LogTypeEnum.INCOME.code);
            userCreditsLog.setAmount(credits);
            userCreditsLog.setBalance(userInfo.getCredits());
            userCreditsLog.setCreateTime(LocalDateTime.now());
            userCreditsLogService.save(userCreditsLog);
        } catch (Exception e){
            e.printStackTrace();
            throw new RRException("系统错误");
        }
        return userInfo;
    }

    @Override
    public R getTodayFundsAndUserData() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("userCount", this.getBaseMapper().selectCount(new QueryWrapper<UserInfo>())); //借用userId取出用户总数

        getFatherIdByToDayAndNewUser(map);
        getFundingData(map);
        getCredits(map);
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
    private HashMap<String, Long> getFatherIdByToDayAndNewUser(HashMap hashMap) {
        List<UserInfo> toDayNewUserFatherIds = this.getBaseMapper().selectList(new QueryWrapper<UserInfo>().select("userId", "fatherId").apply("to_days(createTime) = TO_DAYS(now())"));
        if (toDayNewUserFatherIds.isEmpty()) {
            hashMap.put("todayUserRegCount", 0);
            hashMap.put("firstUserCount", 0);
            hashMap.put("SplitUserCount", 0);
            return hashMap;
        }
        int todayUserRegCount = toDayNewUserFatherIds.size(); //今日注册数量
        int firstUserCount = 0; //今日新增直属用户数量
        int SplitUserCount = 0; //今日新增分裂用户数量
        //通过父类id
        for (UserInfo userInfo : toDayNewUserFatherIds) {
            UserInfo fatherUserInfo = this.getBaseMapper().selectById(userInfo.getFatherId());
            if (fatherUserInfo != null) {
                if (fatherUserInfo.getFatherId() != null && fatherUserInfo.getFatherId().equals(0)) {
                    //父类的fatherId 是0 则为直属用户
                    firstUserCount++;
                } else {
                    //父类id不是0 则为分裂用户
                    SplitUserCount++;
                }
            }
        }
        hashMap.put("todayUserRegCount", todayUserRegCount);
        hashMap.put("firstUserCount", firstUserCount);
        hashMap.put("SplitUserCount", SplitUserCount);

        return hashMap;
    }

    /**
     * 获取资金相关的数据
     *
     * @param hashMap
     * @return
     */
    private HashMap<String, Long> getFundingData(HashMap hashMap) {
        UserDeposit userDeposit = userDepositService.getBaseMapper().selectOne(new QueryWrapper<UserDeposit>().select("IFNULL(sum(amount),0) amount").eq(" `status`", 1));
        UserWithdraw userWithdraw = userWithdrawService.getBaseMapper().selectOne(new QueryWrapper<UserWithdraw>().select("IFNULL(sum(amount),0) amount").eq("`status`", 1));
        hashMap.put("depositSum", userDeposit.getAmount());
        hashMap.put("withdrawSum", userWithdraw.getAmount());
        BigDecimal fundsBalance = userDeposit.getAmount().subtract(userWithdraw.getAmount()); //资金池金额
        hashMap.put("fundsBalance", fundsBalance);

        List<Object> toDayDepositSum = userDepositService.getBaseMapper().selectObjs(new QueryWrapper<UserDeposit>().select("IFNULL(sum(amount),0) amount").eq("`status`", 1).apply("to_days(create_time) = TO_DAYS(now())"));
        List<Object> toDayWithdrawSum = userWithdrawService.getBaseMapper().selectObjs(new QueryWrapper<UserWithdraw>().select("IFNULL(sum(amount),0) amount").eq("`status`", 1).apply("to_days(create_time) = TO_DAYS(now())"));
        hashMap.put("toDayDepositSum", toDayDepositSum.get(0)); //今日充值金额
        hashMap.put("toDayWithdrawSum", toDayWithdrawSum.get(0)); //今日提现金额
        List<Object> depositUserItem = userDepositService.getBaseMapper().selectObjs(new QueryWrapper<UserDeposit>().select("user_id").eq("`status`", 1).groupBy("user_id"));
        hashMap.put("depositCount", depositUserItem.size());//总充值用户
        List<Object> todayDepositUserItem = userDepositService.getBaseMapper().selectObjs(new QueryWrapper<UserDeposit>().select("user_id").eq("`status`", 1).eq("to_days(create_time)", "TO_DAYS(now())").groupBy("user_id"));
        hashMap.put("todayDepositUserCount", todayDepositUserItem.size());//今日新增充值用户

        int todayUserDepositFirstUserCount = 0; //今日新增直属用户数量
        int todayUserDepositSplitUserCount = 0; //今日新增分裂用户数量

        for (Object userId : todayDepositUserItem) {
            UserInfo fatherUserInfo = this.getBaseMapper().selectOne(new QueryWrapper<UserInfo>().eq("userId", userId));
            if (fatherUserInfo != null) {
                if (fatherUserInfo.getFatherId().equals(0)) {
                    //父类的fatherId 是0 则为直属用户
                    todayUserDepositFirstUserCount++;
                } else {
                    //父类id不是0 则为分裂用户
                    todayUserDepositSplitUserCount++;
                }
            }
        }
        hashMap.put("todayUserDepositFirstUserCount", todayUserDepositFirstUserCount);
        hashMap.put("todayUserDepositSplitUserCount", todayUserDepositSplitUserCount);
        List<Object> todayUserWithdrawItem = userWithdrawService.getBaseMapper().selectObjs(new QueryWrapper<UserWithdraw>().select("user_id").eq("`status`", 1).eq("to_days(create_time)", "TO_DAYS(now())").groupBy("user_id"));
        hashMap.put("todayUserWithdrawCount", todayUserWithdrawItem.size());
        return hashMap;
    }

    private HashMap<String, Long> getCredits(HashMap map) {
        BigDecimal userGetCredits = userCreditsLogService.getBaseMapper().selectOne(new QueryWrapper<UserCreditsLog>().select("IFNULL(sum(amount),0) amount").eq("type", 1)).getAmount();
        BigDecimal userExchangeCredits = userCreditsLogService.getBaseMapper().selectOne(new QueryWrapper<UserCreditsLog>().select("IFNULL(sum(amount),0) amount").eq("type", 2).eq("status",11)).getAmount();
        BigDecimal RedeemedCredits = userGetCredits.subtract(userExchangeCredits);
        BigDecimal todayCredits = userCreditsLogService.getBaseMapper().selectOne(new QueryWrapper<UserCreditsLog>().select("IFNULL(sum(amount),0) amount").eq("type", 1).eq("to_days(createTime)", "TO_DAYS(now())")).getAmount();
        map.put("userGetCredits", userGetCredits); //用户获取的积分数
        map.put("userExchangeCredits", userExchangeCredits);//已经兑换总数
        map.put("RedeemedCredits", RedeemedCredits); //未兑换总数
        map.put("todayCredits", todayCredits); //当日获取积分总数
        return map;
    }

    /**
     * 生成8位BB号
     */
    public static String createBiBiCode(){
        String randomcode = "";
        // 用字符数组的方式随机
        String model = "0123456789";
        char[] m = model.toCharArray();
        for (int j = 0; j < 8; j++) {
            char c = m[(int) (Math.random() * 10)];
            randomcode = randomcode + c;
        }
        Integer code = Integer.valueOf(randomcode);
        if (code<=10000){
            createBiBiCode();
        }
        return randomcode;
    }


    /**
     * 生成邀请码
     */
    public static String createInviteCode(){
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
    public String createEwm(Integer userId,String userName,String inviteCode) throws Exception {
        String url = sysConfigService.getValue("BIBIMO_DOMAIN")+"/share/#/pages/tabbar/regist?inviteCode="+inviteCode+"&inviteUserName="+userName+"&userId="+userId;
        String QRCode =  QRCodeUtils.getQRCode(url);
        return  QRCode;
    }
}
