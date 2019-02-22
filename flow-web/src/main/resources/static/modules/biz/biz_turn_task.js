$.namespace("biz.turn");
biz.turn = {
	
	init : function(){
		var bizs = $("#biz-table").bootstrapTable('getSelections');
		if(bizs.length<1){
    		bsAlert('提示',"请选择要转派的工单");
            return;
		}
		var bizId = bizs[0].bizId;
		var index =layer.open({
			title:"人员",
			type: 1,
		    skin: 'layerskin', //样式类名
		    area:"650px",
		    offset:100,
		    content:"<div class='base-table-wrap' style='overflow-y:hidden;'><table class='quick_search' cellpadding='0' cellspacing='0' width='100%'>" +
		    "<tr><td class='ttit'><input id='quickSearch' type='text'style='display: inline-block;margin-left:6px;margin-right:6px;line-height:24px;height:26px;width:70%;'  />"+	
		    "<a href='javascript:void(0);' id='quickSearch_btn' onclick='biz.turn.refresh()' class='search_btn'>搜&nbsp;索</a></td></tr>"+
		    "</table><table class='table table-hover base-table table-striped'id='userTable'></table></div>",
		    btn:["转派","取消"],
		    yes:function(){
		    	var rows = $("#userTable").bootstrapTable('getSelections');
		    	if(rows.length<1){
		    		bsAlert('提示',"请选择要转派的人员");
		            return;
		    	}
				var username = rows[0].USERNAME;
				biz.turn.turnTask(bizId,username);
		    	layer.close(index);
		    },
		    cancel:function(){
		    	layer.close(index);
		    }
		});
		biz.turn.queryUser();
	},
	refresh : function(){
		$("#userTable").bootstrapTable('refresh');
	},
	turnTask : function(bizId,username){
		$.confirm({
            title : "提示",
            content : "确定转派？",
            confirmButton : "确定",
            icon : "glyphicon glyphicon-question-sign",
            cancelButton : "取消",
            confirm:function() {
               $.ajax({
            	 url : path + "/actBizConf/turnTask",
                 type:"post",
			     data : {
						bizId : bizId,
						username : username
				 },
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
	queryUser : function(){
		
		var rows = $("#biz-table").bootstrapTable('getSelections');
		var vender = rows[0].handleVender;
		$("#userTable").bootstrapTable({
			method : 'post',
			contentType:"application/x-www-form-urlencoded",
			queryParams : function queryParams(param) {
				param.role = vender;
				param.fullname = $('#quickSearch').val();
				return param;
            },
			sidePagination : 'server',
			url : path + '/serviceRoleConf/findUsers',
			pagination : true,
			singleSelect : true,
			clickToSelect: true,
			pageSize : 10,
			pageList : [ 10, 20, 50 ],
			columns : [{
				field : "state",
				checkbox : true,
				align : "center"
			},{
                field : "USERNAME",
                title : "账号",
                align : "center"
            },{
                field : "FULLNAME",
                title : "姓名",
                align : "center"
            },{
                field : "MOBILE",
                title : "联系方式",
                align : "center"
            },{
                field : "DESCRIPTION",
                title : "部门",
                align : "center"
            }]
		});
	}
}