<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/head.jsp"%>
<!DOCTYPE html>
<html>
<head>
<base href="${ctx}">
<title>工单统计</title>
<meta name="decorator" content="default" />
</head>
<body>
<style type="text/css">
.form-element {
	margin-left: 0 !important;
	margin-right: 0 !important;
	/*margin-bottom: 5px;*/
	padding-left: 0;
	padding-right: 0;
}
</style>
<link rel="stylesheet" href="${ctxPlugins}/bootstrap/bs-select/css/bootstrap-select.min.css">
<script src="${ctxPlugins}/bootstrap/bs-select/bootstrap-select.min.js" type="text/javascript"></script>
<script src="${ctxPlugins}/bootstrap/bs-select//i18n/defaults-zh_CN.min.js" type="text/javascript"></script>
<script>
	var type = "${type}";
	var statusList = ''  ;
	var processList = '';
	var handleUser = "${handleUser}";
	var taskDefKey = "${taskDefKey}";
	var taskAssignee = "${taskAssignee}";
	var systemName = "${systemName}";
	var field = "${field}";
	var action = "${action}" == "" ? 'leftBiz' : "${action}";
</script>
<script type="text/javascript" src="${ctx}/js/modules/biz/biz_report.js" ></script>
<script type="text/javascript" src="${ctx}/js/modules/biz/biz_report_exportFile.js" ></script>
	<div class="panel panel-box">
		<div class="panel-heading">
			查询条件
		</div>
		<div class="panel-body">
			<div class="mr5">
				<form class="form-horizontal" id="biz-query-form">
					<div class="col-xs-12 btn-list">
						<a id="queryBtn" class="btn btn-y">查询</a>
						<button type="reset" class="btn btn-n mrl10" onclick="biz.report.query.resetClick()">重置</button>
					</div>
				</form>
			</div>
		</div>
	</div>
	<div class="panel panel-ex">
		<div class="panel-heading">
			<c:choose>
				<c:when test="${action=='leftBiz' }">遗留工单</c:when>
				<c:when test="${action=='isShuffle' }">推诿单</c:when>
				<c:when test="${action=='systemStatus' }">各系统事件工单状态统计</c:when>
				<c:when test="${action=='timeout' }">各系统事件工单超时统计</c:when>
				<c:when test="${action=='systemEff' }">各系统事件工单效能统计</c:when>
				<c:when test="${action=='userSatis' }">用户满意度统计</c:when>
				<c:when test="${action=='leftBiz' }">遗留工单</c:when>
				<c:when test="${action=='systemReject' }">各系统驳回统计</c:when>
				<c:when test="${action=='support' }">各系统支持方式统计</c:when>
				<c:when test="${action=='delay' }">各系统延期工单统计</c:when>
				<c:when test="${action=='reportSource' }">各系统报障来源统计</c:when>
				<c:when test="${action=='systemReject' }">各系统驳回统计</c:when>
				<c:when test="${action=='intercept' }">各系统拦截率统计</c:when>
				<c:otherwise>查询结果</c:otherwise>
			</c:choose>
			<span class="func-btn-list">
				<c:if test="${action =='leftBiz'}">
					<a href="javascript:void(0);" onclick="biz.report.table.removeLeft()" class="item-link mrl10">已处理 </a>
				</c:if>
				<a href="javascript:void(0);" onclick="biz.report.exportFile.exportDetail()" class="item-link mrl10">导出 </a>
			</span>
		</div>
		<div class="panel-body">
			<div class="base-table-wrap">
				<table id="biz-table" class="table table-hover base-table table-striped"/>
			</div>
		</div>
	</div>
</body>
</html>