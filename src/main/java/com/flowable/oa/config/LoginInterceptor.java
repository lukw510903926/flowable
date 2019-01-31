package com.flowable.oa.config;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.flowable.oa.util.LoginUser;
import com.flowable.oa.util.WebUtil;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;


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
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        LoginUser loginUser = new LoginUser();
        loginUser.setUsername("admin");
        Set<String> roles = new HashSet<String>();
        roles.add("超级管理员");
        loginUser.setRoles(roles);
        List<String> urls = new ArrayList<>();
        urls.add("/url/template");
        loginUser.setUrls(urls);
        WebUtil.setSessionUser(request, loginUser);
        return true;
    }
}
