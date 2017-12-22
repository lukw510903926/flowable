<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<base href="${ctx}">
<title>流程任务列表</title>
<%-- <c:import url="/WEB-INF/views/include/head.jsp"></c:import> --%>
<meta name="decorator" content="default" />
<script type="text/javascript">
	var processId = "${processDefinitionId}";
	var version = "${version}";
	var taskId = "${taskId}";
	var path = "${ctx}";
	$(function() {
		function detailFormatter(index, row) {
			var content = '';
			content += '<div class="btn-toolbar" role="toolbar" aria-label="...">';
			content += '	<div class="btn-group" role="group" aria-label="...">';
			content += '		<button type="button" class="btn btn-default" onclick="window.open(\'${ctx}/process/variable?processDefinitionId='
					+ row.id + '\', \'_self\');">设置流程任务参数</button>';
			content += '	</div>';
			content += '</div>';
			return content;
		}
	});
</script>
<script type="text/javascript"
	src="${ctx}/js/modules/process/task_list.js"></script>
</head>

<body>
	<div class="panel panel-ex">
		<div class="panel-heading">
			流程任务
				<!-- <div class="pull-right">
					<a href="javascript:refreshData();"><span
						class="glyphicon glyphicon-trash"></span>刷新</a>
				</div> -->
				<span class="table_menu_w right">
					<a href="javascript:refreshData();" >刷新</a>
				</span>
		</div>
		<div class="panel-body">
			<div class="base-table-wrap">
				<table id="process-task-table" class="base-table table-striped">
				</table>
			</div>
		</div>
	</div>
	</div>
</html>

