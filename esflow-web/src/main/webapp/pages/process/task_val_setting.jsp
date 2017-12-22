<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/pages/include/taglib.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";

	String processId = request.getParameter("processId");
	String version = request.getParameter("version");
	String taskId = request.getParameter("taskId");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title>流程参数设置</title>
<%@include file="/pages/include/head.jsp"%>
<link rel="stylesheet" type="text/css"
	href="${ctx}/themes/default/css/workInfo.css">
<script type="text/javascript">
	var processId="<%=processId%>";
	var version="<%=version%>";
	var taskId="<%=taskId%>";
	
	var path="${ctx}";
	$(function() {
		$("#addBtn").click(function(){
			updateOrAddClickHandler(null);
		})
		
		$("#editBtn").click(function(){
			var selectRows = $('#datagrid').datagrid('getSelections');
			if(selectRows.length == 0){
				$.messager.alert('提示','请选择需要修改的属性');
				return;
			}
			if(selectRows.length > 1){
				$.messager.alert('提示','只能选择一条进行修改');
				return;
			}
			
			updateOrAddClickHandler(selectRows[0].id);
		})
		
		$("#delBtn").click(function(){
			var selectRows = $('#datagrid').datagrid('getSelections');
			if(selectRows.length == 0){
				$.messager.alert('提示','请选择需要删除的策略');
				return;
			}
			var valIds = "";
			for(var i = 0;i < selectRows.length;i++){
				valIds += selectRows[i].id;
				if(i < selectRows.length - 1){
					valIds += ',';
				}
			}
			$.messager.confirm('提示','确定要删除吗?',function(r){   
			    if (r){   
			        $.ajax({
						url : path + "/processModelMgr/deleteProcessValById",
						data : {
							valIds : valIds
						},
						dataType : 'json',
						success : function(data) {
							if (data.success) {
								$.messager.alert("提示", data.msg);
								$('#datagrid').datagrid('reload');
							} else {
								$.messager.alert("错误", data.msg, "error");
							}
						},
						error : function(re, status, err) {
							$.messager.alert("错误", re.responseText, "error");
						}
					});
			    }   
			});
		})
		
		/* $("#componentArgs").hide();
		$("#ud_pageComponent").on("change",function(){
			var componentVal = $('#ud_pageComponent option:selected').val();
			if(componentVal == 'COMBOBOX'){
				$("#componentArgs").show();
			}else{
				$("#componentArgs").hide();
			}
		}) */
		
		$("#saveOrUpdateBtn").click(function(){
			if($.trim($('#ud_name').val()) == ""){
				$.messager.alert("提示", "属性名称不能为空");
				return;
			}
			
			var nameOrder=$("#ud_nameOrder").val();
			if(nameOrder != ""){
				if(isNaN(nameOrder)){
					$.messager.alert("提示", "字段只能是数字");
					return;
				}
			}
			
			/* var componentVal = $('#ud_pageComponent option:selected').val();
			if(componentVal != 'COMBOBOX'){
				$("#ud_componentArgs").val('');
			} */
			
			$.ajax({
				type : 'POST',
				url : path + "/processModelMgr/saveOrUpdateProcessVal",
				data : {
					id : updateId,
					processId : processId,
					taskId : taskId,
					version : version,
					name : $('#ud_name').val(),
					nameEn : $('#ud_nameEn').val(),
					nameOrder : $('#ud_nameOrder').val(),
					required : $('input[name="ud_requiredradio"]:checked').val(),
					groupName : $('#ud_groupName').val(),
					groupOrder : $("#ud_groupOrder").val(),
					pageComponent : $('#ud_pageComponent option:selected').val(),
					pageComponentVal : $("#ud_componentArgs").val(),
					isprocVal : $('input[name="ud_isprocValradio"]:checked').val()
				},
				dataType : 'json',
				success : function(data) {
					if (data.success) {
						$('#datagrid').datagrid('reload');
						$('#updateOrAddPopWin').window('close');
						$.messager.alert("提示", data.msg);
					} else {
						$.messager.alert("错误", data.msg, "error");
					}
				},
				error : function(re, status, err) {
					$.messager.alert("错误", re.responseText, "error");
				}
			});
		})
	});
	
	var isUpdate = false;
	var updateId = "0";
	function updateOrAddClickHandler(valId){
		if(valId != null){
			$("#updateOrAddPopWin").window('setTitle','更新');
			isUpdate = true;
			//编辑
			//$("#componentArgs").hide();
			$.ajax({
				type : 'POST',
				url : path + "/processModelMgr/getProcessValById",
				data : {
					processId : processId,
					taskId : valId
				},
				dataType : 'json',
				success : function(data) {
					if (data.success) {
						$('#ud_name').val(data.obj.name);
						$('#ud_nameEn').val(data.obj.nameEN);
						$('#ud_nameOrder').val(data.obj.order);
						$('input[name=ud_requiredradio][value=' + data.obj.required + ']').attr('checked','checked');
						$('#ud_groupName').val(data.obj.groupName);
						$('#ud_groupOrder').val(data.obj.groupOrder);
						
						var pageComponentArr = data.obj.pageComponent.split("|");
						$('#ud_pageComponent').val(pageComponentArr[0]);
						if(pageComponentArr.length == 2){
							//$("#componentArgs").show();
							$('#ud_componentArgs').val(pageComponentArr[1]);
						}
						$('input[name=ud_isprocValradio][value=' + data.obj.SHARE_STATUS + ']').attr('checked','checked');
					} else {
						$.messager.alert("错误", data.msg, "error");
					}
				},
				error : function(re, status, err) {
					$.messager.alert("错误", re.responseText, "error");
				}
			});
			updateId = valId;	
		}else{
			$("#updateOrAddPopWin").window('setTitle','添加');
			
			isUpdate = false;
			updateId = '0';
			//添加
			$('#ud_name').val("");
			$('#ud_nameEn').val("");
			$('#ud_nameOrder').val("");
			$('input[name=ud_requiredradio][value=false]').attr('checked','checked');
			$('#ud_groupName').val("");
			$('#ud_groupOrder').val("");
			$('#ud_pageComponent').val("TEXT");
			$('input[name=ud_isprocValradio][value=false]').attr('checked','checked');
			$("#ud_componentArgs").val("");
			//$("#componentArgs").hide();
		}
		$("#updateOrAddPopWin").window("center");
		$("#updateOrAddPopWin").window("open");
	}

	
	
	function truefalseValue(value){
		return "true"==value||value==true?"是":"否";
	}
</script>
</head>

<body
	style="overflow: auto; background-color: #FFFFFF; padding-left: 10px; padding-right: 10px;">
	<div class="con_bg">
		<div class="con_itembox">
			<a class="btn1" href="javascript:void(0)" id="addBtn">添加<span /></span></a>
			<a class="btn1" href="javascript:void(0)" id="editBtn">修改<span /></span></a>
			<a class="btn1" href="javascript:void(0)" id="delBtn">删除<span /></span></a>
		</div>
		<div class="con_itembox">
			<div class="table" autoResize="true" resizeHeight='25' resizeWidth="false">
				<table id="datagrid" class="easyui-datagrid"
					data-options="data:[],fitColumns:true,striped:false,loadMsg:'数据加载中...',
					url:'${ctx }/processModelMgr/processValList',
					onBeforeLoad:function(param){
						param.processId='<%=processId%>';
						param.version='<%=version%>';
						param.taskId='<%=taskId %>';
					},
					ctrlSelect:false,fit:true,border:false,singleSelect:true"
					autoResize="true" componentType='datagrid' resizeHeight='0'
					resizeWidth="false">
					<thead>
						<tr>
							<th data-options="field:'id',checkbox:true"></th>
							<th data-options="field:'name',width:100">属性中文名</th>
							<th data-options="field:'nameEN',width:100">属性英文名</th>
							<th data-options="field:'order',width:80">属性排序</th>
							<th data-options="field:'required',width:80,formatter:truefalseValue">是否必填</th>
							<th data-options="field:'groupName',width:80">属性分组</th>
							<th data-options="field:'groupOrder',width:100">属性分组排序</th>
							<th data-options="field:'pageComponent',width:100">页面组件类型</th>
							<th data-options="field:'version',width:50">版本</th>
							<th data-options="field:'procVariable',width:120,formatter:truefalseValue">是否流程变量</th>
						</tr>
					</thead>
				</table>
			</div>
		</div>
	</div>
	
	<!-- 派单策略编辑-新增  -->
	<div id="updateOrAddPopWin" class="easyui-window" title="更新/添加"
		style="width:700px;padding: 10px;overflow: hidden;height:300px;"
		data-options="collapsible:false,minimizable:false,maximizable:false,resizable:false,modal:true,closed:true">
		<table width="100%" border="0" cellspacing="0" cellpadding="0"
			class="table_list">
			<tr>
				<td class="td1">属性中文名:</td>
				<td><input type="text" name="ud_name" id="ud_name"
					style="width:150px;" /></td>
				<td class="td1">属性英文名:</td>
				<td><input type="text" name="ud_nameEn" id="ud_nameEn"
					style="width:150px;" /></td>
			</tr>
			<tr>
				<td class="td1">属性排序标识:</td>
				<td><input type="text" name="ud_nameOrder" id="ud_nameOrder"
					style="width:150px;" /></td>
				<td class="td1">是否必填:</td>
				<td>
					<input type="radio" name="ud_requiredradio" value="true" />是 
					<input type="radio" name="ud_requiredradio" value="false" checked="checked"/>否
				</td>
			</tr>
			<tr>
				<td class="td1">属性分组名:</td>
				<td><input type="text" name="ud_groupName" id="ud_groupName"
					style="width:150px;" /></td>
				<td class="td1">属性分组排序标识:</td>
				<td><input type="text" name="ud_groupOrder" id="ud_groupOrder"
					style="width:150px;" /></td>
			</tr>
			<tr>
				<td class="td1">页面组件类型:</td>
				<td>
					<select name="ud_pageComponent" id="ud_pageComponent">
						<option value="TEXT">文本</option>
						<option value="TEXTAREA">大文本</option>
						<option value="NUMBER">数字</option>
						<option value="MOBILE">手机号</option>
						<option value="EMAIL">邮箱</option>
						<option value="DATE">日期</option>
						<option value="DATETIME">日期时间</option>
						<option value="COMBOBOX">下拉选项</option>
						<option value="BOOLEAN">是否</option>
						<option value="HIDDEN">隐藏</option>
					</select>
				</td>
				<td class="td1">是否流程变量:</td>
				<td>
					<input type="radio" name="ud_isprocValradio" value="true" />是 
					<input type="radio" name="ud_isprocValradio" value="false" checked="checked"/>否
				</td>
			</tr>
			<tr id="componentArgs" style="padding: 10px;">
				<td class="td1">组件参数:</td>
				<td colspan="3">
					<input type="text" name="ud_componentArgs" id="ud_componentArgs"
						style="width:350px;" />
				</td>
			</tr>
		</table>
		<div style="padding: 10px;">
			COMBOBOX组件参数使用逗号隔开  A,B,C
		</div>
		<div style="float: right;padding-right: 20px;">
			<a id="saveOrUpdateBtn" class="btn1 ie6png">保存<span class="ie6png"></span></a>
		</div>
	</div>
	
</body>
</html>

