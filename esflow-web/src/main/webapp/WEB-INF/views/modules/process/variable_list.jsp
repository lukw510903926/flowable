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
	var path = "${ctx}";
</script>
<script type="text/javascript"
	src="${ctx}/js/modules/process/variable_list.js"></script>

</head>

<body>
	<div class="panel panel-ex">
		<div class="panel-heading">
				<!-- <span id="htitle">流程参数管理</span>
				<div class="pull-right">
					<a href="javascript:addData();"><span
						class="glyphicon glyphicon-plus"></span>新增</a> <a
						href="javascript:editData();"><span
						class="glyphicon glyphicon-pencil"></span>修改</a> <a
						href="javascript:delData();"><span
						class="glyphicon glyphicon-minus"></span>删除</a> <a
						href="javascript:refreshTable();"><span
						class="glyphicon glyphicon-refresh"></span>刷新</a>
				</div> -->
				流程参数管理
				<span class="table_menu_w right">
				<a href="javascript:addData();" >新增</a> 
				<a href="javascript:editData();" >修改</a> 
				<a href="javascript:delData();" >删除</a> 
				<a href="javascript:refreshTable();" >刷新</a>
				</span>
		</div>
		<div class="panel-body">
			<div class="base-table-wrap">
				<table id="variable-table" class="base-table table-striped">
				</table>
			</div>
		</div>
	</div>
</body>
</html>

