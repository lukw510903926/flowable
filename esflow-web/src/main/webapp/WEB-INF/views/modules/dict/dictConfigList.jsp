<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>

<title>字典列表</title>
<meta name="decorator" content="default" />
</head>

<body>
	<script type="text/javascript">
		//var resList = '${resList}';
	</script>
	<script type="text/javascript"
		src="${ctx }/js/modules/dict/dictConfig.js"></script>

	<div class="panel panel-ex">
		<div class="panel-heading">
			字典列表 
			<span class="table_menu_w right"> <a id="addBtn"
					href='${ctx}/dict/addUI/add'>新增</a> <a
					id="editBtn" href="javascript:void(0);" onclick="editDictUI()"
					>编辑</a> <a id="delBtn"
					href="javascript:void(0);" onclick="delDict()">删除</a>
				</span>
			</h3>
		</div>
		<div class="panel-body">
			<div class="base-table-wrap">
				<table id="dictconfigTable" class="table_data base-table table-striped">
				</table>
			</div>
		</div>
	</div>
</body>
</html>
