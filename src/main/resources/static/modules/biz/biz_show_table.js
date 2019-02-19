$.namespace("biz.show");

biz.show = {
    data : {
        tr : $("<tr></tr>")
    },
    getView : function(option) {
        for (var key in biz.edit.data) {
            delete biz.show.data[key];
        }
        for (var key in option) {
            biz.show.data[key] = option[key];
            if (option.list) {
                biz.show.data.list = biz.show.table.listByTreatment(option.list);
            }
        }
        biz.show.data.tr = $("<tr></tr>");
        return biz.show.table;
    }
};

biz.show.table = {
    setDynamic : function(option) {
        if (option) {
            if (option.table) {
                biz.show.data.table = option.table;
            }
            if (option.tr) {
                biz.show.data.tr = option.tr;
            }
        } else {
            option = {};
        }
        if (!option.list) {
            option.list = biz.show.data.list;
        }
        if (!$.isEmptyObject(option.list)) {
            option.list.forEach(function(entity){
                switch (entity.viewComponent) {
                    case "TEXTAREA":
                        biz.show.table.addTextarea(entity);
                        break;
                    case "TEXT":
                        biz.show.table.addTextField(entity);
                        break;
                    case "REQUIREDFILE":
                        biz.show.table.addRequiredFile(entity);
                        break;
                    case "GROUPHEAD":
                        biz.show.table.addGroupHead(entity);
                        break;
                    default:
                        biz.show.table.addTextField(entity);
                }
            });
            if (option.end || option.end === undefined) {
                biz.show.table.appendTd(option.table);
            }
        }
        return biz.show.data.tr;
    },
    /**
     * 文本域
     * @param data
     * @param table
     * @param tr
     * @returns {*}
     */
    addTextarea : function(data, table, tr) {
        if (!table) {
            table = biz.show.data.table;
        }
        if (tr) {
            biz.show.data.tr = tr;
        }
        biz.show.table.appendTd();
        var th = $("<th></th>");
        th.text(data.alias + ":");
        var td = $("<td colspan='3'></td>");
        var name = data.id ? data.id + "&" + biz.show.data.taskId : "";
        td.html("<span class='fslTextBoxR' name='" + name + "'></span>");

        td.text(data.value);
        biz.show.data.tr.append(th);
        biz.show.data.tr.append(td);
        table.append(biz.show.data.tr);
        biz.show.data.tr = $("<tr></tr>");
        return td.children("span");
    },
    /**
     * 附件
     * @param data
     * @param table
     * @param tr
     * @returns {*}
     */
    addRequiredFile : function(data, table, tr) {

        if (!table) {
            table = biz.show.data.table;
        }
        if (tr) {
            biz.show.data.tr = tr;
        }
        biz.show.table.appendTd();
        var th = $("<th></th>");
        th.text(data.alias + ":");
        var td = $("<td colspan='3'></td>");
        var name = data.id ? data.id + "&" + biz.show.data.taskId : "";
        td.html("<span class='fslTextBoxR' name='" + name + "'></span>");
        biz.show.data.tr.append(th);
        biz.show.data.tr.append(td);
        table.append(biz.show.data.tr);
        biz.show.data.tr = $("<tr></tr>");
        return td.children("span");
    },
    /**
     * 分组头部
     * @param data
     * @param table
     * @param tr
     * @returns {*|jQuery|HTMLElement}
     */
    addGroupHead : function(data, table, tr) {
        if (!table) {
            table = biz.show.data.table;
        }
        if (tr) {
            biz.show.data.tr = tr;
        }
        biz.show.table.appendTd();
        var hidden = $("<input type='hidden' name='" + data.name + "' value='true'>");
        var th = $("<th>");
        var td = $("<td colspan='5' style='padding:0'>");
        var div = $("<h5 style='padding:6px 5px;background:#f7f7f7' >" + data.alias + "：" + data.groupName + "</h5>");
        td.append(hidden, div);
        biz.show.data.tr.append(td);
        table.append(biz.show.data.tr);
        biz.show.data.tr = $("<tr>");
        return biz.show.data.tr;
    },

    /**
     * 文本框
     * @param data
     * @param table
     * @param tr
     * @returns {*}
     */
    addTextField : function(data, table, tr) {
        if (!table) {
            table = biz.show.data.table;
        }
        if (tr) {
            biz.show.data.tr = tr;
        }
        var th = $("<th></th>");
        th.text(data.alias + ":");
        var td = $("<td></td>");
        var name = data.id ? data.id + "&" + biz.show.data.taskId : "";
        td.html("<span class='fslTextBoxR' name='" + name + "'></span>");
        td.text(data.value);
        biz.show.data.tr.append(th);
        biz.show.data.tr.append(td);
        if (biz.show.data.tr.children("td").length === 2) {
            table.append(biz.show.data.tr);
            biz.show.data.tr = $("<tr></tr>");
        }
        return td.children("span");
    },

    /**
     * 用户信息
     * @param data
     * @param table
     * @param tr
     * @returns {*}
     */
    addUserInfo : function(data, table, tr) {

        if (!table) {
            table = biz.show.data.table;
        }
        if (tr) {
            biz.show.data.tr = tr;
        }
        var th = $("<th></th>");
        th.text(data.alias + ":");
        var td = $("<td></td>");
        var name = data.id ? data.id + "&" + biz.show.data.taskId : "";
        td.html("<span class='fslTextBoxR' name='" + name + "'></span>");
        biz.show.data.tr.append(th);
        biz.show.data.tr.append(td);
        if (biz.show.data.tr.children("td").length === 2) {
            table.append(biz.show.data.tr);
            biz.show.data.tr = $("<tr></tr>");
        }
        ;
        return td.children("span");
    },
    appendTd : function(table, tr) {
        if (!table) {
            table = biz.show.data.table;
        }
        if (tr) {
            biz.show.data.tr = tr;
        }
        if (biz.show.data.tr.children("td").length === 1 && biz.show.data.tr.children("td").attr("colspan") !== 3) {
            var th = $("<th></th>");
            var td = $("<td></td>");
            biz.show.data.tr.append(th);
            biz.show.data.tr.append(td);
            table.append(biz.show.data.tr);
            biz.show.data.tr = $("<tr></tr>");
        }
        return biz.show.data.tr;
    },
    /**
     * 处理方式
     */
    listByTreatment : function(list) {
        var treatmentId = null;
        for (var i = 0; i < list.length; i++) {
            if (list[i].name === "treatment") {
                treatmentId = list[i].id;
                break;
            }
        }
        var array = [];
        if (treatmentId != null) {
            var treatment = null;
            var serviceInfo = biz.detail.serviceInfo;
            for (var i in serviceInfo) {
                if (treatmentId === serviceInfo[i].variable.id) {
                    treatment = serviceInfo[i].value;
                    break;
                }
            }
            if (treatment == null) {
                return list;
            }
            for (var i = 0; i < list.length; i++) {
                if (list[i].variableGroup === undefined) {
                    return list;
                }
                var groups = list[i].variableGroup.split(",");
                for (var j = 0; j < groups.length; j++) {
                    if (groups[j].trim() === treatment.trim() || list[i].name === "treatment") {
                        array.push(list[i]);
                        break;
                    }
                }
            }
        } else {
            array = list;
        }
        return array;
    },
    /**
     * 子单
     */
    addSonBiz : function(data, table, tr) {

        if (!table) {
            table = biz.show.data.table;
        }
        if (tr) {
            biz.show.data.tr = tr;
        }
        biz.show.table.appendTd();
        var th = $("<th></th>");
        th.text(data.alias + ":");
        var td = $("<td colspan='3'></td>");
        var div = $("<div></div>");
        td.append(div);
        biz.show.data.tr.append(th);
        biz.show.data.tr.append(td);
        table.append(biz.show.data.tr);
        biz.show.data.tr = $("<tr></tr>");
        td = $("<td colspan='4' style='padding:0;'></td>");
        var bizTable = $("<table style='width:100%;border:hidden;' class='table base-table table-striped'></table>");
        td.append(bizTable);
        biz.show.data.tr.append(td);
        table.append(biz.show.data.tr);
        biz.show.data.tr = $("<tr></tr>");
        biz.show.table.sonBiz.loadSonBiz(bizTable, div, data.data);
        return biz.show.data.tr;
    },
    /**
     * 附件
     */
    addFile : function(annexs, table, tr) {
        if (annexs)
            biz.show.data.annexs = annexs;
        if (!table)
            table = biz.show.data.table;
        if (tr) {
            biz.show.data.tr = tr;
        }
        biz.show.table.appendTd();
        var th = $("<th></th>");
        th.text("相关附件:");
        var td = $("<td colspan='3'></td>");
        if (biz.show.data.annexs && biz.show.data.annexs !== "")
            for (var i = 0; i < biz.show.data.annexs.length; i++) {
                if (annexs[i].fileCatalog == null || annexs[i].fileCatalog === "" || annexs[i].fileCatalog === "uploadFile") {
                    td.append("<span style='margin-right:10px;display:block;'><a href='" + path + "/biz/download?id=" + annexs[i].id + "'>" + annexs[i].name + "</a></span>");
                }
            }
        if (td.children().length === 0){
            td.text("无");
        }
        biz.show.data.tr.append(th);
        biz.show.data.tr.append(td);
        table.append(biz.show.data.tr);
    },

    /**
     * 必填附件
     * @param data
     */
    addReqFiles : function(data) {

        $.ajax({
            url : path + '/biz/getBizFile',
            type : 'post',
            data : {
                bizId : bizId,
                taskId : data.taskId,
                name : data.variable.name
            },
            success : function(result) {
                if (result) {
                    $.each(result, function(index, entity) {
                        var span = $("<span style='margin-right:10px;display:block;'><a href='" + path + "/biz/download?id=" + entity.id + "'>" + entity.name + "</a></span>");
                        $("span[name='" + data.variable.id + "&" + data.taskId + "']").append(span);
                    });
                } else {
                    $("span[name='" + data.variable.id + "&" + data.taskId + "']").html('无');
                }
            }
        });
    },
    /**
     * 必传附件
     */
    addreqFile : function(list, annexs) {
        for (var i = 0; i < list.length; i++) {
            if (list[i].viewComponent === "REQUIREDFILE") {
                var s = new Set();
                if (annexs) {
                    for (var j = 0; j < annexs.length; j++) {
                        if (list[i].name === annexs[j].fileCatalog) {
                            var span = $("<span style='margin-right:10px;display:block;'><a href='" + path + "/biz/download?id=" + annexs[j].id + "'>" + annexs[j].name + "</a></span>");
                            var taskId = (annexs[j].taskId == null) ? "undefined" : annexs[j].taskId;
                            var name = list[i].id + "&" + taskId;
                            s.add(name);
                            $("body").find("span[name='" + name + "']").append(span);
                        }
                    }
                }
                s.forEach(function(item) {
                    if ($("body").find("span[name='" + item + "']").children().length === 0) {
                        $("body").find("span[name='" + item + "']").append("无");
                    }
                });
            }
        }
    },
    /**
     * 确认人信息
     */
    addConfirmUser : function(data, table, tr) {
        if (!table) {
            table = biz.show.data.table;
        }
        if (tr) {
            biz.show.data.tr = tr;
        }
        var list = [
            {
                text : "处理人",
                name : "user"
            }, {
                text : "联系方式",
                name : "mobile"
            }, {
                text : "邮箱",
                name : "email"
            }, {
                text : "确认时间",
                name : "date"
            }, {
                text : "确认部门",
                name : "dep"
            } ];
        for (var i = 0; i < list.length; i++) {

            var th = $("<th></th>");
            th.html(list[i].text);
            var td = $("<td></td>");
            var span = $("<span class='fslTextBoxR'></span>");
            span.attr("name", data.id + "&" + biz.show.data.taskId);
            span.attr("title", list[i].name);
            td.append(span);
            biz.show.data.tr.append(th);
            biz.show.data.tr.append(td);
            if (biz.show.data.tr.children("td").length === 2) {
                table.append(biz.show.data.tr);
                biz.show.data.tr = $("<tr></tr>");
            }
        }
    }
};

biz.show.table.confirmUser = {
    setConfirmUserValue : function(data) {
        var value = eval("(" + data.value + ")");
        for (var key in value) {
            $("span[name='" + data.variable.id + "&" + data.taskId + "'][title='" + key + "']").html(value[key]);
        }
    }
};
/**
 * 用户信息相关
 */
biz.show.table.userInfo = {
    setUserName : function(serviceInfo) {
        $.ajax({
            url : path + '/role/loadUsersByUserName',
            type : 'post',
            async : false,
            data : {
                userName : serviceInfo.value
            },
            success : function(data) {
                $("span[name='" + serviceInfo.variable.id + "&" + serviceInfo.taskId + "']").html(data.name);
                $("input[name='" + serviceInfo.variable.name + "Name']").val(data.name);
                $("input[name='" + serviceInfo.variable.name + "']").val(data.username);
            }
        });
    },

    getUserByUsername : function(username){

        let result = {};
        $.ajax({
            url: path + '/role/loadUsersByUserName',
            type: 'post',
            async: false,
            data: {
                userName: username
            },
            success: function (data) {
                if (data) {
                    result = data;
                }
            }
        });
        return result;
    },

    setUserNames : function(serviceInfo) {
        var userNames = serviceInfo.value.split(",");
        var userName = "";
        var fullName = "";
        $.each(userNames, function(index, username) {
            if (!$.isEmptyObject(username)) {
                $.ajax({
                    url : path + '/role/loadUsersByUserName',
                    type : 'post',
                    async : false,
                    data : {
                        userName : username
                    },
                    success : function(data) {
                        fullName += data.name + ",";
                        userName += data.username + ",";
                    }
                });
            }
        });
        $("span[name='" + serviceInfo.variable.id + "&" + serviceInfo.taskId + "']").html(fullName);
        $("input[name='" + serviceInfo.variable.name + "Name']").val(fullName);
        $("input[name='" + serviceInfo.variable.name + "']").val(userName);
    }
};

biz.show.table.sonBiz = {
    loadSonBiz : function(table, div, data) {
        table.bootstrapTable({
            data : data,
            classes : "table-no-bordered",
            columns : [ {
                field : "workNum",
                title : "工单号",
                align : "center",
                formatter : function(value, row, index) {
                    var url = path + "/biz/" + row.id;
                    return "<a onclick=\"window.open('" + url + "');\">" + value + "</a>";
                }
            }, {
                field : "bizType",
                title : "工单类型",
                align : "center"
            }, {
                field : "title",
                title : "工单标题",
                align : "center"
            }, {
                field : "createUser",
                title : "创建人",
                align : "center"
            }, {
                field : "createTime",
                title : "创建时间",
                align : "center"
            }, {
                field : "status",
                title : "工单状态",
                align : "center"
            }, {
                field : "taskAssignee",
                title : "当前处理人",
                align : "center"
            } ]
        });

        $.each(data, function(index, entity) {
            var checkbox = $("<input type='checkbox'/>");
            var span = $("<span style='display: inline-block;'></span>");
            span.append(checkbox).append(entity.workNum);
            div.append(span);
        });
    }
};