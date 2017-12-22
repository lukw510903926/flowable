<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<base href="${ctx}">
	<title>部署流程</title>
	<%-- <c:import url="/WEB-INF/views/include/head.jsp"></c:import> --%>
	<meta name="decorator" content="default" />
	<script type="text/javascript">
		var message = "${message}";
		$(document).ready(function(){
			if(message) {
				bsAlert("提示", message);
			}
		});
	</script>
	<style>
	.required{line-height:16px;padding:4px 0;}
	</style>
</head>
<body>
	<div class="panel panel-box">
		<sys:message content="${message}"/>
		<div class="panel-heading">部署流程</div>
		<div class="panel-body">
			<div class="mr5">
				<form id="inputForm" action="${ctx}/act/process/deploy" method="post" enctype="multipart/form-data" class="form-horizontal">
					<div class="form-group">
						<label class="col-xs-1 control-label">流程文件：</label>
						<div class="col-xs-11">
							<input type="file" id="file" name="file" class="required"/>
							<span class="help-inline">支持文件格式：zip、bar、bpmn、bpmn20.xml</span>
						</div>
					</div>
					<div class="btn-list">
						<input id="btnSubmit" class="btn btn-y" type="submit" value="提交"/>
						<input id="btnCancel" class="btn btn-n mrl10" type="button" value="返回" onclick="window.open('${ctx}/process', '_self');"/>
					</div>
				</form>
			</div>
		</div>
	</div>
</body>
</html>
