package com.flowable.core.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.flowable.common.utils.LoginUser;

public class WebUtil {
	
	private static final ThreadLocal<LoginUser> loginUsers = new ThreadLocal<LoginUser>();
	
	public static final String LOGIN_USER = "LOGIN_USER";
	
	public static final String USER_URL = "USER_URL";
    
    public static LoginUser getLoginUser(){
        return loginUsers.get();
    }

	public static LoginUser getLoginUser(HttpServletRequest request, HttpServletResponse response) {

		LoginUser user = (LoginUser) request.getSession().getAttribute(WebUtil.LOGIN_USER);
		loginUsers.set(user);
		return user;
	}

	public static LoginUser getLoginUser(HttpServletRequest request) {

    	return getLoginUser(request,null);
	}
	
	public static void setLoginUser(HttpServletRequest request,LoginUser loginUser){

		loginUsers.set(loginUser);
		request.getSession().setAttribute(WebUtil.LOGIN_USER, loginUser);
	}
}
