<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/pages/include/taglib.jsp"%>
<%@ include file="/pages/include/param.jsp"%>
<c:set var="ctx2" value="${ctx}/"/>
<nav class="navbar navbar-default active" role="navigation">
   <div class="navbar-header">
      <a class="navbar-brand">CMDB</a>
   </div>
   <div>
	<ul class="nav navbar-nav navbar-right">
	<c:forEach items="${navMenu.items}" var="mi" varStatus="miStat">
		<c:if test="${mi.visible}">
			<c:choose>
			<c:when test="${not empty mi.items}">
			<li class="dropdown"><a class="dropdown-toggle"
			data-toggle="dropdown" href="${empty mi.href ? 'javascript:void(0);' : fns:genUrl(mi.href, ctx) }" target="${mi.target}">
					${mi.name}<span class="caret"></span>
				</a>
				<ul class="dropdown-menu">
				<c:forEach var="dt" items="${mi.items}">
				<c:if test="${dt.visible}">
					<c:choose>
						<c:when test="${empty dt.items}">
					<li>
						<a href="${fns:genUrl(dt.href, ctx)}" target="${dt.target}">${dt.name}</a>
					</li>
						</c:when>
						<c:otherwise>
					<li>
						${dt.name}
					</li>
							<c:forEach items="${dt.items}" var="dd">
							<c:if test="${dd.visible}">
					<li>
						<a href="${fns:genUrl(dd.href, ctx)}" target="${dd.target}">${dd.name}</a>
					</li>
							</c:if>
							</c:forEach>
						</c:otherwise>
					</c:choose>
				</c:if>
				</c:forEach>
				</ul>
			</li>
			</c:when>
			<c:otherwise>
			<li>
				<a href="${empty mi.href ? 'javascript:void(0);' : fns:genUrl(mi.href, ctx) }" target="${mi.target}">
					${mi.name}
				</a>
			</li>
       		</c:otherwise>
       		</c:choose>
		</c:if>
	</c:forEach>
	</ul>
 </div>
</nav>
