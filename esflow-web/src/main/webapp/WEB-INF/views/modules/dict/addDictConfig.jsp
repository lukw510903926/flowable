<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>

<title>新增字典配置</title>

<meta name="decorator" content="default" />
<script type="text/javascript" src="${ctx }/js/modules/dict/dictConfig.js"></script>
</head>
<body>
<div class="panel panel-box">
	<form id="dictConfigForm" onsubmit="return false;">
		<input type="hidden" name="id" value="${dictId}">
		<div class="panel-heading">
			新增字典配置
		</div>
		<div class="panel-body">
			<div class="mr5">
				<table cellpadding="0" cellspacing="0" class="form-horizontal sea-form">
					<tr>
						<th width="10%">字典名称：</th>
						<td width="40%"><input type="text" name="dictName" value="${dictName }" maxlength="32" class="form-control">
						<th width="10%">状态：</th>
						<td width="30%"><select name="status" class="form-control">
								<option <c:if test="${status=='0' }">selected</c:if> value="0">启用</option>
								<option <c:if test="${status=='1' }">selected</c:if> value="1">停用</option>
						</select></td>
						
					</tr>
					<!--<tr>
						 <td class="ttit">表名</td>
						<td><input type="text" name="nameEn" maxlength="32" /></td> -->
						<!-- <td class="ttit"></td>
						<td></td>
					</tr> -->
				</table>
			
				<div class="btn-list">
					<a href="javascript:void(0);" class="btn btn-y" onclick="saveDictConfig()">确定</a>
					<a href="javascript:void(0);" onclick="history.go(-1)"
						class="btn btn-n mrl10">取消</a>
				</div>
			</div>
		</div>
	</form>
</div>
</body>
</html>