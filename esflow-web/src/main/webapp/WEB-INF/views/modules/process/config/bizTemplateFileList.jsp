<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<base href="${ctx}">
<meta name="decorator" content="default" />
<title>流程模板文件管理</title>
</head>
<body>
	<link rel="stylesheet"
		href="${ctxPlugins}/bootstrap/bs-select/css/bootstrap-select.min.css">
	<script src="${ctxPlugins}/bootstrap/bs-select/bootstrap-select.min.js"></script>
	<script
		src="${ctxPlugins}/bootstrap/bs-select//i18n/defaults-zh_CN.min.js"></script>
	<script type="text/javascript"
		src="${ctx}/js/modules/process/config/templateFileList.js"></script>

	<div class="panel panel-box">
		<div class="panel-heading">
			模版查询
		</div>
		<div class="panel-body">
			<div class="mr5">
				<form class="form-horizontal" role="form" id="queryForm">
					<table class="sea-form" width="100%">
						<tr>
							<th width="12%">所属流程：</th>
							<td width="38%"><select id='flowSelect1' name="flowName" class="form-control"></select></td>
							<th width="12%">附件名称：</th>
							<td width="38%"><input type='text' name='fileName' class="form-control"/></td>
						</tr>
						<tr>
							<td colspan="4" align="center">
								<a class="btn btn-y mrr10" onclick="queryFile()">查询</a>
								<a class="btn btn-n" onclick="resetQueryForm()">重置</a>
							</td>
						</tr>
					</table>
				</form>
			</div>
		</div>
	</div>
	<div class="panel panel-ex">
		<div class="panel-heading">
			模板文件列表
			<span class="table_menu_w right">
				<a href="javascript:void(0);" data-toggle="modal" data-target="#attachmentModal">上传</a>
				<a href="javascript:void(0);" onclick="downLoad()">下载</a>
				<a href="javascript:void(0);" onclick="deleteFile()">删除</a>
				</span>
		</div>
		<div class="panel-body">
			<div class="base-table-wrap">
				<table id="filelist" class="base-table table-striped"></table>
			</div>
		</div>
	</div>
	<!-- 模态框（Modal） -->
	<div class="modal fade" id="attachmentModal" tabindex="-1"
		data-backdrop='static' role="dialog">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">&times;</button>
					<h5 class="modal-title">模板文件上传</h5>
				</div>
				<div class="modal-body">
					<form class="form-horizontal" role="form" id="uploadFileForm">
						<table class="sea-form" width="100%">
							<tr>
								<th width="15%"><span title="*" class="im">*</span>所属流程：</td>
								<td><select class="selectpicker"
									data-live-search="true" name="flowName" style="width : 50%"></select></td>
							</tr>
							<tr>
								<th><span title="*" class="im">*</span>选择附件：</td>
								<td><input type='file' multiple name='file' /></td>
							</tr>
							<tr>
								<td colspan="2" align="right"><a class="btn btn-y" onclick="upload()">保存</a> <a
									class="btn btn-n mrl10" id='closeBtn' data-dismiss="modal" >取消</a></td>
							</tr>
						</table>
					</form>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
