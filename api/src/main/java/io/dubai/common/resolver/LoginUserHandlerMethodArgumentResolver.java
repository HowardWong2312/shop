package io.dubai.common.resolver;

import com.cz.czUser.system.entity.UserInfo;
import io.dubai.common.annotation.LoginUser;
import io.dubai.common.interceptor.AuthorizationInterceptor;
import io.dubai.common.utils.RedisKeys;
import io.dubai.common.utils.RedisUtils;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.annotation.Resource;

/**
 * 有@LoginUser注解的方法参数，注入当前登录用户
 *
 * @author Howard
 */
@Component
public class LoginUserHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Resource
    private RedisUtils redisUtils;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().isAssignableFrom(UserInfo.class) && parameter.hasParameterAnnotation(LoginUser.class);
    }

    @Override
    public UserInfo resolveArgument(MethodParameter parameter, ModelAndViewContainer container, NativeWebRequest request, WebDataBinderFactory factory) {
        //获取token
        String token = request.getHeader(AuthorizationInterceptor.TOKEN_KEY);
        if (token == null) {
            return null;
        }
        return redisUtils.get(RedisKeys.userInfoKey+token, UserInfo.class);
    }
}
