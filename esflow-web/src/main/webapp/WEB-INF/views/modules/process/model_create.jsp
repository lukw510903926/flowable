<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

<%
	String message = (String )request.getAttribute("message");
	String modelId = (String )request.getAttribute("modelId");
%>
<!DOCTYPE html>
<html>
<head>
	<title>新建模型</title>
	<%-- <c:import url="/WEB-INF/views/include/head.jsp"></c:import> --%>
	<meta name="decorator" content="default" />
	<script type="text/javascript">
	var message = "${message}";
	var modelId = "${modelId}";
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
					$("#messageBox").text("非法数据.");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});
		});
		/*function page(n,s){
        	location = '${ctx}/act/model/?pageNo='+n+'&pageSize='+s;
        }*/
	</script>
</head>
<body>
	<div class="panel panel-box">
		<div class="panel-heading">
			新建模型
		</div>
		<div class="panel-body">
			<div class="mr5">
				<form id="inputForm" action="${ctx}/model/create" method="post" class="form-horizontal">
					<div class="form-group">
						<label class="col-xs-1 control-label">模块名称：</label>
						<div class="col-xs-3">
							<input id="name" name="name" type="text" class="form-control" />
							<span class="help-inline"></span>
						</div>
						<label class="col-xs-1 control-label">模块标识：</label>
						<div class="col-xs-3">
							<input id="key" name="key" type="text" class="form-control" />
							<span class="help-inline"></span>
						</div>
					</div>
					<div class="form-group">
						<label class="col-xs-1 control-label">模块描述：</label>
						<div class="col-xs-11">
							<textarea id="description" name="description" class="form-control"></textarea>
						</div>
					</div>
					<div class="btn-list">
						<input id="btnSubmit" class="btn btn-y" type="submit" value="提交"/>
						<input id="btnCancel" class="btn btn-n mrl10" type="button" value="返回" onclick="window.open('${ctx}/model', '_self');"/>
					</div>
				</form>
			</div>
		</div>
	</div>
</body>
</html>
