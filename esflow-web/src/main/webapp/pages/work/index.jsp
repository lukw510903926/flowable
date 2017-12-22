<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/pages/include/taglib.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title>首页</title>
<meta name="decorator" content="default" />
<%@ include file="/pages/include/treeview.jsp"%>

<script type="text/javascript" src="${ctx}/js/common/leftTree.js"></script>
<script type="text/javascript" src="${ctx}/js/work/workInfo.js"></script>
<script type="text/javascript">
	$.namespace("workInfo.temp");
	workInfo.temp.ProcessMap = ${ProcessMapJson}
</script>
<script>
	function openClick(value, action) {
		if (value == 'model_list') {
			openTab(value, "模型列表", "${ctx}/pages/process/model_list.jsp");
		} else if (value == 'process_list') {
			openTab(value, "流程列表", "${ctx}/pages/process/process_list.jsp");
		} else if (value == 'process_running_list') {
			openTab(value, "流程列表",
					"${ctx}/pages/process/process_running_list.jsp");
		} else {
			if (value == null) {
				var li = $(event.srcElement);
				value = li.attr('value');
				var text = li.text();
				openTab(value, text, "${ctx}/workflow/create?tempID=" + value);
			} else if (value == 'count') {
				var li = $(event.srcElement);
				openTab(value + action, li.text(),
						"${ctx}/pages/work/workOrderCount.jsp?action=" + action);
			} else {
				var li = $(event.srcElement);
				openTab(value, li.text(), "${ctx}/workflow/queryWorkOrder/"
						+ value);
			}
		}
	}
	function openTab(value, text, url) {
		Page.utils.addTab("configTab", value, text, url);
	}
	function treeClick(node) {
		if (node.id == null || node.id == '') {
			return;
		}
		var action = node.id.substring(0, node.id.indexOf('_'));
		var value = node.id.substring(node.id.indexOf('_') + 1);
		if (action == 'TY') {
			openTab(node.id, node.text, "${ctx}/workflow/queryWorkOrder/"
					+ value);
		} else if (action == 'NEW') {
			openTab(node.id, node.text, "${ctx}/workflow/create?tempID="
					+ value);
		} else if (action == 'TONGJ') {
			openTab(node.id, node.text,
					"${ctx}/pages/work/workOrderCount.jsp?action=" + value);
		}
	}
	$(function() {

		$('#tt').tree({
			onDblClick : treeClick,
			data : [ {
				text : '通用列表',
				children : [ {
					text : "统一待办",
					id : "TY_myComplete"
				}, {
					text : "待签任务",
					id : "TY_myClaim"
				}, {
					text : "工单查询",
					id : "TY_query"
				}, {
					text : "已建工单",
					id : "TY_myCreate"
				}, {
					text : "处理的工单",
					id : "TY_myHandle"
				}, {
					text : "关闭工单",
					id : "TY_myClose"
				} ]
			}, {
				text : '卓越运维流程',
				children : workInfo.workType.getData()
			}, {

				text : "工单统计",
				children : [ {
					text : "故障处理流程KPI",
					id : "TONGJ_gz"
				}, {
					text : "故障处理流程KPI(告警级别)",
					id : "TONGJ_gz_level"
				}, {
					text : "通用流程KPI",
					id : "TONGJ_ty"
				}, {
					text : "投诉流程KPI",
					id : "TONGJ_ts"
				}, {
					text : "电路调度流程KPI",
					id : "TONGJ_dl"
				} ]
			} ]
		});
	})
	function openInfo(rowIndex, rowData) {

		openTab(rowData.id, rowData.workNumber, "${ctx}/workflow/display?id="
				+ rowData.id);
	}
</script>
</head>

<body>
	<table id="main" width=100% cellpadding="0" cellspacing="0" border=0>
		<tr>
			<td valign="top">
				<div id="leftTree" class="con_left left">
					<div class="con_itembox" style="padding: 0px;">
						<div class="con_item_title">
							<ul id="menu">
								<li class="selected">
									<div class="list-title">
										<span style="padding:0px 5px 0 0;"> <a></a> <a></a> <a class="ico_tab13"></a>
										</span>
										<h5>工作台</h5>
									</div>
									<div class="list-item firstMenu">
										<div class="list-item_list" autoResize="true" resizeHeight="15" resizeType="leftMenu">
											<ul id="tt" class="easyui-tree">

											</ul>
										</div>
									</div>
								</li>
								<!--<c:if test="${fns:authenticate(resList,'IPNET.电子运维.流程管理')||true}">-->
								<li>
									<div class="list-title">
										<span style="padding:0px 5px 0 0;"> <a></a> <a></a> <a class="ico_tab13"></a>
										</span>
										<h5>流程发布管理</h5>
									</div>
									<div class="list-item" style="display:none">
										<div class="list-item_list" autoResize="true" resizeHeight="15" resizeType="leftMenu">

											<ul class="nav nav-list">
												<li style="height: 30px;line-height: 27px;margin-left: 10px;"><a style="cursor: pointer;" onclick="openClick('model_list')">&nbsp;模型列表</a></li>
												<li style="height: 30px;line-height: 27px;margin-left: 10px;"><a style="cursor: pointer;" onclick="openClick('process_list')">&nbsp;流程列表</a></li>
												<!-- <li
													style="height: 30px;line-height: 27px;margin-left: 10px;"><a
													style="cursor: pointer;" onclick="openClick('process_running_list')">&nbsp;运行中流程列表</a></li> -->
											</ul>
										</div>
									</div>
								</li>
								<!--</c:if>-->
							</ul>
						</div>
					</div>
				</div>
			</td>
			<td valign="top">
				<div class="con_right"">
					<div id="configTab" class="easyui-tabs" data-options="border:false" componentType="tabs" autoResize="true" resizeHeight="5" resizeWidth="-3">
						<div title="首页" style="padding:10px">
							<%-- <ul>
								<c:forEach items="${ProcessMap }" var="item">
									<li><a style="cursor: pointer;" onclick="openClick()"
										value="${item.key }">${item.value }</a></li>
								</c:forEach>
							</ul> --%>
							<div class="con_itembox">
								<div class="con_item_title">
									<div class="bg1 left"></div>
									<div class="bg2 right"></div>
									<div class="title">
										<h5>公告</h5>
									</div>
								</div>
								<div class="con_item_list" style="padding: 10px;">
									<marquee scrollamount="3" direction="up" onmouseover="this.stop()" onmouseout="this.start()" <c:if test='${!empty(model.style)}'>${model.style}</c:if> height="80px">
										<c:forEach var="item" items="${notices}">
											<c:forEach var="map" items="${item}">
												<c:if test="${'content' eq map.key}">
													<p>·${map.value}</p>
												</c:if>
											</c:forEach>
										</c:forEach>
									</marquee>
								</div>
							</div>
							<div class="con_itembox">
								<div class="con_item_title">
									<div class="bg1 left"></div>
									<div class="bg2 right"></div>
									<div class="title">
										<h5>待办任务</h5>
									</div>
								</div>
								<div class="table" autoResize="true" resizeHeight='25' resizeWidth="true">
									<table id="datagrid" class="easyui-datagrid"
										data-options="data:[],fitColumns:true,striped:false,loadMsg:'数据加载中...',
										url:'${ctx }/workflow/queryWorkOrder/myComplete',
										ctrlSelect:false,fit:true,onDblClickRow:openInfo,
										pagination:true,pageSize:20,pageList:[10,20,30,40,50],border:false"
										autoResize="true" componentType='datagrid' resizeHeight='0' resizeWidth="true">
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
					</div>
				</div>
			</td>
	</table>
	<script type="text/javascript">
		navList(1);
	</script>
</body>
</html>
