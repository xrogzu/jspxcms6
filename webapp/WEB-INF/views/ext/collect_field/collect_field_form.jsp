<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
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
  $("#pagedTable").tableHighlight();
  $("input[name='control']").change(function(){
    if(this.checked) {
      $(this).parent().parent().find("input,select").not(this).removeAttr("disabled").removeClass("disabled");
    } else {
      $(this).parent().parent().find("input,select").not(this).attr("disabled","disabled").addClass("disabled");
    }
  });
});
function checkControl(name,checked) {
  $("input[name='"+name+"']").each(function() {
    $(this).prop("checked",checked).change();
  });
}
</script>
</head>
<body class="c-body">
<jsp:include page="/WEB-INF/views/commons/show_message.jsp"/>
<div class="c-bar margin-top5">
  <span class="c-position"><s:message code="collect.management"/> - <s:message code="collect.fieldCreate"/> - ${collect.name}</span>
  <span class="c-total">(<s:message code="totalElements" arguments="${fn:length(list)}"/>)</span>
</div>
<div class="ls-bc-opt margin-top5">
  <div id="radio">
  <c:forEach var="type" items="${types}">
    <input type="radio" id="radio_${type}" onclick="location.href='../model/list.do?queryType=${type}&${searchstring}';"<c:if test="${queryType==type}"> checked="checked"</c:if>/><label for="radio_${type}"><s:message code="model.type.${type}"/></label>
  </c:forEach>
  </div>
  <script type="text/javascript">
    $("#radio").buttonset();
  </script>
</div>
<form action="save.do" method="post">
<f:hidden name="collectId" value="${collect.id}"/>
<tags:search_params/>
<div class="ls-bc-opt">
  <div class="ls-btn"><input type="submit" value="<s:message code="save"/>"/></div>
  <div class="ls-btn"></div>
  <div class="in-btn"><input type="button" value="<s:message code="return"/>" onclick="location.href='list.do?collectId=${collect.id}&${searchstring}';"/></div>
  <div style="clear:both"></div>
</div>
<table id="pagedTable" border="0" cellpadding="0" cellspacing="0" class="ls-tb margin-top5">
  <thead>
  <tr class="ls_table_th">
    <th width="25"><input type="checkbox" checked="checked" onclick="checkControl('control',this.checked);"/></th>
    <th><s:message code="modelField.name"/></th>
    <th><s:message code="modelField.label"/></th>
  </tr>
  </thead>
  <tbody>
  <c:forEach var="bean" varStatus="status" items="${list}">
  <tr>
    <td><c:if test="${bean['code'] ne 'title'}"><input type="checkbox" name="control" checked="checked"/></c:if></td>
    <td align="center">
      <f:text name="code" value="${bean['code']}" readonly="readonly" style="width:180px;"/>
      <f:hidden name="type" value="${bean['type']}"/>
    </td>
    <td align="center">
      <c:set var="fieldName" value="${bean['name']}"/>
      <c:if test="${empty fieldName}"><c:set var="fieldName"><s:message code="collectField.${bean['code']}"/></c:set></c:if>
      <f:text name="name" value="${fieldName}" style="width:180px;"/>
    </td>
  </tr>
  </c:forEach>
  </tbody>
</table>
<c:if test="${fn:length(list) le 0}"> 
<div class="ls-norecord margin-top5"><s:message code="recordNotFound"/></div>
</c:if>
</form>
</body>
</html>