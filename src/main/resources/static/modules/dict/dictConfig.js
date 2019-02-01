$(function () {
    loadDictConfigList();
});

function loadDictConfigList() {

    $("#biz-table").bootstrapTable({
        method: "post",
        url: path + "/dictType/list",
        contentType: "application/x-www-form-urlencoded",
        pagination: true,
        sidePagination: 'server',
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
}

function saveDictConfig() {

    $.ajax({
        cache: true,
        type: "POST",
        url: path + '/dictType/addDict',
        data: $('#dictConfigForm').serialize(),
        async: false,
        success: function (data) {
            window.location.href = path + "/dictType/config";
            layer.msg(data.msg);
        }
    });
}


function editDictUI() {
    var rows = $("#biz-table").bootstrapTable("getSelections");
    if (rows.length < 1 || rows.length > 1) {
        layer.msg("请选择一行!");
        return;
    }
    var dictId = rows[0].ID;
    window.location.href = path + '/dictType/addUI/edit?dictId=' + dictId;
}

function delDict() {

    var rows = $("#biz-table").bootstrapTable("getSelections");
    if (rows.length < 1) {
        layer.msg("请选择需删除的行!");
        return;
    }
    var ids = [];
    for (var i = 0; i < rows.length; i++) {
        ids.push(rows[i].ID);
    }
    $.ajax({
        cache: true,
        type: "POST",
        url: path + '/dictType/delDict',
        data: {ids: ids},
        traditional: true,
        async: false,
        success: function (data) {
            $("#biz-table").bootstrapTable("refresh");
            layer.msg(data.msg);
        }
    });
}