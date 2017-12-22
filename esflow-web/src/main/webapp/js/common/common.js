var parseIntOriginal = parseIntOriginal;
if(parseInt && undefined === parseIntOriginal) {	// 为了适应ie8或更老的浏览器，hack一下parseInt，旧的浏览器处理单个参数"0#"的默认进制为8，新的为10。现在统一为10。
	parseIntOriginal = parseInt;
	parseInt = function() {
		var arg = arguments;
		if(arg.length === 1 && typeof arg[0] === "string") {
			var s = arg[0];
			if(/^0\.*/.test(s) && !/^0[xX]\.*/.test(s))
				arg[1] = 10;
		}
		return parseIntOriginal.apply(this, arg);
	};
}
String.prototype.trim = function() {
	var reExtraSpace = /^\s*(.*?)\s+$/;
	return this.replace(reExtraSpace, "$1");
};
//设置emptyText，适用于能用val()设置值的元素
function emptyText(obj,emptyText){
	if(!obj.val()){
		obj.val(emptyText);
		obj.css('color','gray');
	}
	obj.focus(function(){
		var value = obj.val();
		if(value == emptyText){
			obj.val('');
			obj.css('color','black');
		}
	});
	obj.focusout(function(){
		var value = obj.val();
		if(!value){
			obj.css('color','gray');
			obj.val(emptyText);
		}
	});
}
//扩展Date对象，增加日期格式化方法
Date.prototype.Format = function (fmt) { //author: meizz 
    var o = {
        "M+": this.getMonth() + 1,	//月份 
        "d+": this.getDate(),		//日 
        "h+": this.getHours(),		//小时 
        "m+": this.getMinutes(),	//分 
        "s+": this.getSeconds(),	//秒 
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度 
        "S": this.getMilliseconds() //毫秒 
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
    if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
};
//long型日期格式化
function longFormat(longTime) {
	var result;
	try {
		if($.isNumeric(longTime)) {
			result = new Date(parseInt(longTime)).Format('yyyy-MM-dd hh:mm:ss');
		} else {
			result = longtTime;
		}
	} catch (e) {
		result = longTime;
	}
	return result;
}
Date.isLeapYear = function(year) {
	year = year || this.getFullYear();
	return !!((year & 3) == 0 && (year % 100 || (year % 400 == 0 && year)));
};
Date.prototype.isLeapYear = function() {
	Date.isLeapYear(this.getFullYear());
};

Common = {
		init : function(){

		},
		
		resetWindow : function(div){
			$("#" + div).find(":input").val("");
			$("#" + div).find(":input")
					.not(":button, :submit, :reset, :hidden, :radio")
					.removeAttr("checked").removeAttr("selected");
			$("#" + div).find(".easyui-combobox").combobox("clear");
			$("#" + div).find(".easyui-combotree").combotree("clear");
			$("#" + div).find(".easyui-datetimebox").combobox("clear");
			$("input[type='checkbox']").attr("checked", false);
			$("#bank_box_saveOrUpdate").unbind('click');
			$("#device_ping_ok").unbind('click');
			$("#device_trace_ok").unbind('click');
			$("#device_snmp_start").unbind('click');
			
			// 清空dataGrid中的数据
			try{
				$("#device_snmp_table").datagrid('loadData',{total:0,rows:[]});
			}catch(e){};
		},
		
		resetForm : function(){
			$(":input").not(":button, :submit, :reset, :radio").val("")
			.removeAttr("checked").removeAttr("selected");
			$(".easyui-combobox").combobox("clear");
			$(".easyui-combotree").combotree("clear");
			$(".easyui-datetimebox").combobox("clear");
		},
		
		getFormValues : function(div) {
			var s = {};
			var temp = $("#" + div).find(":input");
			temp.each(function() {
				var temp = $(this).attr("name");
				var s_ = $(this).val();
				s_ = Common.delHtml(s_);
				if (temp != null && undefined != temp) {
					s[temp] = s_;
				}
			});
			return s;
		},
		
		setFormValues :function(div, data) {
			for (var t in data) {
				var temp = $("#" + div).find("input[name='" + t + "']");
				if (temp != null && temp != undefined) {
					temp.val(data[t]);
					try {
						$('#' + t + '_box').combobox('setValue', data[t]);
						$('#' + t + '_treebox').combotree('setValue', data[t]);
					} catch (e) {
					}
				}
			}
		},
		
		
		createComboBox : function (inputId, width, data) {
				$("#"+inputId).combobox({
				data : data || [],
				width :width,
				editable: false,
				valueField: 'value',
				textField: 'text'
			});
		},
		
		delHtml:function (Word) {
			a = Word.indexOf("<");
			b = Word.indexOf(">");
			len = Word.length;
			c = Word.substring(0, a);
			if(b == -1)
			b = a;
			d = Word.substring((b + 1), len);
			Word = c + d;
			tagCheck = Word.indexOf("<");
			if(tagCheck != -1)
				Word = Common.delHtml(Word);
			return Word;
			}

};

function bsAlert(title, msg, func) {
    var f = function(){};
    if(typeof func == "function")
        f = func;
	$.alert({
		title : title,
		content : msg,
		//content: 'This is some alert to the user. <br> with some <strong>HTML</strong> <em>contents</em>',
		autoClose : 'cancel|3000',
		confirmButton : '关闭',
		confirmButtonClass : 'btn-primary',
		icon : 'glyphicon glyphicon-remove-sign',
		animation : 'zoom',
		confirm : f
	});
}

// 权限点验证
function authenticate(content,target){
	if(content.indexOf("," + target + ",") != -1)
		return true;
	return false;
}
		

/**
 * 判断是否ie
 * @returns {Boolean}
 */
function isIE() { //ie?
    if (!!window.ActiveXObject || "ActiveXObject" in window)
        return true;
    else
        return false;
}
