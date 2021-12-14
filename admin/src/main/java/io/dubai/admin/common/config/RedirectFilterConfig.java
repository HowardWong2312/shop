package io.dubai.admin.common.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * 功能描述: <br>
 * 解决shiro在https失效跳转http问题
 * @since: 1.0.0
 * @Author:Created By KURO
 * @Date: 2020/02/23 16:10
 */
@Component
@ConditionalOnProperty(name = "project.enable-https", havingValue = "true") // 开启注解才会启动
public class RedirectFilterConfig {

    @Bean
    public FilterRegistrationBean registFilter() {

        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new AbsoluteSendRedirectFilter());
        registration.addUrlPatterns("*");
        registration.setName("filterRegistrationBean");
        registration.setOrder(1);
        return registration;
    }

}