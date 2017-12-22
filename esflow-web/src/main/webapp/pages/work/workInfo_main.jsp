<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/pages/include/taglib.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<%-- 显示工单具体业务字段，按照一行4列2个字段的方式进行呈现，包括区分文本、数字、电话等，
工单信息包括主体信息(公共信息)，流程任务历史信息
主体信息：即工单的公共信息，创单时填写的所有业务信息,只有当前是创单时才呈现编辑模式，否则呈现显示模式<br>
以下信息如果当前为创单，则不会呈现，否则根据具体流程配置进行呈现
流程任务历史信息：即已经处理过的流程当中所填写的业务信息，此信息都为显示模式<br>
关键点：流程任务历史信息需要查看流程引擎是否支持获取当前已经处理过的流程任务ID进行关联

 --%>
<table class="mainTable" border="0" width="100%" cellPadding="0"
	cellSpacing="0">
	<c:forEach items="${ProcessValBeanMap }" var="mapEntity">
		<tr group="${mapEntity.key }" tag='dataRow'>
			<td colspan="4" class="groupName">${mapEntity.key }</td>
		</tr>
		<tr group="${mapEntity.key }" tag='dataRow'>
			<c:forEach items="${mapEntity.value }" var="item" varStatus="status">
				<c:if test="${status.index%2==0&&status.index!=0 }">
					</tr>
					<tr group="${mapEntity.key }" tag='dataRow'>
				</c:if>
				<td class="tableTD1"
					<c:if test="${!item.required }">
						style="padding-left:13px;"
					</c:if>
				>
				<c:if test="${item.required }">
					<span style="color:red">*</span>
				</c:if>
				<c:out value="${item.name }" /></td>
				<td class="tableTD2">
					<c:if test="${workBean.serviceInfo==null }">
 						<c:import url="/pages/work/workInfo_pageComponent.jsp">
							<c:param name="pageCompType" value="${item.pageComponent }" />
							<c:param name="pageCompName" value="${item.nameEN }" />
							<c:param name="pageCompRequired" value="${item.required }" />
							<c:param name="compParams" value="${item.compParames }" />
						</c:import> 
					</c:if>
					<c:if test="${workBean.serviceInfo!=null }">
						<c:out value="${workBean.serviceInfo[item.nameEN]}" />
					</c:if>
				</td>
	</c:forEach>
	<c:if test="${fn:length(mapEntity.value)%2!=0 }">
		<td class="tableTD1"></td>
		<td class="tableTD2"></td>
	</c:if>
	</tr>
	</c:forEach>
</table>