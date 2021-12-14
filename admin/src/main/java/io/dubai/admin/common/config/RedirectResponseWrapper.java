package io.dubai.admin.common.config;

/**
 * 功能描述: <br>
 *
 * @since: 1.0.0
 * @Author:Created By KURO
 * @Date: 2020/02/23 16:21
 */
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class RedirectResponseWrapper extends HttpServletResponseWrapper {
    private static Logger log = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

    private final HttpServletRequest request;

    public RedirectResponseWrapper(final HttpServletRequest inRequest, final HttpServletResponse response) {
        super(response);
        this.request = inRequest;
    }

    @Override
    public void sendRedirect(final String pLocation) throws IOException {

        if (StringUtils.isBlank(pLocation)) {
            super.sendRedirect(pLocation);
            return;
        }

        try {
            final URI uri = new URI(pLocation);
            if (uri.getScheme() != null) {
                super.sendRedirect(pLocation);
                return;
            }
        } catch (URISyntaxException ex) {
            super.sendRedirect(pLocation);
        }

        // !!! FIX Scheme  !!!
        String finalurl = "https://" + this.request.getServerName();
        if (request.getServerPort() != 80 && request.getServerPort() != 443) {
            finalurl += ":" + request.getServerPort();
        }
        finalurl += pLocation;

        log.info("========= HTTPS shiro重定向跳转：" + finalurl);
        super.sendRedirect(finalurl);
    }

}
