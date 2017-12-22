<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/pages/include/taglib.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String action = request.getParameter("action");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">
<c:set var="action" value="<%=action%>"/>
<title>工单信息</title>
<%@include file="/pages/include/head.jsp"%>
<%@ include file="/pages/include/treeview.jsp"%>
<link rel="stylesheet" type="text/css"
	href="${ctx}/themes/default/css/workInfo.css">
<script type="text/javascript" src="${ctx}/js/work/workCount.js"></script>
<script>
	var action = "${action}";
</script>
</head>

<body
	style="overflow: auto;background-color: #FFFFFF;padding-left: 10px;padding-right: 10px;">
	<div class="con_bg">
		<div class="con_itembox">
			<div class="con_item_title">
				<div class="bg1 left"></div>
				<div class="bg2 right"></div>
				<div class="title">
					<h5>查询条件<input type="hidden" id="Query_Target" value="${target }"></h5>
				</div>
			</div>
			<div class="con_item_list" id="queryOpDevice">
				<form id="queryForm">
					<table width="100%" border="0" align="center" cellpadding="0"
						cellspacing="0" class="search">
						<tr>
							<td width="9%" height="30" class="td1">区域：</td>
							<td>
								<select id="area">
									<option value="all">全部</option>
									<option value="香港">香港</option>
								</select>
							</td>
							<td width="9%" class="td1">时间范围：</td>
							<td style="white-space: nowrap;"><input type="text" id="time1" readonly="readonly" class="Wdate"  ${pageCompRequired?"required='true'":"" }
							onFocus="WdatePicker({dateFmt:'yyyy-MM'})" style="width:180px;"
							/> - <input type="text" id="time2" readonly="readonly" class="Wdate"  ${pageCompRequired?"required='true'":"" }
							onFocus="WdatePicker({dateFmt:'yyyy-MM'})" style="width:180px;"
							/></td>
						</tr>
						<tr>
							<td align='center' colspan='6'><a class="btn1"
								href="javascript:void(0)" id="query_OK">查询<span /></span></a> <a
								class="btn1" href="javascript:void(0)" id="query_reset">重 置<span></span></a></td>
						</tr>
					</table>
				</form>
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
			<div class="table"  autoResize="true" resizeHeight='5'
					resizeWidth="false">
				<table id="datagrid" class="easyui-datagrid"
					data-options="data:[],fitColumns:true,striped:false,loadMsg:'数据加载中...',
					ctrlSelect:false,fit:true,
	pagination:false,border:false" autoResize="true" componentType='datagrid' resizeHeight='0'
					resizeWidth="false">
					<thead>
						<tr>
							<c:if test="${action=='gz'||action=='gz_level' }">
								<th data-options="field:'COUNT_TIME',width:60">统计时间</th>
								<th data-options="field:'AREA',width:80">区域</th>
								<c:if test="${action=='gz_level' }">
									<th data-options="field:'ALARM_LEVEL',width:80">告警级别</th>
								</c:if>
								<th data-options="field:'GDS',width:100,formatter:workCount.grid.parseValue.integer">故障数</th>
								<th data-options="field:'CSGDS',width:100,formatter:workCount.grid.parseValue.integer">超时故障数</th>
								<th data-options="field:'PJCLSC',width:100,formatter:workCount.grid.parseValue.timelong">处理平均时长</th>
								<th data-options="field:'YQJJS',width:100,formatter:workCount.grid.parseValue.integer">延期解决故障数</th>
								<th data-options="field:'HDCWS',width:100,formatter:workCount.grid.parseValue.integer">回单错误数</th>
								<th data-options="field:'BMGZL',width:100,formatter:workCount.grid.parseValue.rate">不明故障率</th>
								<th data-options="field:'JSWCL',width:100,formatter:workCount.grid.parseValue.rate">处理及时率</th>
								<th data-options="field:'YCWCL',width:100,formatter:workCount.grid.parseValue.rate">一次完成率</th>
								<th data-options="field:'YQJJL',width:100,formatter:workCount.grid.parseValue.rate">延期解决率</th>
								<th data-options="field:'LEVEL1JJL',width:100,formatter:workCount.grid.parseValue.rate">一级解决率</th>
								<th data-options="field:'LEVEL2JJL',width:100,formatter:workCount.grid.parseValue.rate">二级解决率</th>
								<th data-options="field:'LEVEL3JJL',width:100,formatter:workCount.grid.parseValue.rate">三级解决率</th>
								<th data-options="field:'ZDPDL',width:100,formatter:workCount.grid.parseValue.rate">自动派单率</th>
								<th data-options="field:'SDPDL',width:100,formatter:workCount.grid.parseValue.rate">手动派单率</th>
							</c:if>
							<c:if test="${action=='ty'}">
								<th data-options="field:'COUNT_TIME',width:60">统计时间</th>
								<th data-options="field:'GDS',width:100,formatter:workCount.grid.parseValue.integer">工单数</th>
								<th data-options="field:'CSGDS',width:100,formatter:workCount.grid.parseValue.integer">超时工单数</th>
								<th data-options="field:'CSGDL',width:100,formatter:workCount.grid.parseValue.rate">超时工单率</th>
								<th data-options="field:'PJCLSC',width:100,formatter:workCount.grid.parseValue.timelong">处理平均时长</th>
								<th data-options="field:'JSWCS',width:100,formatter:workCount.grid.parseValue.integer">及时完成数</th>
								<th data-options="field:'JSWCL',width:100,formatter:workCount.grid.parseValue.rate">及时完成率</th>
							</c:if>
							<c:if test="${action=='ts'}">
								<th data-options="field:'COUNT_TIME',width:60">统计时间</th>
								<th data-options="field:'GDS',width:100,formatter:workCount.grid.parseValue.integer">工单数</th>
								<th data-options="field:'CSGDS',width:100,formatter:workCount.grid.parseValue.integer">超时工单数</th>
								<th data-options="field:'PJCLSC',width:100,formatter:workCount.grid.parseValue.timelong">处理平均时长</th>
								<th data-options="field:'HDCWS',width:100,formatter:workCount.grid.parseValue.integer">回单错误数</th>
								<th data-options="field:'JSWCL',width:100,formatter:workCount.grid.parseValue.rate">处理及时率</th>
								<th data-options="field:'WTJJL',width:100,formatter:workCount.grid.parseValue.rate">问题解决率</th>
								<th data-options="field:'YCTGL',width:100,formatter:workCount.grid.parseValue.rate">一次通过率</th>
								<th data-options="field:'SBJSL',width:100,formatter:workCount.grid.parseValue.rate">上报及时率</th>
							</c:if>
							<c:if test="${action=='dl'}">
								<th data-options="field:'COUNT_TIME',width:60">统计时间</th>
								<th data-options="field:'GDS',width:100,formatter:workCount.grid.parseValue.integer">工单数</th>
								<th data-options="field:'CSGDS',width:100,formatter:workCount.grid.parseValue.integer">超时工单数</th>
								<th data-options="field:'CSGDL',width:100,formatter:workCount.grid.parseValue.rate">超时工单率</th>
								<th data-options="field:'PJCLSC',width:100,formatter:workCount.grid.parseValue.timelong">处理平均时长</th>
								<th data-options="field:'YCTGL',width:100,formatter:workCount.grid.parseValue.rate">一次通过率</th>
								<th data-options="field:'ZYMZL',width:100,formatter:workCount.grid.parseValue.rate">资源满足率</th>
								<th data-options="field:'ZYZQL',width:100,formatter:workCount.grid.parseValue.rate">资源准确率</th>
								<th data-options="field:'SSCGL',width:100,formatter:workCount.grid.parseValue.rate">实施成功率</th>
								<th data-options="field:'SSJSL',width:100,formatter:workCount.grid.parseValue.rate">实施及时率</th>
							</c:if>
							<%-- <c:if test="${action=='kpi' }">
							<th data-options="field:'AREA',width:80">区域</th>
							<th data-options="field:'GZS',width:100">故障数(单)</th>
							<th data-options="field:'CSGZS',width:100">超时故障数(单)</th>
							<th data-options="field:'CLJSL',width:100">处理及时率(%)</th>
							<th data-options="field:'CLPJSC',width:100">处理平均时长(小时)</th>
							<th data-options="field:'YYJJGZS',width:100">延期解决故障数(单)</th>
							<th data-options="field:'YYJJL',width:100">延期解决率(%)</th>
							</c:if>
							<c:if test="${action=='replyError' }">
							<th data-options="field:'AREA',width:80">区域</th>
							<th data-options="field:'GZS',width:100">故障数(单)</th>
							<th data-options="field:'HDCWS',width:100">回单错误工单数(单)</th>
							<th data-options="field:'ZDQCS',width:100">告警自动清除工单数(%)</th>
							<th data-options="field:'ZDQCBL',width:100">告警自动消除工单比例(小时)</th>
							<th data-options="field:'BMGZBL',width:100">不明故障原因工单比例(单)</th>
							</c:if>
							<c:if test="${action=='alarmLevel' }">
							<th data-options="field:'LEVAL',width:80">告警级别</th>
							<th data-options="field:'GZS',width:100">故障数(单)</th>
							<th data-options="field:'CSGZS',width:100">超时故障数(单)</th>
							<th data-options="field:'CLJSL',width:100">处理及时率(%)</th>
							<th data-options="field:'CLPJSCF',width:100">处理平均时长(小时)</th>
							</c:if> --%>
						</tr>
					</thead>
				</table>
			</div>
		</div>
	</div>





</body>
</html>


