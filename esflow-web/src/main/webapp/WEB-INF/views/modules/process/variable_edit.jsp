<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<base href="${ctx}">
<title>参数设置</title>
<%-- <c:import url="/WEB-INF/views/include/head.jsp"></c:import> --%>
<meta name="decorator" content="default" />
<script type="text/javascript">
	var processId = "${processDefinitionId}";
	var version = "${version}";
	var taskId = "${taskId}";
	var updateId = "${vId}";
	var path = "${ctx}";
</script>
<link rel="stylesheet" href="${ctxPlugins}/bootstrap/bs-select/css/bootstrap-select.min.css">
<script src="${ctxPlugins}/bootstrap/bs-select/bootstrap-select.min.js"></script>
<script src="${ctxPlugins}/bootstrap/bs-select//i18n/defaults-zh_CN.min.js"></script>
<script type="text/javascript" src="${ctx}/js/modules/process/variable_edit.js"></script>
</head>

<body>
	<!-- 新增/编辑表单  -->
	<div class="panel panel-box">
		<div class="panel-heading">
			添加/编辑流程参数
		</div>
		<div class="panel-body">
			<div class="mr5">
				<form class="form-horizontal">
					<div class="row">
						<div class="col-xs-6 form-group">
							<label for="ud_name" class="col-xs-4 control-label">属性名称：</label>
							<div class="col-xs-8">
								<input type="text" class="form-control" id="ud_name"　placeholder="属性名称">
							</div>
						</div>
						<div class="col-xs-6 form-group">
							<label for="ud_alias" class="col-xs-4 control-label">属性别名：</label>
							<div class="col-xs-8">
								<input type="text" class="form-control" id="ud_alias" placeholder="属性别名">
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-xs-6 form-group">
							<label for="ud_nameOrder" class="col-xs-4 control-label">属性排序：</label>
							<div class="col-xs-8">
								<input type="text" class="form-control" id="ud_nameOrder"  	placeholder="属性排序">
							</div>
						</div>
						<div class="col-xs-6 form-group">
							<label for="ud_viewComponent" class="col-xs-4 control-label">视图组件类型：</label>
							<div class="col-xs-8">
								<select class="form-control selectpicker" data-live-search="true"  name="ud_viewComponent" id="ud_viewComponent" onchange="getViewParams()">
									<option value="TEXT">文本</option>
									<option value="TEXTAREA">大文本</option>
									<option value="NUMBER">数字</option>
									<option value="MOBILE">手机号</option>
									<option value="EMAIL">邮箱</option>
									<option value="DATE">日期</option>
									<option value="DATETIME">日期时间</option>
									<option value="COMBOBOX">下拉组件</option>
									<option value="VENDORCOMBOBOX">服务厂商下拉组件</option>
									<option value="DICTCOMBOBOX">数据字典下拉组件</option>
									<option value="MCMCOMBOBOX">配置项下拉组件</option>
									<option value="MCMLISTBOX">配置项分类组件</option>
									<option value="TREATMENT">处理方式</option>
									<option value="URGENCYLEVEL">紧急程度</option>
									<option value="MCMGRID">配置项表格</option>
									<option value="MEMBERBOX">人员选择组件</option>
									<option value="MEMBERLINKAGE">处理人员联动</option>
									<option value="MEMBERLIST">角色人员列表</option>
									<option value="MEMBERSEELCT">角色人员下拉组件</option>
									<option value="CROSSDIMENSION">交维工作多选</option>
									<option value="BOOLEAN">是否</option>
									<option value="HIDDEN">隐藏</option>
									<option value="CONFIRMUSER">确认人信息</option>
									<option value="EVENTBIZ">事件工单号</option>
									<option value="CHANGEBIZ">变更工单号</option>
									<option value="EVENTBIZLIST">事件工单列表</option>
									<option value="ISSELFREVIEW">是否完成自评</option>
									<option value="EQUIPMENTSELECTION">设备选择组件</option>
									<option value="ORGCOMBOBOX">组织下拉组件</option>
									<option value="STAFFCOMBOBOX">人员下拉组件</option>
									<option value="CHANGCONFIGITEM">变更配置项</option>
									<option value="REMARK">备注</option>
									<option value="DEPTMEMBERLIST">部门人员</option>
									<option value="ITCONFIGUSER">IT人员</option>
									<option value="BUTTON">按钮</option>
									<option value="SHOPPINGCART">购物车</option>
									<option value="REQUIREDFILE">需求附件</option>
									<option value="RECYCLESHOPPINGCART">it回收</option>
									<option value="STAFFINFOLINKAGECOMBOBOX">人员信息联动控件</option>
									<!-- <option value="CTSTDEPARTMENT">会签人员组件</option> -->
									<option value="GROUPHEAD">分组头部</option>
								</select>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-xs-6 form-group">
							<label for="ud_groupName" class="col-xs-4 control-label">属性分组：</label>
							<div class="col-xs-8">
								<input type="text" class="form-control" id="ud_groupName" placeholder="属性分组">
							</div>
						</div>
						<div class="col-xs-6 form-group">
							<label for="ud_groupOrder" class="col-xs-4 control-label">属性分组排序：</label>
							<div class="col-xs-8">
								<input type="text" class="form-control" id="ud_groupOrder" placeholder="属性分组排序">
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-xs-6 form-group">
							<label for="ud_componentArgs" id='ud_componentArgsLabel' class="col-xs-4 control-label">视图参数：</label>
							<div class="col-xs-8">
								<input type="text" class="form-control" id="ud_componentArgs" placeholder="视图参数">
							</div>
						</div>
						<div class="col-xs-6 form-group">
							<label class="col-xs-4 control-label"></label>
							<div class="col-xs-8">
								<label class="checkbox-inline"> <input type="checkbox" name="ud_required" id="ud_required">
									必填
								</label>
								<label class="checkbox-inline"> <input type="checkbox" name="ud_processVariable" id="ud_processVariable">
									流程变量
								</label>
							</div>
						</div>
					</div>
					<span id="helpBlock" class="help-block" style="margin-left: 100px;">下拉组件视图参数使用逗号隔开，如A,B,C。</span>
					<div class="row">
						<div class="col-xs-6 form-group">
							<label for="ud_variableGroup" class="col-xs-4 control-label">参数显示分组：</label>
							<div class="col-xs-8">
								<input type="text" class="form-control" id="ud_variableGroup" placeholder="参数显示分组">
							</div>
						</div>
						<div class="col-xs-6 form-group" style="display: none">
							<label for="ud_viewParams" class="col-xs-4 control-label" ></label>
							<div class="col-xs-8">
								<select class="form-control js-example-basic-single" name="viewParams" id="ud_viewParams">
								</select>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-xs-6 form-group">
							<label for="ud_refVariable" class="col-xs-4 control-label">联动父节点：</label>
							<div class="col-xs-8">
								<select class="form-control" name="ud_refVariable" id="ud_refVariable" onchange="getRefParam()"> </select>
							</div>
						</div>
						<div class="col-xs-6 form-group">
							<label for="ud_refParam" class="col-xs-4 control-label">对应联动属性：</label>
							<div class="col-xs-8">
								<select class="form-control" name="ud_refParam" id="ud_refParam">
								</select>
							</div>
						</div>
					</div>
					<div class="btn-list">
						<a id="saveOrUpdateBtn" class="btn btn-y">保存</a> 
						<a id="cancleBtn" class="btn btn-n mrl10">取消</a>
					</div>
				</form>
			</div>
		</div>
	</div>
</body>
</html>

