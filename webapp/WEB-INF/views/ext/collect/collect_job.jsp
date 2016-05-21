<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fnx" uri="http://java.sun.com/jsp/jstl/functionsx"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="f" uri="http://www.jspxcms.com/tags/form"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
  <tr>
    <td class="in-lab" width="15%"><s:message code="scheduleJob.collectSource"/>:</td>
    <td class="in-ctt" width="85%" colspan="3">
      <select name="data_collectId">
        <c:forEach var="collect" items="${collectList}">
        <f:option value="${collect.id}" selected="${dataMap['collectId']}">${collect.name}</f:option>
        </c:forEach>
      </select>
    </td>
  </tr>