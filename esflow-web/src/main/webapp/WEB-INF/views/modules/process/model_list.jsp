<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<base href="${ctx}">
<title>模型列表</title>
<meta name="decorator" content="default" />

</head>

<body>
	<link rel="stylesheet" type="text/css" href="${ctxPlugins}/bootstrap/bs-table-1.8.1/bootstrap-table.css" >
	<script type="text/javascript" src="${ctxPlugins}/bootstrap/bs-table-1.8.1/locale/bootstrap-table-zh-CN.js"></script>
	<script type="text/javascript" src="${ctxPlugins}/bootstrap/bs-table-1.8.1/bootstrap-table.js"></script>
	<script type="text/javascript">
		$(function() {
			$("#newBtn").click(function() {
				var url = "${ctx}/process/model/create";
				window.open(url, "_blank");
				/*$("#popUpWin").window({
					title : "新建模型",
					content : getIframeContent("actModelCreate_iframe", url)
				});
				$("#popUpWin").window("center");
				$("#popUpWin").window("open");*/
			})

			$("#refreshBtn").click(function() {
				$('#datagrid').datagrid('reload');
			})
		})

		function openCreateView() {
			var url = "${ctx}/process/model/create";
			window.open(url, "_blank");
		}

		//得到Iframe形式的Content
		function getIframeContent(iframeId, iframeUrl) {
			var content = "<iframe name='"
					+ iframeId
					+ "' width=\"100%\" height=\"100%\" frameborder=\"0\" src=\""
					+ iframeUrl
					+ "\" style=\"width:100%;height:100%;\"></iframe>";
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
			return "<a target='_blank' onclick='openProcessResource(\""
					+ row.key + "\",\"xml\")'>" + value + "</a>";
		}

		function processImageFormatter(value, row, index) {
			return "<a target='_blank' onclick='openProcessResource(\""
					+ row.key + "\",\"image\")'>" + value + "</a>";
		}

		function openProcessResource(procDefId, resType) {
			var url = "${ctx}/act/process/resource/read?procDefId=" + procDefId
					+ "&resType=" + resType;
			window.parent.openTab(resType + "_" + procDefId, resType + "_"
					+ procDefId + "-详情", url);
		}
	</script>

	<div class="panel panel-ex">
		<div class="panel-heading">
			模型管理
			<%-- <div class="pull-right">
				<a href="${ctx}/model/create" target="_self"><span
					class="glyphicon glyphicon-file"></span>新建模型</a> <a
					href="javascript:void(0);" onclick="rmconfigtype()"><span
					class="glyphicon glyphicon-trash"></span>删除模型</a>
			</div> --%>
			<span class="table_menu_w right"> <a
				href="${ctx}/model/create" target="_self" >新建</a>
				<a href="javascript:void(0);" onclick="rmconfigtype()"
				>删除</a>
			</span>
		</div>
		<div class="panel-body">
			<div class="base-table-wrap">
				<table id="model-table" data-toggle="table"
					data-url="${ctx }/act/model/list" class="base-table table-striped">
					<thead>
						<tr>
							<th data-field="id">模型ID</th>
							<th data-field="key">模型标识</th>
							<th data-field="name">模型名称</th>
							<th data-field="version">版本号</th>
							<th data-field="createTime">创建时间</th>
							<th data-field="lastUpdateTime">更新时间</th>
							<th data-field="taskAssignee">操作</th>
						</tr>
					</thead>
				</table>
			</div>
		</div>
	</div>
</body>
</html>

