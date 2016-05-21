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
			<div class="in-btn"><input type="button" value="<s:message code="return"/>" onclick="location.href='list.do?${searchstring}';"/></div>
      <div style="clear:both;"></div>
    </td>
  </tr>
  <tr>
    <td colspan="4" class="in-opt">			
		<h1 style="font-size:16px;padding:20px 0 0 10px;">${bean.title} <span style="font-size:14px;font-weight:normal;">(共<span style="color:red;">${bean.total}</span>人参与)</span></h1>								
		<ul style="font-size:14px;padding:0 10px 10px 10px; line-height:200%;">
		<c:forEach var="item" items="${bean.items}" varStatus="status">	
			<li style="padding:10px 0;">
				${status.count}. ${item.title}
				<ul style="padding-left:20px;">
					<c:forEach var="option" items="${item.options}" varStatus="status">	
						<li>
							<label>
							<c:choose>
							<c:when test="${item.maxSelected==1}">
							<input type="radio" name="optionIds" value="${option.id}"/>
							</c:when>
							<c:otherwise>					
							<input type="checkbox" name="optionIds" value="${option.id}"/>
							</c:otherwise>
							</c:choose>					
							${option.title} <span style="font-size:12px;font-weight:normal;">(<span style="color:red;">${option.count}</span>票)</span>
							</label>
						</li>
					</c:forEach>
					<c:if test="${item.essay}">
					<li>
						<ul>
							<c:forEach var="itemRec" items="${item.itemRecs}" varStatus="status">
							<li>${status.count}. <c:out value="${itemRec.answer}"/></li>
							</c:forEach>
						</ul>
					</li>
					</c:if>
				</ul>
			</li>		
		</c:forEach>																	
		</ul>
    </td>
  </tr>
</table>
</form>
</body>
</html>