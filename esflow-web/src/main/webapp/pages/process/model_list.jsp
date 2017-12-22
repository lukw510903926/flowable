<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/pages/include/taglib.jsp"%>

<!DOCTYPE html>
<html style="overflow:hidden;">
<head>
<base href="<%=basePath%>">

<title>模型列表</title>
<%@include file="/pages/include/head.jsp"%>
<%@ include file="/pages/include/treeview.jsp"%>
<link rel="stylesheet" type="text/css" href="${ctx}/themes/default/css/workInfo.css">
<script type="text/javascript" src="${ctx}/js/common/leftTree.js"></script>
<script type="text/javascript">
	$(function() {

		$("#newBtn").click(function() {
			var url = "${ctx}/pages/process/act/actModelCreate.jsp";
			$("#popUpWin").window({
				title : "新建模型",
				content : getIframeContent("actModelCreate_iframe", url)
			});
			$("#popUpWin").window("center");
			$("#popUpWin").window("open");
		})

		$("#refreshBtn").click(function() {
			$('#datagrid').datagrid('reload');
		})

		$('#rightMenu')
				.menu(
						{
							onClick : function(item) {
								var url = "";
								var row = $("#datagrid")
										.datagrid("getSelected");
								if (row != null) {
									var version = row.version;
									switch (item.text) {
									case "编辑模型":
										var url = "../activiti-explorer/modeler.html?modelId="
												+ row.id;
										window.parent.openTab(row.id, "模型ID:"
												+ row.id + " 编辑流程", url);
										break;
									case "删除模型":
										$.messager
												.confirm(
														'提示',
														'确定要删除吗?',
														function(r) {
															if (r) {
																$.messager
																		.progress();
																$
																		.ajax({
																			url : "${ctx}/act/model/delete",
																			data : {
																				id : row.id
																			},
																			dataType : 'json',
																			success : function(
																					data) {
																				$.messager
																						.progress('close');
																				if (data.success) {
																					$.messager
																							.alert(
																									"提示",
																									data.msg);
																					$(
																							'#datagrid')
																							.datagrid(
																									'reload');
																				} else {
																					$.messager
																							.alert(
																									"错误",
																									data.msg,
																									"error");
																				}
																			},
																			error : function(
																					re,
																					status,
																					err) {
																				$.messager
																						.progress('close');
																				$.messager
																						.alert(
																								"错误",
																								re.responseText,
																								"error");
																			}
																		});
															}
														});
										break;
									case "导出模型":
										var url = "${ctx}/act/model/export?id="
												+ row.id;
										window.open(url, "_blank");
										break;
									case "部署模型":
										$.messager
												.confirm(
														'提示',
														'确定要部署吗?',
														function(r) {
															if (r) {
																$.messager
																		.progress();
																$
																		.ajax({
																			url : "${ctx}/act/model/deploy",
																			data : {
																				id : row.id
																			},
																			dataType : 'json',
																			success : function(
																					data) {
																				$.messager
																						.progress('close');
																				if (data.success) {
																					$.messager
																							.alert(
																									"提示",
																									data.msg);
																					$(
																							'#datagrid')
																							.datagrid(
																									'reload');
																				} else {
																					$.messager
																							.alert(
																									"错误",
																									data.msg,
																									"error");
																				}
																			},
																			error : function(
																					re,
																					status,
																					err) {
																				$.messager
																						.progress('close');
																				$.messager
																						.alert(
																								"错误",
																								re.responseText,
																								"error");
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
	function getIframeContent(iframeId, iframeUrl) {
		var content = "<iframe name='" + iframeId
				+ "' width=\"100%\" height=\"100%\" frameborder=\"0\" src=\""
				+ iframeUrl + "\" style=\"width:100%;height:100%;\"></iframe>";
		return content
	}

	function deploySuccessHandler(modelId) {
		$("#popUpWin").window("close");
		$("#datagrid").datagrid("reload");
		//打开编辑界面
		var url = "../activiti-explorer/modeler.html?modelId=" + modelId;
		window.parent.openTab(modelId, "模型ID:" + modelId + " 编辑流程", url);
	}

	function processXmlFormatter(value, row, index) {
		return "<a target='_blank' onclick='openProcessResource(\"" + row.key
				+ "\",\"xml\")'>" + value + "</a>";
	}
	function processImageFormatter(value, row, index) {
		return "<a target='_blank' onclick='openProcessResource(\"" + row.key
				+ "\",\"image\")'>" + value + "</a>";
	}

	function openProcessResource(procDefId, resType) {
		var url = "${ctx}/act/process/resource/read?procDefId=" + procDefId
				+ "&resType=" + resType;
		window.parent.openTab(resType + "_" + procDefId, resType + "_"
				+ procDefId + "-详情", url);
	}
</script>
</head>

<body style="overflow: hidden;background-color: #FFFFFF;padding-left: 10px;padding-right: 25px;">
	<div class="con_bg">
		<div class="con_itembox">
			<a class="btn1" href="javascript:void(0)" id="newBtn">新建流程<span /></span></a> <a class="btn1" href="javascript:void(0)" id="refreshBtn">刷新<span /></span></a>
		</div>
		<div class="con_itembox">
			<div class="table" autoResize="true" resizeHeight='5' resizeWidth="false">
				<table id="datagrid" class="easyui-datagrid"
					data-options="data:[],fitColumns:true,striped:false,loadMsg:'数据加载中...',
					url:'${ctx }/act/model/list',
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
					autoResize="true" componentType='datagrid' resizeHeight='0' resizeWidth="false">
					<thead>
						<tr>
							<th data-options="field:'id',width:100">模型ID</th>
							<th data-options="field:'key',width:150">模型标识</th>
							<th data-options="field:'name',width:100">模型名称</th>
							<th data-options="field:'version',width:100">版本号</th>
							<th data-options="field:'createTime',width:100">创建时间</th>
							<th data-options="field:'lastUpdateTime',width:100">最后更新时间</th>
							<th data-options="field:'taskAssignee',width:100">操作</th>
						</tr>
					</thead>
				</table>
			</div>
		</div>
	</div>

	<div id="rightMenu" class="easyui-menu">
		<div>编辑模型</div>
		<div>删除模型</div>
		<div>导出模型</div>
		<div>部署模型</div>
	</div>

	<div id="popUpWin" class="easyui-window" style="width:900px;padding: 10px;overflow: hidden;height:500px;"
		data-options="collapsible:false,minimizable:false,maximizable:false,resizable:false,modal:true,closed:true"></div>
</body>
</html>