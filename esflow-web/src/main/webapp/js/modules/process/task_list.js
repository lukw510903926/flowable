//$.namespace("processVariableSet");
$(function() {
	function truefalseValue(value) {
		return "true" == value || value == true ? "是" : "否";
	}
	$("#process-task-table").bootstrapTable({
		method : 'get',
		queryParams : function queryParams(params) {
			params.processId = processId;
			return params;
		},
		sidePagination : 'server',
		url : path + "/act/process/processTaskList",
		pagination : false,
		pageSize : 20,
		pageList : [ 10, 25, 50 ],
		columns : [ {
//			field : "state",
//			checkbox : true,
//			align : "center"
//		}, {
			field : "id",
			title : "任务ID",
			align : "left"
		}, {
			field : "name",
			title : "任务名称",
			align : "left"
		}, {
			field : "",
			title : "操作",
			align : "center",
			formatter: function(value, row, index) {
				return "<a class='item-link' href='"+path + "/process/variable?processDefinitionId="+processId+"&version="+version+"&taskId="+row.id+"'>设置任务参数</a>";
			}
		}
		]
	});

});

function refreshData() {
	$('#process-task-table').bootstrapTable('refresh');
}
