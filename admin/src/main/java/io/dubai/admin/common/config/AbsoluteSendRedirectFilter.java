package io.dubai.admin.common.config;

/**
 * 功能描述: <br>
 *
 * @since: 1.0.0
 * @Author:Created By KURO
 * @Date: 2020/02/23 16:19
 */
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AbsoluteSendRedirectFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        RedirectResponseWrapper redirectResponseWrapper = new RedirectResponseWrapper(request, response);
        filterChain.doFilter(request, redirectResponseWrapper);
    }

}
