$(function(){
	var dictId = $('#dictDataTable').data('dictid');
	$("#dictDataTable").bootstrapTable({
				method : "get",
				url : path + "/dict/findDictData?dictId="+dictId,
				//height : 190,
				pagination:true,
				sidePagination:'server',
				pageSize:20,
				pageList:[10, 25, 50, 100],
				clickToSelect : true,
				columns : [
						{
							field : "state",
							checkbox : true,
							align : "center"
						},
						{
							field : "NAME",
							title : "名称",
							formatter : function(value, row, index) {
								return value;
							},
							align : "center"
						}, {
							field : "UPDATEUSER",
							title : "修改人",
							align : "center"
						}, {
							field : "UPDATETIME",
							title : "修改时间",
							formatter : function(value, row, index) {
								return value;
							},
							align : "center"
						} ]
			});
});

function upload() {
	var url = $("#uploadExcel :file").val();
	var str = url.substring(url.lastIndexOf(".") + 1, url.length);

	if (str !== "xls" && str !== "xlsx") {
		//layer.msg("请上传Excel文件!");
		alert("请上传Excel文件!");
		return;
	}
	$("#uploadExcel").submit();
	$('#myModal').modal('hide');
}
function saveDictData(dictId,dataId){
	$.ajax({
//        cache: true,
        type: "POST",
        url:path+'/dict/addDictData',
        data:$('#dictDataForm').serialize(),
        async: false,
        traditional: true,
        success: function(data) {
        	window.location.href=document.referrer; 
        	alert(data.msg);
        	if(isIE()){
        	location.reload();
        	}

        },
        error: function(request) {
            alert("Connection error");
        }
    });

}


function editDictDataUI(dictId){
	var rows = $("#dictDataTable").bootstrapTable("getSelections");
	if(rows.length<1||rows.length>1){
//		layer.msg("请选择一行!");
		alert("请选择一行!");
		return;
	}
	var dataId = rows[0].ID;
	window.location.href=path+'/dict/addDataUI?dictId='+dictId+'&dataId='+dataId;
}

function delDict(dictId,isAll){
	var rows = $("#dictDataTable").bootstrapTable("getSelections");
	var ids = new Array();
	
	if(!isAll){
		if(rows.length<1){
//			layer.msg("请选择一行!");
			alert("请选择需删除的行!");
			return;
		}
		for(var i=0;i<rows.length;i++){
			ids.push(rows[i].ID);
		}
	}
	$.ajax({
        type: "POST",
        url:path+'/dict/delDictData',
        data:{dictId:dictId,ids:ids,isAll:isAll},
        traditional: true,
        async: false,
        success: function(data) {
        	$("#dictDataTable").bootstrapTable("refresh");
        	alert(data.msg);
        },
        error: function(request) {
            alert(JSON.stringify(request));
        }
    });
}