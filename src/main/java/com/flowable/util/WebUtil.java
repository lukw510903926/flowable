package com.flowable.util;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class WebUtil {

	private static final ThreadLocal<LoginUser> loginUsers = new ThreadLocal<LoginUser>();

	public static final String LOGIN_USER = "LOGIN_USER";

	public static final String USER_URL = "USER_URL";

	public static LoginUser getLoginUser() {
		return loginUsers.get();
	}

	public static LoginUser getLoginUser(HttpServletRequest request, HttpServletResponse response) {

		LoginUser user = (LoginUser) request.getSession().getAttribute(WebUtil.LOGIN_USER);
		loginUsers.set(user);
		return user;
	}

	public static LoginUser getLoginUser(HttpServletRequest request) {

		return getLoginUser(request, null);
	}

	public static void setLoginUser(HttpServletRequest request, LoginUser loginUser) {

		loginUsers.set(loginUser);
		request.getSession().setAttribute(WebUtil.LOGIN_USER, loginUser);
	}

	/**
	 * 获取请求 request
	 *
	 * @return
	 */
	public static HttpServletRequest getRequest() {

		return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
	}

	/**
	 * 获取请求参数
	 *
	 * @return
	 */
	public static Map<String, Object> getRequestParam() {

		String paramName;
		HttpServletRequest request = getRequest();
		Map<String, Object> map = new HashMap<>();
		Enumeration<String> enumPks = request.getParameterNames();
		while (enumPks.hasMoreElements()) {
			paramName = enumPks.nextElement();
			map.put(paramName, request.getParameter(paramName));
		}
		return map;
	}
}
