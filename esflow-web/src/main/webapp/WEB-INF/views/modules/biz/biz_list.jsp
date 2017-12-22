<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<base href="${ctx}">
<title>工单查询</title>
<meta name="decorator" content="default" />
</head>
<body>
<style type="text/css">
	.form-element {
		margin-left: 0 !important;
		margin-right: 0 !important;
		padding-left: 0;
		padding-right: 0;
	}
	.rc-handle-container{position:relative;}
	.rc-handle{position:absolute;width:7px;cursor:ew-resize;*cursor:pointer;margin-left:-3px;}
</style>
<script>
	$.namespace("workInfo.temp");
	workInfo.temp.queryAction = "${action}";
	var username = "${user}";
	var status = "${status}";
	var type = "${type}";
	var statusList = ${statusList };
	var processList = ${processList };
	var handleUser = "${handleUser}";
	var taskDefKey = "${taskDefKey}";
	var workName = "${workName}";
	var taskAssignee = "${taskAssignee}";
	var action = "${action}";
</script>
 	<link rel="stylesheet" href="${ctxPlugins}/bootstrap/bs-select/css/bootstrap-select.min.css">
    <script src="${ctxPlugins}/bootstrap/bs-select/bootstrap-select.min.js"></script>
    <script src="${ctxPlugins}/bootstrap/bs-select//i18n/defaults-zh_CN.min.js"></script>
	<script type="text/javascript" src="${ctx}/js/modules/biz/biz_list.js"></script>
	<script type="text/javascript" src="${ctx}/js/modules/biz/biz_turn_task.js"></script>
	<%@include file="biz_list_query.jsp" %>
	<div class="panel panel-ex" id='table'>
		<div class="panel-heading">
			<c:choose>
				<c:when test="${action=='myWork' }">统一待办 </c:when>
				<c:when test="${action=='myTemp' }">我的草稿 </c:when>
				<c:when test="${action=='myCreate' }">我的创建 </c:when>
				<c:when test="${action=='myHandle' }">我的已处理 </c:when>
				<c:when test="${action=='myIntercept' }">我的拦截 </c:when>
				<c:when test="${action=='myEventHandle' }">我的督办</c:when>
				<c:when test="${action=='myEventQA' }">我的质检 </c:when>
				<c:when test="${action=='myEventService' }">我的待办 </c:when>
				<c:otherwise>查询结果 </c:otherwise>
			</c:choose>
			<span class="func-btn-list">
				<c:if test="${action=='myCreate' }">
					<a href="javascript:void(0);" id="deletBiz" onclick="biz.query.removeBizInfo()" class="item-link mrl10">取消</a>
				</c:if>
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