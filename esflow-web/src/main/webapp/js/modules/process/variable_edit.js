//$.namespace("processVariableSet");

var handleMobileEmpty = true;
var preUrl = "";
var timeout;
$(function() {
	$('#ud_viewComponent').selectpicker('render');
	$('#ud_viewComponent').selectpicker('refresh');
	preUrl = path + "/process/variable?processDefinitionId="+processId+"&version="+version+"&taskId=" + taskId;
	if(taskId) {
		$("#htitle").html("添加/编辑流程任务参数");
	}
	$('#ud_refParam').parents('div.form-group').hide();
	//联动父节点
	$.ajax({
		type : 'POST',
		async:false,
		url : path + "/processModelMgr/processValList",
		data : {
			id : updateId,
			processId : processId,
			version : version,
			taskId: taskId
			},
		dataType : 'json',
		success : function(data) {
			if (data !=null && data.rows != null) {
				 var html = new Array();
				 html.push('<option value="">请选择</option>'); 
				 for(var i=0;i<data.rows.length;i++){
					 html.push('<option data = "'+data.rows[i].viewComponent+'@'+data.rows[i].viewParams+'" value="'+data.rows[i].id+'">'+data.rows[i].alias+'</option>'); 
					 if(data.rows[i].name=="handleMobile")
					      handleMobileEmpty = false;
				 }
				 $('#ud_refVariable').append(html.join(""));
			}
		},
		error : function(re, status, err) {
			bsAlert("错误", re.responseText, "error");
		}
	});
	if (updateId) {
		$.ajax({
			type : 'POST',
			url : path + "/processModelMgr/getProcessValById",
			data : {
				processId : updateId
			},
			dataType : 'json',
			success : function(data) {
				if (data.success) {
					$('#ud_name').val(data.obj.name);
					$('#ud_alias').val(data.obj.alias);
					$('#ud_nameOrder').val(data.obj.order);
					if (data.obj.required) {
						$('input[name=ud_required]').attr('checked','checked');
					}
					$('#ud_groupName').val(data.obj.groupName);
					$('#ud_groupOrder').val(data.obj.groupOrder);
					$('#ud_variableGroup').val(data.obj.variableGroup);
					$('#ud_componentArgs').val(data.obj.viewDatas);
					$('#ud_viewComponent').selectpicker('val',data.obj.viewComponent);
					getViewParams();
					$('#ud_viewParams').val(data.obj.viewParams);
					$("#ud_refVariable").val(data.obj.refVariable);
					getRefParam(data.obj.refParam);
					if (data.obj.processVariable) {
						$('input[name=ud_processVariable]').attr('checked','checked');
					}
				} else {
					bsAlert("错误", data.msg);
				}
			},
			error : function(re, status, err) {
				bsAlert("错误", re.responseText);
			}
		});
	}

	$("#saveOrUpdateBtn").click(function() {
		if ($.trim($('#ud_name').val()) == "") {
			bsAlert("提示", "属性名称不能为空");
			return;
		}

		var nameOrder = $("#ud_nameOrder").val();
		if (nameOrder != "") {
			if (isNaN(nameOrder)) {
				bsAlert("提示", "字段只能是数字");
				return;
			}
		}
		if($('#ud_viewComponent option:selected').val()=="MEMBERLINKAGE"&&$('#ud_name').val()!="handleUser"){
		    bsAlert("注意", "处理人员属性名称必须为“handleUser”,将强制修改为正确值并保存！",function(){
                $('#ud_name').val("handleUser");
                var data = {
                        id : updateId,
                        processId : processId,
                        version : version,
                        taskId: taskId,
                        name :  $.trim($('#ud_name').val()),
                        alias : $.trim($('#ud_alias').val()),
                        nameOrder : $.trim($('#ud_nameOrder').val()),
                        required : $('input[name="ud_required"]:checked').val()=="on",
                        groupName : $.trim($('#ud_groupName').val()),
                        groupOrder : $("#ud_groupOrder").val(),
                        variableGroup : $('#ud_variableGroup').val(),
                        viewComponent : $.trim($('#ud_viewComponent option:selected').val()),
                        viewDatas : $.trim($("#ud_componentArgs").val()),
                        viewParams : $.trim($("#ud_viewParams").val()),
                        isprocVal : $('input[name="ud_processVariable"]:checked').val()=="on",
                        refVariable:$("#ud_refVariable").val(),
                        refParam:$("#ud_refParam").val()
                };
               saveAjax(data);
	        });
		}else{
    		var data = {
                    id : updateId,
                    processId : processId,
                    version : version,
                    taskId: taskId,
                    name : $.trim($('#ud_name').val()),
                    alias : $.trim($('#ud_alias').val()),
                    nameOrder : $.trim($('#ud_nameOrder').val()),
                    required : $('input[name="ud_required"]:checked').val()=="on",
                    groupName : $.trim($('#ud_groupName').val()),
                    groupOrder : $.trim($("#ud_groupOrder").val()),
                    variableGroup : $.trim($('#ud_variableGroup').val()),
                    viewComponent : $('#ud_viewComponent option:selected').val(),
                    viewDatas : $.trim($("#ud_componentArgs").val()),
                    viewParams : $.trim($("#ud_viewParams").val()),
                    isprocVal : $('input[name="ud_processVariable"]:checked').val()=="on",
                    refVariable:$.trim($("#ud_refVariable").val()),
                    refParam : $.trim($("#ud_refParam").val())
            };
    		saveAjax(data);
        }
	});
		
	$("#cancleBtn").click(function() {
		window.open(preUrl,"_self");
	});
});
function getViewParams(){
    
    var combobox = $('#ud_viewComponent').val();
    if(combobox=='DICTCOMBOBOX'){
        $('[for="ud_viewParams"]').parent().show();
        $('[for="ud_viewParams"]').text('数据字典：');
        $.ajax({
            type : 'POST',
            async:false,
            url : path + "/dict/findDictConfig",
            data:{status:0},
            dataType:'json',
            success : function(result){
                $('#ud_viewParams').empty();
                for(var i =0 ;i<result.rows.length; i++){
                    var option = $('<option>');
                    option.val(result.rows[i].DICTNAME);
                    option.text(result.rows[i].DICTNAME);
                    $('#ud_viewParams').append(option);
                }
            }
        });
    }else if(combobox=='MCMCOMBOBOX'){
        $('[for="ud_viewParams"]').parent().show();
        $('[for="ud_viewParams"]').text('配置项：');
        onclick='biz.edit.form.memberList.showMenu();';
        $.ajax({
            type : 'get',
            async:false,
            url : path + "/workflow/modelconfigList",
            dataType:'json',
            success : function(result){
                $('#ud_viewParams').empty();
                for(var i =0 ;i<result.list.length; i++){
                    var option = $('<option>');
                    option.val(result.list[i].name);
                    option.text(result.list[i].name);
                    $('#ud_viewParams').append(option);
                }
            }
        });
    }else if(combobox=='REQUIREDFILE'){
    	$('#helpBlock').html('文件名包含文件扩展名,如 xxx.doc,无需模版下载可不配置');
    	$('#ud_componentArgsLabel').html('模版名称: &nbsp');
    }else if(combobox=='ITCONFIGUSER'){
        $('[for="ud_viewParams"]').parent().show();
        $('[for="ud_viewParams"]').text('审批人员：');
        var list = ['预审人','审批人','配置人'];
        $('#ud_viewParams').empty();
        for(var i =0 ;i<list.length; i++){
            var option = $('<option>');
            option.val(list[i]);
            option.text(list[i]);
            $('#ud_viewParams').append(option);
        }
       
    }else if(combobox=='TERMINALUSER'){
    	$('#ud_viewParams').empty();
        $('[for="ud_viewParams"]').parent().show();
        $('[for="ud_viewParams"]').text('人员角色：');
        
        var director = $('<option>');
        director.val('director');
        director.text('主管');
        $('#ud_viewParams').append(director);
        var director = $('<option>');
        director.val('manager');
        director.text('经理');
        $('#ud_viewParams').append(director); 
        var director = $('<option>');
        director.val('agleamOf');
        director.text('一线');
        $('#ud_viewParams').append(director);
        var director = $('<option>');
        director.val('secondLine');
        director.text('二线');
        $('#ud_viewParams').append(director);
        var director = $('<option>');
        director.val('threeWwire');
        director.text('三线');
        var director = $('<option>');
        director.val('projectLeader');
        director.text('项目负责人');
        $('#ud_viewParams').append(director);

    }else if(combobox=="MEMBERLIST"||combobox=="MEMBERSEELCT"){
        $('[for="ud_viewParams"]').parent().show();
        $('[for="ud_viewParams"]').text('角色：');
        $.ajax({
            type : 'get',
            async:false,
            url : path + "/workflow/roleList",
            dataType:'json',
            success : function(result){
                $('#ud_viewParams').empty();
                for(var i =0 ;i<result.length; i++){
                    var option = $('<option>');
                    option.val(result[i].ROLECNNAME);
                    option.text(result[i].ROLECNNAME);
                    $('#ud_viewParams').append(option);
                }
            }
        });
    }else if(combobox=="BUTTON"){
    	var viewDiv = $('#ud_viewParams').parent();
    	$('#ud_viewParams').remove();
        var viewParams = $('[for="ud_viewParams"]').parent();
        viewParams.show();
        var input = $('<input type="text" class="form-control" name="viewParams" id="ud_viewParams"/>');
        viewDiv.append(input);
//        $('#ud_componentArgsLabel').html('按钮名称: &nbsp');
        $('[for="ud_viewParams"]').html('按钮名称: &nbsp');
    }else{
        $('[for="ud_viewParams"]').parent().hide();
    }    
}

/**
 * 获取对应联动属性
 */
function getRefParam(name){
	$('#ud_refParam').empty();
	var id = $('#ud_refVariable').val();
	var data = $('#ud_refVariable option[value="'+id+'"]').attr('data');
	var combobox = null;
	if(data && data.split('@')[0]=='MCMCOMBOBOX'){
		$('#ud_refParam').parents('div.form-group').show();
		combobox = data.split('@')[1];
	}else{
		$('#ud_refParam').parents('div.form-group').hide();
		return false;
	}
		$.ajax({
            type : 'POST',
            url : path + "/workflow/getPropertyByName",
            data:{name:combobox},
            dataType:'json',
            async:false,
            success : function(result){
                if(result){
                	var html = new Array();
	   				for(var i=0;i<result.length;i++){
	   					 html.push('<option value="'+result[i].nameTable+'">'+result[i].name+'</option>'); 
	   				}
	   				$('#ud_refParam').append(html.join(""));
                    if(name){
                    	$("#ud_refParam").val(name);
                    }                  
                }
            }
        });
}

function saveAjax(params){
    $.ajax({
            type : 'POST',
            url : path + "/processModelMgr/saveOrUpdateProcessVal",
            data : params,
            dataType : 'json',
            success : function(data) {
                if (data.success) {
                    if(params.viewComponent=="MEMBERLINKAGE"&&handleMobileEmpty){
                         $.confirm({
                            title:"提示",
                            content:"是否自动添加联系方式参数？",
                            confirmButton: "确认",
                            cancelButton: "取消",
                            confirm:function() {
                                var refVariable = "";
                                $.ajax({
                                    type : 'POST',
                                    async:false,
                                    url : path + "/processModelMgr/processValList",
                                    data : {
                                        id : updateId,
                                        processId : processId,
                                        version : version,
                                        taskId: taskId
                                        },
                                    dataType : 'json',
                                    success : function(result) {
                                        if (result !=null && result.rows != null) {
                                             for(var i=0;i<result.rows.length;i++){
                                                 if(result.rows[i].alias==params.alias){
                                                     refVariable = result.rows[i].id;
                                                     break;
                                                 }
                                             }
                                        }
                                    }
                                });
                                var params2 = {
                                    id:"",
                                    processId : processId,
                                    version : version,
                                    taskId: taskId,
                                    name : "handleMobile",
                                    alias : params.alias+"联系方式",
                                    nameOrder : parseInt(params.nameOrder)+1,
                                    required : false,
                                    viewComponent : "TEXT",
                                    groupName : params.groupName,
                                    groupOrder : params.groupOrder,
                                    variableGroup : params.variableGroup,
                                    refVariable:refVariable,
                                };
                                saveAjax(params2);
                            }
                        });
                    }else{
                        bsAlert("提示", data.msg);
                        setTimeout(function() {
                            window.open(preUrl,"_self");
                        }, 1500);
                    }
                } else {
                    bsAlert("错误", data.msg, "error");
                }
            },
            error : function(re, status, err) {
                bsAlert("错误", re.responseText, "error");
            }
        });
}

