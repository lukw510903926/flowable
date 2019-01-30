<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:if test="${not empty sessionScope.resourceList}">
	<c:set var="resList" value=","></c:set>
	<c:forEach items="${sessionScope.resourceList}" var="res">
		<c:set var="resList" value="${resList}${res},"></c:set>
	</c:forEach>
</c:if>
<c:set var="baseCtx" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/"/>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="ctxPlugins" value="${pageContext.request.contextPath}/plugins"/>
<c:set var="themePath" value="${pageContext.request.contextPath}/themes/default"/>
