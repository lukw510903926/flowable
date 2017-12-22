<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/pages/include/taglib.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<%-- 工单显示附件，包括附件上传功能，以列表的形式对已经上传的附件进行呈现，根据上传时间倒序排序，

	包括字段：上传用户、附件名、上传时间、描述、查看(根据附件类型显示下载还是查看，查看是针对图片可无需下载在线查看，其他为下载)
 --%>
 <c:if test="${workBean.annexs!=null }">
<table class="mainTable" border="0" width="100%" cellPadding="0"
	cellSpacing="0">
	<tr>
		<td class="groupName">附件</td>
	</tr>
	<tr>
		<td>
			<table border="0" width="100%" cellPadding="0"
				cellSpacing="0">
				<c:if test="${workBean.annexs!=null }">
					<tr style="background-color: #EDF5FD">
						<td style="padding-right: 17px;">
							<table border="0" width="100%" cellPadding="0" cellSpacing="0">
								<tr>
									<th class="tableTD3">上传用户</th>
									<th class="tableTD3">附件名</th>
									<th class="tableTD3">上传时间</th>
									<th class="tableTD3" style="width:35%">描述</th>
									<th class="tableTD3" style="width:5%">操作</th>
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td>
							<div style="max-height:150px;overflow-y: scroll;">
								<table class="mainTable2" border="0" width="100%"
									cellPadding="0" cellSpacing="0">
									<c:forEach items="${workBean.annexs}" varStatus="status"
										var="item">
										<tr dataid="${item.id }">
											<td class="tableTD3"><c:out value="${item.createUser }" /></td>
											<td class="tableTD3"><c:out value="${item.name }" /></td>
											<td class="tableTD3"><fmt:formatDate
													value="${item.createDate}" type="date"
													pattern="yyyy-MM-dd HH:mm:ss" /></td>
											<td class="tableTD3" style="width:35%"><c:out
													value="${item.descr }" /></td>
											<td class="tableTD3" style="width:5%"><a
												href="javascript:return void(0);" onclick="workInfo.annex.download('${item.id}')">${item.fileType=='IMAGE'?'查看':'下载' }</a></td>
										</tr>
									</c:forEach>

								</table>
							</div>
						</td>
					</tr>
				</c:if>
			</table>
</table>
</td>
</tr>
</table>
</c:if>