<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="f" uri="http://www.jspxcms.com/tags/form"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>Jspxcms管理平台 - Powered by Jspxcms</title>
<jsp:include page="/WEB-INF/views/commons/head.jsp"></jsp:include>
<style type="text/css">
.line{margin-top:5px;}
.line .label{width:100px;float:left;text-align:right;}
</style>
<script type="text/javascript">
$(function() {
	$("#validForm").validate();
});
</script>
</head>
<body class="c-body">
<jsp:include page="/WEB-INF/views/commons/show_message.jsp"/>
<div class="c-bar margin-top5">
  <span class="c-position"><s:message code="global.configuration"/> - <s:message code="edit"/></span>
</div>
<div class="ls-bc-opt margin-top5">
	 <div id="radio">
		<jsp:include page="types.jsp"/>
	</div>
</div>
<form id="validForm" action="other_update.do" method="post">
<table border="0" cellpadding="0" cellspacing="0" class="in-tb margin-top5">
  <tr>
    <td class="in-lab" width="15%"><em class="required">*</em><s:message code="global.other.bufferNodeViews"/>:</td>
    <td class="in-ctt" width="35%"><f:text name="bufferNodeViews" value="${bean.other.bufferNodeViews}" class="required digits" maxlength="10" style="width:180px;"/></td>
    <td class="in-lab" width="15%"><em class="required">*</em><s:message code="global.other.bufferInfoViews"/>:</td>
    <td class="in-ctt" width="35%"><f:text name="bufferInfoViews" value="${bean.other.bufferInfoViews}" class="required digits" maxlength="10" style="width:180px;"/></td>
  </tr>
  <tr>
    <td class="in-lab" width="15%"><em class="required">*</em><s:message code="global.other.bufferInfoDownloads"/>:</td>
    <td class="in-ctt" width="35%"><f:text name="bufferInfoDownloads" value="${bean.other.bufferInfoDownloads}" class="required digits" maxlength="10" style="width:180px;"/></td>
    <td class="in-lab" width="15%"><em class="required">*</em><s:message code="global.other.bufferInfoDiggs"/>:</td>
    <td class="in-ctt" width="35%"><f:text name="bufferInfoDiggs" value="${bean.other.bufferInfoDiggs}" class="required digits" maxlength="10" style="width:180px;"/></td>
  </tr>
  <tr>
    <td class="in-lab" width="15%"><em class="required">*</em><s:message code="global.other.bufferInfoScore"/>:</td>
    <td class="in-ctt" width="35%"><f:text name="bufferInfoScore" value="${bean.other.bufferInfoScore}" class="required digits" maxlength="10" style="width:180px;"/></td>
    <td class="in-lab" width="15%"><em class="required">*</em><s:message code="global.other.bufferInfoComments"/>:</td>
    <td class="in-ctt" width="35%"><f:text name="bufferInfoComments" value="${bean.other.bufferInfoComments}" class="required digits" maxlength="10" style="width:180px;"/></td>
  </tr>
  <tr>
    <td colspan="4" class="in-opt">
      <div class="in-btn"><input type="submit" value="<s:message code="save"/>"/></div>
    </td>
  </tr>
</table>
</form>
</body>
</html>