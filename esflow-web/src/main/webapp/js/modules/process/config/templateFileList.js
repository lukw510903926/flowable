$(function() {

	$('#flowSelect1').select2();
	$("#filelist").bootstrapTable({
		method : 'post',
		contentType : "application/x-www-form-urlencoded",
		url : path + "/bizTemplateFile/list",
		pagination : true,
		sidePagination : 'server',
		pageSize : 20,
		pageList : [ 10, 25, 50, 100 ],
		clickToSelect : true,
		queryParams : function(param) {

			$('#queryForm').find('[name]').each(function() {
				var value = $.trim($(this).val());
				if (value != '') {
					param[this.name] = value;
				}
			});
			return param;
		},
		columns : [ {
			field : "state",
			checkbox : true,
			align : "center"
		}, {
			field : "fileName",
			title : "文件名",
			align : "center",
			formatter : function(value){
				 if (value && value.length > 30)
                     return "<i title='" + value + "'>" + value.substring(0, 10) + "...</i>";
                 else
                     return value;
			}
		}, {
			field : "flowName",
			title : "所属流程",
			align : "center"
		}, {
			field : "fullName",
			title : "上传人",
			align : "center"
		}, {
			field : "createTime",
			title : "上传时间",
			align : "center"
		} ]
	});
    flowCombobox($("[name='flowName']"));
});

function flowCombobox(ele) {
	
	$.ajax({
		url : path + "/actBizConf/findProcess",
		type : "post",
		data : {},
		success : function(data) {
			if (data != "") {
				var option = $("<option>");
				option.text("请选择");
				option.val("");
				ele.append(option);
				for (var key in data) {
					var option = $("<option>");
					var op = data[key];
					option.text(op);
					option.val(op);
					ele.append(option);
				}
			    $('.selectpicker').selectpicker('render');
			    $('.selectpicker').selectpicker('refresh');
			}
		}
	});
}

function upload() {
	$('#attachmentModal').modal('hide');
	if ($("[name='file'").val() == "") {
		layer.msg("请选择上传文件!");
		return;
	}
	if ($("#uploadFileForm [name='flowName']").val()=='') {
		layer.msg("请选择所属流程!");
		return;
	}
	$("#uploadFileForm").ajaxSubmit({
		
		url : path + "/bizTemplateFile/upload",
		type : "post",
		success : function(result) {
			var data = eval("("+result+")");//转换为json对象 ;
			if (data.success) {
				$('#attachmentModal').modal('hide');
				$('.modal-backdrop').filter('.fade').filter('.in').remove();
				$("#filelist").bootstrapTable("refresh");
			} else {
				bsAlert("提示", data.msg);
			}
		}
	});
}

function queryFile(){
	
	var param = {};
	$('#queryForm').find('[name]').each(function(){
		var value = $.trim($(this).val());
		if(value !=''){
			param[this.name]=value;
		}
	});
	$("#filelist").bootstrapTable('refresh', {query:param});
}

function  resetQueryForm(){

	$('#queryForm').find('[name="flowName"]').val('');
	$('#queryForm').find('[name="fileName"]').val('');
}

function downLoad() {
    var rows = $("#filelist").bootstrapTable("getSelections");
    if (rows.length < 1) {
        layer.msg("请选择需要下载的文件!");
        return;
    }
    var ids = new Array();
    for (var i in rows) {
        ids[i] = rows[i].id;
    }
    ids.toString();
    window.location.href = path + "/bizTemplateFile/download?&ids=" + ids;
}

function deleteFile() {
	
    var rows = $("#filelist").bootstrapTable("getSelections");
    if (rows.length < 1) {
        bsAlert("提示","请选择删除行!");
        return;
    }
    layer.confirm("确认是否删除行", {
        skin : "layerskin",
        btn : ["确定", "取消"]
    }, function(index) {
        layer.close(index);
        var ids = new Array();
        for (var i in rows) {
            ids[i] = rows[i].id;
        }
        $.ajax({
            url : path + "/bizTemplateFile/remove",
            type : "post",
            traditional : true,
            data : {
                ids : ids
            },
			success : function(result) {
				if (result.success) {
					bsAlert("提示", result.msg);
					$("#filelist").bootstrapTable("refresh");
				} else {
					bsAlert("提示", result.msg);
				}
			}
		});
    }, function() {
        return;
    });
}