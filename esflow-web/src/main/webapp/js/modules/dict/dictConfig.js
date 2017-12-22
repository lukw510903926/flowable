function loadDictConfigList(){
	$("#dictconfigTable").bootstrapTable({
		method : "get",
		url : path + "/dict/findDictConfig",
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
					field : "DICTNAME",
					title : "名称",
					formatter : function(value, row, index) {
						if (value != null) {
							return "<a href='"+path+"/dict/index?dictId="+row.ID+"'>"+value+"</a>";
						}else{
							return value;
						}
					},
					align : "center"
				},
				{
					field : "STATUS",
					title : "状态",
					formatter : function(value, row, index) {
						if (value==0) {
							return "启用";
						} else {
							return "停用";
						}
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
}
function saveDictConfig(){
	
	$.ajax({
        cache: true,
        type: "POST",
        url:path+'/dict/addDict',
        data:$('#dictConfigForm').serialize(),
        async: false,
        success: function(data) {
        	window.location.href=path+"/dict/config"; 
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


function editDictUI(){
	var rows = $("#dictconfigTable").bootstrapTable("getSelections");
	if(rows.length<1||rows.length>1){
//		layer.msg("请选择一行!");
		alert("请选择一行!");
		return;
	}
	var dictId = rows[0].ID;
	window.location.href=path+'/dict/addUI/edit?dictId='+dictId;
}

function delDict(){
	var rows = $("#dictconfigTable").bootstrapTable("getSelections");
	if(rows.length<1){
//		layer.msg("请选择一行!");
		alert("请选择需删除的行!");
		return;
	}
	var ids = new Array();
	for(var i=0;i<rows.length;i++){
		ids.push(rows[i].ID);
	}
	$.ajax({
        cache: true,
        type: "POST",
        url:path+'/dict/delDict',
        data:{ids:ids},
        traditional: true,
        async: false,
        success: function(data) {
        	$("#dictconfigTable").bootstrapTable("refresh");
        	alert(data.msg);
        },
        error: function(request) {
            alert(JSON.stringify(request));
        }
    });
}

$(function(){
	loadDictConfigList();
});