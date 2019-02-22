$.namespace("biz");

$(function() {
	biz.query.form.init();
	biz.query.init();
	biz.table.init();
});
biz.query = {
	// 初始化查询
	init : function() {
		biz.query.$queryForm = $('#biz-query-form');
		$("#queryBtn").click(biz.query.queryClick);
		$('#bizType').change(biz.query.loadProcessStatus);
	},

	loadProcessStatus : function() {

		$.ajax({
			url : path + '/biz/process/status',
			type : 'post',
			async : false,
			dataType : 'json',
			traditional : true,
			data : {
				processName : $('#bizType').val()
			},
			success : function(data) {
				if (data) {
					var select = $('#status');
					select.empty();
					var df = $('<option>')
					df.val('');
					df.text('');
					select.append(df);
					for (var i = 0; i < data.length; i++) {
						var option = $('<option>')
						option.val(data[i]);
						option.text(data[i]);
						select.append(option);
					}
				}
				select.selectpicker('render');
				select.selectpicker('refresh');
			}
		});
	},
	// 执行查询
	queryClick : function() {
		biz.$table.bootstrapTable('refresh');
	},
	// 重置查询条件
	resetClick : function() {
		$('.selectpicker').selectpicker('val', '');
	},

	exportDetail : function() {

		var param = {};
		param.action = action;
		param.taskDefKey = taskDefKey;
		$("#biz-query-form").find("[name]").each(function() {
			var value = $.trim($(this).val());
			if (value != null && value !== "" && value !== 'undefined') {
				param[this.name] = value;
			}
		});

		var temp = document.createElement("form");
		temp.action = path + "/workflow/exportWorkOrder";
		temp.method = "post";
		temp.style.display = "none";
		for (var x in param) {
			var opt = document.createElement("input");
			opt.name = x;
			opt.value = param[x];
			temp.appendChild(opt);
		}
		document.body.appendChild(temp);
		temp.submit();
	},

	removeBizInfo : function() {

		var rows = biz.table.getSelections();
		if (rows.length < 1) {
			bsAlert('提示', "请选择要删除的数据");
			return;
		}
		var ids = [];
		for (var i = 0; i < rows.length; i++) {
			ids[i] = rows[i].id;
		}

		$.confirm({
			title : "提示",
			content : "确定删除流程？",
			confirmButton : "确定",
			icon : "glyphicon glyphicon-question-sign",
			cancelButton : "取消",
			confirm : function() {
				$.ajax({
					url : path + "/workflow/bizInfo/delete",
					type : "post",
					data : {
						ids : ids
					},
					traditional : true,
					success : function(result) {
						if (result.success) {
							bsAlert("提示", result.msg);
							biz.query.queryClick();
						} else {
							bsAlert("提示", result.msg);
						}
					}
				});
			}
		});
	}
};

biz.query.form = {
	init : function() {
		var queryFormList = [ {
			name : "workNum",
			align : 'center',
			text : "工单号",
			type : "text"
		}, {
			name : "title",
			align : 'center',
			text : "工单标题",
			type : "text"
		}, {
			name : "bizType",
			align : 'center',
			text : "工单类型",
			type : "combobox",
			params : {
				data : processList,
				type : "list"
			}
		}, {
			name : "status",
			align : 'center',
			text : "工单状态",
			type : "combobox",
			params : {
				data : statusList,
				type : "list"
			}
		}, {
			name : "createTime",
			align : 'center',
			text : "创建时间",
			type : "createTime"
		}, {
			name : "createUser",
			align : 'center',
			text : "创建人",
			type : "text"
		}, {
			name : "taskAssignee",
			align : 'center',
			text : "当前处理人",
			type : "text"
		} ];
		if (!action) {
			queryFormList[5].type = "hidden";
		}

		if (action === 'myTemp') {
			queryFormList[3].type = "hidden";
			queryFormList[5].type = "hidden";
			queryFormList[6].type = "hidden";
		}
		if (action === 'myWork') {
			queryFormList[5].type = "hidden";
		}
		if (action === 'myCreate') {
			queryFormList[5].type = "hidden";
			queryFormList[6].type = "hidden";
		}
		if (action === 'myHandle') {
			queryFormList[5].type = "hidden";
		}
		biz.query.form.createForm(queryFormList);
	},
	createForm : function(list) {
		var length = 0;
		for (var i = 0; i < list.length; i++) {
			if (list[i].type === "createTime") {
				length = length + 2;
			} else if (list[i].type === "hidden") {
				length++;
			} else {
				length++;
			}
		}
		if (length <= 3) {
			biz.query.form.row = $("<div class='col-md-11'>");
			$("#queryBtn").parent().before(this.row);
		} else {
			var row = $("<div class='row'>");
			biz.query.form.row = $("<div class='col-md-11'>");
			row.append(this.row);
			row.append("<div class='col-md-1'><a data-toggle='collapse' data-parent='#accordion' href='#more-condition' style='line-height:26px;'>"
				+ "更多<i class='mrl5 icon-double-angle-down'></i></a></div>");
			$("#queryBtn").parent().before(row);
		}
		this.loadForm(list);
	},
	loadForm : function(list) {
		for (var i = 0; i < list.length; i++) {
			switch (list[i].type) {
			case "text":
				this.addTextField(list[i], biz.query.form.row);
				break;
			case "hidden":
				this.addHidden(list[i], biz.query.form.row);
				break;
			case "combobox":
				this.addCombobox(list[i], biz.query.form.row);
				break;
			case "createTime":
				this.addCreateTime(list[i], biz.query.form.row);
				break;
			default:
				this.addTextField(list[i], biz.query.form.row);
			}
		}
	},
	addTextField : function(data, row) {
		if (row) {
			biz.query.form.row = row;
		}
		var col = $("<div class='col-md-4 form-group form-element' >");
		var lable = "<label for='" + data.name + "' class='col-md-4 control-label form-element'>" + data.text + "：</label>";
		var div = $("<div class='col-md-8 form-element'>");
		var input = $("<input type='text' class='form-control' id='" + data.name + "' name='" + data.name + "' placeholder='" + data.text + "'>");
		div.append(input);
		col.append(lable);
		col.append(div);
		this.row.append(col);
		if (this.row.children(".col-md-4").length === 3) {
			if (!this.collapse) {
				this.collapse = $("<div class='col-md-11 panel-collapse collapse' id='more-condition'>");
				$("#queryBtn").parent().before(this.collapse);
			}
			this.row = $("<div class='row'>");
			this.collapse.append(this.row);
		}
	},
	addCreateTime : function(data, row) {
		if (row) {
			biz.query.form.row = row;
		}
		if (this.row.children(".col-md-4").length >= 2) {
			if (!this.collapse) {
				this.collapse = $("<div class='col-md-11 panel-collapse collapse' id='more-condition'>");
				$("#queryBtn").parent().before(this.collapse);
			}
			this.row = $("<div class='row'>");
			this.collapse.append(this.row);
		}
		var col = $("<div class='col-md-4 form-group form-element'>");
		var lable = "<label for='" + data.name + "' class='col-md-4 control-label form-element'>" + data.text + "：</label>";
		var div = $("<div class='col-md-8 form-element'>");
		var input = $("<input type='text' class='form-control' id='" + data.name + "' name='" + data.name + "' readonly='readonly' >");
		input.attr("onFocus", "WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})");
		div.append(input);
		col.append(lable);
		col.append(div);
		this.row.append(col);
		col = $("<div class='col-md-4 form-group form-element'>");
		lable = "<label for='" + data.name + "2' class='col-md-1 control-label form-element' style='text-align:center'>至</label>";
		div = $("<div class='col-md-8 form-element'>");
		input = $("<input type='text' class='form-control' id='" + data.name + "2' name='" + data.name + "2' readonly='readonly' >");
		input.attr("onFocus", "WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})");
		div.append(input);
		col.append(lable);
		col.append(div);
		this.row.append(col);
		if (this.row.children(".col-md-4").length === 3) {
			if (!this.collapse) {
				this.collapse = $("<div class='col-md-11 panel-collapse collapse' id='more-condition'>");
				$("#queryBtn").parent().before(this.collapse);
			}
			this.row = $("<div class='row'>");
			this.collapse.append(this.row);
		}
	},
	addCombobox : function(data, row) {
		if (row) {
			biz.query.form.row = row;
		}
		var col = $("<div class='col-md-4 form-group form-element'>");
		var lable = "<label for='" + data.name + "' class='col-md-4 control-label form-element'>" + data.text + "：</label>";
		var div = $("<div class='col-md-8 form-element'>");
		var select = $("<select type='text' data-width='100%' class='form-control selectpicker'  data-live-search='true'id='" + data.name + "' name='" + data.name + "' placeholder='" + data.text + "'>");
		div.append(select);
		select.append("<option value=''>请选择</option>");
		col.append(lable);
		col.append(div);
		this.row.append(col);
		if (this.row.children(".col-md-4").length === 3) {
			if (!this.collapse) {
				this.collapse = $("<div class='col-md-11 panel-collapse collapse' id='more-condition'>");
				$("#queryBtn").parent().before(this.collapse);
			}
			this.row = $("<div class='row'>");
			this.collapse.append(this.row);
		}
		this.loadCombobox(select, data.params.data, data.params.type, data.params.key, data.params.value);
	},
	loadCombobox : function(select, params, type, key, value) {

		for (var k in params) {
			var option = $("<option>");
			if (type === "list" || !type) {
				option.val(params[k]);
				option.text(params[k]);
			} else if (type === "map") {
				if (key === "value") {
					option.val(params[k]);
				} else {
					option.val(k);
				}
				if (value === "key") {
					option.text(k);
				} else {
					option.text(params[k]);
				}
			} else {
				if (key|| !key) {
					for (var i in params[k]) {
						option.val(i);
						option.text(params[k][i]);
					}
				} else if (key === "value") {
					for (var i in params[k]) {
						option.val(params[k][i]);
						option.text(params[k][i]);
					}
				} else {
					option.val(params[k][key]);
					option.text(params[k][value]);
				}
			}
			select.append(option);
		}
		$('.selectpicker').selectpicker('render');
		$('.selectpicker').selectpicker('refresh');
	},
	addHidden : function(data) {
		var hidden = "<input type='hidden' name='" + data.name + "' value='" + data.value + "'/>";
		$("#biz-query-form").append(hidden);
	}
};
biz.table = {
	init : function() {
		var single = false;
		var clickSelect = false;
		var columns =[];
		columns.push({
			field : "workNum",
			title : "工单号",
			'class' : "data-resize",
			sortable : true,
			align : "center",
			formatter : function(value, row) {
				var url = path + "/biz/" + row.id;
				if (row.status === "草稿"){
					url = path + "/biz/create/" + row.processDefinitionId.split(":")[0] + "?bizId=" + row.id;
				}
				return "<a style='cursor: pointer' onclick=\"window.open('" + url + "');\">" + value + "</a>";
			}
		}, {
			field : "bizType",
			title : "工单类型",
			'class' : "data-resize",
			sortable : true,
			align : "center"
		}, {
			field : "title",
			title : "工单标题",
			align : "center",
			'class' : "data-resize",
			sortable : true,
			formatter : function(value, row) {
				if (value && value.length > 13) {
					return "<i title='" + value + "'>" + value.substring(0, 10) + "...</i>";
				} else {
					return value;
				}
			}
		}, {
			field : "createUser",
			title : "创建人",
			'class' : "data-resize",
			sortable : true,
			align : "center"
		}, {
			field : "createTime",
			title : "创建时间",
			'class' : "data-resize",
			sortable : true,
			align : "center"
		}, {
			field : "status",
			title : "工单状态",
			'class' : "data-resize",
			sortable : true,
			align : "center"
		}, {
			field : "taskAssignee",
			title : "当前处理人",
			'class' : "data-resize",
			sortable : true,
			align : "center"
		});
		if (action === 'myCreate') {
			columns.splice(0, 0, {
				field : "state",
				checkbox : true,
				align : "center"
			});
		}

		biz.$table = $("#biz-table");
		biz.$table.bootstrapTable({
			method : 'post',
			contentType : "application/x-www-form-urlencoded",
			queryParams : function queryParams(param) {
				param.action = action;
				$("#biz-query-form").find("[name]").each(function() {
					var value = $.trim($(this).val());
					if (value != null && value !== "" && value !== 'undefined') {
						param[this.name] = value;
					}
				});
				return param;
			},
			sidePagination : 'server',
			url : path + "/workflow/queryWorkOrder",
			pagination : true,
			singleSelect : single,
			clickToSelect : clickSelect,
			pageSize : 20,
			pageList : [ 10, 20, 50 ],
			columns : columns
		});
	},
	refresh : function() {
		biz.$table.bootstrapTable('refresh');
	},
	getSelections : function() {
		return biz.$table.bootstrapTable("getSelections");
	},
	exportDetail : function() {

		var param = {};
		$("#biz-query-form").find("[name]").each(function() {
			var value = $.trim($(this).val());
			if (value != null && value !== "" && value !== 'undefined') {
				param[this.name] = value;
			}
		});
		var temp = document.createElement("form");
		temp.action = path + "/biz/exportSupervisionBiz";
		temp.method = "post";
		temp.style.display = "none";
		for (var x in param) {
			var opt = document.createElement("input");
			opt.name = x;
			opt.value = param[x];
			temp.appendChild(opt);
		}
		document.body.appendChild(temp);
		temp.submit();
	}
};

function post(url, params) {
	var temp = document.createElement("form");
	temp.action = url;
	temp.method = "post";
	temp.style.display = "none";
	for (var x in params) {
		var opt = document.createElement("input");
		opt.name = x;
		opt.value = params[x];
		temp.appendChild(opt);
	}
	document.body.appendChild(temp);
	temp.submit();
	return temp;
}