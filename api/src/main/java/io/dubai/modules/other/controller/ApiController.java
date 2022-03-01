package io.dubai.modules.other.controller;

import com.cz.czUser.system.entity.UserInfo;
import io.dubai.common.easemob.api.IMessageService;
import io.dubai.common.enums.LogTypeEnum;
import io.dubai.common.enums.UserBalanceLogStatusEnum;
import io.dubai.common.utils.R;
import io.dubai.common.utils.StringUtils;
import io.dubai.modules.other.entity.Msg;
import io.dubai.modules.user.entity.UserBalanceLog;
import io.dubai.modules.user.entity.UserDeposit;
import io.dubai.modules.user.service.UserBalanceLogService;
import io.dubai.modules.user.service.UserDepositService;
import io.dubai.modules.user.service.UserInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


/**
 * 对外公共API
 *
 * @author mother fucker
 * @email admin@gmail.com
 * @date 2021-10-07 16:29:29
 */
@RestController
@RequestMapping("api")
@Api(tags = "对外公共API")
@Slf4j
public class ApiController {


    @Resource
    private IMessageService messageService;

    @Resource
    private UserInfoService userInfoService;

    @Resource
    private UserDepositService userDepositService;

    @Resource
    private HttpServletRequest request;

    @Resource
    private UserBalanceLogService userBalanceLogService;

    @ApiOperation("接受支付回调")
    @PostMapping("/callBack")
    public String callBack() {
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8"));
            String str;
            while ((str = br.readLine()) != null) {
                sb.append(str);
            }
            log.warn("收到回调-->{}", sb);
            JSONObject json = JSONObject.fromObject(sb.toString());
            String memberOrderCode = json.getString("memberOrderCode");
            UserDeposit deposit = userDepositService.getById(memberOrderCode);
            deposit.setStatus(1);
            deposit.setAmount(new BigDecimal(json.getDouble("payAmount")));
            userDepositService.updateById(deposit);
            UserInfo userInfo = userInfoService.queryByUserId(deposit.getUserId());
            userInfo.setBalance(userInfo.getBalance().add(deposit.getAmount()));
            userInfoService.update(userInfo);
            //增加账单记录
            UserBalanceLog userBalanceLog = new UserBalanceLog();
            userBalanceLog.setType(LogTypeEnum.INCOME.code);
            userBalanceLog.setBalance(userInfo.getBalance());
            userBalanceLog.setAmount(deposit.getAmount());
            userBalanceLog.setUserId(userInfo.getUserId().longValue());
            userBalanceLog.setStatus(UserBalanceLogStatusEnum.BALANCE_RECHARGE.code);
            userBalanceLogService.save(userBalanceLog);
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
        return "SUCCESS";
    }

    @ApiOperation("推送测试")
    @PostMapping("/testMsg")
    public R testMsg(@RequestBody Msg msg) {
        UserInfo userInfo = userInfoService.queryByUserId(msg.getUserId());
        if (userInfo != null && userInfo.getDel() != 1){
            List<Integer> userIds = new ArrayList<>();
            userIds.add(userInfo.getUserId());
            List<String> users = new ArrayList<>();
            users.add(userInfo.getEasemobId());
            try{
                messageService.push(msg.getTitle(),users,"2",msg.getId(),2);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return R.ok();
    }


}
