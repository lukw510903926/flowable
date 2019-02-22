package com.flowable.oa.controller.sso;

import com.flowable.oa.core.entity.auth.SystemRole;
import com.flowable.oa.core.entity.auth.SystemUser;
import com.flowable.oa.core.service.auth.ISystemRoleService;
import com.flowable.oa.core.service.auth.ISystemUserService;
import com.flowable.oa.core.util.DataGrid;
import com.flowable.oa.core.util.RestResult;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *
 * @author yangqi
 * @Description </p>
 * @email 13507615840@163.com
 * @since 19-2-15 下午11:11
 **/
@RestController
@RequestMapping("role")
public class RoleController {

	@Autowired
	private ISystemUserService sysUserService;

	@Autowired
	private ISystemRoleService systemRoleService;
	
	@RequestMapping("findUser")
	public Object findUser(){
		
		return this.sysUserService.findUserByRole(new SystemRole());
	}
	
	@RequestMapping("/loadUsersByUserName")
	public SystemUser loadUsersByUserName(String userName){

		return this.sysUserService.getUserByUsername(userName);
	}

	@GetMapping("/index")
	public String index() {

		return "modules/sso/role/index";
	}

	@GetMapping("/info/edit/{roleId}")
	public String edit(@PathVariable("roleId") String roleId, Model model) {

		model.addAttribute("roleId", roleId);
		return "modules/sso/role/edit";
	}

	@ResponseBody
	@GetMapping("/info/{roleId}")
	public SystemRole info(@PathVariable("roleId") String roleId) {

		return this.systemRoleService.selectByKey(roleId);
	}

	@ResponseBody
	@PostMapping("/save")
	public RestResult<SystemRole> save(SystemRole systemRole) {

		this.systemRoleService.saveOrUpdate(systemRole);
		return RestResult.success(systemRole);
	}

	@ResponseBody
	@PostMapping("/delete")
	public RestResult<Object> delete(@RequestBody List<String> list) {

		this.systemRoleService.deleteByIds(list);
		return RestResult.success();
	}

	@ResponseBody
	@PostMapping("/list")
	public DataGrid<SystemRole> list(PageInfo<SystemRole> pageInfo, SystemRole systemRole){

		PageInfo<SystemRole> page = this.systemRoleService.findByModel(pageInfo, systemRole, true);
		DataGrid<SystemRole> dataGrid = new DataGrid<>();
		dataGrid.setRows(page.getList());
		dataGrid.setTotal(page.getTotal());
		return dataGrid;
	}
}
