package com.flowable.web.controller;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.flowable.common.utils.LoginUser;
import com.flowable.common.utils.ReflectionUtils;
import com.flowable.core.bean.auth.SystemRole;
import com.flowable.core.bean.auth.SystemUser;
import com.flowable.core.service.auth.ISystemUserService;
import com.flowable.core.util.WebUtil;

@Controller
@RequestMapping("/login")
public class LoginController {

	private Logger logger = Logger.getLogger(this.getClass());

	@Autowired
	private ISystemUserService sysUserService;

	@RequestMapping("login")
	public String login(HttpServletRequest request, HttpServletResponse response, Model model) {

		String username = (String) request.getParameter("username");
		if (StringUtils.isNotBlank(username)) {
			SystemUser systemUser = sysUserService.getUserByUsername(username);
			if (systemUser != null) {
				LoginUser loginUser = copySysUser(systemUser);
				WebUtil.setLoginUser(request, loginUser);
				return "redirect:/";
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
			Set<String> roles = new HashSet<String>();
			set.forEach(role -> roles.add(role.getNameCn()));
			loginUser.setRoles(roles);
		}
		return loginUser;
	}

	@RequestMapping("loginOut")
	public String loginOut(HttpServletRequest request, HttpServletResponse response) {

		HttpSession session = request.getSession();
		session.invalidate();
		return "/login/login";
	}

	@RequestMapping("index")
	public String index(HttpServletRequest request, HttpServletResponse response) {

		logger.info("----------------------");
		return "redirect:/biz/aaaaa";
	}
}
