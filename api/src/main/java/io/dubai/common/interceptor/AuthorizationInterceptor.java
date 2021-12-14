package io.dubai.common.interceptor;

import io.dubai.common.annotation.Login;
import io.dubai.common.enums.ResponseStatusEnum;
import io.dubai.common.exception.RRException;
import io.dubai.common.utils.RedisKeys;
import io.dubai.common.utils.RedisUtils;
import io.dubai.modules.user.service.UserInfoService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 权限(Token)验证
 *
 * @author Howard
 * @Date 2020/11/06
 */
@Component
public class AuthorizationInterceptor extends HandlerInterceptorAdapter {

    @Resource
    private RedisUtils redisUtils;

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
        if (null == redisUtils.get(RedisKeys.userInfoKey+token)) {
            throw new RRException(ResponseStatusEnum.AUTH_TOKEN_EXPIRED);
        }
        //设置token到request里
        request.setAttribute(TOKEN_KEY, token);

        return true;
    }
}
