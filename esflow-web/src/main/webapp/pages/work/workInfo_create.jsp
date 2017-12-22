<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/pages/include/taglib.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<%-- 
创建工单信息，
	//如果是新建工单，则只呈现一个工单标题的填写
	//否则呈现PANEL，显示工单创建信息，包括工单标题、工单编号、派单人、派单人部分、派单人联系电话、派单时间
	
 --%>

<div style="margin-top: 25px;">
	<c:if test="${workBean!=null }">
		<a href="javascript:return void(0);"
			onclick="workInfo.processImage.show('${workBean.workInfo.id }','${workBean.workInfo.workTitle }')">查看流程图</a>
	</c:if>
</div>

<table class="mainTable" border="0" width="100%" cellPadding="0"
	cellSpacing="0">
	<c:if test="${workBean==null }">
		<tr>
			<td class="tableTD1" ${_SYS_FORM_TYPE==null?" style='width:15%'":""}><span style="color:red">*</span>工单标题</td>
			<td class="tableTD2" ${_SYS_FORM_TYPE==null?" style='width:85%'":""}><c:if
					test="${workBean==null }">
					<input type="text" name="base.workTitle" style="width:300px;"
						required='true' />
					<input type="hidden" name="base.tempID"
						value="<c:out value="${base_tempID }"/>" />

				</c:if></td>
			<c:if test="${_SYS_FORM_TYPE!=null }">
				<td class="tableTD1">${_SYS_FORM_TYPE.name }</td>
				<td class="tableTD2">
 						<c:import url="/pages/work/workInfo_pageComponent.jsp">
							<c:param name="pageCompType" value="SUBTYPE_SELECT" />
							<c:param name="pageCompName" value="${_SYS_FORM_TYPE.nameEN }" />
							<c:param name="pageCompRequired" value="true" />
							<c:param name="compParams" value="${_SYS_FORM_TYPE.compParames }" />
						</c:import> 
				</td>
			</c:if>
		</tr>
	</c:if>

	<c:if test="${workBean!=null }">
		<tr>
			<td class="tableTD1">工单标题</td>
			<td class="tableTD2" ${_SYS_FORM_TYPE==null?" colspan='3'":"" }><c:out
					value="${workBean.workInfo.workTitle }" /> <input type="hidden"
				name="base.workID" value="<c:out value="${workBean.workInfo.id }"/>" />
				<input type="hidden" name="base.taskID"
				value="<c:out value="${workBean.extInfo.base_taskID }"/>" /></td>
			<c:if test="${_SYS_FORM_TYPE!=null }">
				<td class="tableTD1">${_SYS_FORM_TYPE.processValBean.name }</td>
				<td class="tableTD2">
					<c:out value="${_SYS_FORM_TYPE.value }"/>
					<input type="hidden" id="${_SYS_FORM_TYPE.processValBean.nameEN }" value="${_SYS_FORM_TYPE.value }"/>
				</td>
			</c:if>
		</tr>
		<tr>
			<td class="tableTD1">工单编号</td>
			<td class="tableTD2" id="workBean.workInfo.workNumber"><c:out
					value="${workBean.workInfo.workNumber }" /></td>
			<td class="tableTD1">派单时间</td>
			<td class="tableTD2"><fmt:formatDate
					value="${workBean.workInfo.createTime}" type="date"
					pattern="yyyy-MM-dd HH:mm:ss" /></td>
		</tr>
		<tr>
			<td class="tableTD1">派单人</td>
			<td class="tableTD2"><c:out
					value="${workBean.extInfo.createUser.name }" /></td>
			<td class="tableTD1">派单部门</td>
			<td class="tableTD2"><c:out
					value="${workBean.extInfo.createUser.dep }" /></td>
		</tr>
		<tr>
			<td class="tableTD1">派单人电话</td>
			<td class="tableTD2"><c:out
					value="${workBean.extInfo.createUser.mobile }" /></td>
			<td class="tableTD1">派单人邮箱</td>
			<td class="tableTD2"><c:out
					value="${workBean.extInfo.createUser.email }" /></td>
		</tr>
		<tr>
			<td class="tableTD1">当前环节</td>
			<td class="tableTD2"><c:out
					value="${workBean.workInfo.taskName }" /></td>
			<td class="tableTD1">当前环节处理人</td>
			<td class="tableTD2"><c:out
					value="${workBean.workInfo.taskAssignee }" /></td>
		</tr>
	</c:if>
</table>
