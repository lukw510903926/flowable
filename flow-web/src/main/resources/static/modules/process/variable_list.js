$.namespace("biz.variable.list");
$(function () {
    if (taskId) {
        $("#htitle").html("流程任务参数管理");
    }
    biz.variable.list.loadTable();
});
biz.variable.list = {

    loadTable: function () {
        $("#variable-table").bootstrapTable({
            method: 'get',
            queryParams: function queryParams(params) {
                params.processId = processId;
                params.version = version;
                params.taskId = taskId;
                return params;
            },
            sidePagination: 'server',
            url: "/processModelMgr/processValList",
            pagination: true,
            pageSize: 20,
            pageList: [10, 25, 50],
            clickToSelect: true,
            columns: [{
                field: "state",
                checkbox: true,
                align: "center"
            }, {
                field: "name",
                title: "属性名称",
                align: "center"
            }, {
                field: "alias",
                title: "属性别名",
                align: "center"
            }, {
                field: "order",
                title: "属性排序",
                align: "center"
            }, {
                field: "required",
                title: "是否必填",
                align: "center",
                formatter: function truefalseValue(value) {
                    return "true" === value || value ? "是" : "否";
                }
            }, {
                field: "groupName",
                title: "属性分组",
                align: "center"
            }, {
                field: "groupOrder",
                title: "属性分组排序",
                align: "center"
            }, {
                field: "viewComponent",
                title: "页面组件类型",
                align: "center"
            }, {
                field: "version",
                title: "版本",
                align: "center"
            }, {
                field: "processVariable",
                title: "是否流程变量",
                align: "center",
                formatter: function truefalseValue(value) {
                    return "true" === value || value ? "是" : "否";
                }
            }]
        });
    },
    addData: function () {
        biz.variable.list.openVarEditPage('');
    },

    editData: function () {
        var selectRows = $("#variable-table").bootstrapTable("getSelections");
        if (selectRows.length === 0) {
            bsAlert('提示', '请选择需要修改的属性');
            return;
        }
        if (selectRows.length > 1) {
            bsAlert('提示', '只能选择一条进行修改');
            return;
        }
        biz.variable.list.openVarEditPage(selectRows[0].id);
    },

    openVarEditPage: function (varId) {
        window.open(path + "/process/variable/edit?processDefinitionId=" + processId + "&taskId=" + taskId + "&vId="
            + varId, "_self");
    },

    delData: function () {
        var rows = $("#variable-table").bootstrapTable("getSelections");
        if (rows.length < 1) {
            bsAlert('提示', "请选择删除行!");
            return;
        }
        var valIds = "";
        for (var i = 0; i < rows.length; i++) {
            valIds += rows[i].id;
            if (i < rows.length - 1) {
                valIds += ',';
            }
        }
        $.confirm({
            title: "提示",
            content: "确定删除选择的参数?",
            confirmButton: "确定",
            confirmButtonClass: "btn-primary",
            icon: "glyphicon glyphicon-question-sign",
            cancelButton: "取消",
            confirm: function () {

                $.ajax({
                    url: path + "/processModelMgr/deleteProcessValById",
                    data: {
                        valIds: valIds
                    },
                    dataType: 'json',
                    success: function (data) {
                        if (data.success) {
                            bsAlert("提示", data.msg);
                            biz.variable.list.refreshTable();
                        } else {
                            bsAlert("错误", data.msg);
                        }
                    }
                });
            },
        });
    },

    refreshTable: function () {
        $("#variable-table").bootstrapTable('refresh');
    }
};


