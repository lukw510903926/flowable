<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/pages/include/taglib.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
	String pageCompType = request.getParameter("pageCompType");

	String pageCompName = request.getParameter("pageCompName");
	String pageCompRequired = request.getParameter("pageCompRequired");
	String compParams = request.getParameter("compParams");
	if(pageCompType.indexOf("|")!=-1){
		int p = pageCompType.indexOf("|");
		compParams = pageCompType.substring(p+1);
		pageCompType = pageCompType.substring(0,p);
	}
%>

<c:set var="pageCompType" value="<%=pageCompType %>"/>
<c:set var="pageCompName" value="<%=pageCompName %>"/>
<c:set var="pageCompRequired" value="<%=pageCompRequired %>"/>
<c:set var="compParams" value="<%=compParams %>"/>
<c:if test="${pageCompType == 'TEXT' }">
	<input type="text" name="${pageCompName }" ${pageCompRequired?"required='true'":"" }"/>
</c:if>


<c:if test="${pageCompType == 'TEXTAREA' }">
	<textarea name="${pageCompName }" ${pageCompRequired?"required='true'":"" }"></textarea>
</c:if>
<c:if test="${pageCompType == 'NUMBER' }">
	<input type="text" validType="integer" name="${pageCompName }"
		 ${pageCompRequired?"required='true'":"" }/>
</c:if>

<c:if test="${pageCompType == 'DATE' }">
<input type="text" name="${pageCompName }" readonly="readonly" ${pageCompRequired?"required='true'":"" }
							onFocus="WdatePicker({dateFmt:'yyyy-MM-dd'})"
							/>
</c:if>

<c:if test="${pageCompType == 'TIME' }" >
<input type="text" name="${pageCompName }" readonly="readonly" class="Wdate" ${pageCompRequired?"required='true'":"" }
							onFocus="WdatePicker({dateFmt:'HH:mm:ss'})"
							/>
</c:if>

<c:if test="${pageCompType == 'DATETIME' }">
<input type="text" name="${pageCompName }" readonly="readonly" class="Wdate"  ${pageCompRequired?"required='true'":"" }
							onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
							/>
</c:if>

<c:if test="${pageCompType == 'BOOLEAN' }">
	<input class="easyui-combobox" name="${pageCompName }"  ${pageCompRequired?"required='true'":"" } style="width:206px;"
		data-options="
											valueField: 'id',
											textField: 'text',
											editable:false,
											data: [<c:if test="${!pageCompRequired }">
													{
														text:'',selected:true,id:''
													},
												</c:if>{
												id: '是',
												text: '是'<c:if test="${pageCompRequired }">,
												selected:true
												</c:if>
											},{
												id: '否',
												text: '否'
											}]" />
</c:if>

<c:if test="${pageCompType == 'MOBILE' }">
	<input type="text" validType="mobile" name="${pageCompName }"  ${pageCompRequired?"required='true'":"" } />
</c:if>

<c:if test="${pageCompType == 'EMAIL' }">
	<input type="text" validType="email" name="${pageCompName }"  ${pageCompRequired?"required='true'":"" } />
</c:if>

<c:if test="${pageCompType == 'COMBOBOX' ||pageCompType=='SUBTYPE_SELECT'}">
	<c:set value="${ fn:split(compParams, ',') }" var="comboboxValues" />
	<select id="${pageCompName }" name="${pageCompName }" class="easyui-combobox" data-options="editable:false${pageCompType=='SUBTYPE_SELECT'?",onChange:workInfo.form.subTypeChange":"" }" style="width:206px;">
		<c:if test="${!pageCompRequired }">
			<option value=""></option>
		</c:if>
		<c:forEach items="${ comboboxValues }" var="entity">
			<option value="${entity }">${entity }</option>
		</c:forEach>
	</select>
</c:if>
<c:if test="${pageCompType == 'HIDDEN' }">
	<input type="hidden" name="${pageCompName }"/>
</c:if>