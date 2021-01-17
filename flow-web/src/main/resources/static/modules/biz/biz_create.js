$.namespace("biz.create");
$(function () {
    biz.create.init();
});
biz.create = {
    buttons: {},
    data: {},
    fileNumber: 0,
    init: function () {
        $.ajax({
            url: "/workflow/create/" + key,
            async: false,
            success: function (data) {
                if (data.result) {
                    biz.create.data = data['processValBean'];
                    biz.create.buttons = data['SYS_BUTTON'];
                    $("#base_tempID").val(data['baseTempId']);
                    if (bizId) {
                        biz.create.draftData = biz.create.loadDraftBiz();
                        biz.create.loadStatic(biz.create.draftData.workInfo);
                    } else {
                        biz.create.loadStatic();
                    }
                    biz.create.loadForm(biz.create.data);
                    biz.create.createButtons(".t_content", biz.create.buttons);
                    if (bizId) {
                        $("input[name='base.workTitle']").val(biz.create.draftData.workInfo.title);
                        var hidden = $("<input type='hidden' name='tempBizId'/>");
                        hidden.val(biz.create.draftData.workInfo.id);
                        biz.create.loadProcessData(biz.create.draftData.serviceInfo);
                        $("#form").append(hidden);
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
        var draftBizData = null;
        $.ajax({
            url: "/biz/workInfo/" + bizId,
            async: false,
            success: function (data) {
                draftBizData = data;
            }
        });
        return draftBizData;
    },
    loadStatic: function (workInfo) {
        workInfo = workInfo | {};
        $("#msgtitle").text("提单人信息");
        var list = [];
        switch (key) {
            case "eventManagement":
                list.push({
                    id: "workNum",
                    alias: "工单号",
                    value: workInfo['workNum']
                }, {
                    id: "status",
                    alias: "当前状态",
                    value: workInfo['status']
                }, {
                    id: "name",
                    alias: "姓名",
                    value: createUser['name']
                }, {
                    id: "mobile",
                    alias: "联系方式",
                    value: createUser['mobile'] ? createUser['mobile'] && createUser['mobile'] !== 'null' : ''
                }, {
                    id: "email",
                    alias: "邮箱"
                }, {
                    id: "dep",
                    alias: "部门",
                    value: createUser['dep']
                }, {
                    id: "createTime",
                    alias: "提单时间",
                    value: workInfo['createTime'] ? workInfo['createTime'] : (new Date()).Format("yyyy/MM/dd hh:mm:ss")
                });
                break;
            default:
                list.push({
                    id: "workNum",
                    alias: "工单号",
                    value: workInfo['workNum']
                }, {
                    id: "status",
                    alias: "当前状态",
                    value: workInfo['status']
                }, {
                    id: "name",
                    alias: "姓名",
                    value: createUser['name']
                }, {
                    id: "mobile",
                    alias: "联系方式",
                    value: createUser['mobile']
                }, {
                    id: "email",
                    alias: "邮箱"
                }, {
                    id: "dep",
                    alias: "部门",
                    value: createUser['dep']
                }, {
                    id: "createTime",
                    alias: "提单时间",
                    value: workInfo['createTime'] ? workInfo['createTime'] : (new Date()).Format("yyyy/MM/dd hh:mm:ss")
                });
        }
        biz.create.setStatic(list);

    },
    setStatic: function (list) {
        biz.show.getView({
            table: $("#bjrxx"),
            list: list
        }).setDynamic({
            end: true
        });
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
        if (!$.isEmptyObject(serviceInfo)) {
            serviceInfo.forEach(function (entity) {
                if (ele.find(":input[name='" + entity['variableName'] + "']").length > 0) {
                    ele.find(":input[name='" + entity['variableName'] + "']").val(entity.value == null ? "" : entity.value);
                }
            })
        }
    },

    loadForm: function (list) {

        var $content = $(".t_content");
        var div = $("<div class='import_form'>");
        div.html("<h2 class='white_tit'>工单信息</h2>");
        $content.append(div);
        div = $("<div class='listtable_wrap'>");
        var table = $("<table cellpadding='0' class='listtable'></table>");
        div.append(table);
        $content.append(div);
        var view = biz.edit.getView({
            list: list,
            table: table,
            bizId: bizId
        });
        switch (key) {
            case "eventManagement":
                biz.create.type.event(view, true);
                break;
            default:
                biz.create.type.event(view, true);
        }
        view.addFile(bizId ? biz.create.draftData.annexs : null);
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
            url: '/workflow/bizInfo/create',
            traditional: true,
            dataType: 'json',
            async: false,
            cache: false,
            type: 'post',
            success: function (result) {
                if (result) {
                    if (!result || result.success) {
                        layer.close(index);
                        location.href = result.data;
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