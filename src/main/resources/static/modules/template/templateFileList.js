$.namespace("template.file");
$(function () {
    template.file.init();
});

template.file = {

    init: function () {
        template.file.flowCombobox($("[name='flowName']"));
        template.file.loadTable();
    },

    loadTable: function () {

        $("#fileList").bootstrapTable({
            method: 'post',
            contentType: "application/x-www-form-urlencoded",
            url: path + "/bizTemplateFile/list",
            pagination: true,
            sidePagination: 'server',
            pageSize: 20,
            pageList: [10, 25, 50, 100],
            clickToSelect: true,
            queryParams: function (param) {

                $('#queryForm').find('[name]').each(function () {
                    var value = $.trim($(this).val());
                    if (value !== '') {
                        param[this.name] = value;
                    }
                });
                return param;
            },
            columns: [{
                field: "state",
                checkbox: true,
                align: "center"
            }, {
                field: "fileName",
                title: "文件名",
                align: "center",
                formatter: function (value) {
                    if (value && value.length > 30) {
                        return "<i title='" + value + "'>" + value.substring(0, 10) + "...</i>";
                    }
                    return value;
                }
            }, {
                field: "flowName",
                title: "所属流程",
                align: "center"
            }, {
                field: "fullName",
                title: "上传人",
                align: "center"
            }, {
                field: "createTime",
                title: "上传时间",
                align: "center"
            }]
        });
    },
    flowCombobox: function (ele) {

        if (processList) {
            ele.empty();
            var option = $("<option>");
            option.val('');
            option.text("请选择");
            ele.append(option);
            processList.forEach(function (entity) {
                var option = $("<option></option>");
                option.html(entity.name);
                option.val(entity.name);
                ele.append(option);
            });
        }
    },

    upload: function () {
        if ($("[name='file']").val() === "") {
            layer.msg("请选择上传文件!");
            return;
        }
        if ($("#uploadFileForm [name='flowName']").val() === '') {
            layer.msg("请选择所属流程!");
            return;
        }
        $('#attachmentModal').modal('hide');
        $("#uploadFileForm").ajaxSubmit({

            url: path + "/bizTemplateFile/upload",
            type: "post",
            success: function (result) {
                var data = eval("(" + result + ")");//转换为json对象 ;
                if (data.success) {
                    $('#attachmentModal').modal('hide');
                    $('.modal-backdrop').filter('.fade').filter('.in').remove();
                    $("#fileList").bootstrapTable("refresh");
                } else {
                    bsAlert("提示", data.msg);
                }
            }
        });
    },

    queryFile: function () {

        var param = {};
        $('#queryForm').find('[name]').each(function () {
            var value = $.trim($(this).val());
            if (value !== '') {
                param[this.name] = value;
            }
        });
        $("#fileList").bootstrapTable('refresh');
    },

    resetQueryForm: function () {

        $('#queryForm').find('[name="flowName"]').val('');
        $('#queryForm').find('[name="fileName"]').val('');
    },

    downLoad: function () {
        var rows = $("#fileList").bootstrapTable("getSelections");
        if (rows.length < 1) {
            bsAlert("提示", "请选择需要下载的文件!");
            return;
        }
        if (rows.length > 1) {
            bsAlert("提示", "只能选择一个文件下载");
            return;
        }
        window.location.href = path + "/bizTemplateFile/download?id=" + rows[0].id;
    },

    deleteFile: function () {

        var rows = $("#fileList").bootstrapTable("getSelections");
        if (rows.length < 1) {
            bsAlert("提示", "请选择删除行!");
            return;
        }
        layer.confirm("确认是否删除行", {
            skin: "layerskin",
            btn: ["确定", "取消"]
        }, function (index) {
            layer.close(index);
            var ids = [];
            rows.forEach(function (entity) {
                ids.push(entity.id);
            });
            $.ajax({
                url: path + "/bizTemplateFile/remove",
                type: "post",
                traditional: true,
                data: {
                    ids: ids
                },
                success: function (result) {
                    if (result.success) {
                        bsAlert("提示", result.msg);
                        $("#fileList").bootstrapTable("refresh");
                    } else {
                        bsAlert("提示", result.msg);
                    }
                }
            });
        }, function () {
        });
    }
};