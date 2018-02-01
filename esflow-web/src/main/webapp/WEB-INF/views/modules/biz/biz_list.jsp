<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <base href="${ctx}">
    <title>工单查询</title>
    <meta name="decorator" content="default"/>
    <script>
        var statusList = ${statusList };
        var processList = ${processList };
        var handleUser = "${handleUser}";
        var action = "${action}";
    </script>
    <link rel="stylesheet" href="${ctxPlugins}/bootstrap/bs-select/css/bootstrap-select.min.css">
    <script src="${ctxPlugins}/bootstrap/bs-select/bootstrap-select.min.js"></script>
    <script src="${ctxPlugins}/bootstrap/bs-select//i18n/defaults-zh_CN.min.js"></script>
    <script type="text/javascript" src="${ctx}/js/modules/biz/biz_list.js"></script>
    <script type="text/javascript" src="${ctx}/js/modules/biz/biz_turn_task.js"></script>
</head>
<body>
<div class="panel panel-box">
    <div class="panel-heading">
        查询条件
    </div>
    <div class="panel-body">
        <div class="mr5">
            <form class="form-horizontal" id="biz-query-form">
                <div class="col-xs-12 btn-list">
                    <a id="queryBtn" class="btn btn-y">查询</a>
                    <button type="reset" class="btn btn-n mrl10" onclick="biz.query.resetClick()">重置</button>
                </div>
            </form>
        </div>
    </div>
</div>
<div class="panel panel-ex" id='table'>
    <div class="panel-heading">
        <c:choose>
            <c:when test="${action=='myWork' }">统一待办 </c:when>
            <c:when test="${action=='myTemp' }">我的草稿 </c:when>
            <c:when test="${action=='myCreate' }">我的创建 </c:when>
            <c:when test="${action=='myHandle' }">我的已处理 </c:when>
            <c:otherwise>查询结果 </c:otherwise>
        </c:choose>
        <span class="func-btn-list">
				<c:if test="${action=='myCreate' }">
                    <a href="javascript:void(0);" id="deletBiz" onclick="biz.query.removeBizInfo()"
                       class="item-link mrl10">取消</a>
                </c:if>
			</span>
    </div>
    <div class="panel-body">
        <div class="base-table-wrap">
            <table id="biz-table" class="table table-hover base-table table-striped"/>
        </div>
    </div>
</div>
</body>
</html>