<%@page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%--<%@page import="com.eastcom.common.bean.SessionInfo"%>
<%@page import="com.activiti.core.util.SSOUtil"%>
<%
	SessionInfo util = SSOUtil.getInstance(request,response);
	String userName = util.getName();
	String userFullName = util.getName();
	request.setAttribute("userName",userName);

	Map<String,Object> userInfo = SSOUtil.getUserInfoByUserName(userName);
	String deptment = userInfo.get("structName") == null ? "" : (String)userInfo.get("structName");
	String fullname = userInfo.get("fullname") == null ? "" : (String)userInfo.get("fullname");
	request.setAttribute("deptment",deptment + " " +fullname);
%>--%>
