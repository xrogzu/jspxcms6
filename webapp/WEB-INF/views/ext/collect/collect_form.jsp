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
	$("#validForm").validate();
	$("input[name='name']").focus();
});
function confirmDelete() {
	return confirm("<s:message code='confirmDelete'/>");
}
function patternDialog(itemId, areaId) {
	var pageBegin = $("#pageBegin").val();
	var pageEnd = $("#pageEnd").val();
	var listPattern = $("#listPattern").val();
	var charset = $("#charset").val();
  var userAgent = $("#userAgent").val();
	var desc = $("#desc").prop("checked");
	var url = "list_pattern_dialog.do";
	url += "?listPattern=" + encodeURIComponent(listPattern);
  url += "&charset=" + encodeURIComponent(charset);
  url += "&userAgent=" + encodeURIComponent(userAgent);
  url += "&pageBegin=" + encodeURIComponent(pageBegin);
  url += "&pageEnd=" + encodeURIComponent(pageEnd);
  url += "&desc=" + encodeURIComponent(desc);
  url += "&itemId=" + encodeURIComponent(itemId);
  if(areaId) {
	  url += "&areaId=" + encodeURIComponent(areaId);
  }
	window.open(url, "pattern_dialog", "height=667, width=1000, top=0, left=0, toolbar=no, menubar=no, scrollbars=auto, resizable=yes, location=no, status=no");
}
</script>
</head>
<body class="c-body">
<jsp:include page="/WEB-INF/views/commons/show_message.jsp"/>
<div class="c-bar margin-top5">
  <span class="c-position"><s:message code="collect.management"/> - <s:message code="${oprt=='edit' ? 'edit' : 'create'}"/></span>
</div>
<form id="validForm" action="${oprt=='edit' ? 'update' : 'save'}.do" method="post">
<tags:search_params/>
<f:hidden name="oid" value="${bean.id}"/>
<f:hidden name="position" value="${position}"/>
<input type="hidden" id="redirect" name="redirect" value="edit"/>
<table border="0" cellpadding="0" cellspacing="0" class="in-tb margin-top5">
  <tr>
    <td colspan="4" class="in-opt">
      <shiro:hasPermission name="ext:collect:create">
      <div class="in-btn"><input type="button" value="<s:message code="create"/>" onclick="location.href='create.do?${searchstring}';"<c:if test="${oprt=='create'}"> disabled="disabled"</c:if>/></div>
      <div class="in-btn"></div>
      </shiro:hasPermission>
      <shiro:hasPermission name="ext:collect_field:list">
      <div class="in-btn"><input type="button" value="<s:message code="collect.fieldList"/>" onclick="location.href='../collect_field/list.do?collectId=${bean.id}&${searchstring}';"<c:if test="${oprt=='create'}"> disabled="disabled"</c:if>/></div>
      <div class="in-btn"></div>
      </shiro:hasPermission>
			<shiro:hasPermission name="ext:collect:copy">
			<div class="in-btn"><input type="button" value="<s:message code="copy"/>" onclick="location.href='create.do?id=${bean.id}&${searchstring}';"<c:if test="${oprt=='create'}"> disabled="disabled"</c:if>/></div>
			</shiro:hasPermission>
			<shiro:hasPermission name="ext:collect:delete">
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
    <td class="in-lab" width="15%"><em class="required">*</em><s:message code="collect.name"/>:</td>
    <td class="in-ctt" width="35%"><f:text name="name" value="${oprt=='edit' ? (bean.name) : ''}" class="required" maxlength="100" style="width:180px;"/></td>
    <td class="in-lab" width="15%"><em class="required">*</em><s:message code="collect.node"/>:</td>    
    <td class="in-ctt" width="35%">	    	
	    <f:hidden id="nodeId" name="nodeId" value="${bean.node.id}"/>
	    <f:hidden id="nodeIdNumber" value="${bean.node.id}"/>
	    <f:text id="nodeIdName" value="${bean.node.displayName}" readonly="readonly" class="required" style="width:160px;"/><input id="nodeIdButton" type="button" value="<s:message code='choose'/>"/>
	    <script type="text/javascript">
	    $(function(){
	    	Cms.f7.nodeInfoPerms("nodeId","nodeIdName",{
	    		settings: {"title": "<s:message code='node.f7.selectNode'/>"},
	    		params: {"isRealNode": true}
	    	});
	    });
	    </script>
		</td>
  </tr>
  <tr>
    <td class="in-lab" width="15%"><em class="required">*</em><s:message code="collect.charset"/>:</td>
    <td class="in-ctt" width="35%">
      <f:text id="charset" name="charset" value="${bean.charset}" default="UTF-8" class="required" maxlength="100" style="width:180px;"/>
      <span class="in-prompt" title="<s:message code='collect.charset.prompt' htmlEscape='true'/>"></span>
    </td>
    <td class="in-lab" width="15%"><em class="required">*</em><s:message code="collect.submit"/>:</td>
    <td class="in-ctt" width="35%">
      <label><f:radio name="submit" value="true" checked="${bean.submit}" default="true"/><s:message code="yes"/></label>
      <label><f:radio name="submit" value="false" checked="${bean.submit}"/><s:message code="no"/></label>
    </td>
  </tr>
  <tr>
    <td class="in-lab" width="15%"><em class="required">*</em><s:message code="collect.interval"/>:</td>
    <td class="in-ctt" width="85%" colspan="3">
      <s:message code="collect.intervalMin"/>: <f:text name="intervalMin" value="${bean.intervalMin}" default="0" class="required digits" style="width:70px;"/> &nbsp;
      <s:message code="collect.intervalMax"/>: <f:text name="intervalMax" value="${bean.intervalMax}" default="0" class="required digits" style="width:70px;"/>
    </td>
  </tr>
  <tr>
    <td class="in-lab" width="15%"><em class="required">*</em><s:message code="collect.userAgent"/>:</td>
    <td class="in-ctt" width="85%" colspan="3">
      <f:text id="userAgent" name="userAgent" value="${bean.userAgent}" default="Mozilla/5.0" class="required" maxlength="255" style="width:500px;"/>
    </td>
  </tr>
  <tr>
    <td class="in-lab" width="15%"><em class="required">*</em><s:message code="collect.listPattern"/>:</td>
    <td class="in-ctt" width="85%" colspan="3">
      <div style="padding-bottom:3px;">
        <s:message code="collect.index"/> &nbsp;
        <s:message code="collect.pageBegin"/>: <f:text id="pageBegin" name="pageBegin" value="${bean.pageBegin}" default="2" class="required digits" style="width:70px;"/>
        <s:message code="collect.pageEnd"/>: <f:text id="pageEnd" name="pageEnd" value="${bean.pageEnd}" default="10" class="required digits" style="width:70px;"/> &nbsp;
        <label><f:checkbox id="desc" name="desc" value="${bean.desc}" default="true"/><s:message code="collect.desc"/></label>
      </div>
      <f:textarea id="listPattern" name="listPattern" value="${bean.listPattern}" class="required" maxlength="2000" style="width:95%;height:120px;" spellcheck="false"/>
    </td>
  </tr>
  <%-- 
  <tr>
    <td class="in-lab" width="15%"><s:message code="collect.listNextPattern"/>:</td>
    <td class="in-ctt" width="85%" colspan="3">
      <div style="padding-bottom:3px;">
        <label><f:checkbox name="listNextReg" value="${bean.listNextReg}"/><s:message code="collect.isReg"/></label>
      </div>
      <f:textarea name="listNextPattern" value="${bean.listNextPattern}" maxlength="255" style="width:95%;height:80px;" spellcheck="false"/>
    </td>
  </tr>
   --%>
  <tr>
    <td class="in-lab" width="15%"><em class="required">*</em><s:message code="collect.itemPattern"/>:</td>
    <td class="in-ctt" width="85%" colspan="3">
      <input type="button" value="<s:message code='set'/>" onclick="patternDialog('item','itemArea');"/>
      <div style="padding:5px 0 3px 0;"><s:message code="collect.areaHtml"/>:</div>
      <f:textarea id="itemAreaPattern" name="itemAreaPattern" value="${bean.itemAreaPattern}" maxlength="255" style="width:95%;height:60px;" spellcheck="false"/>
      <div style="padding:3px 0 5px 0;">
        <label><f:checkbox id="itemAreaReg" name="itemAreaReg" value="${bean.itemAreaReg}"/><s:message code="collect.isReg"/></label> &nbsp;
      </div>
      <div style="border-top:solid 1px #ccc;"></div>
      <div style="padding:5px 0 3px 0;"><s:message code="collect.itemHtml"/>:</div>
      <f:textarea id="itemPattern" name="itemPattern" value="${bean.itemPattern}" maxlength="255" class="required" style="width:95%;height:60px;" spellcheck="false"/>
      <div style="padding:3px 0;">
        <label><f:checkbox id="itemReg" name="itemReg" value="${bean.itemReg}"/><s:message code="collect.isReg"/></label>
      </div>
    </td>
  </tr>
  <%-- 
  <tr>
    <td class="in-lab" width="15%"><s:message code="collect.blockAreaPattern"/>:</td>
    <td class="in-ctt" width="85%" colspan="3">
      <div style="padding-bottom:3px;">
        <label><f:checkbox name="blockAreaReg" value="${bean.blockAreaReg}"/><s:message code="collect.isReg"/></label>
      </div>
      <f:textarea name="blockAreaPattern" value="${bean.blockAreaPattern}" maxlength="255" style="width:95%;height:80px;" spellcheck="false"/>
    </td>
  </tr>
  <tr>
    <td class="in-lab" width="15%"><s:message code="collect.blockPattern"/>:</td>
    <td class="in-ctt" width="85%" colspan="3">
      <div style="padding-bottom:3px;">
        <label><f:checkbox name="blockReg" value="${bean.blockReg}"/><s:message code="collect.isReg"/></label>
      </div>
      <f:textarea name="blockPattern" value="${bean.blockPattern}" maxlength="255" style="width:95%;height:80px;" spellcheck="false"/>
    </td>
  </tr>
   --%>
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