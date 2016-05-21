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
	$("#pagedTable").tableHighlight();
	$("#sortHead").headSort();
});
function confirmDelete() {
	return confirm("<s:message code='confirmDelete'/>");
}
function optSingle(opt) {
	if(Cms.checkeds("ids")==0) {
		alert("<s:message code='pleaseSelectRecord'/>");
		return false;
	}
	if(Cms.checkeds("ids")>1) {
		alert("<s:message code='pleaseSelectOne'/>");
		return false;
	}
	var id = $("input[name='ids']:checkbox:checked").val();
	location.href=$(opt+id).attr("href");
}
function optMulti(form, action, msg) {
	if(Cms.checkeds("ids")==0) {
		alert("<s:message code='pleaseSelectRecord'/>");
		return false;
	}
	if(msg && !confirm(msg)) {
		return false;
	}
	form.action=action;
	form.submit();
	return true;
}
function optDelete(form) {
	if(Cms.checkeds("ids")==0) {
		alert("<s:message code='pleaseSelectRecord'/>");
		return false;
	}
	if(!confirmDelete()) {
		return false;
	}
	form.action='delete.do';
	form.submit();
	return true;
}
</script>
</head>
<body class="c-body">
<jsp:include page="/WEB-INF/views/commons/show_message.jsp"/>
<div class="c-bar margin-top5">
  <span class="c-position"><s:message code="attachment.management"/> - <s:message code="list"/></span>
	<span class="c-total">(<s:message code="totalElements" arguments="${pagedList.totalElements}"/>)</span>
</div>
<form action="list.do" method="get">
	<fieldset class="c-fieldset">
    <legend><s:message code="search"/></legend>
    <label class="c-lab"><s:message code="attachment.name"/>: <input type="text" name="search_CONTAIN_name" value="${search_CONTAIN_name[0]}" style="width:120px;"/></label>
    <label class="c-lab"><s:message code="attachment.ftype"/>: <input type="text" name="search_CONTAIN_Jrefs.ftype" value="${requestScope['search_CONTAIN_Jrefs.ftype'][0]}" style="width:70px;"/></label>
    <label class="c-lab"><s:message code="attachment.fid"/>: <input type="text" name="search_EQ_Jrefs.fid" value="${requestScope['search_EQ_Jrefs.fid'][0]}" style="width:50px;"/></label>
    <label class="c-lab"><s:message code="attachment.user"/>: <input type="text" name="search_CONTAIN_Juser.username" value="${requestScope['search_CONTAIN_Juser.username'][0]}" style="width:70px;"/></label>
    <label class="c-lab"><s:message code="attachment.ip"/>: <input type="text" name="search_CONTAIN_ip" value="${search_CONTAIN_ip[0]}" style="width:70px;"/></label>
    <label class="c-lab"><s:message code="beginTime"/>: <f:text name="search_GTE_time_Date" value="${search_GTE_time_Date[0]}" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});" style="width:70px;"/></label>
    <label class="c-lab"><s:message code="endTime"/>: <f:text name="search_LTE_time_Date" value="${search_LTE_time_Date[0]}" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});" style="width:70px;"/></label>
    <label class="c-lab">
      <label><input type="checkbox" name="queryUsed" value="true" <c:if test="${queryUsed eq 'true'}"> checked="checked"</c:if>/><s:message code="attachment.notUsed"/></label>
    </label>
	  <label class="c-lab"><input type="submit" value="<s:message code="search"/>"/></label>
  </fieldset>
</form>
<form method="post">
<tags:search_params/>
<input type="hidden" name="queryUsed" value="${queryUsed}"/>
<div class="ls-bc-opt">
	<shiro:hasPermission name="core:attachment:delete">
	<div class="ls-btn"><input type="button" value="<s:message code="delete"/>" onclick="return optDelete(this.form);"/></div>
	</shiro:hasPermission>
	<div style="clear:both"></div>
</div>
<table id="pagedTable" border="0" cellpadding="0" cellspacing="0" class="ls-tb margin-top5">
  <thead id="sortHead" pagesort="<c:out value='${page_sort[0]}' />" pagedir="${page_sort_dir[0]}" pageurl="list.do?page_sort={0}&page_sort_dir={1}&${searchstringnosort}">
  <tr class="ls_table_th">
    <th width="25"><input type="checkbox" onclick="Cms.check('ids',this.checked);"/></th>
    <th width="50"><s:message code="operate"/></th>
    <th width="30" class="ls-th-sort"><span class="ls-sort" pagesort="id">ID</span></th>
    <th class="ls-th-sort"><span class="ls-sort" pagesort="name"><s:message code="attachment.name"/></span></th>
    <th class="ls-th-sort"><span class="ls-sort" pagesort="refs.ftype"><s:message code="attachment.ftype"/></span></th>
    <th class="ls-th-sort"><span class="ls-sort" pagesort="length"><s:message code="attachment.size"/></span></th>
    <th class="ls-th-sort"><span class="ls-sort" pagesort="user.username"><s:message code="attachment.user"/></span></th>
    <th class="ls-th-sort"><span class="ls-sort" pagesort="time"><s:message code="attachment.time"/></span></th>
    <th class="ls-th-sort"><span class="ls-sort" pagesort="ip"><s:message code="attachment.ip"/></span></th>
    <th class="ls-th-sort"><span class="ls-sort" pagesort="used"><s:message code="attachment.used"/></span></th>
  </tr>
  </thead>
  <tbody>
  <c:forEach var="bean" varStatus="status" items="${pagedList.content}">
  <tr beanid="${bean.id}">
    <td><input type="checkbox" name="ids" value="${bean.id}"<c:if test="${bean.used}"> disabled="disabeld"</c:if>/></td>
    <td align="center">
    	<shiro:hasPermission name="core:attachment:delete">
    	<c:choose>
    	<c:when test="${!bean.used}">
      <a href="delete.do?ids=${bean.id}&queryUsed=${queryUsed}&${searchstring}" onclick="return confirmDelete();" class="ls-opt"><s:message code="delete"/></a>
      </c:when>
      <c:otherwise>
      <span class="disabled"><s:message code="delete"/></span>
      </c:otherwise>
      </c:choose>
      </shiro:hasPermission>
     </td>
    <td><c:out value="${bean.id}"/></td>
    <td><c:out value="${bean.name}"/></td>
    <td>
      <c:forEach var="ref" items="${bean.refs}">
      <div>${ref.ftype}(${ref.fid})</div>
      </c:forEach>
    </td>
    <td align="right"><c:out value="${bean.size}"/></td>
    <td align="right"><c:out value="${bean.user.username}"/></td>
    <td align="center"><fmt:formatDate value="${bean.time}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
    <td><c:out value="${bean.ip}"/></td>
    <td align="center">
      <c:choose>
        <c:when test="${bean.used}">
          <span><s:message code="yes"/></span>
        </c:when>
        <c:otherwise>        
          <span style="color:red;font-weight:bold;"><s:message code="no"/></span>
        </c:otherwise>
      </c:choose>
    </td>
  </tr>
  </c:forEach>
  </tbody>
</table>
<c:if test="${fn:length(pagedList.content) le 0}"> 
<div class="ls-norecord margin-top5"><s:message code="recordNotFound"/></div>
</c:if>
</form>
<form action="list.do" method="get" class="ls-page">
	<tags:search_params excludePage="true"/>
  <tags:pagination pagedList="${pagedList}"/>
</form>
</body>
</html>