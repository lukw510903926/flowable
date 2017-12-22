<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="../include/taglib.jsp"%>
<%@ include file="../include/param.jsp"%>
<%@ taglib prefix="sitemesh" uri="http://www.opensymphony.com/sitemesh/decorator"%>
<!DOCTYPE html>
<html>
<head>
<title>网管开发与运维平台 <sitemesh:title /></title>
<%@include file="../include/head.jsp"%>
<sitemesh:head />
<script type="text/javascript">
$(function() {
	$("#auto").addClass("on");
});
</script>
</head>
<body >
<div class="container">
	<div class="main" id="mainDiv">
		<div class="main_right">
			<div class="con_main" id="content">
				<sitemesh:body />
			</div>
		</div>
	</div>
</div>
</body>
</html>