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
		$("#pressBiz").click(biz.query.terminalPressBiz);
		$('#bizType').change(biz.query.loadProcessStatus);
	},
	
	loadProcessStatus : function(){
		
		$.ajax({
			url : path +'/biz/getProcessStatus',
			type : 'post',
			async: false, 
			dataType: 'json',
			data : {
				processName : $('#bizType').val()
			},
			success : function(data){
				if(data){
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
		$("#biz-table").data('resizableColumns',null);
		$(".rc-handle-container").remove();
		biz.$table.bootstrapTable('refresh');
	},
	// 重置查询条件
	resetClick : function() {
		$('.selectpicker').selectpicker('val','');
	},
	
	exportDetail : function() {

		var param = {};
		param.status = status;
		param.taskDefKey = taskDefKey;
		param.taskAssignee = taskAssignee;
		param.action = action;
		$("#biz-query-form").find("[name]").each(function() {
			if ($(this).val() != null && ($.trim($(this).val())) != "") {
				if ($(this).val() != 'undefined')
					param[this.name] = $.trim($(this).val());
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

	removeBizInfo : function(){
	    
        var rows = biz.table.getSelections();
        if (rows.length < 1) {
            bsAlert('提示',"请选择要删除的数据");
            return;
        }
        var ids = new Array();
        for (var i = 0; i < rows.length; i++) {
            ids[i] = rows[i].id;
        }
        
        $.confirm({
          title : "提示",
          content : "确定删除流程？",
          confirmButton : "确定",
          icon : "glyphicon glyphicon-question-sign",
          cancelButton : "取消",
          confirm:function() {
             $.ajax({
               url : path + "/workflow/bizInfo/delete",
               type:"post",
               data:{ids:ids},
               traditional:true,
               success:function(result){
                   if(result.success){
                       bsAlert("提示",result.msg);
                       biz.query.queryClick();
                   }else{
                       bsAlert("提示",result.msg);
                   }
               }
            });
        }
    });
},
};

biz.query.form = {
	
	init : function() {
		var queryFormList = [ {
			name : "bizId",
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
			name : "systemName",
			align : 'center',
			text : "所属系统",
			type : "combobox",
			params : {
				data : biz.query.form.systemList,
				type : "list"
			}
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
		
		if (action == 'myTemp') {
			queryFormList[3].type = "hidden";
			queryFormList[5].type = "hidden";
			queryFormList[6].type = "hidden";
			queryFormList[7].type = "hidden";
		}
		if (action == 'leftBiz') {
			var temp = queryFormList[2];
			queryFormList[2] = queryFormList[6];
			queryFormList[6] = temp;
			queryFormList[3].type = "hidden";
			queryFormList[5].type = "hidden";
			queryFormList[6].type = "hidden";
			queryFormList[7].type = "hidden";
		}
		if (action == 'myWork' ) {
			queryFormList[5].type = "hidden";
			queryFormList[7].type = "hidden";
		}
		if (action == 'myCreate') {
			queryFormList[5].type = "hidden";
			queryFormList[6].type = "hidden";
		}
		if (action == 'myHandle') {
			queryFormList[5].type = "hidden";
		}
		biz.query.form.createForm(queryFormList);
	},
    createForm:function(list){
        var length = 0;
        for(var i=0;i<list.length;i++){
            if(list[i].type=="createTime")
                length = length+2;
            else if(list[i].type=="hidden")
                continue;
            else
                length++;
        }
        if(length<=3){
            biz.query.form.row = $("<div class='col-md-11'>");
            $("#queryBtn").parent().before(this.row);
        }else{
            var row = $("<div class='row'>");
            biz.query.form.row = $("<div class='col-md-11'>");
            row.append(this.row);
            row.append("<div class='col-md-1'><a data-toggle='collapse' data-parent='#accordion' href='#more-condition' style='line-height:26px;'>"
            +"更多<i class='mrl5 icon-double-angle-down'></i></a></div>");
            $("#queryBtn").parent().before(row);
        }
        this.loadForm(list);
    },
    loadForm:function(list){
        for(var i=0;i<list.length;i++){
            switch(list[i].type){
                case "text":
                    this.addTextField(list[i],biz.query.form.row);break;
                case "hidden":
                    this.addHidden(list[i],biz.query.form.row);break;
                case "combobox":
                    this.addCombobox(list[i],biz.query.form.row);break;
                case "createTime":
                    this.addCreateTime(list[i],biz.query.form.row);break;
                default:
                    this.addTextField(list[i],biz.query.form.row);
            }
        }
    },
    addTextField:function(data,row){
        if(row)
            biz.query.form.row = row;
        var col = $("<div class='col-md-4 form-group form-element' >");
        var lable = "<label for='"+data.name+"' class='col-md-4 control-label form-element'>"+data.text+"：</label>";
        var div = $("<div class='col-md-8 form-element'>");
        var input = $("<input type='text' class='form-control' id='"+data.name+"' name='"+data.name+"' placeholder='"+data.text+"'>");
        div.append(input);
        col.append(lable);
        col.append(div);
        this.row.append(col);
        if(this.row.children(".col-md-4").length==3){
            if(!this.collapse){
                this.collapse = $("<div class='col-md-11 panel-collapse collapse' id='more-condition'>");
                $("#queryBtn").parent().before(this.collapse);
            }
            this.row = $("<div class='row'>");
            this.collapse.append(this.row);
        }
    },
    addCreateTime:function(data,row){
        if(row)
            biz.query.form.row = row;
        if(this.row.children(".col-md-4").length>=2){
           if(!this.collapse){
                this.collapse = $("<div class='col-md-11 panel-collapse collapse' id='more-condition'>");
                $("#queryBtn").parent().before(this.collapse);
            }
            this.row = $("<div class='row'>");
            this.collapse.append(this.row);
        }
        var col = $("<div class='col-md-4 form-group form-element'>");
        var lable = "<label for='"+data.name+"' class='col-md-4 control-label form-element'>"+data.text+"：</label>";
        var div = $("<div class='col-md-8 form-element'>");
        var input = $("<input type='text' class='form-control' id='"+data.name+"' name='"+data.name+"' readonly='readonly' >");
        input.attr("onFocus","WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})");
        div.append(input);
        col.append(lable);
        col.append(div);
        this.row.append(col);
        col = $("<div class='col-md-4 form-group form-element'>");
        lable = "<label for='"+data.name+"2' class='col-md-1 control-label form-element' style='text-align:center'>至</label>";
        div = $("<div class='col-md-8 form-element'>");
        input = $("<input type='text' class='form-control' id='"+data.name+"2' name='"+data.name+"2' readonly='readonly' >");
        input.attr("onFocus","WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})");
        div.append(input);
        col.append(lable);
        col.append(div);
        this.row.append(col);
        if(this.row.children(".col-md-4").length==3){
            if(!this.collapse){
                this.collapse = $("<div class='col-md-11 panel-collapse collapse' id='more-condition'>");
                $("#queryBtn").parent().before(this.collapse);
            }
            this.row = $("<div class='row'>");
            this.collapse.append(this.row);
        }
    },
    addCombobox:function(data,row){
        if(row)
            biz.query.form.row = row;
        var col = $("<div class='col-md-4 form-group form-element'>");
        var lable = "<label for='"+data.name+"' class='col-md-4 control-label form-element'>"+data.text+"：</label>";
        var div = $("<div class='col-md-8 form-element'>");
        var select = $("<select type='text' data-width='100%' class='form-control selectpicker'  data-live-search='true'id='"+data.name+"' name='"+data.name+"' placeholder='"+data.text+"'>");
        div.append(select);
        select.append("<option value=''>请选择</option>");
        col.append(lable);
        col.append(div);
        this.row.append(col);
        if(this.row.children(".col-md-4").length==3){
            if(!this.collapse){
                this.collapse = $("<div class='col-md-11 panel-collapse collapse' id='more-condition'>");
                $("#queryBtn").parent().before(this.collapse);
            }
            this.row = $("<div class='row'>");
            this.collapse.append(this.row);
        }
        this.loadCombobox(select,data.params.data,data.params.type,data.params.key,data.params.value);
    },
    loadCombobox:function(select,params,type,key,value){
        for(var k in params){
            var option = $("<option>");
            if(type=="list"||type==undefined){
                option.val(params[k]);
                option.text(params[k]);
            }else if(type=="map"){
                if(key=="value")
                    option.val(params[k]);
                else
                    option.val(k);
                if(value=="key")
                    option.text(k);
                else
                    option.text(params[k]);
            }else{
                if(key==true||key==undefined){
                    for(var i in params[k]){
                        option.val(i);
                        option.text(params[k][i]);
                    }
                }else if(key=="value"){
                    for(var i in params[k]){
                        option.val(params[k][i]);
                        option.text(params[k][i]);
                    }
                }else{
                    option.val(params[k][key]);
                    option.text(params[k][value]);
                }
            }
            select.append(option);
        }
        $('.selectpicker').selectpicker('render');
	    $('.selectpicker').selectpicker('refresh');
    },
    addHidden:function(data){
        var hidden = "<input type='hidden' name='"+data.name+"' value='"+data.value+"'/>";
        $("#biz-query-form").append(hidden);
    }
};
biz.table = {
	init : function() {
		var single = false;
		var clickSelect = false;
		var columns = new Array();
		var url = path + "/workflow/queryWorkOrder/";

		columns.push({
            field : "workNum",
            title : "工单号",
            'class' : "data-resize",
            sortable : true,
            align : "center",
            formatter : function(value, row, index) {
                var url = path + "/biz/" + row.id;
                if(row.status=="草稿")
                    url = path + "/biz/create/"+row.processDefinitionId.split(":")[0]+"?bizId="+row.id;
                return "<a onclick=\"window.open('"+url+"');\">"+value+"</a>";
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
            formatter:function(value,row){
                if(value && value.length>13){
                    return "<i title='"+value+"'>"+value.substring(0,10)+"...</i>";
                }else{
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
        },{
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
        if(action=='myCreate'){
            columns.splice(0,0,{field : "state",checkbox : true,align : "center"});
        }
        
		biz.$table = $("#biz-table");
		biz.$table.bootstrapTable({
			method : 'post',
			contentType:"application/x-www-form-urlencoded",
			queryParams : function queryParams(param) {
                    
                    param.status = status;
                    param.taskDefKey = taskDefKey;
                    param.taskAssignee = taskAssignee;
                    param.action = action;
                    $("#biz-query-form").find("[name]").each(function() {
                        if ($(this).val() != null && ($.trim($(this).val()))!="") {
                            if($(this).val()!='undefined')
                                param[this.name] = $.trim($(this).val());
                        }
                    });
                    return param;
            },
			sidePagination : 'server',
			url : url,
			pagination : true,
			singleSelect : single,
			clickToSelect : clickSelect,
			pageSize : 20,
			pageList : [ 10, 20, 50 ],
			columns : columns,
			onLoadSuccess : function(data){
				$("#biz-table").find('tr th').each(function(i, el) {
					if(!$("#biz-table").find('tr th').eq(i).hasClass('data-resize'))
						$("#biz-table").find('tr th').eq(i).attr({'data-noresize':''});
				});
				$("#biz-table").data('resizableColumns',null);
				$(".rc-handle-container").remove();
				$("#biz-table").resizableColumns({});
			}
		});
	},
	refresh : function(param) {
		biz.$table.bootstrapTable('refresh');
		
	},
	getSelections : function() {
		var rows = biz.$table.bootstrapTable("getSelections");
		return rows;
	},
	remindersBiz:function(){
	    var rows = this.getSelections();
	    if(rows.length<1){
	       bsAlert("提示","请选择催办工单!");
	       return;
       }
	    var ids = []; 
	    for(var i=0;i<rows.length;i++){
	        ids.push(rows[i].id);
	    }
	    $.ajax({
	       url:path+"/biz/pressBizInfo",
	       type:"post",
	       data:{bizIds:ids,message:$("#message").val()},
	       async:false,
	       traditional:true,
	       success:function(data){
	           if(data.success){
	               bsAlert("提示",data.msg);
	               biz.$table.bootstrapTable('refresh');
	           }else{
	               bsAlert("提示",data.msg);
	           }
	       }
	    });
	},
	removeLeft:function(){
	    var rows = this.getSelections();
	    if(rows.length<1){
	       bsAlert("提示","请选择要移除的工单!");
	       return;
       }
	    var ids = []; 
	    for(var i=0;i<rows.length;i++){
	        ids.push(rows[i].id);
	    }
		$.confirm({
            title:"提示",
            content:"确认移除选中的工单？",
            confirmButton: "确认",
            cancelButton: "取消",
            confirm:function() {
            	 $.ajax({
          	       url:path+"/biz/removeLeft",
          	       type:"post",
          	       data:{bizIds:ids},
          	       async:false,
          	       traditional:true,
          	       success:function(data){
          	           if(data.success){
          	               bsAlert("提示",data.msg);
          	               biz.$table.bootstrapTable('refresh');
          	           }else{
          	               bsAlert("提示",data.msg);
          	           }
          	       }
          	    });
            }
		});
	    
	},
	exportDetail:function(){
        
        var param = {};
        
        $("#biz-query-form").find("[name]").each(function() {
            if ($(this).val() != null && $.trim($(this).val() != "")) {
                param[this.name] = $.trim($(this).val());
            }
         });
        
        var temp = document.createElement("form");
        temp.action = path+"/biz/exportSupervisionBiz";
        temp.method = "post";
        temp.style.display = "none";
        for (var x in param) {
            var opt = document.createElement("input");
            opt.name = x;
            opt.value = param[x];
            temp.appendChild(opt);
        }
        var description = document.createElement("input");
        description.name = 'description';
        description.value = $("#description")[0].checked;
        temp.appendChild(description);
        var solution = document.createElement("input");
        solution.name = 'solution';
        solution.value = $("#solution")[0].checked;
        temp.appendChild(solution);
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
