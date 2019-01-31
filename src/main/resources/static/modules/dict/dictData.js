$(function () {
    $("#dictDataTable").bootstrapTable({
        method: "post",
        url: path + "/dictValue/list",
        contentType: "application/x-www-form-urlencoded",
        pagination: true,
        sidePagination: 'server',
        pageSize: 20,
        pageList: [10, 25, 50, 100],
        clickToSelect: true,
        queryParams: function queryParams(param) {
            param['dictTypeId'] = typeId
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
});

function saveDictData(dictId, dataId) {
    $.ajax({
        type: "POST",
        url: path + '/dictValue/addDictData',
        data: $('#dictDataForm').serialize(),
        async: false,
        traditional: true,
        success: function (data) {
            window.location.href = document.referrer;
            layer.msg(data.msg);
        }
    });

}


function editDictDataUI(dictId) {
    var rows = $("#dictDataTable").bootstrapTable("getSelections");
    if (rows.length < 1 || rows.length > 1) {
        layer.msg("请选择一行!");
        return;
    }
    var dataId = rows[0].ID;
    window.location.href = path + '/dict/addDataUI?dictId=' + dictId + '&dataId=' + dataId;
}

function delDict(dictId, isAll) {

    var rows = $("#dictDataTable").bootstrapTable("getSelections");
    var ids = new Array();
    if (!isAll) {
        if (rows.length < 1) {
            layer.msg("请选择一行!");
            return;
        }
        for (var i = 0; i < rows.length; i++) {
            ids.push(rows[i].id);
        }
    }
    $.ajax({
        type: "POST",
        url: path + '/dictValue/delDictData',
        data: {dictId: dictId, ids: ids, isAll: isAll},
        traditional: true,
        async: false,
        success: function (data) {
            $("#dictDataTable").bootstrapTable("refresh");
            layer.msg(data.msg);
        }
    });
}