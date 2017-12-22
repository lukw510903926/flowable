<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html style="overflow:hidden;">
<head>
<title>权限管理</title>
<meta name="decorator" content="index" />
<script type="text/javascript">
	$(function() {
		$("#auto").addClass("on");
	});
</script>
</head>
<body style="overflow-y:hidden; ">
	<iframe scrolling="no" frameBorder="no"
		src="/ipnet/pages/modules/secure/index.jsp"
		style="width:100%;height:100%;"></iframe>
</body>
</html>