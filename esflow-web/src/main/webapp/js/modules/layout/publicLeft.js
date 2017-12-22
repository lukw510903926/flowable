var TBRows = new Array();
var purchaseId = '';
function checkBuyCar(){
	$.ajax({
		url : path + "/market/findByPurchaseId",
		type : "post",
		dataType : "json",
		async : false,
		success : function(data){
			TBRows = data.rows;
			if(data.rows.length>0){
				$("#dataTable").bootstrapTable('destroy');
				purchaseId = data.rows[0].purchaseId;
			}
			loadPurchaseTB();
		}
	})
}
function loadPurchaseTB(){
	$("#dataTable").bootstrapTable({
		data : TBRows,
		pagination : true,
		pageSize : 2,
		sidePagination : "client",
		columns:[{
					field : "state",
					checkbox : true,
					align : "center"
				},{
					field : "productName",
					title : "产品名称",
					align : "center",
					formatter : function(value, row, index) {
						var imgs = row.image.split("^");
						var imagePath = "/"+row.productId+"/"+ imgs[0];
						return "<div class=\"item-pic pull-left\"> <img src=\""+ path+"/it/showImg?path=" + encodeURI(imagePath)  +"\" width=\"100%\" height=\"100%\" /></div><div class=\"item-info\">"+ row.productName +"</div>";
					}
				},{
					field : "count",
					title : "购买数量",
					align : "center"
				},{
					field : "parts",
					title : "部件名称",
					align : "center",
					formatter : function(value, row, index) {
						if(value){
							var span = "";
							$.each(value,function(k,v){
								if(v.partsName){
									span += "<span class=\"item-po\">"+ v.partsName +"</span>"
								}
							})
							return span;
						}else
							return '';
					}
				},{
					field : "parts",
					title : "部件数量",
					align : "center",
					formatter : function(value, row, index) {
						var span = "";
						$.each(row.parts,function(k,v){
							span += "<span class=\"item-po\">"+ v.partsCount +"</span>"
						})
						return span;
					}
				},{
					field : "parts",
					title : "单价",
					align : "center",
					formatter : function(value, row, index) {
						var span = "";
						$.each(row.parts,function(k,v){
							span += "<span class=\"item-po\">"+ v.price +"</span>"
						})
						return span;
					}
				},{
					field : "HANDLE",
					align : "center",
					title : "操作",
					formatter : function(value, row, index) {
						var button = ("<a href='javascript:void(0)' onclick='delPurchase(\""+row.purchaseId+"\",\""+row.addPurchaseId+"\")' class=\"item-option\">删除</a>");
						return button;
					}
				}]
	})
}
function delPurchase(purchaseId,addPurchaseId){
	$.confirm({
        title:"提示",
        content:"确认从购物车中删除该产品？",
        confirmButton: "确认",
        cancelButton: "取消",
        confirm:function() {
			var url = path + "/market/deleteByPurchaseId";
			$.ajax({
				   url : url,
	               type:"post",
	               data: {purchaseId : purchaseId,addPurchaseId:addPurchaseId},
	               traditional:true,
	               success:function(data){
	            	   if(data.success){
	            		   checkBuyCar();
						}
	               }
			});
        }
	});
}
function goBuy(){
	if($("#dataTable tr").length==0){
		bsAlert("提示","请选择需要购买的产品");
		return;
	}
	var returnUrl= path+"/biz/create/ITSuperMarket?cartId="+purchaseId;
	window.location.href=returnUrl;
}