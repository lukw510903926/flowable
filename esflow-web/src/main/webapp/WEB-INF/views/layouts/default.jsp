<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="../include/taglib.jsp"%>
<%@ include file="../include/param.jsp"%>
<%@ taglib prefix="sitemesh"
	uri="http://www.opensymphony.com/sitemesh/decorator"%>
<!DOCTYPE html>
<html>
<head>
<title>网管开发与运维平台 <sitemesh:title /></title>
<%@include file="../include/head.jsp"%>
<script type="text/javascript" src="${ctx}/js/layouts/default.js"></script>
<sitemesh:head />
<script type="text/javascript">
	var userName = '${userName}';
	$(function() {
		$("#process").addClass("on");
	});
</script>
</head>
<body>
	<nav class="navbar top-navbar">
		<div class="pull-right">
			<ul class="list-unstyled header-func">
				<li class="head-member"><span class="head-member-ico mrr5"></span>${deptment }</li>
				<li><a href="javascript:exit();"><i class="head-ico3"></i></a></li>
			</ul>
		</div>
	</nav>
	<%-- <div class="top-menu-wp">
		<ul id="top-menu" class="list-unstyled clearfix top-menu">
			<li class="active"><a href="${base}/esflow/biz?action=myWork"class="top-mu">运维管理</a></li>
		</ul>
	</div> --%>
	<div>
	<%-- 	<ol class="breadcrumb">
			  <li><i class="ico-index mrr5"></i>您现在的位置</li><li class="active"><a href="${ctx }">运维管理</a></li>
		</ol> --%>
		<div class="con-main" id="mainDiv">
			<c:choose>
				<c:when test="${decoratorType=='report' }">
					<jsp:include page="reportLeftTree.jsp"></jsp:include>
				</c:when>
				<c:otherwise>
					<jsp:include page="publicLeftTree.jsp"></jsp:include>
				</c:otherwise>
			</c:choose>
			<div class="con-right2">
				<div id="content">
					<sitemesh:body />
				</div>
			</div>
		</div>
	</div>
</body>
</html>