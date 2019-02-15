$.namespace("dictType");
$(function () {
    dictType.loadTable();
});

dictType = {

    loadTable: function () {
        $("#biz-table").bootstrapTable({
            method: "post",
            url: path + "/dictType/list",
            contentType: "application/x-www-form-urlencoded",
            pagination: true,
            sidePagination: 'server',
            clickToSelect: true,
            pageSize: 20,
            pageList: [10, 25, 50, 100],
            columns: [{
                field: "state",
                checkbox: true,
                align: "center"
            }, {
                field: "name",
                title: "名称",
                align: "center",
                formatter: function (value, row, index) {
                    if (value != null) {
                        return "<a href='" + path + "/dictValue/list/" + row.id + "'>" + value + "</a>";
                    }
                    return value;
                }
            }, {
                field: "createTime",
                title: "创建时间",
                align: "center"
            }]
        });
    },

    add: function () {

        var name = $('#name').val();
        if (!name) {
            bsAlert("提示", "字典名称不可为空");
            return;
        }
        var param = {};
        $('#dictTypeForm').find('[name]').each(function () {
            param[this.name] = $(this).val();
        });
        $.ajax({
            cache: true,
            type: "POST",
            url: path + '/dictType/save',
            data: param,
            async: false,
            success: function (data) {
                if (data.success) {
                    $('#attachmentModal').modal('hide');
                    $('.modal-backdrop').filter('.fade').filter('.in').remove();
                    $("#biz-table").bootstrapTable('refresh');
                    dictType.clearModel();
                } else {
                    bsAlert("提示", data.msg);
                }
            }
        });
    },
    clearModel: function () {
        $('.modal-title').text('新增字典');
        $('#dictTypeForm').find('[name]').each(function () {
            $(this).val('');
        });
    },

    editDictUI: function () {
        var rows = $("#biz-table").bootstrapTable("getSelections");
        if (rows.length !== 1) {
            layer.msg("请选择一行!");
            return;
        }
        $('#attachmentModal').modal('show');
        $('.modal-title').text('编辑字典');
        $('#dictTypeForm').find('[name]').each(function () {
            $(this).val(rows[0][this.name]);
        });
    },

    delDict: function () {

        var rows = $("#biz-table").bootstrapTable("getSelections");
        if (rows.length < 1) {
            layer.msg("请选择需删除的行!");
            return;
        }
        var list = [];
        rows.forEach(function (entity) {
            list.push(entity.id);
        });
        $.ajax({
            type: "POST",
            url: path + '/dictType/delete',
            dataType: 'json',
            contentType: "application/json",
            data: JSON.stringify(list),
            traditional: true,
            success: function (data) {
                if (data.success) {
                    $("#biz-table").bootstrapTable("refresh");
                    bsAlert("提示", '删除成功');
                } else {
                    bsAlert("提示", data.msg);
                }
            }
        });
    }
};

