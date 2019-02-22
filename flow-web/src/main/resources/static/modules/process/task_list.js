$.namespace("biz.processVariable");
$(function () {
    biz.processVariable.loadTable();
});
biz.processVariable = {
    loadTable: function () {
        $("#process-task-table").bootstrapTable({
            method: 'get',
            queryParams: function queryParams(params) {
                params.processId = processId;
                return params;
            },
            sidePagination: 'server',
            url: path + "/act/process/processTaskList",
            pagination: false,
            pageSize: 20,
            pageList: [10, 25, 50],
            columns: [{
//			field : "state",
//			checkbox : true,
//			align : "center"
//		}, {
                field: "id",
                title: "任务ID",
                align: "left"
            }, {
                field: "name",
                title: "任务名称",
                align: "left"
            }, {
                field: "",
                title: "操作",
                align: "center",
                formatter: function (value, row) {
                    return "<a class='item-link' href='/process/variable?processDefinitionId=" + processId + "&version=" + version + "&taskId=" + row.id + "'>设置任务参数</a>";
                }
            }
            ]
        });
    },
    refreshData: function () {
        $('#process-task-table').bootstrapTable('refresh');
    }
};
