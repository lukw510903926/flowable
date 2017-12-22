$.namespace("process.terminal");

$(function() {
	process.terminal.loadGrid();
//    process.terminal.loadCombobox(path+"/workflow/dictComboBoxList",$("[name='city']"),true,{dictName:"地市公司"},"NAME");
    process.terminal.flowCombobox($("[name='bizType']"));
    process.terminal.loadSystem();
});

process.terminal = {
		
	loadSystem:function(){
		var select = $("[name='systemName']");
	    $.ajax({
	       url:path+"/workflow/comboBoxList",
	       type:"post",
	       data:{selectName:"业务系统"},
		   success : function(data) {
				if (data) {
					var option = $("<option>");
					option.text("请选择");
					option.val("");
					select.append(option);
					for (var i = 0; i < data.length; i++) {
						var option = $("<option>");
						option.text(data[i]);
						option.val(data[i]);
						select.append(option);
					}
					$('[name="systemName"]').selectpicker('render');
		       		$('[name="systemName"]').selectpicker('refresh');
				}
			}
	    });
	},
    
    loadGrid:function(){
        $("#serviceUser-table").bootstrapTable({
            method : 'post',
            contentType:"application/x-www-form-urlencoded",
            sidePagination : 'server',
            url : path + "/actBizConf/findBizSystemUserConf",
            pagination : true,
            pageSize : 10,
            queryParams : function(params){
                $('#queryGridForm').find('[name]').each(function() {
                    if($(this).val()!=""&&($.trim($(this).val()))!="")
                        params[this.name] = $.trim($(this).val());
                });
                return params;
            },
            pageList : [ 10, 20, 50 ],
            clickToSelect:true,
            columns : [ {
                field : "state",
                checkbox : true,
                align : "center"
            }, {
                field : "SYSTEMNAME",
                title : "系统名称",
                align : "left"
            }, {
                field : "FULLNAME",
                title : "姓名",
                align : "left"
            }, {
                field : "USERNAME",
                title : "英文名",
                align : "left"
            }, {
                field : "USERTYPE",
                title : "类型",
                align : "left",
                formatter : function(value){
                    switch(value){
                        case 'director':
                            return "主管";
                        case 'manager':
                            return "经理";
                        case 'agleamOf':
                        	return "一线";
                        case 'secondLine':
                        	return "二线";
                        case 'threeWwire':
                        	return "三线";
                        case 'needInterfaceMan':
                        	return '需求接口人'
                        case 'projectLeader':
                        	return '项目负责人'
                        default:
                            return value;
                    }
                }
            }, {
                field : "CREATETIME",
                title : "添加时间",
                align : "left"
            }, {
                field : "BIZTYPE",
                title : "所属流程",
                align : "left"
            }],
            onLoadSuccess:function(data){
                process.terminal.gridData = data; 
            }
        });
    },
    
    resetForm : function(formId){
    	
    	$('#'+formId).find('[name]').each(function(){
    		$(this).val('');
    	});
    	$('#'+formId).find("[name='systemName']").selectpicker('refresh');;
    	$('#'+formId).find("[name='bizType']").selectpicker('refresh');;
    },
    userInfoGrid:function(params){
        $("#userTable").bootstrapTable({
            method : 'post',
            contentType:"application/x-www-form-urlencoded",
            sidePagination : 'server',
            url : path + "/bizHandle/loadMembers",
            queryParams : function(params){
                $("#queryUserForm").find('[name]').each(function(){
                    if($(this).val()!=""&&($.trim($(this).val()))!="")
                        params[this.name]=encodeURI($.trim($(this).val()));
                });
                return params;
            },
            pagination : true,
            pageSize : 10,
            pageList : [ 10, 20, 50 ],
            clickToSelect:true,
            columns : [ {
                field : "state",
                checkbox : true,
                align : "center"
            },{
                field : "fullname",
                title : "姓名",
                align : "left"
            }, {
                field : "mobile",
                title : "电话",
                align : "left"
            }, {
                field : "structure_name",
                title : "所在部门",
                align : "left"
            }],
            onLoadSuccess:function(data){
                process.terminal.gridData = data; 
            }
        });
    },
    hideMenu:function() {
        $("#treeDiv").fadeOut("fast");
        $("body").unbind("mousedown", process.terminal.onBodyDown);
    },
    clickAdd:function(){
        process.terminal.userInfoGrid();
        process.terminal.deptTree.loadSectorBox();
        process.terminal.deptTree.sectorInit();
    },
    clickQuery:function(){
        
        var params = {};
        $('#queryGridForm').find('[name]').each(function() {
            if($(this).val()!=""&&$.trim($(this).val())!=""){
                 params[this.name] = $.trim($(this).val());
            }
        });
        params.nameType = "ch";
        $("#serviceUser-table").bootstrapTable('refresh', {query:params});
    },
    queryUser : function(){
        $("#userTable").bootstrapTable('destroy');
        process.terminal.userInfoGrid();
    },
    saveUser : function(){
       
        var userType = $.trim($("#saveUserForm [name='userType']").val());
        if(userType==''){
            bsAlert("提示","请选择用户类型");
            return;
        }
        var systemName = $("#saveUserForm [name='systemName']").val();
        if(systemName==''){
            bsAlert("提示","请选择系统");
            return;
        }
        var bizType = $.trim($("#saveUserForm [name='bizType']").val());
        if(bizType==''){
            bsAlert("提示","请选择所属流程");
            return;
        }
        var rows = $("#userTable").bootstrapTable("getSelections");
        if(rows.length<1){
            bsAlert("提示","请选择要保存的人员");
            return;
        }
        var list = [];
        for(var i =0 ;i<rows.length; i++){
            list.push(rows[i].username);
        }
        $.ajax({
            url : path + "/actBizConf/saveBizSystemUserConf",
            type : 'post',
            dataType : 'json',
            data : {
               list : list,
               userType : userType,
               systemName : systemName,
               bizType:bizType
            },
            traditional : true,
            success:function(result) {
                if (result.success) {
                    $('#serviceUser-table').bootstrapTable("refresh");
                    $('#attachmentModal').modal('hide');
                } else {
                    bsAlert("提示", result.msg);
                }
            }
        });
    },
//    loadCombobox:function(url,ele,empty,data,node,root){
//        $.ajax({
//           url:url,
//           type:"post",
//           data:data,
//		   success : function(data) {
//				if (data) {
//					var option = $("<option>");
//					option.text("请选择");
//					option.val("");
//					ele.append(option);
//					process.terminal.systemCombobox(ele, data.rows[0].ID);
//				}
//			}
//        });
//    },
//    //系统名称下拉查询
//    systemCombobox:function(ele,dictId){
//    	  $.ajax({
//              url:path+"/dict/findDictData",
//              type:"post",
//              data:{"dictId":dictId},
//              success:function(data){
//            	  if(data!=""){
//            		  for(var i=0;i<data.rows.length;i++){
//            			  var option = $("<option>");
//                          var op = data.rows[i].NAME;
//                          option.text(op);
//                          option.val(op);
//                          ele.append(option);
//            		  }
//            	  }
//              	$('[name="systemName"]').selectpicker('render');
//            	$('[name="systemName"]').selectpicker('refresh');
//              }
//    	  });
//    },
    
    //流程下拉查询
    flowCombobox:function(ele){
    	 $.ajax({
             url:path+"/actBizConf/findProcess",
             type:"post",
             data:{},
             success:function(data){
           	  if(data!=""){
           		var option = $("<option>");
    		  	  option.text("请选择");
    		  	  option.val("");
    		  	  ele.append(option);
           		  for(var key in data){
           			  var option = $("<option>");
                         var op = data[key];
                         option.text(op);
                         option.val(op);
                         ele.append(option);
           		  }
           	  }
          	$('[name="bizType"]').selectpicker('render');
        	$('[name="bizType"]').selectpicker('refresh');
             }
   	  });
    },
    clickDelete:function(){
        var rows = $("#serviceUser-table").bootstrapTable("getSelections");
        if(rows.length<1){
            bsAlert("提示","请选择删除行！");
            return;
        }
        var list = [];
        for(var i=0;i<rows.length;i++){
            list.push(rows[i].ID);
        }
        $.confirm({
            title:"提示",
            content:"确认删除选中行吗？",
            confirmButton: "确认",
            cancelButton: "取消",
            confirm:function() {
                $.ajax({
                   url:path+"/actBizConf/deleteChangeConf",
                   type:"post",
                   data:{list:list},
                   traditional:true,
                   success:function(result){
                       if(result.success){
                           bsAlert("提示",result.msg);
                           $("#serviceUser-table").bootstrapTable("refresh");
                       }else{
                           bsAlert("提示",result.msg);
                       }
                   }
                });
        }});
    }
};

process.terminal.deptTree = {
    
    setting:{
            view: {
                dblClickExpand: false
            },
            data: {
                simpleData: {
                    enable: true
                }
            },
            callback: {
                beforeClick: function(treeId, treeNode) {
                    var check = (treeNode && !treeNode.isParent);
                    },
                onClick: function(e, treeId, treeNode){
                    var zTree = $.fn.zTree.getZTreeObj("sectorTree");
                    nodes = zTree.getSelectedNodes(),
                    vId="",
                    v = "";
                    nodes.sort(function compare(a,b){return a.id-b.id;});
                    for (var i=0, l=nodes.length; i<l; i++) {
                        v += nodes[i].name + ",";
                        vId+=nodes[i].id+",";
                    }
                    if (v.length > 0 ) v = v.substring(0, v.length-1);
                    if (vId.length > 0 ) vId = vId.substring(0, vId.length-1);
                    var cityObj = $("#sectorCombo"),cityValue = $('#sectorComboVal');
                    if(vId==""){
                        cityObj.val("");
                        cityValue.val("");
                    }else{
                        cityObj.val(v);   
                        cityValue.val(vId);    
                    } 
                    process.terminal.deptTree.hideMenu();
                }
            }
        },
        zNodes:null,
        showMenu:function() {
            var cityObj = $("#sectorComboVal");
            var cityOffset = $("#sectorCombo").position();
            $("#sectorMenuContent").css({left:cityOffset.left + "px", top:cityOffset.top + cityObj.outerHeight()+ 30 + "px"}).slideDown("fast");
            $("body").bind("mousedown", process.terminal.deptTree.onBodyDown);
        },
        hideMenu:function() {
            $("#sectorMenuContent").fadeOut("fast");
            $("body").unbind("mousedown", process.terminal.deptTree.onBodyDown);
        },
        onBodyDown:function(event) {
            if (!(event.target.id == "sectorCombo" || event.target.id == "sectorMenuContent" || $(event.target).parents("#sectorMenuContent").length>0)) {
                process.terminal.deptTree.hideMenu();
            }
        },
        sectorInit:function(){
            $.fn.zTree.init($("#sectorTree"), process.terminal.deptTree.setting, process.terminal.deptTree.zNodes);
            //初始化选择第一个
            var zTree = $.fn.zTree.getZTreeObj("sectorTree");
            zTree.addNodes(null,0,{
                id : "",
                name : "选空",
                nocheck : true
            });
            var nodes = zTree.getNodes();    
            zTree.selectNode(nodes[0]); 
            zTree.setting.callback.onClick(null, zTree.setting.treeId, nodes[0]);
        },
        loadSectorBox:function(){
            $.ajax({
                type:"post",
                url:path+"/bizHandle/loadSectors",
                async:false,
                success:function(data){
                    if(data!=null && data.success && data.obj != null){
                        process.terminal.deptTree.zNodes = data.obj;
                    }else{
                    }
                }
            });
       }
};

function post(url, params,userType) {
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
    var type = document.createElement("input");
    type.name = 'userType';
    type.value = userType;
    temp.appendChild(type);
    document.body.appendChild(temp);
    temp.submit();
    return temp;
}