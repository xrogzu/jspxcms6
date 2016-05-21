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
<form id="validForm" action="upload_update.do" method="post">
<table border="0" cellpadding="0" cellspacing="0" class="in-tb margin-top5">
  <tr>
    <td class="in-lab" width="15%"><s:message code="global.upload.fileExtensions"/>:</td>
    <td class="in-ctt" width="85%" colspan="3">
      <f:text name="fileExtensions" value="${bean.upload.fileExtensions}" style="width:450px;"/><span class="in-prompt" title="<s:message code='global.upload.extensions.prompt'/>">&nbsp;</span>
      <s:message code="global.upload.sizeLimit"/>: <f:text name="fileLimit" value="${bean.upload.fileLimit}" class="required digits" style="width:70px;" /> KB
      <span class="in-prompt" title="<s:message code='global.upload.limit.prompt'/>">&nbsp;</span>
    </td>
  </tr>
  <tr>
    <td class="in-lab" width="15%"><s:message code="global.upload.imageExtensions"/>:</td>
    <td class="in-ctt" width="85%" colspan="3">
      <f:text name="imageExtensions" value="${bean.upload.imageExtensions}" style="width:450px;"/><span class="in-prompt" title="<s:message code='global.upload.extensions.prompt'/>">&nbsp;</span>
      <s:message code="global.upload.sizeLimit"/>: <f:text name="imageLimit" value="${bean.upload.imageLimit}" class="required digits" style="width:70px;" /> KB
      <span class="in-prompt" title="<s:message code='global.upload.limit.prompt'/>">&nbsp;</span>
    </td>
  </tr>
  <tr>
    <td class="in-lab" width="15%"><s:message code="global.upload.flashExtensions"/>:</td>
    <td class="in-ctt" width="85%" colspan="3">
      <f:text name="flashExtensions" value="${bean.upload.flashExtensions}" style="width:450px;"/><span class="in-prompt" title="<s:message code='global.upload.extensions.prompt'/>">&nbsp;</span>
      <s:message code="global.upload.sizeLimit"/>: <f:text name="flashLimit" value="${bean.upload.flashLimit}" class="required digits" style="width:70px;" /> KB
      <span class="in-prompt" title="<s:message code='global.upload.limit.prompt'/>">&nbsp;</span>
    </td>
  </tr>
  <tr>
    <td class="in-lab" width="15%"><s:message code="global.upload.videoExtensions"/>:</td>
    <td class="in-ctt" width="85%" colspan="3">
      <f:text name="videoExtensions" value="${bean.upload.videoExtensions}" style="width:450px;"/><span class="in-prompt" title="<s:message code='global.upload.extensions.prompt'/>">&nbsp;</span>
      <s:message code="global.upload.sizeLimit"/>: <f:text name="videoLimit" value="${bean.upload.videoLimit}" class="required digits" style="width:70px;" /> KB
      <span class="in-prompt" title="<s:message code='global.upload.limit.prompt'/>">&nbsp;</span>
    </td>
  </tr>
  <tr>
    <td class="in-lab" width="15%"><s:message code="global.upload.docExtensions"/>:</td>
    <td class="in-ctt" width="85%" colspan="3">
      <f:text name="docExtensions" value="${bean.upload.docExtensions}" style="width:450px;"/><span class="in-prompt" title="<s:message code='global.upload.extensions.prompt'/>">&nbsp;</span>
      <s:message code="global.upload.sizeLimit"/>: <f:text name="docLimit" value="${bean.upload.docLimit}" class="required digits" style="width:70px;" /> KB
      <span class="in-prompt" title="<s:message code='global.upload.limit.prompt'/>">&nbsp;</span>
    </td>
  </tr>
  <tr>
    <td class="in-lab" width="15%"><em class="required">*</em><s:message code="global.upload.imageMaxWidth"/>:</td>
    <td class="in-ctt" width="35%">
    	<f:text name="imageMaxWidth" value="${bean.upload.imageMaxWidth}" class="required digits" style="width:180px;"/>px<span class="in-prompt" title="<s:message code='global.upload.limit.prompt'/>">&nbsp;</span>
     </td>
    <td class="in-lab" width="15%"><em class="required">*</em><s:message code="global.upload.imageMaxHeight"/>:</td>
    <td class="in-ctt" width="35%">
    	<f:text name="imageMaxHeight" value="${bean.upload.imageMaxHeight}" class="required digits" style="width:180px;"/>px<span class="in-prompt" title="<s:message code='global.upload.limit.prompt'/>">&nbsp;</span>
    </td>
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