<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fnx" uri="http://java.sun.com/jsp/jstl/functionsx"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="f" uri="http://www.jspxcms.com/tags/form"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>Jspxcms管理平台 - Powered by Jspxcms</title>
<jsp:include page="/WEB-INF/views/commons/head.jsp"></jsp:include>
<style type="text/css">
* html{overflow-y: scroll;}
.ztree li span.button.switch.level0 {visibility:hidden; width:1px;}
.ztree li ul.level0 {padding:0; background:none;}
</style>
<script type="text/javascript">
function dblClickExpand(treeId, treeNode) {
  return treeNode.level > 0;
}
function destOnClick(event, treeId, treeNode) {
  var destTree = $.fn.zTree.getZTreeObj("tree");
  destTree.checkNode(treeNode,null,false);
}
var setting = {
  check: {
    enable: true,
    chkStyle: "radio",
    radioType: "all"
  },
  callback: {
    onClick: destOnClick
  },
  view: {
    dblClickExpand: dblClickExpand
  },
  data: {
    simpleData: {
      enable: true
    }
  }
};
var nodes =[
  <c:forEach var="node" items="${nodeList}" varStatus="status">
    {"id":${node.id},"pId":<c:out value="${node.parent.id}" default="null"/>,"name":"${node.name}",<c:choose><c:when test="${empty node.parent}">"open":true</c:when><c:otherwise>"open":${fnx:contains_oxo(selectedPids,node.id)}</c:otherwise></c:choose>}<c:if test="${!status.last}">,</c:if>
  </c:forEach>
];
$(function() {
  var tree = $.fn.zTree.init($("#tree"), setting, nodes);
  $("#validForm").validate();
  $("#validForm").submit(function(){
    var checkedArr = tree.getCheckedNodes(true);
    if(checkedArr.length==0) {
      alert("<s:message code='info.pleaseSelectNode'/>");
      return false;
    }
    $("<input>",{
      "type": "hidden",
      "name": "nodeId",
      "value": checkedArr[0].id
    }).appendTo($(this));
  });
});
</script>
</head>
<body class="c-body">
<jsp:include page="/WEB-INF/views/commons/show_message.jsp"/>
<div class="c-bar margin-top5">
  <span class="c-position"><s:message code="info.management"/> - <s:message code="move"/>
</div>
<form id="validForm" action="move_submit.do" method="post">
<tags:search_params/>
<f:hidden name="queryNodeId" value="${queryNodeId}"/>
<f:hidden name="queryNodeType" value="${queryNodeType}"/>
<f:hidden name="queryInfoPermType" value="${queryInfoPermType}"/>
<f:hidden id="queryStatus" name="queryStatus" value="${queryStatus}"/>
<c:forEach var="id" items="${ids}">
<input type="hidden" name="ids" value="${id}"/>
</c:forEach>
<table border="0" cellpadding="0" cellspacing="0" class="in-tb margin-top5">
  <tr>
    <td colspan="4" class="in-opt">
			<div class="in-btn"><input type="button" value="<s:message code="return"/>" onclick="location.href='list.do?queryNodeId=${queryNodeId}&queryNodeType=${queryNodeType}&queryInfoPermType=${queryInfoPermType}&queryStatus=${queryStatus}&${searchstring}';"/></div>
      <div style="clear:both;"></div>
    </td>
  </tr>
  <tr>
    <th colspan="4" align="center" class="in-ctt"><s:message code="info.destNode"/></th>
  </tr>
  <tr>
    <td colspan="4" valign="top" class="in-ctt">
      <ul id="tree" class="ztree"></ul>
    </td>
  </tr>
  <tr>
    <td colspan="4" class="in-opt">
      <div class="in-btn"><input type="submit" value="<s:message code="save"/>"<c:if test="${oprt=='edit' && !bean.auditPerm}"> disabled="disabled"</c:if>/></div>
      <div style="clear:both;"></div>
    </td>
  </tr>
</table>
</form>
</body>
</html>