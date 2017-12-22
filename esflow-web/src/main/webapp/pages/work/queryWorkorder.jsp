<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/pages/include/taglib.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title>工单查询</title>
<%@include file="/pages/include/head.jsp"%>
<%@ include file="/pages/include/treeview.jsp"%>
<link rel="stylesheet" type="text/css"
	href="${ctx}/themes/default/css/workInfo.css">
	<script>
	$.namespace("workInfo.temp");
	workInfo.temp.queryAction = '${action}';
	</script>
<script type="text/javascript" src="${ctx}/js/common/leftTree.js"></script>
<script type="text/javascript" src="${ctx}/js/work/workInfo.js"></script>
<script type="text/javascript" src="${ctx}/js/work/workQuery.js"></script>
</head>

<body
	style="overflow: auto;background-color: #FFFFFF;padding-left: 10px;padding-right: 10px;">
	<div class="con_bg">
		<div class="con_itembox">
			<div class="con_item_title">
				<div class="bg1 left"></div>
				<div class="bg2 right"></div>
				<div class="title">
					<h5>
						查询条件
						
					</h5>
				</div>
			</div>
			<div class="con_item_list" id="queryOpDevice">
				<c:import url="/pages/work/queryWorkorderForm.jsp">
					<c:param name="action" value="${action }"/>
				</c:import>
			</div>
		</div>
		<div class="con_itembox">
			<div class="con_item_title">
				<div class="bg1 left"></div>
				<div class="bg2 right"></div>
				<div class="title">
					<h5>查询结果</h5>
				</div>
			</div>
			<div class="table" autoResize="true" resizeHeight='5'
				resizeWidth="false">
				<table id="datagrid" class="easyui-datagrid"
					data-options="data:[],fitColumns:true,striped:false,loadMsg:'数据加载中...',
					<c:if test="${action!='query' }">
					url:'${ctx }/workflow/queryWorkOrder/${action }',
					</c:if>
					ctrlSelect:false,fit:true,onDblClickRow:workInfo.grid.clickRow,
	pagination:true,pageSize:20,pageList:[10,20,30,40,50],border:false"
					autoResize="true" componentType='datagrid' resizeHeight='0'
					resizeWidth="false">
					<thead>
						<tr>
							<th data-options="field:'id',checkbox:true"></th>
							<th data-options="field:'workNumber',width:150">工单号</th>
							<th data-options="field:'workType',width:100">工单类型</th>
							<th data-options="field:'workTitle',width:100">工单标题</th>
							<th data-options="field:'createUser',width:100">创建人</th>
							<th data-options="field:'createTime',width:100">创建时间</th>
							<th data-options="field:'taskName',width:100">当前环节</th>
							<th data-options="field:'taskAssignee',width:100">当前环节处理人</th>
						</tr>
					</thead>
				</table>
			</div>
		</div>
	</div>





</body>
</html>

