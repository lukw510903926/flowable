$.namespace("biz.show");

biz.show = {
    data: {
        tr: $("<tr></tr>")
    },
    getView: function (option) {
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
    setDynamic: function (option) {
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
            option.list.forEach(function (entity) {
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
    addTextarea: function (data, table, tr) {
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
    addRequiredFile: function (data, table, tr) {

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
        if (!$.isEmptyObject(data.value)) {
            var file = JSON.parse(data.value);
            file.forEach(function (entity) {
                td.append("<span style='margin-right:10px'><a href='/biz/download?id=" + entity.id + "'>" + entity.name + "</a></span>");
            });
        }
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
    addGroupHead: function (data, table, tr) {
        if (!table) {
            table = biz.show.data.table;
        }
        if (tr) {
            biz.show.data.tr = tr;
        }
        biz.show.table.appendTd();
        var hidden = $("<input type='hidden' name='" + data.name + "' value='true'>");
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
    addTextField: function (data, table, tr) {
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

    appendTd: function (table, tr) {
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
    listByTreatment: function (list) {
        var treatmentId = null;
        list.forEach(function (entity) {
            if (entity.name === "treatment") {
                treatmentId = entity.id;
            }
        });
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
    loadTable: function (data, table, tr) {

        if (!table) {
            table = biz.show.data.table;
        }
        if (tr) {
            biz.show.data.tr = tr;
        }
        biz.show.table.appendTd();
        table.append(biz.show.data.tr);
        biz.show.data.tr = $("<tr></tr>");
        var td = $("<td colspan='4' style='padding:0;'></td>");
        var bizTable = $("<table class='table base-table table-striped'></table>");
        td.append(bizTable);
        biz.show.data.tr.append(td);
        table.append(biz.show.data.tr);
        biz.show.data.tr = $("<tr></tr>");
        bizTable.bootstrapTable({
            data: data.data,
            classes: "table-no-bordered",
            columns: data.columns
        });
        return biz.show.data.tr;
    },
    /**
     * 附件
     */
    addFile: function (bizFiles, table, tr) {

        biz.show.data.annexs = bizFiles ? bizFiles : [];
        if (!table) {
            table = biz.show.data.table;
        }
        if (tr) {
            biz.show.data.tr = tr;
        }
        biz.show.table.appendTd();
        var th = $("<th></th>");
        th.text("相关附件:");
        var td = $("<td colspan='3'></td>");
        if (!$.isEmptyObject(biz.show.data.annexs)) {
            $.each(biz.show.data.annexs, function (index, entity) {
                if ($.isEmptyObject(entity['fileCatalog']) || entity['fileCatalog'] === "uploadFile") {
                    td.append("<span style='margin-right:10px;display:block;'><a href='/biz/download?id=" + entity.id + "'>" + entity.name + "</a></span>");
                }
            });
        }
        if (td.children().length === 0) {
            td.text("无");
        }
        biz.show.data.tr.append(th);
        biz.show.data.tr.append(td);
        table.append(biz.show.data.tr);
    }
};