<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<base href="${ctx}">
<title>人员配置管理</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
	var path = "${ctx}";
	var userType = "${userType}"
</script>
<link rel="stylesheet" href="${ctxPlugins}/bootstrap/bs-select/css/bootstrap-select.min.css">
<script src="${ctxPlugins}/bootstrap/bs-select/bootstrap-select.min.js"></script>
<script src="${ctxPlugins}/bootstrap/bs-select//i18n/defaults-zh_CN.min.js"></script>
<script type="text/javascript" src="${ctx}/js/modules/process/config/terminalConf.js"></script>
</head>
<body>
	<div class="panel panel-default">
		<div class="panel-heading details">
			<h2 id="panelTitle" class="panel-title details_tit">终端人员配置</h2>
		</div>
		<div class="panel-body">
			<div id="queryDiv" class="collapse in">
				<form id="queryGridForm" class="form-horizontal" role="form" method="post">
					<table cellpadding="0" cellspacing="0" class="ver_table_data">
						<tr>
							<td class="ttit" width="5%">人员类型：</td>
							<td><select name="userType">
									<option value="">全部</option>
									<option value="director">主管</option>
									<option value="manager">经理</option>
									<option value="agleamOf">一线</option>
									<option value="secondLine">二线</option>
									<option value="threeWwire">三线</option>
									<option value="needInterfaceMan">需求接口人</option>
									<option value="projectLeader">项目负责人</option>
							</select></td>
							<td class="ttit" width="5%">姓名：</td>
							<td><input type="text" name="username" /></td>
						</tr>
						<tr>
							<td class="ttit" width="5%">系统名称：</td>
							<td width="40%"><select id="systemName1" class="selectpicker" data-live-search="true" data-width="100%" name="systemName"></select></td>
							<td class="ttit" width="6%"><span title="*" style="color:#ff0000">*</span>所属流程：</td>
							<td width="40%"><select class="selectpicker" data-live-search="true" data-width="100%" name="bizType"></select></td>
						</tr>
						<tr>
							<td class="btn_list" colspan="2">
								<a href="javascript:void(0);" class="yes_btn right" style="margin-right: 10px" onclick="process.terminal.clickQuery()">查询</a>
							</td>
							<td class="btn_list" colspan="2">
								<a href="javascript:void(0);" class="resetbtn" onclick="process.terminal.resetForm('queryGridForm')">重置</a>
							</td>
						</tr>
					</table>
				</form>
				<div>
					<div class="table_menu">
						<a data-toggle="modal"  onclick="process.terminal.clickAdd()" data-target="#attachmentModal" class="c_new">新增</a>
						<a onclick="process.terminal.clickDelete()" class="c_del">删除</a>
					</div>
					<table id="serviceUser-table"></table>
				</div>
			</div>
		</div>
	</div>
	<!-- 模态框（Modal） -->
	<div class="modal fade" id="attachmentModal" tabindex="-1" data-backdrop='static'  role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	   <div class="modal-dialog" role="document">
	      <div class="modal-content panel panel-default" style="width:650px">
	         <div class="panel-heading details">
	            <button type="button" class="close" data-dismiss="modal" aria-hidden="true"> &times;</button>
	            <h3 class="panel-title details_tit"><span class="details_icon"></span>新增
	         </div>
	         <div class="modal-body">
	         	<form class="form-horizontal" role="form" id="queryUserForm">
					<table cellpadding="0" cellspacing="0" class="ver_table_data">
						<tr>
							<td class="ttit" width="10%">部门：</td>
							<td width="40%">
								<input id="sectorCombo" type="text" name="deptname" onclick="process.terminal.deptTree.showMenu();" value="" readonly="" /> 
								<input id="sectorComboVal" type="hidden" value="" name="sector" />
							</td>
							<td class="ttit" width="10%">姓名：</td>
							<td width="40%">
								<input id="cnname" type="text" value="" name="cnname" />
							</td>
						</tr>
						<tr>
							<td class="btn_list" colspan="2">
								<a class="yes_btn right" style="margin-right: 10px" onclick="process.terminal.queryUser();">查询</a>
							</td>
							<td class="btn_list" colspan="2">
								<a class="no_btn" onclick="process.terminal.resetForm('queryUserForm')">重置</a>
							</td>
						</tr>
					</table>
				</form>
		         <div class="panel-heading details">
		            <h4 class="panel-title details_tit" style="text-align: right;">
		            	<a href="javascript:void(0);" onclick="process.terminal.saveUser()" style="margin-right: 20px" class="yes_btn">保存  </a>
		            </h4>
		         </div>
		         <form class="form-horizontal" role="form" id="saveUserForm">
			         <table cellpadding="0" cellspacing="0" class="ver_table_data">
			         	<tr>
							<td class="ttit" width="6%"><span title="*" style="color: #ff0000">*</span>人员类型：</td>
							<td width="40%">
								<select name="userType">
									<option value="">请选择</option>
									<option value="director">主管</option>
									<option value="manager">经理</option>
									<option value="agleamOf">一线</option>
									<option value="secondLine">二线</option>
									<option value="threeWwire">三线</option>
									<option value="needInterfaceMan">需求接口人</option>
									<option value="projectLeader">项目负责人</option>
								</select>
							</td>
							<td class="ttit" width="6%"><span title="*" style="color: #ff0000">*</span>系统名称：</td>
							<td width="40%"><select id="systemName2" class="selectpicker" data-live-search="true"  data-width="100%" name="systemName"></select></td>
						</tr>
						<tr>
							<td class="ttit" width="6%"><span title="*" style="color:#ff0000">*</span>所属流程：</td>
							<td width="40%"><select class="selectpicker" data-live-search="true" data-width="100%" name="bizType"></select></td>
						</tr>
			         </table>
		         </form>
		         <div class="modal-body">
					<table id="userTable"></table>
				</div>
			</div>
			<div id="sectorMenuContent" class="menuContent"	style="display:none; position: absolute;">
				<ul id="sectorTree" class="ztree" style="margin-top: 0px; width: 220px; height: 300px; -moz-user-select: none;" />
			</div>
	      </div>
	    </div>
	</div>
</body>
</html>

