<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
	String action = request.getParameter("action");
%>
<%-- 定义各组件的名称及ID --%>
<c:set var="action" value="<%=action%>" />
<c:set var="workNumber" value="workNumber" />
<c:set var="workTitle" value="workTitle" />
<c:set var="workType" value="workType" />
<c:set var="status" value="status" />
<c:set var="taskAssignee" value="taskAssignee" />
<c:set var="createUser" value="createUser" />
<c:set var="createTime2" value="createTime2" />
<c:set var="createTime" value="createTime" />
<c:set var="processTime2" value="processTime2" />
<c:set var="processTime" value="processTime" />

<div class="container">
	<div class="panel panel-default">
		<div class="panel-heading">
			<h2 class="panel-title">查询条件</h2>
		</div>
		<div class="panel-body">
			<form class="form-horizontal">
				<div class="row">
					<div class="col-md-11">
						<div class="col-md-4 form-group" style="margin-bottom: 5px">
							<label for="ud_name" class="col-md-4 control-label">工单号：</label>
							<div class="col-md-8">
								<input type="text" class="form-control" id="bizId" name="bizId" placeholder="工单号">
							</div>
						</div>
						<div class="col-md-4 form-group" style="margin-bottom: 5px">
							<label for="ud_alias" class="col-md-4 control-label">工单标题：</label>
							<div class="col-md-8">
								<input type="text" class="form-control" id="title" name="title" placeholder="工单标题">
							</div>
						</div>
						<div class="col-md-4 form-group" style="margin-bottom: 5px">
							<label for="ud_alias" class="col-md-4 control-label">工单类型：</label>
							<div class="col-md-8">
								<select class="form-control" name="bizType" id="bizType" placeholder="工单类型">
									<option value="">--所有--</option>
									<c:forEach items="${ProcessMap }" var="item">
										<option value="${item.value }"><c:out value="${item.value }" /></option>
									</c:forEach>
								</select>
							</div>
						</div>
					</div>
					<div class="col-md-1">
						<a data-toggle="collapse" data-parent="#accordion" href="#more-condition"> <span class="glyphicon glyphicon-collapse-down">更多</span>
						</a>
					</div>
				</div>

				<div class="col-md-11 panel-collapse collapse" id="more-condition">
					<div class="row">
						<div class="col-md-8 form-group" style="margin-bottom: 5px">
							<label for="ud_order" class="col-md-4 control-label">属性排序：</label>
							<div class="col-md-8">
								<input type="text" name="${createTime}" readonly="readonly" class="Wdate" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" style="width:140px;" /> - <input type="text" name="${createTime2}" readonly="readonly" class="Wdate" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" style="width:140px;" />
								<input type="text" class="form-control" id="ud_order" placeholder="属性排序">
							</div>
						</div>
						<div class="col-md-4 form-group" style="margin-bottom: 5px">
							<label for="ud_viewComponent" class="col-md-4 control-label">视图组件类型：</label>
							<div class="col-md-8">
								<select class="form-control" name="ud_viewComponent" id="ud_viewComponent">
									<option value="">-所有-</option>
									<c:forEach items="${ProcessMap }" var="item">
										<option value="${item.value }"><c:out value="${item.value }" /></option>
									</c:forEach>
								</select>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-4 form-group" style="margin-bottom: 5px">
							<label for="ud_groupName" class="col-md-4 control-label">属性分组：</label>
							<div class="col-md-8">
								<input type="text" class="form-control" id="ud_groupName" placeholder="属性分组">
							</div>
						</div>
						<div class="col-md-4 form-group" style="margin-bottom: 5px">
							<label for="ud_groupOrder" class="col-md-4 control-label">分组排序：</label>
							<div class="col-md-8">
								<input type="text" class="form-control" id="ud_groupOrder" placeholder="属性分组排序">
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-4 form-group" style="margin-bottom: 5px">
							<label for="ud_componentArgs" class="col-md-4 control-label">视图参数：</label>
							<div class="col-md-8">
								<input type="text" class="form-control" id="ud_componentArgs" placeholder="视图参数">
							</div>
						</div>
					</div>
				</div>

				<div class="col-md-12 text-center row" style="padding-bottom: 5px;">
					<a id="saveOrUpdateBtn" class="btn btn-primary">保存</a> <a href="#" class="btn btn-default">取消</a>
				</div>

			</form>
		</div>
	</div>
</div>



<form id="queryForm">
	<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="search">
		<c:if test="${action=='myComplete'||action=='myClaim' }">
			<tr>
				<td width="9%" class="td1" style="color: aqua;">创建时间：</td>
				<td style="white-space: nowrap;"><input type="text" name="${createTime }" readonly="readonly" class="Wdate" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" style="width:140px;" /> - <input type="text" name="${createTime2 }" readonly="readonly" class="Wdate" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" style="width:140px;" /></td>
			</tr>
		</c:if>
		<tr>
			<td width="9%" class="td1">创建人：</td>
			<td style="white-space: nowrap;"><input type="text" name="${createUser}" id="${createUser}" class="inputbk" style="width:210px;" /></td>
			<td width="9%" class="td1">当前处理人：</td>
			<td style="white-space: nowrap;"><input type="text" name="${taskAssignee}" id="${taskAssignee}" class="inputbk" style="width:210px;" /></td>
			<td width="9%" class="td1">工单状态：</td>
			<td style="white-space: nowrap;"><select class="easyui-combobox" id="${status}" name="${status}" style="width:216px;" data-options="editable:false">
					<option value="">所有</option>
					<option value="处理中">处理中</option>
					<option value="已结束">已结束</option>
			</select></td>
		</tr>
		<tr>
			<td width="9%" class="td1">创建时间：</td>
			<td style="white-space: nowrap;"><input type="text" name="${createTime}" readonly="readonly" class="Wdate" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" style="width:140px;" /> - <input type="text" name="${createTime2}" readonly="readonly" class="Wdate" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" style="width:140px;" /></td>
		</tr>
		<c:if test="${action=='myCreate' }">
			<tr>
				<td width="9%" class="td1">创建时间：</td>
				<td style="white-space: nowrap;"><input type="text" name="${createTime}" readonly="readonly" class="Wdate" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" style="width:140px;" /> - <input type="text" name="${createTime2}" readonly="readonly" class="Wdate" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" style="width:140px;" /></td>
				<td width="9%" class="td1">当前处理人：</td>
				<td style="white-space: nowrap;"><input type="text" name="${taskAssignee}" id="${taskAssignee}" class="inputbk" style="width:210px;" /></td>
				<td width="9%" class="td1">工单状态：</td>
				<td style="white-space: nowrap;"><select class="easyui-combobox" id="${status}" name="${status}" style="width:216px;" data-options="editable:false">
						<option value="">所有</option>
						<option value="处理中">处理中</option>
						<option value="已结束">已结束</option>
				</select></td>
			</tr>
		</c:if>
		<c:if test="${action=='myHandle' }">
			<tr>
				<td width="9%" class="td1">创建人：</td>
				<td style="white-space: nowrap;"><input type="text" name="${createUser}" id="${createUser}" class="inputbk" style="width:210px;" /></td>
				<td width="9%" class="td1">工单状态：</td>
				<td style="white-space: nowrap;"><select class="easyui-combobox" id="${status}" name="${status}" style="width:216px;" data-options="editable:false">
						<option value="">所有</option>
						<option value="处理中">处理中</option>
						<option value="已结束">已结束</option>
				</select></td>
				<td width="9%" class="td1">处理时间：</td>
				<td style="white-space: nowrap;"><input type="text" name="${processTime}" readonly="readonly" class="Wdate" ${pageCompRequired?"required='true'":"" } onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" style="width:140px;" /> - <input type="text" name="${processTime2}" readonly="readonly" class="Wdate" ${pageCompRequired?"required='true'":"" } onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" style="width:140px;" /></td>
			</tr>
			<tr>
				<td width="9%" class="td1">创建时间：</td>
				<td style="white-space: nowrap;"><input type="text" name="${createTime}" readonly="readonly" class="Wdate" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" style="width:140px;" /> - <input type="text" name="${createTime2}" readonly="readonly" class="Wdate" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" style="width:140px;" /></td>
			</tr>
		</c:if>
		<c:if test="${action=='myClose' }">
			<tr>
				<td width="9%" class="td1">当前处理人：</td>
				<td style="white-space: nowrap;"><input type="text" name="${taskAssignee}" id="${taskAssignee}" class="inputbk" style="width:210px;" /></td>
				<td width="9%" class="td1">工单状态：</td>
				<td style="white-space: nowrap;"><select class="easyui-combobox" id="${status}" name="${status}" style="width:216px;" data-options="editable:false">
						<option value="">所有</option>
						<option value="处理中">处理中</option>
						<option value="已结束">已结束</option>
				</select></td>
				<td width="9%" class="td1">创建时间：</td>
				<td style="white-space: nowrap;"><input type="text" name="${createTime}" readonly="readonly" class="Wdate" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" style="width:140px;" /> - <input type="text" name="${createTime2}" readonly="readonly" class="Wdate" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" style="width:140px;" /></td>
			</tr>
		</c:if>
		<tr>
			<td align='center' colspan='6'><a class="btn1" href="javascript:void(0)" id="query_OK">查询<span /></span></a> <a class="btn1" href="javascript:void(0)" id="query_reset">重 置<span></span></a></td>
		</tr>
	</table>

</form>