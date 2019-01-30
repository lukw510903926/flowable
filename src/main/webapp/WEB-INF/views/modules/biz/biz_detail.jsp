<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<base href="${ctx}">
<title>工单流转</title>
<%@ include file="/WEB-INF/views/include/head.jsp"%>
<script type="text/javascript" src="${ctx}/js/modules/biz/biz_edit_form.js"></script>
<script type="text/javascript" src="${ctx}/js/modules/biz/biz_edit_paramfunc.js"></script>
<script type="text/javascript" src="${ctx}/js/modules/biz/biz_show_table.js"></script>
<script type="text/javascript" src="${ctx}/js/modules/biz/biz_stepPic.js"></script>
<script type="text/javascript" src="${ctx}/js/modules/biz/biz_detail.js"></script>
<script type="text/javascript">
	var id = "${id}";
	var bizId = "${id}";
	$(document).ready(function() {
		  $("#vendor").select2();
	});
</script>
<style>
.select2-container .select2-selection--multiple .select2-selection__rendered {
	display:inline-flex;
}
.select2-container--default .select2-selection--multiple .select2-selection__choice {
	margin-top:0px;
}

.Wdate {
	line-height: 20px;
	height:28px;
}

.listtable>tbody>tr>th {
	width: 15%;
	white-space: normal;
}

.listtable>tbody>tr>td {
	width: 35%;
	white-space: normal;
}

.listtable>tbody>tr>td[colspan='3'] {
	width: 85%;
}

.panel-body {
	padding: 0px;
}

.panel-heading {
	padding: 0;
	margin: 0;
}

.listtable {
	table-layout: fixed;
}

#layoutTable tbody {
	vertical-align: top;
}

</style>
<script type="text/javascript">
</script>
<script>
$(document).ready(function () {
  	 $(".js-example-basic-single").select2();
	 var stepLiLength=$("#step_log li").length;
	 var stepTxtLiLength=$("#step_text li").length;
	 var stepLogWidth=140*(stepLiLength-1);
	 var stepTxtWidth=140*stepTxtLiLength; 
	 $("#stepPic").width(stepLogWidth);
	 $("#stepPic2").width(stepTxtWidth);
	 var stepLogMar=stepLogWidth/2+20;
	 var stepTxtMar=stepTxtWidth/2;
	 $("#stepPic").css("margin-left", -stepLogMar+"px");
	 $("#stepPic2").css("margin-left", -stepTxtMar+"px");
	 $("#stepwrap").height(50+$("#stepPic2").height()+"px");
	}
)
</script>
</head>
<body style="padding: 10px 150px;">
	<form id="form" method="post" enctype="multipart/form-data">
		<input type="hidden" name="base.workNumber" />
		<input type="hidden" name="base.handleResult" />
		<input type="hidden" name="base.buttonId" />
		<input type="hidden" name="base.handleName" />
		<input type="hidden" name="base.bizId" />
		<div>
			<h5 style="margin: 10px 0px;" id="biz_detail_info">工单状态</h5>
			<div class="stepwrap" id="stepwrap">
				<div id="stepPic" class="logistics clearfix"></div>
				<div id="stepPic2" class="clearfix logistics-text"></div>
			</div>
		</div>
		<div class="import_form">
			<h2 class="white_tit">
				<i id="msgtitle">报障人信息</i><a role="button" data-toggle="collapse" href="#collapse" class="drop"></a>
			</h2>
		</div>
		<div class="listtable_wrap panel-collapse collapse in" id="collapse">
			<table id="fqrxx" cellpadding="0" cellspacing="0" class="listtable"> </table>
		</div>
		<div class="close_all mrt10">
			<div class="close_all_tit">
				<a class="gray_drop" onclick="javascript:$('#workLogs a[href^=#work]').click();"
					style="float:none;vertical-align:middle;margin:-3px 5px 0;"></a>展开/合并所有处理流程
			</div>
			<div id="workLogs"></div>
		</div>
	</form>
</body>