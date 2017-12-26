<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta name="decorator" content="default" />
<script type="text/javascript" src="${ctxPlugins}/jquery/jquery.form.js"></script>
<%-- <c:import url="/WEB-INF/pages/include/addTab.jsp" /> --%>
<script src="${ctx}/js/modules/dict/dictData.js"></script>
</head>
<body>
	<%-- <table class="quick_search" cellpadding="0" cellspacing="0"
		width="100%">
		<tr>
			<td width="70">快速检索：</td>
			<td><input id="quickSearch" type="text" class="search_txt" /></td>
			<td width="70"><a href="#" id="quickSearch_btn"
				class="search_btn">搜&nbsp;索</a></td>
			<td width="90">
				<!-- <a href="#" class="search_btn">搜&nbsp;索</a> --> <a
				data-toggle='modal'
				href="${ctx}/cim/HighSearch?id=<%=request.getParameter("dictId")%>"
				data-keyboard='false' data-backdrop='false' class="search_high ">高级检索</a>
			</td>
		</tr>
	</table> --%>
	<div class="panel panel-ex">
		<div class="panel-heading">
			编辑字典
			<div class="func-btn-list">
				<a id="doAdd"
					href="${ctx }/dict/addDataUI?dictId=<%=request.getParameter("dictId")%>"
					class="item-link mrl10">新增</a> <a id="doEdit" href="javascript:void(0);" onclick="editDictDataUI('<%=request.getParameter("dictId")%>')"
					class="item-link mrl10">编辑</a> <a id="doDelete" href="javascript:void(0);" onclick="delDict('<%=request.getParameter("dictId")%>',false)"
					class="item-link mrl10">删除</a><a id="allDelete" href="javascript:void(0);"onclick="delDict('<%=request.getParameter("dictId")%>',true)"
					class="item-link mrl10">全量删除</a> <a href="javascript:void(0);"
					data-toggle="modal" data-target="#myModal" class="item-link mrl10">批量导入</a>
					<!-- <a href="javascript:void(0);" class="item-link mrl10" onclick="doExport()">批量导出</a> -->
			</div>
		</div>
		<div class="panel-body">
			<div id="tabdiv" class="base-table-wrap">
				<table id="dictDataTable" data-dictid="<%=request.getParameter("dictId")%>" class="base-table table-striped">
				</table>
			</div>
		</div>
	</div>
	<!-- 	Modal -->
	<div class="modal  fade" id="myModal" tabindex="-1" role="dialog">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">×</button>
					<h5 class="modal-title">上传Excel文件</h5>
				</div>
				<div class="modal-body">
					<form id="uploadExcel" method="post" enctype="multipart/form-data" action="${ctx}/dict/uploadFile?dictId=${param.dictId}">
						<table style="margin:30px 0;">
							<tr>
								<td><input type="file" name="files" /></td>
							</tr>
						</table>
					</form>
				</div>
				<div class="modal-footer">
					<button href="javascript:void(0);" onclick="upload()"
						class="btn btn-y">导入</button>
					<button type="button" class="btn btn-n" data-dismiss="modal">关闭</button>
				</div>
			</div>
		</div>
	</div>
</body>
</html>