<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/pages/include/taglib.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<%-- 以列表的形式显示流程处理记录按照时间倒序排序，显示字段为 处理人、处理时间、处理意见、处理结果。表格默认收起
 --%>
<c:if test="${workBean.workLogs!=null }">
	<table class="mainTable" border="0" width="100%" cellPadding="0"
		cellSpacing="0">
		<tr>
			<td class="groupName">流程过程</td>
		</tr>
		<tr>
			<td>
				<table border="0" width="100%" cellPadding="0"
					cellSpacing="0">
					<c:if test="${workBean.workLogs!=null }">
						<tr style="background-color: #EDF5FD">
							<td style="padding-right: 17px;">
								<table border="0" width="100%" cellPadding="0" cellSpacing="0">
									<tr>
										<th class="tableTD3" style="width:12%">处理人</th>
										<th class="tableTD3" style="width:12%">处理时间</th>
										<th class="tableTD3" style="width:10%">处理环节</th>
										<th class="tableTD3" style="width:12%">处理结果</th>
										<th class="tableTD3" style="width:45%">处理意见</th>
										<th class="tableTD3" style="width:20px"></th>
									</tr>
								</table>
							</td>
						</tr>
						<tr>
							<td>
								<div style="max-height:150px;overflow-y: scroll;">
									<table class="mainTable2" border="0" width="100%"
										cellPadding="0" cellSpacing="0">
										<c:forEach items="${workBean.workLogs}" varStatus="status"
											var="item">
											<tr>
												<td class="tableTD3" style="width:12%"><c:out
														value="${item.handleUser }" /></td>
												<td class="tableTD3" style="width:12%"><fmt:formatDate
														value="${item.createTime}" type="date"
														pattern="yyyy-MM-dd HH:mm:ss" /></td>
												<td class="tableTD3" style="width:10%"><c:out
														value="${item.taskName }" /></td>
												<td class="tableTD3" style="width:12%"><c:out
														value="${item.handleName }" /></td>
												<td class="tableTD3" style="width:45%"><c:out
														value="${item.handleMes }" /></td>
												<td class="tableTD3" style="width:20px">
													<a href="javascript:return void(0);" onclick="workInfo.log.showInputData('${item.id}')">查看填单信息</a>
												</td>
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