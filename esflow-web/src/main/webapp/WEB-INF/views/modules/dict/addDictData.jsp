<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>

<title></title>

<meta name="decorator" content="default" />
<script type="text/javascript" src="${ctx }/js/modules/dict/dictData.js"></script>
<script type="text/javascript">
$(function(){
	var p = "${operateType}";
	if(p=='add'){
		$('title').text('新增字典配置');
	}else{
		$('title').text('编辑字典配置');	
	console.log('type1:'+p);
	}
});
</script>
</head>
<body>
<div class="panel panel-box">
	<form id="dictDataForm" onsubmit="return false;">
		<input type="hidden" id="dictId" name="dictId" value="${dictId}">
		<input type="hidden" id="dataId" name="dataId" value="${dataId}">
		<div class="panel-heading">
			字典配置
		</div>
		<div class="panel-body">
			<div class="mr5">
				<table cellpadding="0" cellspacing="0" class="form-horizontal sea-form">
					<tr>
						<th width="10%">字典名称：</td>
						<td width="40%"><input type="text" name="name" value="${name }" maxlength="32" class="form-control">
						<%-- <td class="ttit" width="10%">状态</td>
						<td width="30%"><select name="status">
								<option <c:if test="${status=='0' }">selected</c:if> value="0">启用</option>
								<option <c:if test="${status=='1' }">selected</c:if> value="1">停用</option>
						</select></td> --%>
						<td>
							<a href="javascript:void(0);" class="btn btn-y mrl10" onclick="saveDictData()">确定</a>
							<a href="javascript:void(0);" onclick="history.go(-1)" class="btn btn-n mrl10">取消</a>
						</td>
					</tr>
					<!--<tr>
						 <td class="ttit">表名</td>
						<td><input type="text" name="nameEn" maxlength="32" /></td> -->
						<!-- <td class="ttit"></td>
						<td></td>
					</tr> -->
				</table>
			</div>
		</div>
	</form>
</div>
</body>
</html>