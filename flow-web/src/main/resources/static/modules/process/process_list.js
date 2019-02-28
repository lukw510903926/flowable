$(function () {

    var processTable = $("#process-table");
    processTable.bootstrapTable({
        method: 'get',
        queryParams: function queryParams(params) {
            return params;
        },
        sidePagination: 'server',
        url: "/act/process/list",
        pagination: true,
        pageSize: 20,
        pageList: [10, 25, 50],
        columns: [{
            field: "id",
            title: "流程ID",
            align: "center"
        }, {
            field: "key",
            title: "流程标识",
            align: "center"
        }, {
            field: "name",
            title: "流程名称",
            align: "center"
        }, {
            field: "version",
            title: "流程版本",
            align: "center"
        }, {
            field: "resourceName",
            title: "流程XML",
            align: "center",
            formatter: function (index, row) {
                return "<a target='_blank' style='cursor: pointer'  onclick='openProcessResource(\"" + row.id + "\",\"xml\")'>查看XML</a>";
            }
        }, {
            field: "diagramResourceName",
            title: "流程图片",
            align: "center",
            formatter: function processImageFormatter(index, row) {
                return "<a target='_blank' style='cursor: pointer' onclick='openProcessResource(\"" + row.id + "\",\"image\")'>查看流程图</a>";
            }
        }, {
            field: "deploymentTime",
            title: "部署时间",
            align: "center"
        }]
    });
});

function openProcessResource(processDefinitionId, type) {
    window.open("/act/process/resource/read?processDefinitionId=" + processDefinitionId + "&type=" + type, "_blank");
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
    content += '		<button type="button" class="btn btn-y" onclick="window.open(\'/process/variable?processDefinitionId=' + row.id + '&taskId=START\', \'_self\');">设置流程参数</button>';
    content += '		<button type="button" class="btn btn-y" onclick="window.open(\'/process/task/list?processDefinitionId=' + row.id + '\', \'_self\');">查看流程任务</button>';
    content += '	</div>';
    content += '	<div class="btn-group" role="group" aria-label="...">';
    content += '		<button type="button" class="btn btn-y" onclick="window.open(\'/biz/create/' + row.key + '\');">创建工单</button>';
    content += '	</div>';
    content += '</div>';

    return content;
}

function updateState(id, state) {
    if (state !== "active" && state !== "suspend") {
        $.alert({
            title: "错误",
            content: "无效的状态.",
            autoClose: 'cancel|3000',
            confirmButton: '关闭',
            confirmButtonClass: 'btn-primary',
            icon: 'glyphicon glyphicon-remove-sign',
            animation: 'zoom',
            confirm: function () {
            }
        });
        return;
    }

    $.confirm({
        title: "提示",
        content: state === "active" ? "确定激活流程？" : "确定挂起流程？",
        // autoClose: 'cancel|6000',
        confirmButton: "确定",
        confirmButtonClass: "btn-primary",
        icon: "glyphicon glyphicon-question-sign",
        cancelButton: "取消",
        confirm: function () {
            $.ajax({
                url: "/act/process/update/" + state,
                data: {
                    processDefinitionId: id
                },
                dataType: 'json',
                success: function (result) {
                    if (result.success) {
                        bsAlert("提示", result.msg);
                        $("#process-table").bootstrapTable('refresh');
                    } else {
                        bsAlert("错误", result.msg);
                    }
                }
            });
        },
    });
}

function deleteDeployment(deploymentId) {
    $.confirm({
        title: "提示",
        content: "确定删除流程？",
        confirmButton: "确定",
        confirmButtonClass: "btn-primary",
        icon: "glyphicon glyphicon-question-sign",
        cancelButton: "取消",
        confirm: function () {
            $.ajax({
                url: "/act/process/delete",
                data: {
                    deploymentId: deploymentId
                },
                dataType: 'json',
                success: function (data) {
                    if (data.success) {
                        bsAlert("提示", data.msg);
                        $("#process-table").bootstrapTable('refresh');
                    } else {
                        bsAlert("错误", data.msg);
                    }
                }
            });
        }
    });
}

function convertToModel(id) {
    $.confirm({
        title: "提示",
        content: "确定将流程转换为模型？",
        confirmButton: "确定",
        confirmButtonClass: "btn-primary",
        icon: "glyphicon glyphicon-question-sign",
        cancelButton: "取消",
        confirm: function () {
            $.ajax({
                url: "/act/process/convert",
                data: {
                    processDefinitionId: id
                },
                dataType: 'json',
                success: function (data) {
                    if (data.success) {
                        bsAlert("提示", data.msg);
                        $("#process-table").bootstrapTable('refresh');
                    } else {
                        bsAlert("错误", data.msg);
                    }
                }
            });
        }
    });
}