<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/pages/include/taglib.jsp"%>

<%
	String message = (String )request.getAttribute("message");
%>
<html>
<head>
	<title>部署流程</title>
	<%@include file="/pages/include/head.jsp"%>
	
	<link href="${ctx}/plugins/jquery-validation/1.11.1/jquery.validate.min.css" type="text/css" rel="stylesheet" />
	<script src="${ctx}/plugins/jquery-validation/1.11.1/jquery.validate.min.js" type="text/javascript"></script>
	<script src="${ctx}/plugins/jquery-validation/1.11.1/jquery.validate.method.min.js" type="text/javascript"></script>
	<link href="${ctx}/plugins/bootstrap/2.3.2/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
	<script src="${ctx}/plugins/bootstrap/2.3.2/js/bootstrap.min.js" type="text/javascript"></script>
	
	<script type="text/javascript">
		var message = "<%=message%>";
		$(document).ready(function(){
			
			if(message == "success"){
				window.parent.deploySuccessHandler();
				return;
			}
			
			$("#inputForm").validate({
				submitHandler: function(form){
					
					form.submit();
					$.messager.progress();
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
	</script>
</head>
<body>
	<br/>
	<sys:message content="${message}"/>
	<form id="inputForm" action="${ctx}/act/process/deploy" method="post" enctype="multipart/form-data" class="form-horizontal">
		<div class="control-group">
			<label class="control-label">流程文件：</label>
			<div class="controls">
				<input type="file" id="file" name="file" class="required"/>
				<span class="help-inline">支持文件格式：zip、bar、bpmn、bpmn20.xml</span>
			</div>
		</div>
		<div class="form-actions">
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="提 交"/>
			<!-- <input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/> -->
		</div>
	</form>
</body>
</html>
