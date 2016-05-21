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
});
function confirmDelete() {
	return confirm("<s:message code='confirmDelete'/>");
}
</script>
</head>
<body class="c-body">
<jsp:include page="/WEB-INF/views/commons/show_message.jsp"/>
<div class="c-bar margin-top5">
  <span class="c-position"><s:message code="questionItem.management"/> - <s:message code="${oprt=='edit' ? 'edit' : 'create'}"/></span>
</div>
<form id="validForm" action="${oprt=='edit' ? 'update' : 'save'}.do" method="post">
<tags:search_params/>
<f:hidden name="oid" value="${bean.id}"/>
<f:hidden name="position" value="${position}"/>
<input type="hidden" id="redirect" name="redirect" value="edit"/>
<table border="0" cellpadding="0" cellspacing="0" class="in-tb margin-top5">
  <tr>
    <td colspan="4" class="in-opt">
    <%--
			<div class="in-btn"><input type="button" value="<s:message code="prev"/>" onclick="location.href='edit.do?id=${side.prev.id}&position=${position-1}';"<c:if test="${empty side.prev}"> disabled="disabled"</c:if>/></div>
			<div class="in-btn"><input type="button" value="<s:message code="next"/>" onclick="location.href='edit.do?id=${side.next.id}&position=${position+1}';"<c:if test="${empty side.next}"> disabled="disabled"</c:if>/></div>
			<div class="in-btn"></div>
			 --%>
			<div class="in-btn"><input type="button" value="<s:message code="return"/>" onclick="location.href='../question/edit.do?id=${question.id}';"/></div>
      <div style="clear:both;"></div>
    </td>
  </tr>
  <tr>
    <td class="in-lab" width="15%"><s:message code="questionItem.question"/>:</td>
    <td class="in-ctt" width="85%" colspan="3"><c:out value="${question.title}"/></td>
  </tr>
  <tr>
    <td class="in-lab" width="15%"><em class="required">*</em><s:message code="questionItem.title"/>:</td>
    <td class="in-ctt" width="85%" colspan="3"><f:text name="title" value="${oprt=='edit' ? (bean.title) : ''}" class="required" maxlength="150" style="width:500px;"/></td>
  </tr>
  <tr>
    <td class="in-lab" width="15%"><s:message code="questionItem.multi"/>:</td>
    <td class="in-ctt" width="35%">
    	<label><f:radio name="maxSelected" value="0" checked="${bean.maxSelected}"/><s:message code="yes"/></label>
    	<label><f:radio name="maxSelected" value="1" checked="${bean.maxSelected}" default="1"/><s:message code="no"/></label>
		</td>
    <td class="in-lab" width="15%"><s:message code="questionItem.essay"/>:</td>
    <td class="in-ctt" width="35%">
    	<label><f:radio name="essay" value="true" checked="${bean.essay}"/><s:message code="yes"/></label>
    	<label><f:radio name="essay" value="false" checked="${bean.essay}" default="false"/><s:message code="no"/></label>
		</td>
  </tr>
</table>

<div class="inls-opt margin-top5">
  <b><s:message code="questionItem.options"/></b> &nbsp;
  <a href="javascript:;" onclick="addRow();" class="ls-opt"><s:message code='addRow'/></a> &nbsp;
  <a href="javascript:;" onclick="Cms.moveTop('itemIds');" class="ls-opt"><s:message code='moveTop'/></a>
  <a href="javascript:;" onclick="Cms.moveUp('itemIds');" class="ls-opt"><s:message code='moveUp'/></a>
  <a href="javascript:;" onclick="Cms.moveDown('itemIds');" class="ls-opt"><s:message code='moveDown'/></a>
  <a href="javascript:;" onclick="Cms.moveBottom('itemIds');" class="ls-opt"><s:message code='moveBottom'/></a>     
</div>
<textarea id="templateArea" style="display:none">
	<tr>
    <td align="center">
    	<input type="checkbox" name="optionIds" value=""/>
    	<input type="hidden" name="dy-optionId-{0}" value=""/>
    </td>
    <td align="center">    	
      <a href="javascript:;" onclick="$(this).parent().parent().remove();" class="ls-opt"><s:message code="delete"/></a>
    </td>
    <td align="center"><f:text name="dy-optionTitle-{0}" value="" class="required" maxlength="150" style="width:300px;"/></td>
  </tr>
</textarea>
<script type="text/javascript">
var rowIndex = 0;
<c:if test="${!empty bean && fn:length(bean.options) gt 0}">
rowIndex = ${fn:length(bean.options)};
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
    <th><s:message code="questionOption.title"/></th>
  </tr>
  </thead>
  <tbody>
  <c:forEach var="option" varStatus="status" items="${bean.options}">
  <tr beanid="${option.id}">
    <td align="center">
    	<input type="checkbox" name="optionIds" value="${option.id}"/>
    	<input type="hidden" name="dy-optionId-${status.index}" value="${option.id}"/>
    </td>
    <td align="center">
      <a href="javascript:;" onclick="$(this).parent().parent().remove();" class="ls-opt"><s:message code="delete"/></a>
    </td>
    <td align="center"><f:text name="dy-optionTitle-${status.index}" value="${option.title}" class="required" maxlength="150" style="width:300px;"/></td>
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