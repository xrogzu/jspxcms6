<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<jsp:include page="version.jsp"></jsp:include>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="Cache-Control" content="no-cache, no-store, max-age=0"/>
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="Expires" content="0"/>
<title>Jspxcms 安装向导</title>
<link rel="stylesheet" href="${ctx}/setup/images/style.css" type="text/css"/>
<script type="text/javascript">
	function $(id) {
		return document.getElementById(id);
	}
</script>
</head>
<div class="container">
	<div class="header">
		<h1>安装向导</h1>
		<span>v${version}</span>
	</div>
	<div class="main" style="margin-top:-123px;">
		<div class="licenseblock">
			<p style="color:red;padding-bottom:10px;text-align:center;">环境要求：JDK1.6或更高版本、Tomcat6.0或更高版本、MySQL5.0或更高版本。</p>
<textarea style="width:100%;height:280px;">
<%@ include file="/license.txt"%>
</textarea>
		</div>
		<div class="btnbox marginbot">
			<form action="${ctx}/" method="post">
				<input type="hidden" name="step" value="0">
				<input type="submit" name="submit" value="我同意" style="padding: 2px">&nbsp;
				<input type="button" name="exit" value="我不同意" style="padding: 2px" onclick="javascript: window.close(); return false;">
			</form>
		</div>
		<div class="footer">&copy;2010 - 2016 <a href="http://www.jspxcms.com/">Jspxcms</a></div>
	</div>
</div>
</body>
</html>
