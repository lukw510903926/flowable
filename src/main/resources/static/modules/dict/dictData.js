$.namespace("dict.value");

$(function () {
    dict.value.loadData();
});
dict.value = {

    loadData: function () {

        $("#dictDataTable").bootstrapTable({
            method: "post",
            url: "/dictValue/list",
            contentType: "application/x-www-form-urlencoded",
            pagination: true,
            sidePagination: 'server',
            pageSize: 20,
            pageList: [10, 25, 50, 100],
            clickToSelect: true,
            queryParams: function queryParams(param) {
                param['dictTypeId'] = typeId;
                return param;
            },
            columns: [
                {
                    field: "state",
                    checkbox: true,
                    align: "center"
                }, {
                    field: "name",
                    title: "名称",
                    align: "center"
                }, {
                    field: "code",
                    title: "编码",
                    align: "center"
                }, {
                    field: "createTime",
                    title: "创建时间",
                    align: "center"
                }]
        });
    },
    edit: function () {
        var rows = $("#dictDataTable").bootstrapTable("getSelections");
        if (rows.length < 1 || rows.length > 1) {
            layer.msg("请选择一行!");
            return;
        }
        $('#attachmentModal').modal('show');
        $('#dictForm').setForm(rows[0]);
    },

    delDict: function () {

        var rows = $("#dictDataTable").bootstrapTable("getSelections");
        var list = [];
        if (rows.length < 1) {
            layer.msg("请选择一行!");
            return;
        }
        rows.forEach(function (entity) {
            list.push(entity.id);
        });
        $.ajax({
            type: "POST",
            url: path + '/dictValue/delete',
            dataType: 'json',
            contentType: "application/json",
            data: JSON.stringify(list),
            traditional: true,
            async: false,
            success: function (data) {
                if(data.success){
                    $("#dictDataTable").bootstrapTable("refresh");
                }else {
                    bsAlert("错误",data.msg);
                }
            }
        });
    },

    add: function () {

        let $dictForm = $('#dictForm');
        let validateForm = $dictForm.validateForm();
        if(!validateForm){
            layer.msg('表单校验失败');
            return;
        }
        let form = $dictForm.getForm();
        form['dictTypeId'] = typeId;
        $.ajax({
            type: "POST",
            url: path + '/dictValue/save',
            data: form,
            async: false,
            traditional: true,
            success: function (data) {
                if (data.success) {
                    layer.msg(data.msg);
                    $('#attachmentModal').modal('hide');
                    $('.modal-backdrop').filter('.fade').filter('.in').remove();
                    $("#dictDataTable").bootstrapTable('refresh');
                    $('#dictForm').clearForm();
                } else {
                    bsAlert("提示", data.msg);
                }
            }
        });
    },
};