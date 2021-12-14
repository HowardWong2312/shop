package io.dubai.common.xss;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * XSS过滤
 *
 * @author howard
 */
public class XssFilter implements Filter {

	private String[] excludedUris;

	@Override
	public void init(FilterConfig config) throws ServletException {
		excludedUris = config.getInitParameter("notice").split(",");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
		XssHttpServletRequestWrapper xssRequest = new XssHttpServletRequestWrapper((HttpServletRequest) request);
		String url = xssRequest.getServletPath();
		if (isExcludedUri(url)){
			chain.doFilter(request, response);
		}else {
			chain.doFilter(xssRequest, response);
		}
	}

	@Override
	public void destroy() {
	}

	private boolean isExcludedUri(String uri) {
		if (excludedUris == null || excludedUris.length <= 0) {
			return false;
		}
		for (String ex : excludedUris) {
			uri = uri.trim();
			ex = ex.trim();
			if (uri.toLowerCase().matches(ex.toLowerCase().replace("*",".*")))
				return true;
		}
		return false;
	}

}