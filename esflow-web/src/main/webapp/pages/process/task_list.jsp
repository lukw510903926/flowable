<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/pages/include/taglib.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	
	String processId = request.getParameter("processId");
	String version = request.getParameter("version");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title>流程任务列表</title>
<%@include file="/pages/include/head.jsp"%>
<%@ include file="/pages/include/treeview.jsp"%>
<link rel="stylesheet" type="text/css"
	href="${ctx}/themes/default/css/workInfo.css">
<script type="text/javascript" src="${ctx}/js/common/leftTree.js"></script>
<script type="text/javascript">

var processId="<%=processId%>";
var version="<%=version%>";

$(function(){
	
	$('#rightMenu').menu({
		onClick:function(item){
			var url = "";
			var row = $("#datagrid").datagrid("getSelected");
			if(row != null){
				var processId = "<%=processId%>";
				var version = "<%=version%>";
				var taskId = row.id;
				switch(item.text){
				case "设置任务参数":
					var url = "${ctx}/pages/process/task_val_setting.jsp?processId=" + processId + "&version=" + version + "&taskId=" + taskId;
					$("#popUpWin").window({
						title : "任务参数设置 processId=" + processId + " Version=" + version + " TaskId=" + taskId,
						content : getIframeContent("task_val_setting_iframe", url)
					});
					$("#popUpWin").window("center");
					$("#popUpWin").window("open");
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
					url:'${ctx }/act/process/processTaskList',
					onBeforeLoad:function(param){
						param.processId='<%=processId%>';
						param.version='<%=version%>';
					},
					ctrlSelect:false,singleSelect:true,fit:true,border:false,onRowContextMenu : function(e, rowIndex, rowData){			
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
							<th data-options="field:'id',width:100">环节ID</th>
							<th data-options="field:'name',width:100">环节名称</th>
						</tr>
					</thead>
				</table>
			</div>
		</div>
	</div>
	
	<div id="rightMenu" class="easyui-menu">
		<div>设置任务参数</div>
	</div>
	
	<div id="popUpWin" class="easyui-window"
		style="width:900px;padding: 10px;overflow: hidden;height:500px;"
		data-options="collapsible:false,minimizable:false,maximizable:false,resizable:false,modal:true,closed:true">
		
	</div>
	
</body>
</html>

