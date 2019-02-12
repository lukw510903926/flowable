package com.flowable.oa.controller;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.flowable.oa.service.auth.ISystemUserService;
import com.flowable.oa.util.LoginUser;
import com.flowable.oa.util.ReflectionUtils;
import com.flowable.oa.util.WebUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.flowable.oa.entity.auth.SystemRole;
import com.flowable.oa.entity.auth.SystemUser;

@Controller
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private ISystemUserService sysUserService;

    @RequestMapping("login")
    public String login(HttpServletRequest request, Model model) {

        String username = request.getParameter("username");
        if (StringUtils.isNotBlank(username)) {
            SystemUser systemUser = sysUserService.getUserByUsername(username);
            if (systemUser != null) {
                LoginUser loginUser = copySysUser(systemUser);
                WebUtil.setSessionUser(loginUser);
                return "redirect:/biz/list/myWork";
            } else {
                model.addAttribute("LOGIN_MSG", "用户名或密码错误");
            }
        }
        return "/login/login";
    }

    private LoginUser copySysUser(SystemUser systemUser) {

        LoginUser loginUser = new LoginUser();
        ReflectionUtils.copyBean(systemUser, loginUser);
        Set<SystemRole> set = systemUser.getRoles();
        if (CollectionUtils.isNotEmpty(set)) {
            Set<String> roles = new HashSet<>();
            set.forEach(role -> roles.add(role.getNameCn()));
            loginUser.setRoles(roles);
        }
        return loginUser;
    }

    @RequestMapping("loginOut")
    public String loginOut(HttpServletRequest request) {

        HttpSession session = request.getSession();
        session.invalidate();
        return "/login/login";
    }
}
