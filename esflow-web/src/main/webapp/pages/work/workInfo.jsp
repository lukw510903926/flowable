<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/pages/include/taglib.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title>工单信息</title>
<%@include file="/pages/include/head.jsp"%>
<%@ include file="/pages/include/treeview.jsp"%>
<link rel="stylesheet" type="text/css" href="${ctx}/themes/default/css/workInfo.css">
<script type="text/javascript" src="${ctx}/js/common/leftTree.js"></script>
<script type="text/javascript" src="${ctx}/js/work/workInfo.js"></script>
</head>

<body
	style="overflow: auto;background-color: #FFFFFF;padding-left: 10px;padding-right: 10px;">
	<form id="MainForm" action="${ctx }/workflow/submit" method="post"
		enctype="multipart/form-data">
		<div>

			<%--创建信息 --%>
			<c:import url="/pages/work/workInfo_create.jsp" />
			<%--主体信息 --%>
			<c:import url="/pages/work/workInfo_main.jsp" />
			<%--附件 --%>
			<c:import url="/pages/work/workInfo_annex.jsp" />
			<%--操作日志 --%>
			<c:import url="/pages/work/workInfo_log.jsp" />
			<%--当前操作信息 --%>
			<c:import url="/pages/work/workInfo_handle.jsp" />
			</table>
		</div>
	</form>
</body>
</html>
