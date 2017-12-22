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

<title>运行中流程列表</title>
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
		
		$('#rightMenu').menu({
			onClick:function(item){
				var url = "";
				var row = $("#datagrid").datagrid("getSelected");
				if(row != null){
					var version = row.version;
					switch(item.text){
					case "删除流程实例":
						$.messager.confirm('提示','确定要删除流程实例吗?',function(r){   
						    if (r){   
						    	$.messager.progress(); 
						        $.ajax({
									url : "${ctx}/act/process/deleteProcIns",
									data : {
										procInsId : row.processInstanceId,
										reason : '人为删除'
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
	
</script>
</head>

<body style="overflow: hidden;background-color: #FFFFFF;padding-left: 10px;padding-right: 25px;">
	<div class="con_bg">
		<div class="con_itembox">
			<div class="table"  autoResize="true" resizeHeight='5'
					resizeWidth="false">
				<table id="datagrid" class="easyui-datagrid"
					data-options="data:[],fitColumns:true,striped:false,loadMsg:'数据加载中...',
					url:'${ctx }/act/process/running',
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
							<th data-options="field:'id',width:100">执行ID</th>
							<th data-options="field:'processInstanceId',width:150">流程实例ID</th>
							<th data-options="field:'processDefinitionId',width:100">流程定于ID</th>
							<th data-options="field:'activityId',width:100">当前环节</th>
							<th data-options="field:'suspended',width:100">是否挂起</th>
							<th data-options="field:'taskAssignee',width:100">操作</th>
						</tr>
					</thead>
				</table>
			</div>
		</div>
	</div>
	
	<div id="rightMenu" class="easyui-menu">
		<div>删除流程实例</div>
	</div>
	
	<div id="popUpWin" class="easyui-window"
		style="width:900px;padding: 10px;overflow: hidden;height:500px;"
		data-options="collapsible:false,minimizable:false,maximizable:false,resizable:false,modal:true,closed:true">
		
	</div>
</body>
</html>

