$.namespace("login");
login = {
	login : function() {

		var param = {};
		$('#loginForm').find("[name]").each(function() {
			var value = $.trim($(this).val());
			if (value != '') {
				param[this.name] = value;
			}
		});
		console.info(path +'/shiro/ajaxLogin');
		$.ajax({
			url : path +'/shiro/ajaxLogin',
			type : 'post',
			data : param,
			traditional : true,
			success : function(result){
				console.info(result);
			}
		});
	}
}