package com.flowable.web.controller.sso;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.flowable.core.bean.auth.SystemRole;
import com.flowable.core.bean.auth.SystemUser;
import com.flowable.core.service.auth.ISystemUserService;

@RestController
@RequestMapping("role")
public class RoleController {

	@Autowired
	private ISystemUserService sysUserService;
	
	@RequestMapping("findUser")
	public Object findUser(){
		
		return this.sysUserService.findUserByRole(new SystemRole());
	}
	
	@RequestMapping("/loadUsersByUserName")
	public SystemUser loadUsersByUserName(String userName){
		
		return this.sysUserService.getUserByUsername(userName);
	}
}
