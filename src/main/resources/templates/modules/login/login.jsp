<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/head.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>登录页</title>
<link rel="stylesheet" type="text/css" href="${themePath}/css/login.css">
<script type="text/javascript"  src="${ctx}/js/modules/shiro/login.js"></script>
</head>
<body>
	<div class="container">
	<div class="row">
        <div class="col-md-offset-3 col-md-6">
            <form class="form-horizontal" id="loginForm" method="post" action="${ctx}/login/login">
                <span class="heading">用户登录</span>
                <div class="form-group">
                    <input type="text" class="form-control" name="username" placeholder="用户名或电子邮件">
                    <i class="fa fa-user"></i>
                </div>
                <div class="form-group help">
                    <input type="password" class="form-control" name="password" placeholder="密　码">
                    <i class="fa fa-lock"></i>
                    <a href="#" class="fa fa-question-circle"></a>
                </div>
                <div class="form-group">
                 <!--    <div class="main-checkbox">
                        <input type="checkbox" value="None" id="checkbox1" name="check"/>
                        <label for="checkbox1"></label>
                    </div>
                    <span class="text">Remember me</span> -->
                    <button type="submit"  class="btn btn-default">登录</button>
                </div>
            </form>
        </div>
    </div>
</div>
</body>
</html>
