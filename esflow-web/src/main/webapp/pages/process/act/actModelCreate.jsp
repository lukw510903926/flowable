<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/pages/include/taglib.jsp"%>

<%
	String message = (String )request.getAttribute("message");
	String modelId = (String )request.getAttribute("modelId");
%>

<html>
<head>
	<title>新建模型 - 模型管理</title>
	<%@include file="/pages/include/head.jsp"%>
	
	<link href="${ctx}/plugins/jquery-validation/1.11.1/jquery.validate.min.css" type="text/css" rel="stylesheet" />
	<script src="${ctx}/plugins/jquery-validation/1.11.1/jquery.validate.min.js" type="text/javascript"></script>
	<script src="${ctx}/plugins/jquery-validation/1.11.1/jquery.validate.method.min.js" type="text/javascript"></script>
	<link href="${ctx}/plugins/bootstrap/2.3.2/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
	<script src="${ctx}/plugins/bootstrap/2.3.2/js/bootstrap.min.js" type="text/javascript"></script>
	
	<script type="text/javascript">
	var message = "<%=message%>";
	var modelId = "<%=modelId%>";
		$(document).ready(function(){
			
			if(message == "success"){
				window.parent.deploySuccessHandler(modelId);
				return;
			}
			
			$("#inputForm").validate({
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});
		});
		function page(n,s){
        	location = '${ctx}/act/model/?pageNo='+n+'&pageSize='+s;
        }
	</script>
</head>
<body>
	<br/>
	<sys:message content="${message}"/>
	<form id="inputForm" action="${ctx}/act/model/create" method="post" class="form-horizontal">
		<div class="control-group">
			<label class="control-label">模块名称：</label>
			<div class="controls">
				<input id="name" name="name" type="text" class="required" />
				<span class="help-inline"></span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">模块标识：</label>
			<div class="controls">
				<input id="key" name="key" type="text" class="required" />
				<span class="help-inline"></span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">模块描述：</label>
			<div class="controls">
				<textarea id="description" name="description" class="required"></textarea>
			</div>
		</div>
		<div class="form-actions">
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="提 交"/>
			<!-- <input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/> -->
		</div>
	</form>
</body>
</html>
