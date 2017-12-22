<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<base href="${ctx}">
<title>流程管理</title>
<meta name="decorator" content="default" />
</head>
<body>
	<script type="text/javascript">
		var path = "${ctx}";
	</script>
	<script type="text/javascript"
		src="${ctx}/js/modules/process/process_list.js"></script>

	<div class="panel panel-ex">
		<div class="panel-heading">
			流程管理
			<%-- <div class="pull-right">
				<a href="${ctx}/process/deploy" target="_self"><span class="glyphicon glyphicon-file"></span>部署流程</a> <a href="javascript:$('#table').bootstrapTable('refresh');"><span class="glyphicon glyphicon-trash"></span>刷新</a>
			</div> --%>
			<span class="func-btn-list">
				<a href="${ctx}/process/deploy" target="_self" class="item-link mrl10">部署</a>
				<a href="javascript:$('#table').bootstrapTable('refresh');" class="item-link mrl10">刷新</a>
			</span>
		</div>
		<div class="panel-body">
			<div class="base-table-wrap">
				<table id="process-table" data-detail-view="true"
					data-detail-formatter="detailFormatter" class="base-table table-striped">
				</table>
			</div>
		</div>
	</div>
</body>
</html>

