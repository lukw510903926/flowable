package com.flowable.oa.controller.sso;

import com.flowable.oa.service.auth.ISystemUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.flowable.oa.entity.auth.SystemRole;
import com.flowable.oa.entity.auth.SystemUser;

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
