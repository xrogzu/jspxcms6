<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fnx" uri="http://java.sun.com/jsp/jstl/functionsx"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="f" uri="http://www.jspxcms.com/tags/form"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>Jspxcms管理平台 - Powered by Jspxcms</title>
<jsp:include page="/WEB-INF/views/commons/head.jsp"></jsp:include>
<script type="text/javascript">
$(function() {
	$("#validForm").validate({
		submitHandler: function(form) {
		   $("#validForm input[name|='dy']").each(function() {
			   var name = $(this).attr("name");
			   $(this).attr("name",name.substring(3,name.lastIndexOf("-")));
		   });
		   form.submit();
		}
	});
	$("input[name='name']").focus();
	$("#pagedTable").tableHighlight();
	$("#pagedTable tbody tr").dblclick(function(eventObj) {
		var nodeName = eventObj.target.nodeName.toLowerCase();
		if(nodeName!="input"&&nodeName!="select"&&nodeName!="textarea") {
			location.href=$("#edit_opt_"+$(this).attr("beanid")).attr('href');
		}
	});
});
function confirmDelete() {
	return confirm("<s:message code='confirmDelete'/>");
}
</script>
</head>
<body class="c-body">
<jsp:include page="/WEB-INF/views/commons/show_message.jsp"/>
<div class="c-bar margin-top5">
  <span class="c-position"><s:message code="question.management"/> - <s:message code="${oprt=='edit' ? 'edit' : 'create'}"/></span>
</div>
<form id="validForm" action="${oprt=='edit' ? 'update' : 'save'}.do" method="post">
<tags:search_params/>
<f:hidden name="oid" value="${bean.id}"/>
<f:hidden name="position" value="${position}"/>
<input type="hidden" id="redirect" name="redirect" value="edit"/>
<table border="0" cellpadding="0" cellspacing="0" class="in-tb margin-top5">
  <tr>
    <td colspan="4" class="in-opt">
			<shiro:hasPermission name="ext:question:create">
			<div class="in-btn"><input type="button" value="<s:message code="create"/>" onclick="location.href='create.do?${searchstring}';"<c:if test="${oprt=='create'}"> disabled="disabled"</c:if>/></div>
			<div class="in-btn"></div>
			</shiro:hasPermission>
			<shiro:hasPermission name="ext:question:copy">
			<div class="in-btn"><input type="button" value="<s:message code="copy"/>" onclick="location.href='create.do?id=${bean.id}&${searchstring}';"<c:if test="${oprt=='create'}"> disabled="disabled"</c:if>/></div>
			</shiro:hasPermission>
			<shiro:hasPermission name="ext:question:delete">
			<div class="in-btn"><input type="button" value="<s:message code="delete"/>" onclick="if(confirmDelete()){location.href='delete.do?ids=${bean.id}&${searchstring}';}"<c:if test="${oprt=='create'}"> disabled="disabled"</c:if>/></div>
			</shiro:hasPermission>
			<div class="in-btn"></div>
			<div class="in-btn"><input type="button" value="<s:message code="prev"/>" onclick="location.href='edit.do?id=${side.prev.id}&position=${position-1}&${searchstring}';"<c:if test="${empty side.prev}"> disabled="disabled"</c:if>/></div>
			<div class="in-btn"><input type="button" value="<s:message code="next"/>" onclick="location.href='edit.do?id=${side.next.id}&position=${position+1}&${searchstring}';"<c:if test="${empty side.next}"> disabled="disabled"</c:if>/></div>
			<div class="in-btn"></div>
			<div class="in-btn"><input type="button" value="<s:message code="return"/>" onclick="location.href='list.do?${searchstring}';"/></div>
      <div style="clear:both;"></div>
    </td>
  </tr>
  <tr>
    <td class="in-lab" width="15%"><em class="required">*</em><s:message code="question.title"/>:</td>
    <td class="in-ctt" width="85%" colspan="3"><f:text name="title" value="${oprt=='edit' ? (bean.title) : ''}" class="required" maxlength="150" style="width:500px;"/></td>
  </tr>
  <tr>
    <td class="in-lab" width="15%"><s:message code="question.description"/>:</td>
    <td class="in-ctt" width="85%" colspan="3"><f:textarea name="description" value="${bean.description}" maxlength="255" style="width:500px;height:80px;"/></td>
  </tr>
  <tr>
    <td class="in-lab" width="15%"><s:message code="question.beginDate"/>:</td>
    <td class="in-ctt" width="35%"><input type="text" name="beginDate" value="<fmt:formatDate value="${bean.beginDate}" pattern="yyyy-MM-dd'T'HH:mm:ss"/>" onclick="WdatePicker({dateFmt:'yyyy-MM-ddTHH:mm:ss'});" style="width:180px;"/></td>
    <td class="in-lab" width="15%"><s:message code="question.endDate"/>:</td>
    <td class="in-ctt" width="35%"><input type="text" name="endDate" value="<fmt:formatDate value="${bean.endDate}" pattern="yyyy-MM-dd'T'HH:mm:ss"/>" onclick="WdatePicker({dateFmt:'yyyy-MM-ddTHH:mm:ss'});" style="width:180px;"/></td>
  </tr>
  <tr>
    <td class="in-lab" width="15%"><em class="required">*</em><s:message code="question.mode"/>:</td>
    <td class="in-ctt" width="35%">
    	<select name="mode">
    		<f:option value="0" selected="${bean.mode}" default="1"><s:message code="question.mode.0"/></f:option>
    		<f:option value="1" selected="${bean.mode}" default="1"><s:message code="question.mode.1"/></f:option>
    		<f:option value="2" selected="${bean.mode}" default="1"><s:message code="question.mode.2"/></f:option>
    		<f:option value="3" selected="${bean.mode}" default="1"><s:message code="question.mode.3"/></f:option>
    	</select>
		</td>
    <td class="in-lab" width="15%"><em class="required">*</em><s:message code="question.interval"/>:</td>
    <td class="in-ctt" width="35%">
    	<f:text name="interval" value="${bean.interval}" default="0" class="{required:true,digits:true,min:0}" maxlength="9" style="width:180px;"/><span class="in-prompt" title="<s:message code='question.interval.prompt'/>">&nbsp;</span>
    </td>
  </tr>
  <tr>
    <td class="in-lab" width="15%"><s:message code="question.creationDate"/>:</td>
    <td class="in-ctt" width="35%"><input type="text" name="creationDate" value="<c:if test="${oprt=='edit'}"><fmt:formatDate value="${bean.creationDate}" pattern="yyyy-MM-dd'T'HH:mm:ss"/></c:if>" onclick="WdatePicker({dateFmt:'yyyy-MM-ddTHH:mm:ss'});" class="${oprt=='edit' ? 'required' : ''}" style="width:180px;"/></td>
    <td class="in-lab" width="15%"><s:message code="question.total"/>:</td>
    <td class="in-ctt" width="35%"><f:text name="total" value="${oprt=='edit' ? (bean.total) : ''}" class="disabled" disabled="disabled" maxlength="9" style="width:180px;"/></td>
  </tr>
</table>

<div class="inls-opt margin-top5">
  <b><s:message code="question.items"/></b> &nbsp;
  <a href="javascript:;" onclick="addRow();" class="ls-opt"><s:message code='addRow'/></a> &nbsp;
  <a href="javascript:;" onclick="Cms.moveTop('itemIds');" class="ls-opt"><s:message code='moveTop'/></a>
  <a href="javascript:;" onclick="Cms.moveUp('itemIds');" class="ls-opt"><s:message code='moveUp'/></a>
  <a href="javascript:;" onclick="Cms.moveDown('itemIds');" class="ls-opt"><s:message code='moveDown'/></a>
  <a href="javascript:;" onclick="Cms.moveBottom('itemIds');" class="ls-opt"><s:message code='moveBottom'/></a>     
</div>
<textarea id="templateArea" style="display:none">
	<tr>
    <td align="center">
    	<input type="checkbox" name="itemIds" value=""/>
    	<input type="hidden" name="dy-itemId-{0}" value=""/>
    </td>
    <td align="center">    	
      <a href="javascript:;" onclick="$(this).parent().parent().remove();" class="ls-opt"><s:message code="delete"/></a>
    </td>
    <td align="center"><f:text name="dy-itemTitle-{0}" value="" class="required" maxlength="150" style="width:300px;"/></td>
    <td align="center"><f:checkbox name="dy-itemEssay-{0}"/></td>
  </tr>
</textarea>
<script type="text/javascript">
var rowIndex = 0;
<c:if test="${!empty bean && fn:length(bean.items) gt 0}">
rowIndex = ${fn:length(bean.items)};
</c:if>
var rowTemplate = $.format($("#templateArea").val());
function addRow() {
	$(rowTemplate(rowIndex++)).appendTo("#pagedTable tbody");
	$("#pagedTable").tableHighlight();
}
$(function() {
	if(rowIndex==0) {
		<c:if test="${oprt=='create'}">
		addRow();
		</c:if>
	}
});
</script>
<table id="pagedTable" border="0" cellpadding="0" cellspacing="0" class="inls-tb">
  <thead>
  <tr>
    <th width="25"><input type="checkbox" onclick="Cms.check('ids',this.checked);"/></th>
    <th width="100"><s:message code="operate"/></th>
    <th><s:message code="questionItem.title"/></th>
    <th><s:message code="questionItem.essay"/></th>
  </tr>
  </thead>
  <tbody>
  <c:forEach var="item" varStatus="status" items="${bean.items}">
  <tr beanid="${item.id}">
    <td align="center">
    	<input type="checkbox" name="itemIds" value="${item.id}"/>
    	<input type="hidden" name="dy-itemId-${status.index}" value="${item.id}"/>
    </td>
    <td align="center">
    	<a id="edit_opt_${item.id}" href="../question_item/edit.do?id=${item.id}&position=${status.index}" class="ls-opt"><s:message code="edit"/></a>
      <a href="javascript:;" onclick="$(this).parent().parent().remove();" class="ls-opt"><s:message code="delete"/></a>
    </td>
    <td align="center"><f:text name="dy-itemTitle-${status.index}" value="${item.title}" class="required" maxlength="150" style="width:300px;"/></td>
    <td align="center"><f:checkbox name="dy-itemEssay-${status.index}" value="${item.essay}"/></td>
  </tr>
  </c:forEach>
  </tbody>
</table>

<table border="0" cellpadding="0" cellspacing="0" class="in-tb margin-top5">
  <tr>
    <td colspan="4" class="in-opt">
      <div class="in-btn"><input type="submit" value="<s:message code="save"/>"/></div>
      <div class="in-btn"><input type="submit" value="<s:message code="saveAndReturn"/>" onclick="$('#redirect').val('list');"/></div>
      <c:if test="${oprt=='create'}">
      <div class="in-btn"><input type="submit" value="<s:message code="saveAndCreate"/>" onclick="$('#redirect').val('create');"/></div>
      </c:if>
      <div style="clear:both;"></div>
    </td>
  </tr>
</table>
</form>
</body>
</html>