package com.flowable.web.controller.sso;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.flowable.core.bean.auth.SystemRole;
import com.flowable.core.service.auth.ISystemUserService;

@Controller
@RequestMapping("role")
public class RoleController {

	@Autowired
	private ISystemUserService sysUserService;
	
	@ResponseBody
	@RequestMapping("findUser")
	public Object findUser(){
		
		return this.sysUserService.findUserByRole(new SystemRole());
	}
}
