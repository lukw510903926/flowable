package com.flowable.web.filter;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;

import com.flowable.common.utils.LoginUser;
import com.flowable.core.util.WebUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginFilter implements Filter {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private Set<String> prefixIgnores = new HashSet<String>();

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

		String ignoresParam = filterConfig.getInitParameter("ignores");
		String[] ignoreArray = ignoresParam.split(",");
		for (String s : ignoreArray) {
			prefixIgnores.add(s);
		}
		logger.info("loginFilter init -------------------");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)throws IOException, ServletException {

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpSession session = httpRequest.getSession();
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		LoginUser loginUser = (LoginUser) httpRequest.getSession().getAttribute(WebUtil.LOGIN_USER);
		if (canIgnore(httpRequest)) {
			chain.doFilter(httpRequest, response);
			return;
		}
		logger.info("doFilter---------------------------------------------");
		if (loginUser == null) {
			String requestUri = httpRequest.getRequestURI();
			String queryStr = httpRequest.getQueryString();
			String contextPath = httpRequest.getContextPath();
			if (session.getAttribute(WebUtil.USER_URL) == null) {
				if (StringUtils.isNotBlank(queryStr)) {
					requestUri += "?" + queryStr;
				}
				session.setAttribute(WebUtil.USER_URL, requestUri.substring(contextPath.length()));
			}
			httpResponse.sendRedirect(contextPath + "/login/login");
//			httpRequest.getRequestDispatcher("/login/login").forward(httpRequest, response);
		} else {
			WebUtil.getLoginUser(httpRequest, httpResponse);
			httpRequest.getSession().setAttribute(WebUtil.LOGIN_USER, loginUser);
			chain.doFilter(httpRequest, response);
		}
	}

	private boolean canIgnore(HttpServletRequest request) {

		String url = request.getRequestURI();
		for (String ignore : prefixIgnores) {
			if (url.endsWith(ignore)) {
				return true;
			}
		}
		if("/login/login".equalsIgnoreCase(request.getServletPath())){
			return true;
		}
		return false;
	}

	@Override
	public void destroy() {
		logger.info("loginFilter destroy -------------------");
	}
}
