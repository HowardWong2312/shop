package io.dubai.modules.other.controller;

import com.cz.czUser.system.entity.UserInfo;
import io.dubai.common.easemob.api.IMessageService;
import io.dubai.common.utils.R;
import io.dubai.modules.other.entity.Msg;
import io.dubai.modules.user.service.UserInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
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
public class ApiController {

    @Resource
    private IMessageService messageService;

    @Resource
    private UserInfoService userInfoService;

    @ApiOperation("接受支付回调")
    @GetMapping("/callBack")
    public R callBack() {
        return R.ok();
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
