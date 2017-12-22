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
	<c:if test="${action=='myEventHandle' }">
		<div class="panel panel-box">
			<div class="panel-heading">
				导出明细可选项
			</div>
			<div class="panel-body">
				<div class="mr5">
					<table class="mrb10">
						<tr>
							<td><input type="checkbox" id="description">详细描述</td>
							<td><span class="mrl10"><input type="checkbox" id="solution">解决方案</span></td>
							<td><input type="button" value="导出" onclick="biz.table.exportDetail()" class="btn btn-y mrl10"></td>
							<td style="width: 10px;"></td>
							<td style="color: red;">温馨提示：<span class="yel_bg mrr5">&nbsp;</span>表示即将超时的工单；<span class="red_bg mrr5">&nbsp;</span>表示已超时的工单</td>
						<tr>
					</table>
					<font color="red">说明：只有已经关联了服务厂商角色的应用系统下面的事件工单，才可以发送催办短信给相关服务厂商负责人。</font>
					<table style="width: 100%">
						<tr>
							<td width="80"><a onclick="biz.table.remindersBiz()"class="btn btn-y mrr10">催办</a></td>
							<td style="text-align: right; width:60px;">催办原因：</td>
							<td><input type="text" id="message" style="width: 90%;" /></td>
						<tr>
					</table>
				</div>
			</div>
		</div>
	</c:if>

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
				<c:if test="${action!='myTemp' && action !='myIntercept'&& action !='leftBiz'}">
					<a href="javascript:void(0);" onclick="biz.query.exportDetail()" class="item-link mrl10">
						导出
					</a>
				</c:if>
				<c:if test="${action=='venderHandle' }">
					<a href="javascript:void(0);" onclick="biz.turn.init()" class="item-link mrl10">转派</a>
				</c:if>
				<c:if test="${action=='myCreate' }">
					<a href="javascript:void(0);" id="deletBiz" onclick="biz.query.removeBizInfo()" class="item-link mrl10">取消</a>
				</c:if>
				<c:if test="${action =='leftBiz'}">
					<a href="javascript:void(0);" onclick="biz.table.removeLeft()" class="item-link mrl10">
						已处理
					</a>
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