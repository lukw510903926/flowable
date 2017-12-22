<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/pages/include/taglib.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<%-- 
工单处理人信息，包括当前处理环节的业务信息
	//如果是新建工单或者处理人没有指定，则呈现为空
	//否则呈现PANEL，显示处理人信息，包括当前处理人姓名、处理人部分、处理人联系方式、处理意见
	
	//显示附件上传功能
 --%>
<%-- POP_HANDLE:标记为当前是否可以进行处理，逻辑为，只有非新增单，并且当前任务操作人是当前登录人 --%>
<input type="hidden" name="base.handleResult" id="base.handleResult" />
<input type="hidden" name="base.handleName" id="base.handleName" />
<input type="hidden" name="base.transfer_value" id="base.transfer_value" />
<input type="hidden" name="base.transfer_type" id="base.transfer_type" />
<c:if test="${CURRE_OP=='HANDLE'}">
	<table class="mainTable" border="0" width="100%" cellPadding="0"
		cellSpacing="0">
		<tr>
			<td class="groupName" colspan="4">处理信息</td>
		</tr>
		<tr>
			<td class="tableTD1">处理人</td>
			<td class="tableTD2"><c:out
					value="${workBean.extInfo.handleUser.name }" /></td>
			<td class="tableTD1">处理部门</td>
			<td class="tableTD2"><c:out
					value="${workBean.extInfo.handleUser.dep }" /></td>
		</tr>
		<tr>
			<td class="tableTD1">联系电话</td>
			<td class="tableTD2"><c:out
					value="${workBean.extInfo.handleUser.mobile }" /></td>
			<td class="tableTD1">邮箱</td>
			<td class="tableTD2"><c:out
					value="${workBean.extInfo.handleUser.email }" /></td>
		</tr>
		<tr>
			<c:forEach items="${ProcessTaskValBeans }" var="item"
				varStatus="status">
				<c:if test="${status.index%2==0&&status.index!=0 }">
		</tr>
		<tr>
			</c:if>
			<td class="tableTD1">
							<c:if test="${item.required }">
					<span style="color:red">*</span>
				</c:if>
			<c:out value="${item.name }" /></td>
			<td class="tableTD2"><c:import
					url="/pages/work/workInfo_pageComponent.jsp">
					<c:param name="pageCompType" value="${item.pageComponent }" />
					<c:param name="pageCompName" value="${item.nameEN }" />
					<c:param name="pageCompRequired" value="${item.required }" />
				</c:import></td>
			</c:forEach>
			<c:if test="${fn:length(ProcessTaskValBeans)%2!=0 }">
				<td class="tableTD1">处理意见</td>
				<td class="tableTD2"><c:import
						url="/pages/work/workInfo_pageComponent.jsp">
						<c:param name="pageCompType" value="TEXTAREA" />
						<c:param name="pageCompName" value="base.handleMessage" />
						<c:param name="pageCompRequired" value="false" />
					</c:import></td>
			</c:if>
		</tr>
		<c:if test="${fn:length(ProcessTaskValBeans)%2==0 }">
			<tr>
				<td class="tableTD1">处理意见</td>
				<td class="tableTD2"><c:import
						url="/pages/work/workInfo_pageComponent.jsp">
						<c:param name="pageCompType" value="TEXTAREA" />
						<c:param name="pageCompName" value="base.handleMessage" />
						<c:param name="pageCompRequired" value="false" />
					</c:import></td>
				<td class="tableTD1"></td>
				<td class="tableTD2"></td>
			</tr>
		</c:if>
	</table>

</c:if>
<%--新增工单显示附件上传 --%>
<%-- 
<c:if test="${CURRE_OP=='HANDLE'||CURRE_OP=='SIGN'||workBean==null }"> --%>

<table class="mainTable" border="0" width="100%" cellPadding="0"
	cellSpacing="0">
	<tr>
		<td class="groupName" colspan="4"></td>
	</tr>
	<c:if test="${CURRE_OP=='HANDLE'||workBean==null}">
		<tr>
			<td class="tableTD1" style="height:25px;">附件上传</td>
			<td class="tableTD2" colspan=3 style="width:85%"><a
				class='span_but_a' onclick="workInfo.annex.addUpload()">添加附件</a> <a
				class='span_but_a' onclick="workInfo.annex.delUpload()">删除附件</a><br>
				<div id="annex_upload_div"></div></td>
		</tr>
	</c:if>
	<tr>
		<td colspan="4" align="center" class="tableTD2"
			style="width:100%;height:40px;"><c:if test="${CURRE_OP=='SIGN'}">
				<input type="button" value="签收"
					onclick="workInfo.form.submit('签收','SIGN')" />
			</c:if> <c:forEach items="${SYS_BUTTON }" var="entity">
				<c:if test="${fn:indexOf(entity.key, '$command_')!=0}">
					<c:set var="commandKey" value="$command_${entity.key}" />
					<input type="button" value="${entity.value }"
						onclick="workInfo.form.submit('${entity.value }','${entity.key }','${SYS_BUTTON[commandKey]}')" />
				</c:if>
			</c:forEach> <input type="button" value="关闭"
			onclick="workInfo.form.submit('关闭','close')" /></td>
	</tr>
</table>
<%-- </c:if> --%>