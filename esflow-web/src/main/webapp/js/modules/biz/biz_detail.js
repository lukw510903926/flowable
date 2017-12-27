$.namespace("biz");

biz.detail = {
    mark: 0,
    init: function () {
        $.ajax({
            url: path + "/workflow/display/" + id,
            cache: false,
            async: false,
            success: function (result) {
                biz.detail.groups = result.processVariablesMap;
                if (!result) {
                    bsAlert("错误", "异常数据，请验证数据正确性！", function () {
                        window.opener = null;
                        window.close();
                    });
                    return;
                }
                biz.detail.workInfo = result.workInfo;
                document.title = biz.detail.workInfo.title;
                $('#biz_detail_info').text('工单号 : ' + result.workInfo.workNum + ' 工单状态 : ' + result.workInfo.status);
                biz.detail.workLogs = result.workLogs;
                biz.detail.logVars = result.logVars;
                biz.detail.serviceInfo = result.serviceInfo;
                biz.detail.createUser = result.extInfo.createUser;
                console.info(result)
                biz.detail.currentVariables = $.isEmptyObject(result.currentVariables) ? [] : result.currentVariables;
                biz.detail.buttons = result.SYS_BUTTON;
                biz.detail.currentTaskName = result.$currentTaskName;
                biz.detail.annexs = result.annexs;
                biz.detail.curreop = result.CURRE_OP;
                biz.detail.subBizInfo = result.subBizInfo;
                biz.detail.bizKey = biz.detail.workInfo.processDefinitionId;
                biz.detail.files = result.files;
                biz.detail.buttonGroup = biz.detail.groupButton(biz.detail.currentVariables, biz.detail.buttons);

                $("[name='base.bizId']").val(biz.detail.workInfo.id);
                biz.detail.loadStatic(biz.detail.workInfo, biz.detail.createUser);
                var _flag = typeof biz.detail.currentTaskName == "string" ? (biz.detail.currentTaskName.indexOf("重新提交") != -1) : false;
                biz.detail.loadProcessLabel(biz.detail.groups, _flag);
                biz.detail.loadProcessData(biz.detail.serviceInfo);
                biz.detail.loadWorkLogs(biz.detail.workLogs);
                biz.detail.loadWorkForm(biz.detail.buttons, biz.detail.currentVariables, biz.detail.currentTaskName);
                $("#stepPicBizId").append(biz.detail.workInfo.bizId);
                biz.stepPic.loadStepPic(biz.detail.workLogs, biz.detail.workInfo.bizType);
                console.info(biz.detail.buttonGroup)
                if (!biz.detail.buttonGroup) {
                    biz.detail.createButtons("#workLogs", biz.detail.buttons);
                }
            }
        });
    },

    /**
     * 加载工单流程参数信息
     * @param grops
     * @param flag
     */
    loadProcessLabel: function (groups, flag) {

        if ($.inArray("工单信息", groups) == -1) {
            var table = biz.detail.getTable("工单信息");
            biz.detail.loadWorkTitle(table);
            biz.detail.setView(table, [], flag);
        }
        $.each(groups, function (key, list) {
            var table = biz.detail.getTable(key);
            if (key == "工单信息") {
                biz.detail.loadWorkTitle(table);
            }
            biz.detail.setView(table, list, flag);
        });
        biz.show.table.addFile(biz.detail.annexs, table);
        if (!$.isEmptyObject(biz.detail.subBizInfo)) {
            var table = biz.detail.getTable("子单信息");
            biz.show.table.addSonBiz({
                alias: "推诿单",
                data: biz.detail.subBizInfo
            }, table, $("<tr></tr>"));
        }

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

    /**
     * 设定工单流程参数
     * @param serviceInfo
     * @param ele
     */
    loadProcessData: function (serviceInfo, ele) { //回显

        if (ele == undefined) {
            ele = $("body");
        }
        for (var i in serviceInfo) {
            //特殊处理组件
            if (serviceInfo[i].variable.viewComponent == "REQUIREDFILE") {
                biz.show.table.addReqFiles(serviceInfo[i]);
                continue;
            }
            if (serviceInfo[i].variable.viewComponent == "CONFIRMUSER") {
                biz.show.table.confirmUser.setConfirmUserValue(serviceInfo[i]);
                continue;
            }
            if (serviceInfo[i].variable.viewComponent == "MEMBERBOX") {
                biz.show.table.userInfo.setUserNames(serviceInfo[i]);
                continue;
            }
            if (serviceInfo[i].variable.viewComponent == "MEMBERLIST") {
                biz.show.table.userInfo.setUserNames(serviceInfo[i]);
                continue;
            }
            //文本类组件
            if (ele.find(":input[name='" + serviceInfo[i].variable.name + "']").length > 0) {
                ele.find(":input[name='" + serviceInfo[i].variable.name + "']").val(serviceInfo[i].value == null ? "" : serviceInfo[i].value);
            } else if (ele.find(":input[name='" + serviceInfo[i].variable.id + "&" + serviceInfo[i].taskId + "']").length > 0) {
                ele.find(":input[name='" + serviceInfo[i].variable.id + "&" + serviceInfo[i].taskId + "']").val(serviceInfo[i].value == null ? "" : serviceInfo[i].value);
            } else {
                ele.find("span[name='" + serviceInfo[i].variable.id + "&" + serviceInfo[i].taskId + "']").text(serviceInfo[i].value == null ? "" : serviceInfo[i].value);
            }
        }
    },
    loadStatic: function (workInfo, createUser) {
        var key = biz.detail.bizKey.split(":")[0];
        switch (key) {
            case "eventManagement":
                $("#msgtitle").text("报障人信息");
                var list = [{
                    name: "workNum",
                    alias: "工单号"
                }, {
                    name: "status",
                    alias: "当前状态"
                }, {
                    name: "dep",
                    alias: "报障部门"
                }, {
                    name: "name",
                    alias: "报障人姓名"
                }, {
                    name: "mobile",
                    alias: "报障人联系方式"
                }, {
                    name: "email",
                    alias: "邮箱地址"
                }, {
                    name: "city",
                    alias: "报障地市"
                }, {
                    name: "createTime",
                    alias: "故障发生时间"
                }, {
                    name: "limitTime",
                    alias: "最迟解决时间"
                }];
                biz.detail.setStatic(list, workInfo, createUser);
                break;
            default:
                $("#msgtitle").text("申请人信息");
                var list = [{
                    name: "workNum",
                    alias: "工单号"
                }, {
                    name: "status",
                    alias: "当前状态"
                }, {
                    name: "dep",
                    alias: "申请人部门"
                }, {
                    name: "createUser",
                    alias: "申请人姓名"
                }, {
                    name: "mobile",
                    alias: "申请人联系方式"
                }, {
                    name: "email",
                    alias: "邮箱地址"
                }, {
                    name: "city",
                    alias: "申请人地市"
                }, {
                    name: "createTime",
                    alias: "申请时间"
                }, {
                    name: "limitTime",
                    alias: "最迟解决时间"
                }];
                //回显
                biz.detail.setStatic(list, workInfo, createUser);
        }
    },
    setStatic: function (list, workInfo, createUser) {
        var view = biz.show.getView({
            table: $("#fqrxx"),
            list: []
        });
        for (var i = 0; i < list.length; i++) {
            if (list[i].name == "status") {
                var text = workInfo[list[i].name] == undefined ? createUser[list[i].name] : workInfo[list[i].name];
                view.addTextField(list[i]).text(text);
            } else if (list[i].name == "createUser") {
                view.addTextField(list[i]).text(createUser['name']);
            } else {
                var text = workInfo[list[i].name] == undefined ? createUser[list[i].name] : workInfo[list[i].name];
                view.addTextField(list[i]).text(text == null ? "" : text);
            }
        }
        view.appendTd();
    },
    loadWorkLogs: function (workLogs) {
        var mark = 1;
        for (var i in workLogs) {
            var div = $("<div class='import_form'>");
            var title = "<h2 class='white_tit'>处理流程：" + workLogs[i].taskName +
                "<a class='drop'  role='button' data-toggle='collapse' href='#workLogs" + mark + "'></a></h2>";
            div.html(title);
            $("#workLogs").append(div);
            div = $("<div class='listtable_wrap panel-collapse collapse in'>");
            div.attr("id", "workLogs" + mark);
            var table = $("<table cellpadding='0' cellspacing='0' class='listtable'>");
            div.append(table);
            $("#workLogs").append(div);
            mark++;

            var logVars = biz.detail.logVars;
            var logVar = [];
            var list = [];

            if (workLogs[i].handleResult != "签收") {
                for (var key in logVars) {
                    if (workLogs[i].id == key) {
                        logVar = logVars[key];
                        break;
                    }
                }
                for (var j = 0; j < logVar.length; j++) {
                    list.push(logVar[j].variable);
                }
            }
            var view = biz.show.getView({
                table: table,
                list: list,
                taskId: workLogs[i].taskID,
                bizId: biz.detail.workInfo.id
            });
            view.setDynamic({
                end: false
            });
            var handleUser = workLogs[i].handleUser == null ? "" : workLogs[i].handleUser;
            $.ajax({
                url: path + '/role/loadUsersByUserName',
                type: 'post',
                async: false,
                data: {
                    userName: handleUser
                },
                success: function (data) {
                    if (data) {
                        view.addTextField({alias: "处理人"}).text(data.name);
                    } else {
                        view.addTextField({alias: "处理人"}).text(handleUser);
                    }
                }
            });
            view.addTextField({alias: "处理时间"}).text(workLogs[i].createTime == null ? "" : workLogs[i].createTime);
            view.addTextarea({alias: "处理结果"}).text(workLogs[i].handleResult == null ? "" : workLogs[i].handleResult);
            if (workLogs[i].taskID != "START") {
                view.addTextarea({alias: "处理意见"}).text(workLogs[i].handleDescription == null ? "" : workLogs[i].handleDescription);
                if (workLogs[i].handleResult != "签收" && workLogs[i].taskName != "申请人处理") {
                    view.addFile(biz.detail.files[workLogs[i].id]);
                }
            }
            //回显
            biz.detail.loadProcessData(logVar, table);
        }
    },
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
            var buttons = biz.detail.buttonGroup ? biz.detail.buttonGroup.all : buttons;
            $.each(buttons, function (index, value) {
                if (value == "签收") {
                    _sign = true;
                    biz.edit.form.addMessage({
                        alias: "处理意见",
                        name: "handleResult",
                        required: false
                    });
                    return false;
                }
            });
            biz.detail.loadProcessData({}, table);
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
        //确定处理方式属性
        for (var i = 0; i < currentVariables.length; i++) {
            if (currentVariables[i].viewComponent == "TREATMENT") {
                treatment = currentVariables[i];
            }
        }
        //按钮分组，command为之前画图出错时出现的英文按钮可以去掉
        if (treatment) {
            var command = {};
            var flow = {};
            for (var key in buttons) {
                if (key.match("command_") != null) {
                    command[key.substring(9)] = buttons[key];
                } else {
                    flow[key] = buttons[key];
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
                    if (flow[key] == group[i]) {
                        if (groupButtons[group[i]] == undefined) {
                            groupButtons[group[i]] = {};
                        }
                        groupButtons[group[i]][key] = flow[key];
                        if (command[key] != undefined) {
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
                        if (groupButtons[group[i]] == undefined) {
                            groupButtons[group[i]] = {};
                        }
                        groupButtons[group[i]][key] = flow[key];
                        if (command[key] != undefined) {
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
    createButtons: function (container, buttons) { //未分组方法，旧
        $("#formButtons").remove();
        var buttonList = $("<div id='formButtons' class='btn_list' style='padding:10px 0;margin:0;'>");
        $(container).append(buttonList);
        $.each(buttons,function(key,value){
            buttonList.append("<a class='yes_btn mrr10' onclick=biz.detail.save('" + key + "')>" + value + "</a>");
        });
        buttonList.append("<a onclick='javascript:window.opener=null;window.close();'>关闭</a>");
    },
    getTable: function (group) { //工单基本数据分组获取布局table
        var table;
        if (group == $("#msgtitle").text()) {
            table = $("#fqrxx");
        } else {
            var div = $("<div class='import_form mrt10'>");
            var title = "<h2 class='white_tit'>" + group +
                "<a class='drop'  role='button' data-toggle='collapse' href='#collapse" + biz.detail.mark + "'></a></h2>";
            div.html(title);
            $(".close_all").before(div);
            div = $("<div class='listtable_wrap panel-collapse collapse in'>");
            div.attr("id", "collapse" + biz.detail.mark);
            table = $("<table cellpadding='0' cellspacing='0' class='listtable'>");
            div.append(table);
            $(".close_all").before(div);
            biz.detail.mark++;
        }
        return table;
    },
    getWorkLogsTable: function (currentTaskName) { //创建获取当前流程布局table，传入流程名称，早期改名有瑕疵
        var div = $("<div class='import_form'>");
        var title = "<h2 class='white_tit'>我的处理：" + currentTaskName +
            "<a class='drop'  role='button' data-toggle='collapse' href='#workForm'></a></h2>";
        div.html(title);
        $("#workLogs").append(div);
        div = $("<div class='listtable_wrap panel-collapse collapse in'>");
        div.attr("id", "workForm");
        var table = $("<table cellpadding='0' cellspacing='0' class='listtable'>");
        div.append(table);
        $("#workLogs").append(div);
        return table;
    }
};


biz.detail.save = function (key) {
    var url = path + "/workflow/submit";
    var jwWorkChecked = $(":checkbox[name='jwWork']:checked");
    if (jwWorkChecked.length > 0 || key == 'save')
        $(":input[name='crossDimension']").val("true");
    else $(":input[name='crossDimension']").val(null);

    var input = $(":input[checkEmpty='true']");
    for (var i = 0; i < input.length; i++) {
        checkEmpty(input[i]);
    }
    if (input.siblings("i").length > 0) {
        bsAlert("提示", "请完善表单再提交！");
        return;
    }
    var file = $(":file");
    for (var i = 0; i < file.length; i++) {
        if (file.eq(i).val() == "")
            file.eq(i).remove();
    }
    //重新提交，交维工作做了处理
    if (typeof biz.detail.currentTaskName == "string" ? (biz.detail.currentTaskName.indexOf("重新提交") != -1) : false) {
//		$("#form [name='startProc']").val(true);
        url = path + "/workflow/bizInfo/updateBiz";
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
                    layer.close(index);
                    bsAlert("异常", result);
                    return;
                }
            } else {
                layer.close(index);
                bsAlert("异常", result);
                return;
            }
            if (!result || result.success == true) {
                layer.close(index);
                location.reload();
            } else {
                layer.close(index);
                bsAlert("异常", result.msg);
            }
        },
        error: function (result) {
            layer.close(index);
            bsAlert("异常", "提交失败");
        }
    });
};


$(function () {
    biz.detail.init();
    $(".gray_drop").click(function () {
        if ($(this).hasClass("gray_drop")) {
            $(this).removeClass("gray_drop");
            $(this).addClass("gray_droped");
        } else {
            $(this).removeClass("gray_droped");
            $(this).addClass("gray_drop");
        }
    });
    $(".drop").click(function () {
        if ($(this).hasClass("drop")) {
            $(this).removeClass("drop");
            $(this).addClass("droped");
        } else {
            $(this).removeClass("droped");
            $(this).addClass("drop");
        }
    });
});