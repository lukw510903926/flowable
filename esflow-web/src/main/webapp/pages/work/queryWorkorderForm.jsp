<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/pages/include/taglib.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
	String action = request.getParameter("action");
%>
<%-- 定义各组件的名称及ID --%>
<c:set var="action" value="<%=action %>"/>
<c:set var="workNumber" value="workNumber"/>
<c:set var="workTitle" value="workTitle"/>
<c:set var="workType" value="workType"/>
<c:set var="status" value="status"/>
<c:set var="taskAssignee" value="taskAssignee"/>
<c:set var="createUser" value="createUser"/>
<c:set var="createTime2" value="createTime2"/>
<c:set var="createTime" value="createTime"/>
<c:set var="processTime2" value="processTime2"/>
<c:set var="processTime" value="processTime"/>
<form id="queryForm">
	<table width="100%" border="0" align="center" cellpadding="0"
		cellspacing="0" class="search">
		<tr>
			<td width="9%" height="30" class="td1">工单号：</td>
			<td><input type="text" name="${workNumber }" id="${workNumber }"
				class="inputbk" style="width:210px;" /></td>
			<td width="9%" class="td1">工单标题：</td>
			<td style="white-space: nowrap;"><input type="text"
				name="${workTitle }" id="${workTitle }" class="inputbk" style="width:210px;" /></td>
			<td width="9%" class="td1">工单类型：</td>
			<td style="white-space: nowrap;"><select class="easyui-combobox"
				id="${workType }" name="${workType }" style="width:216px;"
				data-options="editable:false">
					<option value="">所有</option>
					<c:forEach items="${ProcessMap }" var="item">
						<option value="${item.value }"><c:out
								value="${item.value }" /></option>
					</c:forEach>
			</select></td>
		</tr>
		<c:if test="${action=='myComplete'||action=='myClaim' }">
			<tr>
				<td width="9%" class="td1">创建时间：</td>
				<td style="white-space: nowrap;"><input type="text"
					name="${createTime }" readonly="readonly" class="Wdate"
					onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
					style="width:140px;" /> - <input type="text" name="${createTime2 }"
					readonly="readonly" class="Wdate"
					onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
					style="width:140px;" /></td>
			</tr>
		</c:if>
		<c:if test="${action=='query' }">
			<tr>
				<td width="9%" class="td1">创建人：</td>
				<td style="white-space: nowrap;"><input type="text"
					name="${createUser}" id="${createUser}" class="inputbk"
					style="width:210px;" /></td>
				<td width="9%" class="td1">当前处理人：</td>
				<td style="white-space: nowrap;"><input type="text"
					name="${taskAssignee}" id="${taskAssignee}" class="inputbk"
					style="width:210px;" /></td>
				<td width="9%" class="td1">工单状态：</td>
				<td style="white-space: nowrap;"><select
					class="easyui-combobox" id="${status}" name="${status}"
					style="width:216px;" data-options="editable:false">
						<option value="">所有</option>
						<option value="处理中">处理中</option>
						<option value="已结束">已结束</option>
				</select></td>
			</tr>
			<tr>
				<td width="9%" class="td1">创建时间：</td>
				<td style="white-space: nowrap;"><input type="text"
					name="${createTime}" readonly="readonly" class="Wdate"
					onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
					style="width:140px;" /> - <input type="text" name="${createTime2}"
					readonly="readonly" class="Wdate"
					onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
					style="width:140px;" /></td>
			</tr>
		</c:if>
		<c:if test="${action=='myCreate' }">
			<tr>
				<td width="9%" class="td1">创建时间：</td>
				<td style="white-space: nowrap;"><input type="text"
					name="${createTime}" readonly="readonly" class="Wdate"
					onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
					style="width:140px;" /> - <input type="text" name="${createTime2}"
					readonly="readonly" class="Wdate"
					onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
					style="width:140px;" /></td>
				<td width="9%" class="td1">当前处理人：</td>
				<td style="white-space: nowrap;"><input type="text"
					name="${taskAssignee}" id="${taskAssignee}" class="inputbk"
					style="width:210px;" /></td>
				<td width="9%" class="td1">工单状态：</td>
				<td style="white-space: nowrap;"><select
					class="easyui-combobox" id="${status}" name="${status}"
					style="width:216px;" data-options="editable:false">
						<option value="">所有</option>
						<option value="处理中">处理中</option>
						<option value="已结束">已结束</option>
				</select></td>
			</tr>
		</c:if>
		<c:if test="${action=='myHandle' }">
			<tr>
				<td width="9%" class="td1">创建人：</td>
				<td style="white-space: nowrap;"><input type="text"
					name="${createUser}" id="${createUser}" class="inputbk"
					style="width:210px;" /></td>
				<td width="9%" class="td1">工单状态：</td>
				<td style="white-space: nowrap;"><select
					class="easyui-combobox" id="${status}" name="${status}"
					style="width:216px;" data-options="editable:false">
						<option value="">所有</option>
						<option value="处理中">处理中</option>
						<option value="已结束">已结束</option>
				</select></td>
				<td width="9%" class="td1">处理时间：</td>
				<td style="white-space: nowrap;"><input type="text"
					name="${processTime}" readonly="readonly" class="Wdate"
					${pageCompRequired?"required='true'":"" }
					onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
					style="width:140px;" /> - <input type="text" name="${processTime2}"
					readonly="readonly" class="Wdate"
					${pageCompRequired?"required='true'":"" }
					onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
					style="width:140px;" /></td>
			</tr>
			<tr>
				<td width="9%" class="td1">创建时间：</td>
				<td style="white-space: nowrap;"><input type="text"
					name="${createTime}" readonly="readonly" class="Wdate"
					onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
					style="width:140px;" /> - <input type="text" name="${createTime2}"
					readonly="readonly" class="Wdate"
					onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
					style="width:140px;" /></td>
			</tr>
		</c:if>
		<c:if test="${action=='myClose' }">
			<tr>
				<td width="9%" class="td1">当前处理人：</td>
				<td style="white-space: nowrap;"><input type="text"
					name="${taskAssignee}" id="${taskAssignee}" class="inputbk"
					style="width:210px;" /></td>
				<td width="9%" class="td1">工单状态：</td>
				<td style="white-space: nowrap;"><select
					class="easyui-combobox" id="${status}" name="${status}"
					style="width:216px;" data-options="editable:false">
						<option value="">所有</option>
						<option value="处理中">处理中</option>
						<option value="已结束">已结束</option>
				</select></td>
				<td width="9%" class="td1">创建时间：</td>
				<td style="white-space: nowrap;"><input type="text"
					name="${createTime}" readonly="readonly" class="Wdate"
					onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
					style="width:140px;" /> - <input type="text" name="${createTime2}"
					readonly="readonly" class="Wdate"
					onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
					style="width:140px;" /></td>
			</tr>
		</c:if>
		<tr>
			<td align='center' colspan='6'><a class="btn1"
				href="javascript:void(0)" id="query_OK">查询<span /></span></a> <a
				class="btn1" href="javascript:void(0)" id="query_reset">重 置<span></span></a></td>
		</tr>
	</table>

</form>