package io.dubai.common.interceptor;

import com.cz.czUser.system.entity.UserInfo;
import io.dubai.common.annotation.Login;
import io.dubai.common.enums.ResponseStatusEnum;
import io.dubai.common.exception.RRException;
import io.dubai.common.utils.RedisKeys;
import io.dubai.common.utils.RedisUtils;
import io.dubai.modules.user.service.UserInfoService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Calendar;
import java.util.HashSet;

/**
 * 权限(Token)验证
 *
 * @author mother fucker
 * @Date 2020/11/06
 */
@Component
public class AuthorizationInterceptor extends HandlerInterceptorAdapter {

    @Resource
    private RedisUtils redisUtils;

    @Resource
    private UserInfoService userInfoService;

    public static final String TOKEN_KEY = "Authorization";
    public static final String LANG = "lang";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String lang = request.getHeader(LANG);
        if (lang == null) {
            lang = "en";
        }
        request.setAttribute(LANG, lang);
        Login annotation;
        if (handler instanceof HandlerMethod) {
            annotation = ((HandlerMethod) handler).getMethodAnnotation(Login.class);
        } else {
            return true;
        }
        if (annotation == null) {
            return true;
        }

        //从header中获取token
        String token = request.getHeader(TOKEN_KEY);

        //如果header中不存在token，则从参数中获取token
//        if(StringUtils.isBlank(token)){
//            token = request.getParameter(USER_KEY);
//        }

        //token为空
        if (StringUtils.isBlank(token)) {
            throw new RRException(ResponseStatusEnum.NOT_AUTH_TOKEN);
        }

        //查询token信息
        if (null == redisUtils.get(RedisKeys.userInfoKey + token)) {
            throw new RRException(ResponseStatusEnum.AUTH_TOKEN_EXPIRED);
        }
        //设置token到request里
        request.setAttribute(TOKEN_KEY, token);

        return true;
    }


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
        String token = request.getHeader(TOKEN_KEY);
        if (StringUtils.isEmpty(token)) {
            return;
        }
        UserInfo userInfo = redisUtils.get(RedisKeys.userInfoKey + token, UserInfo.class);
        if (null == userInfo) {
            return;
        }
        if(userInfo.getIsLogged() == null || userInfo.getIsLogged() == 0){
            userInfo.setIsLogged(1);
            userInfoService.update(userInfo);
        }
        Calendar c = Calendar.getInstance();
        long now = c.getTimeInMillis();
        c.add(Calendar.DAY_OF_MONTH, 1);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        long millis = c.getTimeInMillis() - now;
        int leftDayTime = (int) millis / 1000;  //当前时间距离凌晨的秒

        HashSet userCount = redisUtils.get(RedisKeys.userOnlineKey, HashSet.class);
        if (userCount == null) {
            userCount = new HashSet();
        }
        userCount.add(userInfo.getUserId());
        redisUtils.set(RedisKeys.userOnlineKey, userCount, leftDayTime);
    }
}
