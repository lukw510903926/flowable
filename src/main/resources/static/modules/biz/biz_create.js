$.namespace("biz");
$(function () {
    biz.create.init();
});
biz.create = {
    buttons: {},
    data: {},
    fileNumber: 0,
    init: function () {
        $.ajax({
            url: path + "/workflow/create/" + key,
            async: false,
            success: function (data) {
                if (data.result) {
                    biz.create.data = data.processValBeanMap;
                    biz.create.buttons = data.SYS_BUTTON;
                    $("#base_tempID").val(data.baseTempId);
                    if (bizId) {
                        biz.create.draftData = biz.create.loadDraftBiz();
                        biz.create.loadStatic(biz.create.draftData.workInfo, biz.create.draftData.extInfo.createUser);
                    } else {
                        biz.create.loadStatic();
                    }
                    biz.create.loadForm(biz.create.data);
                    biz.create.createButtons(".t_content", biz.create.buttons);
                    if (bizId) {
                        $("input[name='base.workTitle']").val(biz.create.draftData.workInfo.title);
                        $("input[name='base.limitTime']").val(biz.create.draftData.workInfo.limitTime);
                        var hidden = $("<input type='hidden' name='tempBizId'>");
                        hidden.val(biz.create.draftData.workInfo.id);
                        $("#form").append(hidden);
                        biz.create.loadProcessData(biz.create.draftData.serviceInfo);
                    }
                } else {
                    bsAlert("提示", data.msg);
                }
            }
        });
    },

    /**
     * 草稿时加载参数
     * @returns {*}
     */
    loadDraftBiz: function () {
        var DraftBizData = null;
        $.ajax({
            url: path + "/biz/workInfo/" + bizId,
            async: false,
            success: function (data) {
                DraftBizData = data;
            }
        });
        return DraftBizData;
    },
    loadStatic: function (workInfo, saveUser) {
        var $msgtitle = $("#msgtitle");
        switch (key) {
            case "eventManagement":
                $msgtitle.text("报障人信息");
                var list = [{
                    id: "workNum",
                    alias: "工单号"
                }, {
                    id: "status",
                    alias: "当前状态"
                }, {
                    id: "dep",
                    alias: "报障部门"
                }, {
                    id: "name",
                    alias: "报障人姓名"
                }, {
                    id: "mobile",
                    alias: "报障人联系方式"
                }, {
                    id: "email",
                    alias: "邮箱地址"
                }, {
                    id: "city",
                    alias: "报障地市"
                }, {
                    id: "createTime",
                    alias: "故障发生时间"
                }];
                biz.create.setStatic(list, workInfo, saveUser);
                break;
            default:
                $msgtitle.text("申请人信息");
                var list = [{
                    id: "workNum",
                    alias: "工单号"
                }, {
                    id: "status",
                    alias: "当前状态"
                }, {
                    id: "dep",
                    alias: "所在部门"
                }, {
                    id: "name",
                    alias: "姓名"
                }, {
                    id: "mobile",
                    alias: "联系方式"
                }, {
                    id: "email",
                    alias: "邮箱地址"
                }];
                biz.create.setStatic(list, workInfo, saveUser);
        }

    },
    setStatic: function (list, workInfo, saveUser) {
        var view = biz.show.getView({
            table: $("#bjrxx"),
            list: []
        });
        if (workInfo && saveUser) {
            $.each(list, function (index, entity) {
                var text = workInfo[entity.id] === undefined ? saveUser[entity.id] : workInfo[entity.id];
                view.addTextField(entity).text(text == null ? "" : text);
            });
            view.appendTd();
        } else {
            $.each(list, function (index, entity) {
                var text = createUser[entity.id];
                if (entity.id === "createTime") {
                    text = (new Date()).Format("yyyy/MM/dd hh:mm:ss");
                }
                view.addTextField(entity).text(text == null ? "" : text);
            });
            view.appendTd();
        }
    },

    /**
     * 草稿提交时的回显
     * @param serviceInfo
     * @param ele
     */
    loadProcessData: function (serviceInfo, ele) {

        if (!ele) {
            ele = $("body");
        }
        $.each(serviceInfo, function (index, entity) {
            if (entity.variable.viewComponent === "CONFIRMUSER") {
                biz.show.table.confirmUser.setConfirmUserValue(entity);
            } else if (ele.find(":input[name='" + entity.variable.name + "']").length > 0) {
                ele.find(":input[name='" + entity.variable.name + "']").val(entity.value == null ? "" : entity.value);
            }
        });

    },
    loadForm: function (data) {
        for (var group in data) {
            var list = data[group];
            var table;
            if (group === $("#msgtitle").text()) {
                table = $("#bjrxx");
            } else {
                var $content = $(".t_content");
                var div = $("<div class='import_form'>");
                div.html("<h2 class='white_tit'>" + group + "</h2>");
                $content.append(div);
                div = $("<div class='listtable_wrap'>");
                table = $("<table cellpadding='0' cellspacing='0' class='listtable'>");
                div.append(table);
                $content.append(div);
            }
            var view = biz.edit.getView({
                list: list,
                table: table,
                bizId: bizId
            });
            switch (key) {
                case "eventManagement":
                    biz.create.type.event(view, group === "工单信息");
                    break;
                default:
                    biz.create.type.event(view, group === "工单信息");
            }
            view.addFile(bizId ? biz.create.draftData.annexs : null);
        }
        var $form = $("#form");
        $form.find('[name="actualCreator"]').val(createUser.fullname);
        $form.find('[name="actualCreatePhone"]').val(createUser.mobile);
    },
    createButtons: function (container, buttons) {

        var buttonsList = $("<div class='btn-list'>");
        $(container).append(buttonsList);
        var button = "<a class='btn btn-y mrr10' onclick=biz.create.submit('saveTemp')>草稿</a>";
        buttonsList.append(button);
        $.each(buttons, function (key, value) {
            button = "<a class='btn btn-y mrr10' onclick=biz.create.submit('" + key + "')>" + value + "</a>";
            buttonsList.append(button);
        });
    },
    submit: function (key) {
        var file = $(":file");
        for (var i = 0; i < file.length; i++) {
            if (file.eq(i).val() === "") {
                file.eq(i).remove();
            }
        }
        var input = $(":input[checkEmpty='true']");

        if ("submit" === key) {
            $("#form [name='startProc']").val(true);
            for (var i = 0; i < input.length; i++) {
                checkEmpty(input[i]);
            }
            if (input.siblings("i").length > 0) {
                bsAlert("提示", "请完善表单再提交！");
                return;
            }
        }
        if ("saveTemp" === key) {
            $("#form [name='startProc']").val(false);
        }
        $("#form [name='base.buttonId']").val(key);
        $("#form [name='base.handleResult']").val(biz.create.buttons[key]);
        $("#form").attr("action", path + "/workflow/bizInfo");
        var index = layer.load(1, {
            shade: [0.1, '#fff']
        });

        $('#form').ajaxSubmit({
            url: path + '/workflow/bizInfo/create',
            traditional: true,
            dataType: 'json',
            async: false,
            cache: false,
            type: 'post',
            success: function (result) {
                if (result) {
                    if (!result || result.success) {
                        layer.close(index);
                        location.href = path + result.msg;
                    } else {
                        layer.close(index);
                        bsAlert("异常", result.msg);
                    }
                }
            },
            error: function () {
                layer.close(index);
                bsAlert("异常", "提交失败");
            }
        });
    }
};

biz.create.type = {
    event: function (view, flag) {
        if (flag) {
        }
        view.addTitle("工单标题");
        view.setDynamic();
    }
};