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
        if (!$.isEmptyObject(option.list)) {
            option.list.forEach(function (entity) {
                switch (entity.viewComponent) {
                    case "TEXTAREA":
                        biz.edit.form.addTextarea(entity);
                        break;
                    case "TEXT":
                        biz.edit.form.addTextField(entity, option.list);
                        break;
                    case "COMBOBOX":
                        biz.edit.form.addComboBox(entity, option.list);
                        break;
                    case "DICTCOMBOBOX":
                        biz.edit.form.addComboBox(entity, option.list);
                        break;
                    case "TREATMENT":
                        biz.edit.form.addComboBox(entity, option.list);
                        break;
                    case "BOOLEAN":
                        biz.edit.form.addBoolean(entity);
                        break;
                    case "GROUPHEAD":
                        biz.edit.form.addGroupHead(entity);
                        break;
                    case "REMARK":
                        biz.edit.form.addRemark(entity);
                        break;
                    case "REQUIREDFILE":
                        biz.edit.form.addRequiredFile(entity);
                        break;
                    default:
                        biz.edit.form.addTextField(entity);
                }
            });
            if (option.end || !option.end) {
                biz.edit.form.appendTd();
            }
        }
        biz.edit.data.tr = $("<tr></tr>");
        return biz.edit.data.tr;
    },

    /**
     * 处理方式分组表单元素
     * @param option
     */
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

    /**
     * 处理方式分组按钮
     * @param buttons
     */
    variableButton: function (buttons) {
        $("#formButtons").remove();
        var buttonList = $("<div id='formButtons' class='btn_list' style='padding:10px 0;margin:0;'></div>");
        $("#workForm").append(buttonList);
        $.each(buttons, function (k, n) {
            buttonList.append("<a class='yes_btn mrr10' onclick=biz.detail.save('" + k.trim() + "')>" + n + "</a>");
        });
        buttonList.append("<a onclick='javascript:window.opener=null;window.close();'>关闭</a>");
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
        biz.edit.form.addCheckEmpty(data, th, td.children("textarea"));
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
        biz.edit.form.addCheckEmpty(data, th, input);
        //三个文本框类型不同组件
        if (data.viewComponent === "NUMBER") {
            input.keydown(banNumber).change(checkNumber).val(0);
            input.attr('checkNumber',true);
        } else if (data.viewComponent === "DATETIME") {
            input.attr("readonly", "readonly").addClass("Wdate");
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

    /**
     * 下拉框
     * @param data
     * @param list
     * @param table
     * @param tr
     * @returns {*|jQuery|HTMLElement}
     */
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
        biz.edit.form.addCheckEmpty(data, th, select);
        th.append(data.alias + ":");
        select.attr("name", data.name);
        select.addClass("js-example-basic-single");
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
        } else if (!$.isEmptyObject(data.viewDatas)) {
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

    /**
     * 补充本行单元格及生成新行
     * @param table
     * @param tr
     * @returns {*|jQuery|HTMLElement}
     */
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
        biz.edit.form.addCheckEmpty(data, th, td.children("textarea"));
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
            "<div class='FileUploadOperation'><img src='/images/attach.gif' style='border-width:0px;'/>" +
            "<a onclick='biz.edit.form.file.addFileInput(this)' style='padding-right: 6px;' data-toggle='modal' data-target='#selectFile' class='UploadButton'>继续添加</a>" +
            "<img src='/images/deleteAll.gif' style='border-width:0px;'/>" +
            "<a onclick='biz.edit.form.file.removeFile(this)' class='RemoveButton'>移除附件</a></div></div></span>";
        td.html(tdText);
        if (data) {
            data.forEach(function (entity) {
                if ($.isEmptyObject(entity['fileCatalog']) || entity['fileCatalog'] === "uploadFile") {
                    biz.edit.fileNumber++;
                    span = $("<span style='margin-right: 10px; display: block;'></span>");
                    span.attr("id", "spanfile" + biz.edit.fileNumber);
                    var checkbox = $("<input type='checkbox'/>");
                    span.append(checkbox);
                    var a = $("<a id='" + entity.id + "' href='/biz/download?id=" + entity.id + "'></a>");
                    a.text(entity.name);
                    span.append(a);
                    td.append(span);
                }
            })
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

    /**
     * 必传附件
     * @param data
     * @param table
     * @param tr
     * @returns {*|jQuery|HTMLElement}
     */
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
            "<div class='FileUploadOperation'><img src='/images/attach.gif' style='border-width:0px;'/>" +
            "<a onclick='biz.edit.form.file.addFileInput(this,\"" + data.name + "\")' style='padding-right: 6px;' data-toggle='modal' data-target='#selectFile' class='UploadButton'>继续添加</a>" +
            "<img src='/images/deleteAll.gif' style='border-width:0px;'/>" +
            "<a onclick='biz.edit.form.file.removeFile(this)' style='padding-right: 6px;' class='RemoveButton'>移除附件</a>";
        if (data.viewDatas) {
            biz.edit.form.file.data.downLoadFile = data.viewDatas;
            tdText = tdText + "<img src='/themes/default/img/download.png' style='border-width:0px;'/>";
            tdText = tdText + "<a class='c_download' onclick='biz.edit.form.file.downLoadFile()'>模板下载</a>";
        }
        tdText = tdText + "</div></div></span>";
        td.html(tdText);
        var hiddenInput = $("<input type='hidden' name='requiredFileCount' />");
        td.append(hiddenInput);
        biz.edit.form.addCheckEmpty(data, th, hiddenInput);
        if ((biz.create && biz.create.draftData && biz.create.draftData.annexs) || (biz.detail && biz.detail.annexs)) {
            var fileList;
            if (biz.create && biz.create.draftData && biz.create.draftData.annexs) {
                fileList = biz.create.draftData.annexs;
            } else {
                fileList = biz.detail.annexs;
            }
            if(fileList){
                fileList.forEach(function (entity) {
                    if (data.name === entity['fileCatalog']) {
                        biz.edit.fileNumber++;
                        var span = $("<span style='margin-right: 10px; display: block;'></span>");
                        span.attr("id", "spanfile" + biz.edit.fileNumber);
                        var checkbox = $("<input type='checkbox' />");
                        span.append(checkbox);
                        var a = $("<a id='" + entity.id + "' href='/biz/download?id=" + entity.id + "'></a>");
                        a.text(entity.name);
                        span.append(a);
                        td.append(span);
                        hiddenInput.val("附件不为空");
                    }
                })
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

    /**
     * 非空处理
     * @param data
     * @param th
     * @param component
     */
    addCheckEmpty: function (data, th, component) {
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

    /**
     * 添加个分组头部
     * @param data
     * @param table
     * @param tr
     * @returns {*|jQuery|HTMLElement}
     */
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
    var $memberLinkageContainer = $('#memberLinkageContainer');
    if ($memberLinkageContainer.length > 0 && !$memberLinkageContainer.is(":hidden")) {
        $memberLinkageContainer.css('width', "0");
        var width = $memberLinkageContainer.parent('td').css('width');
        width = width.substring(0, width.length - 2) - 10;
        $memberLinkageContainer.css('width', (parseInt(width) + 2));
    }
});