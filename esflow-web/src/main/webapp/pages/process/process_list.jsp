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

<title>流程列表</title>
<%@include file="/pages/include/head.jsp"%>
<%@ include file="/pages/include/treeview.jsp"%>
<link rel="stylesheet" type="text/css"
	href="${ctx}/themes/default/css/workInfo.css">
<script type="text/javascript" src="${ctx}/js/common/leftTree.js"></script>
<script type="text/javascript">
	$(function(){
		
		$("#deployBtn").click(function(){
			var url = "${ctx}/pages/process/act/actProcessDeploy.jsp";
			$("#popUpWin").window({
				title : "部署流程",
				content : getIframeContent("actProcessDeploy_iframe", url)
			});
			$("#popUpWin").window("center");
			$("#popUpWin").window("open");
		})
		
		$("#refreshBtn").click(function(){
			$('#datagrid').datagrid('reload');
		})
		
		$('#rightMenu').menu({
			onClick:function(item){
				var url = "";
				var row = $("#datagrid").datagrid("getSelected");
				if(row != null){
					var version = row.version;
					switch(item.text){
					case "激活流程":
						$.messager.confirm('提示','确定要激活流程吗?',function(r){   
						    if (r){   
						    	$.messager.progress(); 
						        $.ajax({
									url : "${ctx}/act/process/update/active",
									data : {
										procDefId : row.id
									},
									dataType : 'json',
									success : function(data) {
										$.messager.progress('close'); 
										if (data.success) {
											$.messager.alert("提示", data.msg);
											$('#datagrid').datagrid('reload');
										} else {
											$.messager.alert("错误", data.msg, "error");
										}
									},
									error : function(re, status, err) {
										$.messager.progress('close'); 
										$.messager.alert("错误", re.responseText, "error");
									}
								});
						    }   
						});
						break;
					case "挂起流程":
						$.messager.confirm('提示','确定要挂起流程吗?',function(r){   
						    if (r){   
						    	$.messager.progress(); 
						        $.ajax({
									url : "${ctx}/act/process/update/suspend",
									data : {
										procDefId : row.id
									},
									dataType : 'json',
									success : function(data) {
										$.messager.progress('close'); 
										if (data.success) {
											$.messager.alert("提示", data.msg);
											$('#datagrid').datagrid('reload');
										} else {
											$.messager.alert("错误", data.msg, "error");
										}
									},
									error : function(re, status, err) {
										$.messager.progress('close'); 
										$.messager.alert("错误", re.responseText, "error");
									}
								});
						    }   
						});
						break;
					case "删除流程":
						$.messager.confirm('提示','确定要删除流程吗?',function(r){   
						    if (r){   
						    	$.messager.progress(); 
						        $.ajax({
									url : "${ctx}/act/process/delete",
									data : {
										deploymentId : row.deploymentId
									},
									dataType : 'json',
									success : function(data) {
										$.messager.progress('close'); 
										if (data.success) {
											$.messager.alert("提示", data.msg);
											$('#datagrid').datagrid('reload');
										} else {
											$.messager.alert("错误", data.msg, "error");
										}
									},
									error : function(re, status, err) {
										$.messager.progress('close'); 
										$.messager.alert("错误", re.responseText, "error");
									}
								});
						    }   
						});
						break;
					case "将流程转换为模型":
						$.messager.confirm('提示','确定要将流程转换为模型吗?',function(r){   
						    if (r){   
						    	$.messager.progress(); 
						        $.ajax({
									url : "${ctx}/act/process/convert/toModel",
									data : {
										procDefId : row.id
									},
									dataType : 'json',
									success : function(data) {
										$.messager.progress('close'); 
										if (data.success) {
											$.messager.alert("提示", data.msg);
											$('#datagrid').datagrid('reload');
										} else {
											$.messager.alert("错误", data.msg, "error");
										}
									},
									error : function(re, status, err) {
										$.messager.progress('close'); 
										$.messager.alert("错误", re.responseText, "error");
									}
								});
						    }   
						});
						break;
					case "设置流程参数":
						var url = "${ctx}/pages/process/process_val_setting.jsp?processId=" + row.key + "&version=" + version;
						$("#popUpWin").window({
							title : "流程参数设置 ID=" + row.key + " Version=" + version,
							content : getIframeContent("process_val_setting_iframe", url)
						});
						$("#popUpWin").window("center");
						$("#popUpWin").window("open");
						break;
					case "查看流程任务列表":
						window.parent.openTab(row.key,row.key + "-" + row.version + "-流程任务列表","${ctx}/pages/process/task_list.jsp?processId=" + row.key + "&version=" + version);
						break;
					default:
						$.messager.alert("提示", "功能未实现");
					break;
					}
				}
			}
		})
	})
	
	//得到Iframe形式的Content
	function getIframeContent(iframeId,iframeUrl){
		var content = "<iframe name='" + iframeId + "' width=\"100%\" height=\"100%\" frameborder=\"0\" src=\"" + iframeUrl + "\" style=\"width:100%;height:100%;\"></iframe>";
		return content
	}
	
	function deploySuccessHandler(){
		$("#popUpWin").window("close");
		$("#datagrid").datagrid("reload");
	}
	
	function processXmlFormatter(value,row,index){
		return "<a target='_blank' onclick='openProcessResource(\"" + row.key + "\",\"xml\")'>" + value + "</a>";
	}
	function processImageFormatter(value,row,index){
		return "<a target='_blank' onclick='openProcessResource(\"" + row.key + "\",\"image\")'>" + value + "</a>";
	}
	
	function openProcessResource(procDefId,resType){
		var url = "${ctx}/act/process/resource/read?procDefId=" + procDefId + "&resType=" + resType;
		window.parent.openTab(resType + "_" + procDefId,resType + "_" + procDefId + "-详情",url);
	}
	
</script>
</head>

<body style="overflow: hidden;background-color: #FFFFFF;padding-left: 10px;padding-right: 25px;">
	<div class="con_bg">
		<div class="con_itembox">
			<a class="btn1" href="javascript:void(0)" id="deployBtn">部署流程<span /></span></a>
			<a class="btn1" href="javascript:void(0)" id="refreshBtn">刷新<span /></span></a>
		</div>
		<div class="con_itembox">
			<div class="table"  autoResize="true" resizeHeight='5'
					resizeWidth="false">
				<table id="datagrid" class="easyui-datagrid"
					data-options="data:[],fitColumns:true,striped:false,loadMsg:'数据加载中...',
					url:'${ctx }/act/process/list',
					ctrlSelect:false,singleSelect:true,fit:true,pagination:true,pageSize:20,border:false,onRowContextMenu : function(e, rowIndex, rowData){			
						e.preventDefault();
						$('#rightMenu').menu('show', {
							left : e.pageX,
							top : e.pageY
						});
						// 选中当前右键项
						$('#datagrid').datagrid('selectRow',
								rowIndex);
					}"
					autoResize="true" componentType='datagrid' resizeHeight='0'
					resizeWidth="false">
					<thead>
						<tr>
							<th data-options="field:'id',width:100">流程ID</th>
							<th data-options="field:'key',width:150">流程标识</th>
							<th data-options="field:'name',width:100">流程名称</th>
							<th data-options="field:'version',width:100">流程版本</th>
							<th data-options="field:'resourceName',width:100,formatter:processXmlFormatter">流程XML</th>
							<th data-options="field:'diagramResourceName',width:100,formatter:processImageFormatter">流程图片</th>
							<th data-options="field:'deploymentTime',width:100">部署时间</th>
							<th data-options="field:'taskAssignee',width:100">操作</th>
						</tr>
					</thead>
				</table>
			</div>
		</div>
	</div>
	
	<div id="rightMenu" class="easyui-menu">
		<div>设置流程参数</div>
		<div>查看流程任务列表</div>
		<!-- <div>激活流程</div>
		<div>挂起流程</div> -->
		<div>删除流程</div>
		<div>将流程转换为模型</div>
	</div>
	
	<div id="popUpWin" class="easyui-window"
		style="width:900px;padding: 10px;overflow: hidden;height:500px;"
		data-options="collapsible:false,minimizable:false,maximizable:false,resizable:false,modal:true,closed:true">
		
	</div>
</body>
</html>

