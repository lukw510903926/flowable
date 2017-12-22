<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" isErrorPage="true"%>
<%@ page import="com.eastcom.common.beanvalidator.BeanValidators"%>
<%@ page import="org.slf4j.Logger,org.slf4j.LoggerFactory"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%
	response.setStatus(200);
%>
<%
	Throwable ex = null;
	if (exception != null)
		ex = exception;
	if (request.getAttribute("javax.servlet.error.exception") != null)
		ex = (Throwable) request.getAttribute("javax.servlet.error.exception");
	//记录日志
	if (ex != null) {
		Logger logger = LoggerFactory.getLogger("500.jsp");
		logger.error(ex.getMessage(), ex);
	}
%>
<!DOCTYPE html>
<html>
<head>
<title>500 - 系统内部错误</title>
<c:import url="/WEB-INF/views/include/head.jsp"></c:import>
</head>
<body>
	<div>
		<div>
			<h1>系统发生内部错误.</h1>
		</div>
		<p>错误信息：</p>
		<p>
			<%
				if (ex != null) {
					if (ex instanceof javax.validation.ConstraintViolationException) {
						for (String s : BeanValidators.extractPropertyAndMessageAsList((javax.validation.ConstraintViolationException) ex, ": ")) {
							out.print(s + "<br/>");
						}
					} else {
						out.print(ex + "<br/>");
					}
				}
			%>
		</p>
	</div>
</body>
</html>