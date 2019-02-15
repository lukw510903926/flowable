$.namespace("biz.edit");

function checkEmpty(ele) {
    if (ele === undefined) {
        ele = this;
    } else if (ele.originalEvent) {
        ele = this;
    }
    $(ele).siblings("i").remove();
    if (/^(\s*)+$/.test(ele.value)) {
        $(ele).after("<i style='color:red;'>&nbsp;不能为空！</i>");
    }
}

function lastSevenDay() {
    var today = new Date();
    var y = today.getFullYear();
    var M = today.getMonth() + 1;
    if (M < 10)
        M = '0' + M;
    var day = new Date(y, M, 0);
    var d = day.getDate();
    var d2 = d - 6 - 20;
    var disableDay = y + "-" + M + "-(2[" + d2 + "-9]|3[0-1])";
    return disableDay;
}

function banNumber() {
    var keyCode = event.keyCode;
    if ((keyCode < 48 && keyCode !== 8) || (keyCode > 57 && keyCode < 96) || (keyCode > 105 && keyCode !== 110 && keyCode !== 190)) {
        event.keyCode = 0;
        event.returnValue = false;
    }
}

function checkNumber(ele) {
    if (!ele) {
        ele = this;
    } else if (ele.originalEvent) {
        ele = this;
    }
    if (!/^((([1-9]+\d*)?|\d?)(\.\d*)?)?$/.test(ele.value)) {
        ele.value = "";
        $(ele).after("<i style='color:red;'>&nbsp;请输入数字！</i>");
    }
}

function cleanCheck(ele) {
    if (!ele) {
        ele = this;
    } else if (ele.originalEvent) {
        ele = this;
    }
    $(ele).siblings("i").remove();
}

$(function () {
    biz.edit.data.form = $("#form");
});
biz.edit = {
    confirmUser: {},
    fileNumber: 0,
    data: {
        tr: $("<tr></tr>")
    },
    getView: function (option) {
        for (var k in biz.edit.data) {
            delete biz.edit.data[k];
        }
        for (var key in option) {
            biz.edit.data[key] = option[key];
        }
        if (!option.form) {
            biz.edit.data.form = $("#form");
        } else {
            biz.edit.data.form = option.form;
        }
        biz.edit.data.tr = $("<tr></tr>");
        return biz.edit.form;
    }
};

biz.edit.form = {
    //动态加载表单组件
    setDynamic: function (option) {
        if (option !== undefined) {
            if (option.table !== undefined) {
                biz.edit.data.table = option.table;
            }
            if (option.tr !== undefined) {
                biz.edit.data.tr = option.tr;
            }
        } else {
            option = {};
        }
        if (option.list === undefined) {
            option.list = biz.edit.data.list;
        }
        if (!$.isEmptyObject(option.list)) {
            for (var j = 0; j < option.list.length; j++) {
                switch (option.list[j].viewComponent) {
                    case "TEXTAREA":
                        biz.edit.form.addTextarea(option.list[j]);
                        break;
                    case "TEXT":
                        biz.edit.form.addTextField(option.list[j], option.list);
                        break;
                    case "BUTTON":
                        biz.edit.form.addButton(option.list[j], option.list);
                        break;
                    case "COMBOBOX":
                        biz.edit.form.addComboBox(option.list[j], option.list);
                        break;
                    case "DICTCOMBOBOX":
                        biz.edit.form.addComboBox(option.list[j], option.list);
                        break;
                    case "MCMLISTBOX":
                        biz.edit.form.addComboBox(option.list[j], option.list);
                        break;
                    case "TREATMENT":
                        biz.edit.form.addComboBox(option.list[j], option.list);
                        break;
                    case "URGENCYLEVEL":
                        biz.edit.form.addUrgencyLevel(option.list[j]);
                        break;
                    case "MEMBERBOX":
                        biz.edit.form.addMember(option.list[j]);
                        break;
                    case "BOOLEAN":
                        biz.edit.form.addBoolean(option.list[j]);
                        break;
                    case "MEMBERLINKAGE":
                        biz.edit.form.addMemberLinkage(option.list[j], option.list);
                        break;
                    case "MEMBERLIST":
                        biz.edit.form.addMemberList(option.list[j]);
                        break;
                    case "CONFIRMUSER":
                        biz.edit.form.addConfirmUser(option.list[j]);
                        break;
                    case "GROUPHEAD":
                        biz.edit.form.addGroupHead(option.list[j]);
                        break;
                    case "REMARK":
                        biz.edit.form.addRemark(option.list[j]);
                        break;
                    case "REQUIREDFILE":
                        biz.edit.form.addRequiredFile(option.list[j]);
                        break;
                    default:
                        biz.edit.form.addTextField(option.list[j]);
                }
            }
            if (option.end || !option.end){
                biz.edit.form.appendTd();
            }
        }
        biz.edit.data.tr = $("<tr></tr>");
        return biz.edit.data.tr;
    },

    //处理方式分组表单元素
    variableGroup: function (option) {

        if (option) {
            if (option.table) {
                biz.edit.data.table = option.table;
            }
            if (option.tr) {
                biz.edit.data.tr = option.tr;
            }
        } else {
            option = {};
        }
        if (!option.list) {
            option.list = biz.edit.data.list;
        }
        if (option.list == null) {
            return;
        }
        var treatment = null;
        var group = null;
        if ($(option.ele).length > 0 && $(option.ele).val() !== "") {
            group = $(option.ele).val();
        } else {
            if (option.list) {
                for (var i = 0; i < option.list.length; i++) {
                    if (option.list[i].viewComponent === "TREATMENT") {
                        var treatmentList = [];
                        if ($.isEmptyObject(option.list[i].viewDatas)) {
                            $.each(biz.detail.buttons, function (index, entity) {
                                treatmentList.push(entity);
                            });
                        } else {
                            treatmentList = option.list[i].viewDatas.split(",");
                        }
                        group = treatmentList[0];
                        treatment = option.list[i];
                        break;
                    }
                }
            }
        }
        if (group != null) {
            var array = [];
            for (var i = 0; i < option.list.length; i++) {
                var vgroup = option.list[i].variableGroup;
                if ($.isEmptyObject(vgroup) || option.list[i].viewComponent === "TREATMENT") {

                    var treatments = [];
                    if ($.isEmptyObject(option.list[i].viewDatas)) {
                        $.each(biz.detail.buttons, function (index, entity) {
                            treatments.push(entity);
                        });
                    } else {
                        treatments = option.list[i].viewDatas.split(",");
                    }
                    array.push(option.list[i]);
                    continue;
                }
                var groups = vgroup.split(",");
                for (var j = 0; j < groups.length; j++) {
                    if (groups[j] === group) {
                        array.push(option.list[i]);
                        break;
                    }
                }
            }
        } else {
            array = option.list;
        }
        var trs = biz.edit.data.table.find("tr");
        var length = trs.length;
        if (length > 2) {
            var tr1 = trs.eq(length - 2).clone();
            var tr2 = trs.eq(length - 1).clone();
        }
        biz.edit.data.table.empty();
        option.list = array;
        biz.edit.form.variableButton({});
        biz.edit.form.setDynamic(option);
        if (biz.edit.data.buttonGroup) {
            biz.edit.form.variableButton(!biz.edit.data.buttonGroup[group] ? biz.edit.data.buttonGroup.all : biz.edit.data.buttonGroup[group]);
        }
        if ($(option.ele).length > 0) {
            $(option.ele).val(group);
        } else {
            if (treatment) {
                $("[name='" + treatment.name + "']").val(group);
            }
        }
        biz.edit.form.addFile();
    },
    //处理方式分组按钮
    variableButton: function (buttons) {
        $("#formButtons").remove();
        var buttonlist = $("<div id='formButtons' class='btn_list' style='padding:10px 0;margin:0;'></div>");
        $("#workForm").append(buttonlist);
        $.each(buttons, function (k, n) {
            buttonlist.append("<a class='yes_btn mrr10' onclick=biz.detail.save('" + k.trim() + "')>" + n + "</a>");
        });
        buttonlist.append("<a onclick='javascript:window.opener=null;window.close();'>关闭</a>");
    },
    addTextarea: function (data, table, tr) {

        if (!table) {
            table = biz.edit.data.table;
        }
        if (tr) {
            biz.edit.data.tr = tr;
        }
        biz.edit.form.appendTd();
        var th = $("<th></th>");
        var td = $("<td colspan='3'></td>");
        th.append(data.alias + ":");
        var textarea = "<textarea placeholder='不可超过400个中文' name='" + data.name + "' rows='2' cols='20' class='fslTextBox' style='height:81px;width:90%;'></textarea>";
        td.html(textarea);
        biz.edit.form.addCkeckEmpty(data, th, td.children("textarea"));
        biz.edit.data.tr.append(th);
        biz.edit.data.tr.append(td);
        table.append(biz.edit.data.tr);
        biz.edit.data.tr = $("<tr></tr>");
        return biz.edit.data.tr;
    },
    addButton: function (data, list, table, tr) {

        if (!table) {
            table = biz.edit.data.table;
        }
        if (tr) {
            biz.edit.data.tr = tr;
        }
        biz.edit.form.appendTd();
        var th = $("<th></th>");
        var td = $("<td colspan='3'></td>");
        th.append(data.alias + ":");
        var viewParams = data.viewParams;
        var viewDatas = data.viewDatas;
        if (viewParams) {
            var buttons = viewParams.split(",");
            var urls = viewDatas.split(",");
            var styleClassName = 'btn btn-y mrr10';
            for (var i = 0; i < buttons.length; i++) {
                var button = $("<a style='' target='_blank'>" + buttons[i] + "</a>");
                button.attr("name", buttons[i]).attr('class', styleClassName);
                button.attr('href', path + '/biz/create/' + urls[i]);
                td.append(button);
            }
        }
        biz.edit.data.tr.append(th);
        biz.edit.data.tr.append(td);
        table.append(biz.edit.data.tr);
        biz.edit.data.tr = $("<tr></tr>");
        return biz.edit.data.tr;
    },

    addTextField: function (data, list, table, tr) {

        if (!table) {
            table = biz.edit.data.table;
        }
        if (tr) {
            biz.edit.data.tr = tr;
        }
        if (data.order === 1) {
            biz.edit.form.appendTd();
        }
        var th = $("<th></th>");
        var td = $("<td></td>");
        var input = $("<input type='text' class='fslTextBox'/>");
        biz.edit.form.addCkeckEmpty(data, th, input);
        //三个文本框类型不同组件
        if (data.viewComponent === "NUMBER") {
            input.keydown(banNumber).change(checkNumber).val(0);
        } else if (data.viewComponent === "DATETIME") {
            input.attr("readonly", "readonly").addClass("Wdate");
            if (data.name === "lastHandleTime") {
                var lastHandleTime = biz.detail.workInfo.limitTime;
                lastHandleTime = lastHandleTime.replace(/-/g, "/");
                var date = new Date(lastHandleTime);
                date -= 12 * 60 * 60 * 1000;
                lastHandleTime = new Date(parseInt(date));
                input.focus(function () {
                    WdatePicker({
                        lang: 'zh-cn',
                        dateFmt: 'yyyy-MM-dd HH:mm:ss',
                        maxDate: $.formatDate('yyyy-MM-dd HH:mm:ss', lastHandleTime)
                    })
                });
            } else
                input.focus(function () {
                    WdatePicker({
                        lang: 'zh-cn',
                        dateFmt: 'yyyy-MM-dd HH:mm:ss'
                    })
                });
        } else if (data.viewComponent === "DATE") {
            input.attr("readonly", "readonly");
            input.addClass("Wdate");
            input.focus(function () {
                WdatePicker({lang: 'zh-cn'})
            });
        }
        th.append(data.alias + ":");
        input.attr("name", data.name);
        td.append(input);
        biz.edit.data.tr.append(th);
        biz.edit.data.tr.append(td);
        if (biz.edit.data.tr.children("td").length === 2) {
            table.append(biz.edit.data.tr);
            biz.edit.data.tr = $("<tr></tr>");
        }
        return biz.edit.data.tr;
    },

    addComboBox: function (data, list, table, tr) {

        if (!table) {
            table = biz.edit.data.table;
        }
        if (tr) {
            biz.edit.data.tr = tr;
        }
        var th = $("<th></th>");
        var td = $("<td></td>");
        var select = $("<select id='" + data.name + "' name='" + data.name + "' class='fslTextBox'></select>");
        biz.edit.form.addCkeckEmpty(data, th, select);
        th.append(data.alias + ":");
        select.attr("name", data.name);
        select.addClass("js-example-basic-single");
        select.attr('data-width', '60%');
        if (data.viewComponent === "TREATMENT") {
            select.attr("onchange", "biz.edit.form.variableGroup({list:biz.edit.data.list,ele:'[name=" + data.name + "]'});$('.js-example-basic-single').select2();");
            if ($.isEmptyObject(data.viewParams)) {
                $.each(biz.detail.buttons, function (index, entity) {
                    var option = $('<option></option>');
                    option.text(entity);
                    option.val(entity);
                    select.append(option);
                });
            }
        }
        if (data.viewComponent === "DICTCOMBOBOX") {
            biz.edit.form.combobox.loadDictComboBox(select, data.viewParams);
        }  else if (!$.isEmptyObject(data.viewDatas)) {
            biz.edit.form.combobox.loadComboBox(select, data.viewDatas);
        }
        td.append(select);
        biz.edit.data.tr.append(th);
        biz.edit.data.tr.append(td);
        if (biz.edit.data.tr.children("td").length === 2) {
            table.append(biz.edit.data.tr);
            biz.edit.data.tr = $("<tr></tr>");
        }
        return biz.edit.data.tr;
    },

    //紧急级别组件
    addUrgencyLevel: function (data, table, tr) {
        if (!table) {
            table = biz.edit.data.table;
        }
        if (tr) {
            biz.edit.data.tr = tr;
        }
        var th = $("<th></th>");
        var td = $("<td></td>");
        var select = $("<select class='fslTextBox'></select>");
        biz.edit.form.addCkeckEmpty(data, th, select);
        th.append(data.alias + ":");
        select.attr("name", data.name);
        biz.edit.form.combobox.loadComboBox(select, data.viewDatas);
        td.append(select);
        biz.edit.data.tr.append(th);
        biz.edit.data.tr.append(td);
        if (biz.edit.data.tr.children("td").length === 2) {
            table.append(biz.edit.data.tr);
            biz.edit.data.tr = $("<tr></tr>");
        }
        ;
        th = $("<th></th>");
        td = $("<td></td>");
        th.append("最迟解决时间:");
        select.change(biz.edit.form.combobox.otherUrgencyLevel);
        var input = $("<input type='text' class='fslTextBox' readonly='readonly'/>");
        input.attr("name", "base.limitTime");
        input.addClass("Wdate");
        input.attr("style", "height: 22px;");
        var now = new Date();
        now.setDate(now.getDate() + 7);
        input.val(now.Format("yyyy-MM-dd hh:mm:ss"));
        td.append(input);
        biz.edit.data.tr.append(th);
        biz.edit.data.tr.append(td);
        if (biz.edit.data.tr.children("td").length === 2) {
            table.append(biz.edit.data.tr);
            biz.edit.data.tr = $("<tr></tr>");
        }
        return biz.edit.data.tr;
    },
    
    addBoolean: function (data, table, tr) {

        if (!table) {
            table = biz.edit.data.table;
        }
        if (tr) {
            biz.edit.data.tr = tr;
        }
        var th = $("<th></th>");
        var td = $("<td></td>");
        th.append(data.alias + ":");
        var yes = $("<input type='radio' value='是'/>");
        var no = $("<input type='radio' value='否' checked='checked'/>");
        yes.attr("name", data.name);
        no.attr("name", data.name);
        td.append(yes);
        td.append("是");
        td.append(no);
        td.append("否");
        biz.edit.data.tr.append(th);
        biz.edit.data.tr.append(td);
        if (biz.edit.data.tr.children("td").length === 2) {
            table.append(biz.edit.data.tr);
            biz.edit.data.tr = $("<tr></tr>");
        }
        return biz.edit.data.tr;
    },

    showMenu: function () { //显示树图
        var cityObj = $("#sectorCombo");
        var cityOffset = $("#sectorCombo").offset();
        $("#sectorMenuContent").css({
            left: cityOffset.left + "px",
            top: cityOffset.top + cityObj.outerHeight() + "px"
        }).slideDown("fast");

        $("body").bind("mousedown", biz.edit.form.memberLinkage.onBodyDown);
    },
    //补充本行单元格及生成新行
    appendTd: function (table, tr) {
        if (!table) {
            table = biz.edit.data.table;
        }
        if (tr) {
            biz.edit.data.tr = tr;
        }
        if (biz.edit.data.tr.children("td").length === 1 && biz.edit.data.tr.children("td").attr("colspan") !== 3) {
            var th = $("<th></th>");
            var td = $("<td></td>");
            biz.edit.data.tr.append(th);
            biz.edit.data.tr.append(td);
            table.append(biz.edit.data.tr);
            biz.edit.data.tr = $("<tr></tr>");
        }
        return biz.edit.data.tr;
    },
    addMessage: function (data, table, tr) {

        if (!table) {
            table = biz.edit.data.table;
        }
        if (tr) {
            biz.edit.data.tr = tr;
        }
        biz.edit.form.appendTd();
        var th = $("<th></th>");
        var td = $("<td colspan='3'></td>");
        th.append(data.alias + ":");
        var textarea = "<textarea name='base.handleMessage' rows='2' cols='20' class='fslTextBox' style='height:81px;width:90%;'></textarea>";
        td.html(textarea);
        biz.edit.form.addCkeckEmpty(data, th, td.children("textarea"));
        biz.edit.data.tr.append(th);
        biz.edit.data.tr.append(td);
        table.append(biz.edit.data.tr);
        biz.edit.data.tr = $("<tr></tr>");
        return biz.edit.data.tr;
    },
    addTitle: function (title, table, tr) {
        if (!table) {
            table = biz.edit.data.table;
        }
        if (tr) {
            biz.edit.data.tr = tr;
        }
        var th = $("<th></th>");
        th.html("<span title='*' style='color: #ff0000'>*</span>" + title + ":");
        var td = $("<td colspan='3'></td>");
        var input = $("<input type='text' maxlength='100' class='fslTextBox' style='width:70%;'/>");
        input.attr("name", "base.workTitle");
        input.attr("onchange", "checkEmpty(this)");
        input.attr("onfocus", "cleanCheck(this)");
        input.attr("checkEmpty", true);
        td.append(input);
        biz.edit.data.tr.append(th);
        biz.edit.data.tr.append(td);
        table.append(biz.edit.data.tr);
        biz.edit.data.tr = $("<tr></tr>");
        return biz.edit.data.tr;
    },

    /**
     * 备注禁用文本框
     * @param data
     * @param table
     * @param tr
     * @returns {*|jQuery|HTMLElement}
     */
    addRemark: function (data, table, tr) {

        if (!table) {
            table = biz.edit.data.table;
        }
        if (tr) {
            biz.edit.data.tr = tr;
        }

        biz.edit.form.appendTd();
        var th = $("<th></th>");
        var td = $("<td colspan='3' style='color:#FF0000;'></td>");
        th.text("备注:");
        td.text(data.viewDatas);
        biz.edit.data.tr.append(th);
        biz.edit.data.tr.append(td);
        table.append(biz.edit.data.tr);
        biz.edit.data.tr = $("<tr></tr>");
        return biz.edit.data.tr;
    },
    addFile: function (data, table, tr) {
        if (!table) {
            table = biz.edit.data.table;
        }
        if (tr) {
            biz.edit.data.tr = tr;
        }
        var th = $("<th></th>");
        th.text("相关附件:");
        var td = $("<td colspan='3'></td>");
        var tdText = "<span class='fslFileUpload' inputfileclass='FileUploadInputFileClass'><div class='fslFileUpload'>" +
            "<div class='FileUploadOperation'><img src='" + path + "/images/attach.gif' style='border-width:0px;'/>" +
            "<a onclick='biz.edit.form.file.addFileInput(this)' style='padding-right: 6px;' data-toggle='modal' data-target='#selectFile' class='UploadButton'>继续添加</a>" +
            "<img src='" + path + "/images/deleteAll.gif' style='border-width:0px;'/>" +
            "<a onclick='biz.edit.form.file.removeFile(this)' class='RemoveButton'>移除附件</a></div></div></span>";
        td.html(tdText);
        if (data) {
            for (var i = 0; i < data.length; i++) {
                if ($.isEmptyObject(data[i].fileCatalog) || data[i].fileCatalog === "uploadFile") {
                    biz.edit.fileNumber++;
                    span = $("<span style='margin-right: 10px; display: block;'></span>");
                    span.attr("id", "spanfile" + biz.edit.fileNumber);
                    var checkbox = $("<input type='checkbox'/>");
                    span.append(checkbox);
                    var a = $("<a id='" + data[i].id + "' href='" + path + "/biz/download?id=" + data[i].id + "'></a>");
                    a.text(data[i].name);
                    span.append(a);
                    td.append(span);
                }
            }
        }
        biz.edit.data.tr.append(th);
        biz.edit.data.tr.append(td);
        table.append(biz.edit.data.tr);
        biz.edit.data.tr = $("<tr></tr>");
        if ($("#selectFile").length < 1) {
            biz.edit.form.file.creatFileWindow();
        }
        return biz.edit.data.tr;
    },

    addRequiredFile: function (data, table, tr) {

        if (!table) {
            table = biz.edit.data.table;
        }
        if (tr) {
            biz.edit.data.tr = tr;
        }
        biz.edit.form.appendTd();
        var th = $("<th></th>");
        th.append(data.alias + ":");
        var td = $("<td colspan='3'></td>");
        var tdText = "<span class='fslFileUpload' inputfileclass='FileUploadInputFileClass'><div class='fslFileUpload'>" +
            "<div class='FileUploadOperation'><img src='" + path + "/themes/default/img/attach.gif' style='border-width:0px;'/>" +
            "<a onclick='biz.edit.form.file.addFileInput(this,\"" + data.name + "\")' style='padding-right: 6px;' data-toggle='modal' data-target='#selectFile' class='UploadButton'>继续添加</a>" +
            "<img src='" + path + "/themes/default/img/deleteAll.gif' style='border-width:0px;'/>" +
            "<a onclick='biz.edit.form.file.removeFile(this)' style='padding-right: 6px;' class='RemoveButton'>移除附件</a>";
        if (data.viewDatas) {
            biz.edit.form.file.data.downLoadFile = data.viewDatas;
            tdText = tdText + "<img src='" + path + "/themes/default/img/download.png' style='border-width:0px;'/>";
            tdText = tdText + "<a class='c_download' onclick='biz.edit.form.file.downLoadFile()'>模板下载</a>";
        }
        tdText = tdText + "</div></div></span>";
        td.html(tdText);
        var hiddenInput = $("<input type='hidden' name='requiredFileCount' />");
        td.append(hiddenInput);
        biz.edit.form.addCkeckEmpty(data, th, hiddenInput);
        if ((biz.create && biz.create.draftData && biz.create.draftData.annexs) || (biz.detail && biz.detail.annexs)) {
            var data2;
            if (biz.create && biz.create.draftData && biz.create.draftData.annexs) {
                data2 = biz.create.draftData.annexs;
            } else {
                data2 = biz.detail.annexs;
            }
            for (var i = 0; i < data2.length; i++) {
                if (data.name === data2[i].fileCatalog) {
                    biz.edit.fileNumber++;
                    var span = $("<span style='margin-right: 10px; display: block;'></span>");
                    span.attr("id", "spanfile" + biz.edit.fileNumber);
                    var checkbox = $("<input type='checkbox' />");
                    span.append(checkbox);
                    var a = $("<a id='" + data2[i].id + "' href='" + path + "/biz/download?id=" + data2[i].id + "'></a>");
                    a.text(data2[i].name);
                    span.append(a);
                    td.append(span);
                    hiddenInput.val("附件不为空");
                }
            }
        }
        biz.edit.data.tr.append(th);
        biz.edit.data.tr.append(td);
        table.append(biz.edit.data.tr);
        biz.edit.data.tr = $("<tr></tr>");
        if ($("#selectFile").length < 1) {
            biz.edit.form.file.creatFileWindow();
        }
        return biz.edit.data.tr;
    },
    addMember: function (data, table, tr) {

        biz.edit.form.memberbox.loadSectorBox();
        if (!table) {
            table = biz.edit.data.table;
        }
        if (tr) {
            biz.edit.data.tr = tr;
        }
        biz.edit.form.appendTd();
        var th = $("<th></td>");
        var td = $("<td colspan='3'></td>");
        th.append(data.alias + ":");
        var handleUser = data.name;
        biz.edit.form.memberbox.data.input = handleUser;
        biz.edit.form.memberbox.data.viewDatas = data.viewDatas;
        biz.edit.form.memberbox.data.showDept = true;
        var input = $("<input type='hidden' name='" + handleUser + "' class='fslTextBox'/>");
        td.append(input);
        handleUser = data.name + 'Name';
        input = $("<input type='text' name='" + handleUser + "' class='fslTextBox' style='width:60%' readonly='readonly'/>");
        biz.edit.form.memberbox.data.inputname = handleUser;

        biz.edit.form.addCkeckEmpty(data, th, input);
        var add = "<a class='btn btn-y' onclick='biz.edit.form.memberbox.openMemberContainer()'>选择人员</a>";
        var remove = "<a class='btn btn-n' onclick='biz.edit.form.memberbox.clearMember()'>清空人员</a>";
        td.append(input);
        td.append(add);
        td.append(remove);

        var container = biz.edit.form.memberbox.createMemberContainer();
        td.append(container);
        biz.edit.data.tr.append(th);
        biz.edit.data.tr.append(td);
        table.append(biz.edit.data.tr);
        biz.edit.data.tr = $("<tr></tr>");

        return biz.edit.data.tr;

    },
    //人员及联系方式联动
    addMemberLinkage: function (data, list, table, tr, type) {
        biz.edit.form.memberLinkage.loadSectorBox();
        if (!table) {
            table = biz.edit.data.table;
        }
        if (tr) {
            biz.edit.data.tr = tr;
        }
        biz.edit.form.appendTd();
        var th = $("<th></th>");
        var td = $("<td colspan='3'></td>");
        th.append(data.alias + ":");
        var handleUser = data.name;
        var input = $("<input type='hidden' name='" + handleUser + "' class='fslTextBox' />");
        handleUser = data.name + 'Name';
        var chInput = $("<input type='text' name='" + handleUser + "' class='fslTextBox' style='width:40%;' readonly='readonly'/>");
        biz.edit.form.addCkeckEmpty(data, th, input);
        var add = "<a class='btn btn-y mrl10' onclick='biz.edit.form.memberLinkage.openMemberContainer(\"" + data.name + "\")'>人员</a>";
        var addRole = "<a class='btn btn-y mrl10' onclick='biz.edit.form.memberLinkage.openRoleContainer()'>组别</a>";
        var remove = "<a class='btn btn-n mrl10' onclick='biz.edit.form.memberLinkage.clearMember(\"" + data.name + "\")'>清空</a>";
        td.append(input);
        td.append(chInput);
        td.append(add);
        if (type !== 1 && type !== 2) {
            td.append(addRole);
        }
        td.append(remove);
        biz.edit.form.memberLinkage.data[data.name + "inputName"] = data.name;
        //加入联动，注意联系方式参数是否设置联动
        if (list) {
            if (list !== null && list.length > 0 && type !== 2) {
                for (var i = 0; i < list.length; i++) {
                    if (!list[i].refVariable)
                        continue;
                    if (data.id === list[i].refVariable) {
                        biz.edit.form.memberLinkage.data.mobileName = list[i].name;
                    }
                }
            } else if (list != null && list.length > 0 && type === 2) {
                for (var i = 0; i < list.length; i++) {
                    if (!list[i].refVariable){
                        continue;
                    }
                    if (data.id === list[i].refVariable) {
                        if (list[i].viewComponent === "TEXT"){
                            biz.edit.form.memberLinkage.data[data.name + "department"] = list[i].name;
                        }else if (list[i].viewComponent === "MOBILE"){
                            biz.edit.form.memberLinkage.data[data.name + "mobileName"] = list[i].name;
                        } else if (list[i].viewComponent === "EMAIL"){
                            biz.edit.form.memberLinkage.data[data.name + "email"] = list[i].name;
                        }
                    }
                }
            }
        }
        var container = biz.edit.form.memberLinkage.createMemberContainer(data.name);
        td.append(container);
        var div = $('<div id="roleMenuContent" class="menuContent" style="display:none; position: absolute;"></div>');
        var ul = $('<ul id="roleTree" class="ztree" style="margin-top:0; width:180px; height: 300px;"></ul>');
        div.append(ul);
        td.append(div);
        biz.edit.data.tr.append(th);
        biz.edit.data.tr.append(td);
        table.append(biz.edit.data.tr);
        biz.edit.data.tr = $("<tr></tr>");
        biz.edit.form.memberLinkage.roleTree.loadRoleTree();
        return biz.edit.data.tr;
    },
    //角色人员组件
    addMemberList: function (data, table, tr) {

        if (!table) {
            table = biz.edit.data.table;
        }
        if (tr) {
            biz.edit.data.tr = tr;
        }
        biz.edit.form.appendTd();
        var th = $("<th></td>");
        var td = $("<td colspan='3'></td>");
        var input = $("<input type='text' class='fslTextBox' readonly='readonly' style='width:50%;'/>");
        input.attr("id", data.name);
        input.click(function () {
            biz.edit.form.memberList.openWindow()
        });
        biz.edit.form.addCkeckEmpty(data, th, input);
        th.append(data.alias + ":");
        td.append(input);
        var hidden = $("<input type='hidden' class='fslTextBox' readonly='readonly' style='width:50%;'/>");
        hidden.attr("name", data.name);
        td.append(hidden);
        td.append(biz.edit.form.memberList.createWindow(data));
        biz.edit.data.tr.append(th);
        biz.edit.data.tr.append(td);
        table.append(biz.edit.data.tr);
        biz.edit.data.tr = $("<tr></tr>");
        return biz.edit.data.tr;
    },

    //非空处理
    addCkeckEmpty: function (data, th, component) {
        if (data.required) {
            if (th.text()) {
                th.empty();
                th.append("<span title='*' style='color: #ff0000'>*</span>");
                th.append(data.alias + ":");
            } else {
                th.append("<span title='*' style='color: #ff0000'>*</span>");
            }
            component.attr("checkEmpty", true);
            component.change(checkEmpty);
            component.focus(cleanCheck);
        }
    },
    //添加个分组头部
    addGroupHead: function (data, table, tr) {
        if (!table) {
            table = biz.edit.data.table;
        }
        if (tr) {
            biz.edit.data.tr = tr;
        }
        biz.edit.form.appendTd();
        var hidden = $("<input type='hidden' name='" + data.name + "' value='true'>");
        var th = $("<th>");
        var td = $("<td colspan='5' style='padding:0;'>");
        var div = $("<h5 style='padding:6px 5px;background:#f7f7f7;'>" + data.alias + "：" + data.groupName + "</h5>");
        td.append(hidden, div);
        biz.edit.data.tr.append(td);
        table.append(biz.edit.data.tr);
        biz.edit.data.tr = $("<tr>");
        return biz.edit.data.tr;
    },
    //确认人组件，包括确认人、联系方式、时间、部门
    addConfirmUser: function (data, table, tr) {
        if (!table) {
            table = biz.edit.data.table;
        }
        if (tr) {
            biz.edit.data.tr = tr;
        }
        var hidden = $("<input type='hidden' name='" + data.name + "'/>");
        biz.edit.data.tr.append(hidden);
        //text：label，name：文本框相关标志涉及回显，value：显示值，disable：是否可编辑
        var list = [
            {
                text: "处理人",
                name: "user",
                value: currentUser.name,
                disabled: true
            }, {
                text: "联系方式",
                name: "mobile",
                value: currentUser.mobile
            }, {
                text: "邮箱",
                name: "email",
                value: currentUser.email
            }, {
                text: "确认时间",
                name: "date",
                value: (new Date()).Format("yyyy-MM-dd hh:mm"),
                disabled: true
            }, {
                text: "确认部门",
                name: "dep",
                value: currentUser.dep
            }];
        for (var key in list) {
            var input = biz.edit.form.addDisable(list[key]);
            biz.edit.confirmUser[list[key].name] = list[key].value;
            input.change(function () {
                biz.edit.confirmUser[$(this).attr("title")] = $(this).val();
                biz.edit.data.table.find("[name='" + data.name + "']").val(JSON.stringify(biz.edit.confirmUser));
            });
        }
        hidden.val(JSON.stringify(biz.edit.confirmUser));
    },
    //加入禁用文本框，不属于流程组件
    addDisable: function (data, table, tr) {
        if (!table) {
            table = biz.edit.data.table;
        }
        if (tr) {
            biz.edit.data.tr = tr;
        }
        var th = $("<th></th>");
        th.html(data.text);
        var td = $("<td></td>");
        var input = $("<input type='text' class='fslTextBox' title='" + data.name + "'/>");
        if (data.disabled)
            input.attr("disabled", "disabled");
        input.val(data.value);
        td.append(input);
        biz.edit.data.tr.append(th);
        biz.edit.data.tr.append(td);
        if (biz.edit.data.tr.children("td").length === 2) {
            table.append(biz.edit.data.tr);
            biz.edit.data.tr = $("<tr></tr>");
        }
        ;
        return input;
    }
};

biz.edit.form.orgTree = {
    setting: {
        view: {
            dblClickExpand: false
        },
        data: {
            simpleData: {
                enable: true
            }
        },
        callback: {
            beforeClick: function (treeId, treeNode) {
                var check = (treeNode && !treeNode.isParent);
            },
            onClick: function (e, treeId, treeNode) {
                var zTree = $.fn.zTree.getZTreeObj("orgSectorTree");
                nodes = zTree.getSelectedNodes(),
                    vId = "",
                    v = "";
                nodes.sort(function compare(a, b) {
                    return a.id - b.id;
                });
                for (var i = 0, l = nodes.length; i < l; i++) {
                    v += nodes[i].name + ",";
                    vId += nodes[i].id + ",";
                }
                if (v.length > 0)
                    v = v.substring(0, v.length - 1);
                if (vId.length > 0)
                    vId = vId.substring(0, vId.length - 1);
                var cityObj = $("#orgSectorCombo"),
                    cityValue = $('#orgSectorComboVal');
                if (vId === "") {
                    cityObj.val("");
                    cityValue.val("");
                } else {
                    cityObj.val(v);
                    cityValue.val(vId);
                }
                biz.edit.form.orgTree.hideMenu();
            }
        }
    },
    zNodes: null,
    showMenu: function () {
        biz.edit.form.orgTree.sectorInit();
        var cityObj = $("#orgSectorCombo");
        var cityOffset = $("#orgSectorCombo").position();
        $("#orgSectorMenuContent").css({
            left: cityOffset.left + "px",
            top: cityOffset.top + cityObj.outerHeight() + "px"
        }).slideDown("fast");
        $("body").bind("mousedown", biz.edit.form.orgTree.onBodyDown);
    },
    hideMenu: function () {
        $("#orgSectorMenuContent").fadeOut("fast");
        $("body").unbind("mousedown", biz.edit.form.orgTree.onBodyDown);
    },
    onBodyDown: function (event) {
        if (!(event.target.id === "orgSectorCombo" || event.target.id === "orgSectorMenuContent" || $(event.target).parents("#orgSectorMenuContent").length > 0)) {
            biz.edit.form.orgTree.hideMenu();
        }
    },
    sectorInit: function () {
        $.fn.zTree.init($("#orgSectorTree"), biz.edit.form.orgTree.setting, biz.edit.form.orgTree.zNodes);
        //初始化选择第一个
        var zTree = $.fn.zTree.getZTreeObj("orgSectorTree");
        zTree.addNodes(null, 0, {
            id: "",
            name: "选空",
            nocheck: true
        });
        var nodes = zTree.getNodes();
        zTree.selectNode(nodes[0]);
        zTree.setting.callback.onClick(null, zTree.setting.treeId, nodes[0]);
    },
    loadSectorBox: function () {
        $.ajax({
            type: "post",
            url: path + "/bizHandle/loadSectors",
            async: false,
            success: function (data) {
                if (data != null && data.success && data.obj != null) {
                    biz.edit.form.orgTree.zNodes = data.obj;
                }
            }
        });
    }
};

$(window).resize(function () {
    if ($('#configcontainer').length > 0 && !$('#configcontainer').is(":hidden")) {
        $('#configcontainer').css('width', "0");
        var width = $("[name='configtable']").css('width');
        var width = width.substring(0, width.length - 2);
        $('#configcontainer').css('width', (parseInt(width) + 2));
    }
    if ($('#memberContainer').length > 0 && !$('#memberContainer').is(":hidden")) {
        $('#memberContainer').css('width', "0");
        var width = $("[name='" + biz.edit.form.memberbox.data.inputname + "']").parent('td').css('width');
        width = width.substring(0, width.length - 2) - 10;
        $('#memberContainer').css('width', (parseInt(width) + 2));
    }
    if ($('#memberLinkageContainer').length > 0 && !$('#memberLinkageContainer').is(":hidden")) {
        $('#memberLinkageContainer').css('width', "0");
        var width = $("#memberLinkageContainer").parent('td').css('width');
        width = width.substring(0, width.length - 2) - 10;
        $('#memberLinkageContainer').css('width', (parseInt(width) + 2));
    }
});