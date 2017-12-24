<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/pages/include/taglib.jsp"%>
<script type="text/javascript">
$(function() {
	var location = window.location.href;
	var herf = location.substr(location.indexOf(path), location.length);
	var target = $('[href="'+herf+'"]')[0];
	if (target) {
		$(".sidebar-nav1 li").removeClass("active").removeClass("collapsed");
		$(".sidebar-nav3 li").removeClass("active").removeClass("collapsed");
		$(target).parent().addClass("active");
		$(target).parent().parent().addClass('in');
		$(target).parent().parent().parent().addClass("active");
	}
	$('#histroy').on('click',function(){window.open(path +'/archived/index','_blank');})
});
</script>
 <div class="con-left2">
  	<!--一级菜单-->
  	<ul class="list-unstyled sidebar-nav1">
      <li class="active">
        <a class="mu collapsed" data-toggle="collapse" href="#ul0"><span class="pull-right"><i class="icon-chevron-up"></i></span><i class="ico-menu mrr5"></i>通用功能</a>
        <!--三级菜单-->
	    <ul id="ul0" class="list-unstyled sidebar-nav3 collapse in">
			<li><a class="mu3" href="${pageContext.request.contextPath}/biz/list/myWork"><i class="ico-dot2 mrr5"></i>统一待办</a></li>
			<li><a class="mu3" href="${pageContext.request.contextPath}/biz/list/myTemp"><i class="ico-dot2 mrr5"></i>工单草稿</a></li>
			<li><a class="mu3" href="${pageContext.request.contextPath}/biz/list/myCreate"><i class="ico-dot2 mrr5"></i>已创建工单</a></li>
			<li><a class="mu3" href="${pageContext.request.contextPath}/biz/list/myHandle"><i class="ico-dot2 mrr5"></i>已处理工单</a></li>
			<li><a class="mu3" href="${pageContext.request.contextPath}/biz/list/all"><i class="ico-dot2 mrr5"></i>工单查询</a></li>
		</ul>
      </li>
      <li>
		<a class="mu collapsed" data-toggle="collapse" href="#ul4"><span class="pull-right"><i class="icon-chevron-down"></i></span><i class="ico-menu mrr5"></i>运维流程创建</a>
		<ul id="ul4" class="list-unstyled sidebar-nav2 collapse">
			<li><a class="mu2" href="${pageContext.request.contextPath}/biz/create/eventManagement" target="_blank"><i class="ico-dot mrr5"></i>事件管理</a></li>
		</ul>
      </li>
	  <li>
		<a class="mu collapsed" data-toggle="collapse" href="#ul5"><span class="pull-right"><i class="icon-chevron-down"></i></span><i class="ico-menu mrr5"></i>运维流程管理</a>
		<ul id="ul5" class="list-unstyled sidebar-nav2 collapse">
			<li><a class="mu2" href="${pageContext.request.contextPath}/model"><i class="ico-dot mrr5"></i>模型列表</a></li>
			<li><a class="mu2" href="${pageContext.request.contextPath}/process/"><i class="ico-dot mrr5"></i>流程列表</a></li>
		</ul>
	  </li>
	  <li>
		<a class="mu collapsed" data-toggle="collapse" href="#ul6"><span class="pull-right"><i class="icon-chevron-down"></i></span><i class="ico-menu mrr5"></i>运维流程配置</a>
	  	<ul id="ul6" class="list-unstyled sidebar-nav2 collapse">
			<li><a class="mu2" href="${pageContext.request.contextPath}/dict/config"><i class="ico-dot mrr5"></i>字典管理</a></li>
			<li><a class="mu2" href="${pageContext.request.contextPath}/bizTemplateFile/index"><i class="ico-dot mrr5"></i>模版管理</a></li>
		</ul>
	  </li>
    </ul>
  </div>