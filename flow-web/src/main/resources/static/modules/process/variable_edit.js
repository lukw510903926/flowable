$.namespace('biz.variable.edit');

$(function () {
    biz.variable.edit.init();
});
biz.variable.edit = {

    preUrl: '',
    init: function () {

        $("#saveOrUpdateBtn").on('click', biz.variable.edit.saveVariable);
        biz.variable.edit.loadVariableList();
        if (updateId) {
            biz.variable.edit.getById();
        }
        $("#cancleBtn").click(function () {
            window.open(biz.variable.edit.preUrl, "_self");
        });
        var $udViewComponent = $('.selectpicker');
        $udViewComponent.selectpicker('render');
        $udViewComponent.selectpicker('refresh');
    },

    loadVariableList: function () {
        biz.variable.edit.preUrl = "/process/variable?processDefinitionId=" + processId +  "&taskId=" + taskId;
        if (taskId) {
            $("#htitle").html("添加/编辑流程任务参数");
        }
        $('#ud_refParam').parents('div.form-group').hide();
        //联动父节点
        $.ajax({
            type: 'POST',
            async: false,
            url: "/processModelMgr/processValList",
            data: {
                id: updateId,
                processId: processId,
                version: version,
                taskId: taskId
            },
            dataType: 'json',
            success: function (data) {
                if (data != null && data.rows != null) {
                    var html = [];
                    html.push('<option value="">请选择</option>');
                    for (var i = 0; i < data.rows.length; i++) {
                        html.push('<option data = "' + data.rows[i].viewComponent + '@' + data.rows[i].viewParams + '" value="' + data.rows[i].id + '">' + data.rows[i].alias + '</option>');
                    }
                    $('#ud_refVariable').append(html.join(""));
                }
            }
        });
    },
    getById: function () {

        $.ajax({
            type: 'get',
            url: "/processModelMgr/getProcessValById/" + updateId,
            dataType: 'json',
            success: function (result) {
                if (result.success) {
                    $('#ud_name').val(result.data.name);
                    $('#ud_alias').val(result.data.alias);
                    $('#ud_nameOrder').val(result.data.order);
                    if (result.data.required) {
                        $('input[name=ud_required]').attr('checked', 'checked');
                    }
                    $('#ud_groupName').val(result.data.groupName);
                    $('#ud_groupOrder').val(result.data.groupOrder);
                    $('#ud_variableGroup').val(result.data.variableGroup);
                    $('#ud_componentArgs').val(result.data.viewDatas);
                    $('#ud_viewComponent').selectpicker('val', result.data.viewComponent);
                    biz.variable.edit.getViewParams();
                    $('#ud_viewParams').val(result.data.viewParams);
                    $("#ud_refVariable").val(result.data.refVariable);
                    if (result.data['processVariable']) {
                        $('input[name=ud_processVariable]').attr('checked', 'checked');
                    }
                } else {
                    bsAlert("错误", result.msg);
                }
            }
        });
    },
    saveVariable: function () {

        if ($.trim($('#ud_name').val()) === "") {
            bsAlert("提示", "属性名称不能为空");
            return;
        }
        var nameOrder = $("#ud_nameOrder").val();
        if (nameOrder !== "") {
            if (isNaN(nameOrder)) {
                bsAlert("提示", "字段只能是数字");
                return;
            }
        }
        var data = {
            processDefinitionId: processId,
            version: version,
            taskId: taskId,
            name: $.trim($('#ud_name').val()),
            alias: $.trim($('#ud_alias').val()),
            order: $.trim($('#ud_nameOrder').val()),
            isRequired: $('input[name="ud_required"]:checked').val() === "on",
            groupName: $.trim($('#ud_groupName').val()),
            groupOrder: $.trim($("#ud_groupOrder").val()),
            variableGroup: $.trim($('#ud_variableGroup').val()),
            viewComponent: $('#ud_viewComponent option:selected').val(),
            viewDatas: $.trim($("#ud_componentArgs").val()),
            viewParams: $.trim($("#ud_viewParams").val()),
            isProcessVariable: $('input[name="ud_processVariable"]:checked').val() === "on",
            refVariable: $.trim($("#ud_refVariable").val()),
            refParam: $.trim($("#ud_refParam").val())
        };
        if(!$.isEmptyObject(updateId)){
            data['id']= updateId;
        }
        biz.variable.edit.saveAjax(data);
    },

    getViewParams: function () {

        var combobox = $('#ud_viewComponent').val();
        var $udViewParams = $('#ud_viewParams');
        var $viewParams = $('[for="ud_viewParams"]');
        if (combobox === 'DICTCOMBOBOX') {
            $viewParams.parent().show();
            $viewParams.text('数据字典：');
            $.ajax({
                type: 'POST',
                async: false,
                url: path + "/dictType/list",
                dataType: 'json',
                success: function (result) {
                    $udViewParams.empty();
                    result.rows.forEach(function(entity){
                        var option = $('<option>');
                        option.val(entity.id);
                        option.text(entity.name);
                        $udViewParams.append(option);
                    });
                }
            });
            $udViewParams.selectpicker('render');
            $udViewParams.selectpicker('refresh');
        } else if (combobox === 'REQUIREDFILE') {
            $('#helpBlock').html('文件名包含文件扩展名,如 xxx.doc,无需模版下载可不配置');
            $('#ud_componentArgsLabel').html('模版名称: &nbsp');
            $viewParams.parent().hide();
        } else {
            $viewParams.parent().hide();
        }
    },


    saveAjax: function (params) {
        $.ajax({
            type: 'POST',
            url: "/processModelMgr/saveOrUpdate",
            data: params,
            dataType: 'json',
            success: function (data) {
                if (data.success) {
                    bsAlert("提示", data.msg);
                    setTimeout(function () {
                        window.open(biz.variable.edit.preUrl, "_self");
                    }, 1500);
                } else {
                    bsAlert("错误", data.msg, "error");
                }
            }
        });
    }
};