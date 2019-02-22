$(function() {
	showtime();
//	loadDictList();
});

/**
 * 注销系统
 */
function exit() {
	window.location.href = "/SSO/service/logoff.form?service="+ basePath;
}
/**
 * 
 * 修改密码
 */
function modifyPWD() {
//	var href = basePath + "/secure/user/updatePassword";
	//设定secure与ipnet部署在同一个TOMCAT下，否则请使用相对URL
	var href = "/secure/security/account/updatePasswordPage";
	var con = '<iframe id="modifyPWD" name = "modifyPWD" src=' + href + ' frameborder="0" '
				+ 'style="width: 100%; height:97%;overflow:hidden;border: none;"></iframe>';
	$('#win').window({
		title:'修改密码',
	    width:500,   
	    height:280, 
	    content:con,
	    modal:true,
	    collapsible:false,
	    maximizable:false,
	    minimizable:false
	}); 
}

/**
 * 时间显示
 */
function showtime() {
	var now = new Date();
	var year = now.getFullYear();
	var month = now.getMonth() + 1;
	var day = now.getDate();
	var h = now.getHours();
	var m = now.getMinutes();
	var s = now.getSeconds();
	if (month < 10)
		month = "0" + month;
	if (day < 10)
		day = "0" + day;
	if (h < 10)
		h = "0" + h;
	if (m < 10)
		m = "0" + m;
//	if (s < 10)
//		s = "0" + s;
	var dateTime = year + "-" + month + "-" + day + " " + h + ":" + m /*+ ":" + s*/;
	$("#time").text(dateTime);
	setTimeout("showtime()", 1000);
}

function wSize() {
	var minHeight = 500, minWidth = 1000;
	var strs = getWindowSize().toString().split(",");
	/*$("#content").height((strs[0] < minHeight ? minHeight : strs[0])
			- $("#header").height() - $("#foot").height());
	if ($("#leftTree")) {
		$("#leftTree").height((strs[0] < minHeight ? minHeight : strs[0])
			- $("#header").height() - $("#foot").height());
	}*/
	var height = strs[0] - $("#header_new").height();// - $("#foot").height();
			
	/*if (strs[0] < minHeight) {
		$("html,body").css({
			"overflow-y" : "auto"
		});
	} else {*/
		$("html,body").css({
			"overflow-y" : "hidden"
		});
	/*}*/
	if (strs[1] < minWidth) {
//		$("#content").css("width", minWidth);
		$("html,body").css({
			/* "overflow":"auto", */
			"overflow-x" : "auto"
		});
		//height = height-20;
	} else {
//		$("#content").css("width", "auto");
		$("html,body").css({
			/* "overflow":"hidden", */
			"overflow-x" : "hidden"
		});
	}
	$("#content").height(height);
	if ($("#leftTree")) {
		$("#leftTree").height(height);
	}
	
}

/*function loadDictList(){
	$.ajax({
        cache: true,
        type: "POST",
        url:path+'/dict/findDictEnable',
        success: function(data) {
        	if(data != null && data.length>0){
        		var list ='';
        		for(var i=0;i<data.length;i++){
        			list+='<li id="tree_bdcb_list"><a href="'+path+'/dict/index?dictId='+data[i].id+'">'+data[i].dictName+'</a></li>';
        		}
        		$('#dictDataMangeList').html(list); 
        	}
        }
    });
	
}*/