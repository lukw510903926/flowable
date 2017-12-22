var processTable;
$(function() {

	function valueFormatter(value, row) {
		if (value && value.length > 13) {
			return "<i title='" + value + "'>" + value.substring(0, 10) + "...</i>";
		} else {
			return value;
		}
	}

	processTable = $("#process-table");
	processTable.bootstrapTable({
		method : 'get',
		queryParams : function queryParams(params) {
			return params;
		},
		sidePagination : 'server',
		url : path + "/act/process/list",
		pagination : true,
		pageSize : 20,
		pageList : [ 10, 25, 50 ],
		columns : [ {
			field : "id",
			title : "流程ID",
			align : "left"
		}, {
			field : "key",
			title : "流程标识",
			align : "left"
		}, {
			field : "name",
			title : "流程名称",
			align : "left"
		}, {
			field : "version",
			title : "流程版本",
			align : "left"
		}, {
			field : "resourceName",
			title : "流程XML",
			align : "left",
			formatter : processXmlFormatter
		}, {
			field : "diagramResourceName",
			title : "流程图片",
			align : "left",
			formatter : processImageFormatter
		}, {
			field : "deploymentTime",
			title : "部署时间",
			align : "left"
		} ]
	});
});

function processXmlFormatter(index, row) {
	return "<a target='_blank' onclick='openProcessResource(\"" + row.id + "\",\"xml\")'>查看XML</a>";
}
function processImageFormatter(index, row) {
	return "<a target='_blank' onclick='openProcessResource(\"" + row.id + "\",\"image\")'>查看流程图</a>";
}

function openProcessResource(processDefinitionId, type) {
	var url = path + "/act/process/resource/read?processDefinitionId=" + processDefinitionId + "&type=" + type;
	window.open(url, "_blank");
}

function detailFormatter(index, row) {
	var content = '';
	content += '<div class="btn-toolbar" role="toolbar" aria-label="...">';
	content += '	<div class="btn-group" role="group" aria-label="...">';
	content += '		<button type="button" class="btn btn-y" onclick="updateState(\'' + row.id + '\', \'active\');">激活</button>';
	content += '		<button type="button" class="btn btn-y" onclick="updateState(\'' + row.id + '\', \'suspend\');">挂起</button>';
	content += '		<button type="button" class="btn btn-y" onclick="deleteDeployment(\'' + row.deploymentId + '\');">删除</button>';
	content += '	</div>';
	content += '	<div class="btn-group" role="group" aria-label="...">';
	content += '		<button type="button" class="btn btn-y" onclick="convertToModel(\'' + row.id + '\');">转换为模型</button>';
	content += '		<button type="button" class="btn btn-y" onclick="window.open(\'' + path + '/process/variable?processDefinitionId=' + row.id + '&version=' + row.version + '&taskId=\', \'_self\');">设置流程参数</button>';
	content += '		<button type="button" class="btn btn-y" onclick="window.open(\'' + path + '/process/task/list?processDefinitionId=' + row.id + '&version=' + row.version + '\', \'_self\');">查看流程任务</button>';
	content += '	</div>';
	content += '	<div class="btn-group" role="group" aria-label="...">';
	content += '		<button type="button" class="btn btn-y" onclick="window.open(\'' + path + '/biz/create/' + row.key + '\');">创建工单</button>';
	content += '	</div>';
	content += '</div>';

	return content;
}

function updateState(id, state) {
	if (state != "active" && state != "suspend") {
		$.alert({
			title : "错误",
			content : "无效的状态.",
			// content: 'This is some alert to the user. <br> with some
			// <strong>HTML</strong> <em>contents</em>',
			autoClose : 'cancel|3000',
			confirmButton : '关闭',
			confirmButtonClass : 'btn-primary',
			icon : 'glyphicon glyphicon-remove-sign',
			animation : 'zoom',
			confirm : function() {
				// alert('Okay action clicked.');
			}
		});
		return;
	}

	$.confirm({
		title : "提示",
		content : state == "active" ? "确定激活流程？" : "确定挂起流程？",
		// autoClose: 'cancel|6000',
		confirmButton : "确定",
		confirmButtonClass : "btn-primary",
		icon : "glyphicon glyphicon-question-sign",
		cancelButton : "取消",
		confirm : function() {
			$.ajax({
				url : path + "/act/process/update/" + state,
				data : {
					processDefinitionId : id
				},
				dataType : 'json',
				success : function(data) {
					if (data.success) {
						bsAlert("提示", data.msg);
						processTable.bootstrapTable('refresh');
					} else {
						bsAlert("错误", data.msg);
					}
				},
				error : function(re, status, err) {
					bsAlert("错误", re.responseText);
				}
			});
		},
	});
}

function deleteDeployment(deploymentId) {
	$.confirm({
		title : "提示",
		content : "确定删除流程？",
		confirmButton : "确定",
		confirmButtonClass : "btn-primary",
		icon : "glyphicon glyphicon-question-sign",
		cancelButton : "取消",
		confirm : function() {
			$.ajax({
				url : path + "/act/process/delete",
				data : {
					deploymentId : deploymentId
				},
				dataType : 'json',
				success : function(data) {
					if (data.success) {
						bsAlert("提示", data.msg);
						processTable.bootstrapTable('refresh');
					} else {
						bsAlert("错误", data.msg);
					}
				},
				error : function(re, status, err) {
					bsAlert("错误", re.responseText);
				}
			});
		},
		cancel : function() {
			// alert('Vacation cancelled!');
		}
	});
}

function convertToModel(id) {
	$.confirm({
		title : "提示",
		content : "确定将流程转换为模型？",
		confirmButton : "确定",
		confirmButtonClass : "btn-primary",
		icon : "glyphicon glyphicon-question-sign",
		cancelButton : "取消",
		confirm : function() {
			$.ajax({
				url : path + "/act/process/convert",
				data : {
					processDefinitionId : id
				},
				dataType : 'json',
				success : function(data) {
					if (data.success) {
						bsAlert("提示", data.msg);
						processTable.bootstrapTable('refresh');
					} else {
						bsAlert("错误", data.msg);
					}
				},
				error : function(re, status, err) {
					bsAlert("错误", re.responseText);
				}
			});
		},
		cancel : function() {
			// alert('Vacation cancelled!');
		}
	});
}