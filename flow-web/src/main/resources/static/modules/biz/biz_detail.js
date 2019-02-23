$.namespace("biz");
$(function () {
    biz.detail.init();
    $(".gray_drop").on('click', function () {
        if ($(this).hasClass("gray_drop")) {
            $(this).removeClass("gray_drop");
            $(this).addClass("gray_droped");
        } else {
            $(this).removeClass("gray_droped");
            $(this).addClass("gray_drop");
        }
    });
    $(".drop").on('click', function () {
        if ($(this).hasClass("drop")) {
            $(this).removeClass("drop");
            $(this).addClass("droped");
        } else {
            $(this).removeClass("droped");
            $(this).addClass("drop");
        }
    });
});
biz.detail = {
    mark: 0,
    init: function () {
        var id = $('#bizId').val();
        $.ajax({
            url: "/workflow/display/" + id,
            cache: false,
            async: false,
            success: function (result) {
                if (!result) {
                    bsAlert("错误", "异常数据，请验证数据正确性！", function () {
                        window.opener = null;
                        window.close();
                    });
                    return;
                }
                biz.detail.workInfo = result.workInfo;
                document.title = biz.detail.workInfo.title;
                $('#biz_detail_info').text('工单号 : ' + result.workInfo['workNum'] + ' 工单状态 : ' + result.workInfo.status);
                biz.detail.workLogs = result.workLogs;
                biz.detail.createUser = result['extInfo']['createUser'];
                biz.detail.currentVariables = result.currentVariables;
                biz.detail.buttons = result['SYS_BUTTON'];
                biz.detail.currentTaskName = result['$currentTaskName'];
                biz.detail.curreop = result['CURRE_OP'];
                biz.detail.subBizInfo = result.subBizInfo;
                biz.detail.bizKey = biz.detail.workInfo.processDefinitionId;
                biz.detail.buttonGroup = biz.detail.groupButton(biz.detail.currentVariables, biz.detail.buttons);

                $("[name='base.bizId']").val(biz.detail.workInfo.id);
                biz.detail.loadStatic(biz.detail.workInfo, biz.detail.createUser);
                var _flag = typeof biz.detail.currentTaskName == "string" ? (biz.detail.currentTaskName.indexOf("重新提交") !== -1) : false;
                biz.detail.loadBIzInfo(_flag);
                biz.detail.loadWorkLogs(biz.detail.workLogs);
                biz.detail.loadWorkForm(biz.detail.buttons, biz.detail.currentVariables, biz.detail.currentTaskName);
                $("#stepPicBizId").append(biz.detail.workInfo.bizId);
                biz.stepPic.loadStepPic(biz.detail.workLogs, biz.detail.workInfo['bizType']);
                if (!biz.detail.buttonGroup) {
                    biz.detail.createButtons("#workLogs", biz.detail.buttons);
                }
            }
        });
    },

    /**
     * 工单标题
     * @param table
     */
    loadWorkTitle: function (table) {

        var tr = $("<tr></tr>");
        var th = $("<th>");
        th.text("工单标题:");
        var td = $("<td colspan='3'></td>");
        var span = "<span class='fslTextBoxR'>" + biz.detail.workInfo.title + "</span>";
        td.append(span);
        tr.append(th).append(td);
        table.append(tr);
    },

    /**
     * 加载工单信息
     * @param flag
     */
    loadBIzInfo: function (flag) {

        var table = biz.detail.getTable("工单信息");
        biz.detail.loadWorkTitle(table);
        var list = [{
            name: "workNum",
            alias: "工单号",
            value: biz.detail.workInfo['workNum']
        }, {
            name: "bizType",
            alias: "工单类型",
            value: biz.detail.workInfo['bizType']
        }, {
            name: "status",
            alias: "工单状态",
            value: biz.detail.workInfo['status']
        }, {
            name: "createTime",
            alias: "创建时间",
            value: biz.detail.workInfo['createTime']
        }];
        biz.detail.setView(table, list, flag);
        if (!$.isEmptyObject(biz.detail.subBizInfo)) {
            var _table = biz.detail.getTable("子单信息");
            var columns = [{
                field: "workNum",
                title: "工单号",
                align: "center",
                formatter: function (value, row) {
                    return "<a style='cursor: pointer' onclick=\"window.open('" + "/biz/" + row.id + "');\">" + value + "</a>";
                }
            }, {
                field: "bizType",
                title: "工单类型",
                align: "center"
            }, {
                field: "title",
                title: "工单标题",
                align: "center"
            }, {
                field: "createTime",
                title: "创建时间",
                align: "center"
            }, {
                field: "status",
                title: "工单状态",
                align: "center"
            }, {
                field: "taskAssignee",
                title: "当前处理人",
                align: "center"
            }];
            biz.show.table.loadTable({
                alias: "推诿单",
                data: biz.detail.subBizInfo,
                columns: columns
            }, _table, $("<tr></tr>"));
        }
    },

    setView: function (table, list, flag) {
        var view = null;
        //是否可编辑
        if (flag) {
            $("#form").append($("<input type='hidden' name='startProc'>"));
            view = biz.edit.getView({
                table: table,
                taskId: "START",
                list: list
            });
        } else {
            view = biz.show.getView({
                table: table,
                list: list,
                taskId: "START",
                bizId: biz.detail.workInfo.id
            });
        }
        view.setDynamic();
    },

    loadStatic: function (workInfo, createUser) {

        var key = biz.detail.bizKey.split(":")[0];
        $("#msgtitle").text("提单人信息");
        var list = [];
        switch (key) {
            case "eventManagement":
                list.push({
                    name: "createUser",
                    alias: "姓名",
                    value: createUser['name']
                }, {
                    name: "dep",
                    alias: "部门",
                    value: createUser['dep']
                }, {
                    name: "mobile",
                    alias: "联系方式",
                    value: createUser['mobile']
                }, {
                    name: "email",
                    alias: "邮箱",
                    value: createUser['email']
                }, {
                    name: "city",
                    alias: "报障地市",
                    value: createUser['city']
                });
                break;
            default:
                list.push({
                    name: "dep",
                    alias: "部门",
                    value: createUser['dep']
                }, {
                    name: "createUser",
                    alias: "姓名",
                    value: createUser['name']
                }, {
                    name: "mobile",
                    alias: "联系方式",
                    value: createUser['mobile']
                }, {
                    name: "email",
                    alias: "邮箱",
                    value: createUser['email']
                });
        }
        biz.detail.setStatic(list);
    },

    /**
     * 提单人信息
     * @param list
     */
    setStatic: function (list) {

        var view = biz.show.getView({
            table: $("#fqrxx"),
            bizId: biz.detail.workInfo.id,
            list: list
        });
        view.setDynamic({
            end: false
        });
    },


    loadWorkLogs: function (workLogs) {

        var mark = 1;
        $.each(workLogs, function (index, entity) {

            var $workLogs = $("#workLogs");
            var div = $("<div class='import_form'>");
            var title = "<h2 class='white_tit'>处理流程：" + entity['taskName'] +
                "<a class='drop'  role='button' data-toggle='collapse' href='#workLogs" + mark + "'></a></h2>";
            div.html(title);
            $workLogs.append(div);
            div = $("<div class='listtable_wrap panel-collapse collapse in'>");
            div.attr("id", "workLogs" + mark);
            var table = $("<table cellpadding='0' cellspacing='0' class='listtable'>");
            div.append(table);
            $workLogs.append(div);
            mark++;

            var list = [];
            if (entity['handleResult'] !== "签收") {
                var logVar = entity['variableInstances'];
                if (!$.isEmptyObject(logVar)) {
                    $.each(logVar, function (index, instance) {
                        list.push({
                            name: instance['variableName'],
                            viewComponent: instance.viewComponent,
                            alias: instance['variableAlias'],
                            id: instance.id,
                            value: instance.value
                        });
                    });
                }
            }
            list.push({
                name: "handleUser",
                alias: "处理人",
                value: entity['handleUserName']
            }, {
                name: "createTime",
                alias: "处理时间",
                value: entity['createTime']
            }, {
                name: "handleResult",
                alias: "处理结果",
                value: entity['handleResult']
            }, {
                name: "handleDescription",
                alias: "处理意见",
                viewComponent: 'TEXTAREA',
                value: entity['handleDescription']
            });
            var view = biz.show.getView({
                table: table,
                list: list,
                taskId: entity['taskID'],
                bizId: biz.detail.workInfo.id
            });
            view.setDynamic({
                end: false
            });
            if (entity['handleResult'] !== "签收" && entity['taskName'] !== "申请人处理") {
                view.addFile(entity['bizFiles']);
            }
        });
    },

    /**
     * 当前任务form
     * @param buttons
     * @param taskVariableBeans
     * @param currentTaskName
     */
    loadWorkForm: function (buttons, taskVariableBeans, currentTaskName) {

        if (buttons != null) {
            var table = biz.detail.getWorkLogsTable(currentTaskName);
            var view = biz.edit.getView({
                table: table,
                list: taskVariableBeans,
                bizId: biz.detail.workInfo.bizId,
                buttonGroup: biz.detail.buttonGroup,
                taskId: biz.detail.workInfo.taskId
            });
            //调用处理方式分组加载组件
            view.variableGroup();
            //判断是否签收
            var _sign = false;
            var groupButtons = biz.detail.buttonGroup ? biz.detail.buttonGroup.all : buttons;
            $.each(groupButtons, function (index, value) {
                if (value === "签收") {
                    _sign = true;
                    biz.edit.form.addMessage({
                        alias: "处理意见",
                        name: "handleResult",
                        required: false
                    });
                    return false;
                }
            });
        }
    },

    /**
     * 按钮分组
     * @param currentVariables
     * @param buttons
     * @returns {{all: *}}
     */
    groupButton: function (currentVariables, buttons) {

        var treatment = null;
        var groupButtons = {
            all: buttons
        };
        if ($.isEmptyObject(currentVariables)) {
            return "";
        }
        //确定处理方式属性
        $.each(currentVariables, function (index, entity) {
            if (entity.viewComponent === 'TREATMENT') {
                treatment = entity;
            }
        });
        //按钮分组，command为之前画图出错时出现的英文按钮可以去掉
        if (treatment) {
            var command = {};
            var flow = {};
            for (var _key in buttons) {
                if (_key.match("command_") != null) {
                    command[_key.substring(9)] = buttons[_key];
                } else {
                    flow[_key] = buttons[_key];
                }
            }
            var group = [];
            if ($.isEmptyObject(treatment.viewDatas)) {
                $.each(biz.detail.buttons, function (index, entity) {
                    group.push(entity);
                });
            } else {
                group = treatment.viewDatas.split(",");
            }
            //处理分组文本与按钮文本相同情况
            for (var key in flow) {
                for (var i = 0; i < group.length; i++) {
                    if (flow[key] === group[i]) {
                        if (!groupButtons[group[i]]) {
                            groupButtons[group[i]] = {};
                        }
                        groupButtons[group[i]][key] = flow[key];
                        if (command[key]) {
                            for (var k in command[key]) {
                                groupButtons[group[i]][k] = command[key][k];
                            }
                        }
                        delete flow[key];
                    }
                }
            }
            //处理分组文本包含按钮文本情况
            for (var key in flow) {
                for (var i = 0; i < group.length; i++) {
                    if (group[i].match(flow[key])) {
                        if (!groupButtons[group[i]]) {
                            groupButtons[group[i]] = {};
                        }
                        groupButtons[group[i]][key] = flow[key];
                        if (command[key]) {
                            for (var k in command[key]) {
                                groupButtons[group[i]][k] = command[key][k];
                            }
                        }
                    }
                }
            }
        }

        return groupButtons;
    },

    /**
     * 创建按钮
     * @param container
     * @param buttons
     */
    createButtons: function (container, buttons) { //未分组方法，旧
        $("#formButtons").remove();
        var buttonList = $("<div id='formButtons' class='btn_list' style='padding:10px 0;margin:0;'>");
        $(container).append(buttonList);
        for (var key in buttons) {
            buttonList.append("<a class='yes_btn mrr10' onclick=biz.detail.save('" + key + "')>" + buttons[key] + "</a>");
        }
        buttonList.append("<a onclick='javascript:window.opener=null;window.close();'>关闭</a>");
    },

    /**
     * 工单基本数据分组获取布局table
     * @param group
     * @returns {*}
     */
    getTable: function (group) {
        var table;
        if (group === $("#msgtitle").text()) {
            table = $("#fqrxx");
        } else {
            var div = $("<div class='import_form mrt10'>");
            var title = "<h2 class='white_tit'>" + group +
                "<a class='drop'  role='button' data-toggle='collapse' href='#collapse" + biz.detail.mark + "'></a></h2>";
            div.html(title);
            var $closeAll = $(".close_all");
            $closeAll.before(div);
            div = $("<div class='listtable_wrap panel-collapse collapse in'>");
            div.attr("id", "collapse" + biz.detail.mark);
            table = $("<table cellpadding='0' cellspacing='0' class='listtable'>");
            div.append(table);
            $closeAll.before(div);
            biz.detail.mark++;
        }
        return table;
    },

    /**
     * 工单日志 创建获取当前流程布局table，传入流程名称
     * @param currentTaskName
     * @returns {*|jQuery|HTMLElement}
     */
    getWorkLogsTable: function (currentTaskName) {
        var div = $("<div class='import_form'>");
        var title = "<h2 class='white_tit'>我的处理：" + currentTaskName +
            "<a class='drop'  role='button' data-toggle='collapse' href='#workForm'></a></h2>";
        div.html(title);
        var $workLogs = $("#workLogs");
        $workLogs.append(div);
        div = $("<div class='listtable_wrap panel-collapse collapse in'>");
        div.attr("id", "workForm");
        var table = $("<table cellpadding='0' cellspacing='0' class='listtable'>");
        div.append(table);
        $workLogs.append(div);
        return table;
    }
};


biz.detail.save = function (key) {

    var url = "/workflow/submit";
    var input = $(":input[checkEmpty='true']");
    for (var i = 0; i < input.length; i++) {
        checkEmpty(input[i]);
    }
    if (input.siblings("i").length > 0) {
        bsAlert("提示", "请完善表单再提交！");
        return;
    }
    if (input.siblings("i").length > 0) {
        bsAlert("提示", "请完善表单再提交！");
        return;
    }
    var file = $(":file");
    for (var i = 0; i < file.length; i++) {
        if (file.eq(i).val() === "") {
            file.eq(i).remove();
        }
    }
    //重新提交
    if (typeof biz.detail.currentTaskName == "string" ? (biz.detail.currentTaskName.indexOf("重新提交") !== -1) : false) {
        url = "/workflow/bizInfo/updateBiz";
    }

    $("[name='base.buttonId']").val(key);
    $("[name='base.handleResult']").val(biz.detail.buttons[key]);
    $("[name='base.handleName']").val(biz.detail.currentTaskName);
    var index = layer.load(1, {
        shade: [0.1, '#fff']
    });
    //提交，修改时注意后台返回方式
    $("#form").ajaxSubmit({
        url: url,
        type: "post",
        cache: false,
        success: function (result) {

            if (result) {
                try {
                    result = eval("(" + result.replace("<PRE>", "").replace("</PRE>", "").replace("<pre>", "").replace("</pre>", "") + ")");
                } catch (e) {
                }
                if (result.success) {
                    layer.close(index);
                    location.reload();
                } else {
                    layer.close(index);
                    bsAlert("提交失败", result.msg);
                }
            } else {
                layer.close(index);
                bsAlert("异常", result);
            }
        },
        error: function () {
            layer.close(index);
            bsAlert("异常", "提交失败");
        }
    });
};