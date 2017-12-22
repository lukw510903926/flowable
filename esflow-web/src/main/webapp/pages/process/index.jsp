<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/pages/include/taglib.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE html>
<html>
<head>
<base href="<%=basePath%>">

<title>首页</title>
<meta name="decorator" content="default" />
<%@ include file="/pages/include/treeview.jsp"%>

<script type="text/javascript" src="${ctx}/js/common/leftTree.js"></script>
<script>
	$(function(){
		setTimeout('openClick("model_list")', 100);
	})
	
	function openClick(value) {
		switch(value){
			case 'model_list':
				openTab(value,"模型列表","${ctx}/pages/process/model_list.jsp");
				break;
			case 'process_list':
				openTab(value,"流程列表","${ctx}/pages/process/process_list.jsp");
				break;
			case 'process_running_list':
				openTab(value,"流程列表","${ctx}/pages/process/process_running_list.jsp");
				break;
			default:
				break;
		}
	}
	function openTab(value,text,url){
		Page.utils.addTab("configTab", value, text,url
				);
	}
</script>
</head>

<body>
	<table id="main" width=100% cellpadding="0" cellspacing="0" border=0>
		<tr>
			<td valign="top">
				<div id="leftTree" class="con_left left">
					<div class="con_itembox" style="padding: 0px;">
						<div class="con_item_title">
							<ul id="menu">
								<li class="selected">
									<div class="list-title">
										<span style="padding:0px 5px 0 0;"> <a></a> <a></a> <a
											class="ico_tab13"></a>
										</span>
										<h5>流程管理</h5>
									</div>
									<div class="list-item firstMenu">
										<div class="list-item_list" autoResize="true"
											resizeHeight="15" resizeType="leftMenu">

											<ul class="nav nav-list">
												<li
													style="height: 30px;line-height: 27px;margin-left: 10px;"><a
													style="cursor: pointer;" onclick="openClick('model_list')">&nbsp;模型列表</a></li>
												<li
													style="height: 30px;line-height: 27px;margin-left: 10px;"><a
													style="cursor: pointer;" onclick="openClick('process_list')">&nbsp;流程列表</a></li>
												<!-- <li
													style="height: 30px;line-height: 27px;margin-left: 10px;"><a
													style="cursor: pointer;" onclick="openClick('process_running_list')">&nbsp;运行中流程列表</a></li> -->
											</ul>
										</div>
									</div>
								</li>
							</ul>
						</div>
					</div>
				</div>
			</td>
			<td valign="top">
				<div class="con_right"">
					<div id="configTab" class="easyui-tabs" data-options="border:false"
						componentType="tabs" autoResize="true" resizeHeight="5"
						resizeWidth="-3">
					</div>
				</div>
			</td>
	</table>
	<script type="text/javascript">
		navList(1);
	</script>
</body>
</html>
