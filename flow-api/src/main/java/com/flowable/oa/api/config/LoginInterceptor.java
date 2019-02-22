package com.flowable.oa.api.config;


import com.flowable.oa.core.util.LoginUser;
import com.flowable.oa.core.util.WebUtil;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * 登录拦截器
 *
 * @author : lukewei
 * @project : tykj-oa
 * @createTime : 2018年2月1日 : 下午12:39:08
 * @description :
 */
public class LoginInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){

        LoginUser loginUser = new LoginUser();
        loginUser.setUsername("admin");
        Set<String> roles = new HashSet<>();
        roles.add("超级管理员");
        loginUser.setRoles(roles);
        List<String> urls = new ArrayList<>();
        urls.add("/url/template");
        loginUser.setUrls(urls);
        WebUtil.setSessionUser(loginUser);
        return true;
    }
}
