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
</style>
<script type="text/javascript">
$(function() {
	$("#validForm").validate({
		submitHandler: function(form) {
			if(!$("#linkCheck").is(":checked")) {
				$("#link").val("");
			}
			form.submit();
		}
	});
	$("input[name='title']").focus();
});
function imgCrop(name) {
	if($("#"+name).val()=="") {alert("<s:message code='noImageToCrop'/>");return;}
	Cms.imgCrop("../../commons/img_area_select.do",name);
}
function confirmDelete() {
	return confirm("<s:message code='confirmDelete'/>");
}
</script>
</head>
<body class="c-body">
<jsp:include page="/WEB-INF/views/commons/show_message.jsp"/>
<div class="c-bar margin-top5">
  <span class="c-position"><s:message code="info.management"/> - <s:message code="${oprt=='edit' ? 'edit' : 'create'}"/><c:if test="${oprt=='edit'}"> <span style="font-weight:normal;font-size:12px;">(<s:message code="info.status"/>: <s:message code="info.status.${bean.status}"/>, ID:${bean.id})</span></c:if></span>
</div>
<form id="validForm" action="${oprt=='edit' ? 'update' : 'save'}.do" method="post">
<tags:search_params/>
<f:hidden name="queryNodeId" value="${queryNodeId}"/>
<f:hidden name="queryNodeType" value="${queryNodeType}"/>
<f:hidden name="queryInfoPermType" value="${queryInfoPermType}"/>
<f:hidden id="queryStatus" name="queryStatus" value="${queryStatus}"/>
<f:hidden name="oid" value="${bean.id}"/>
<f:hidden name="position" value="${position}"/>
<input type="hidden" id="redirect" name="redirect" value="edit"/>
<table border="0" cellpadding="0" cellspacing="0" class="in-tb margin-top5">
  <tr>
    <td colspan="4" class="in-opt">
			<shiro:hasPermission name="core:info:create">
			<div class="in-btn"><input type="button" value="<s:message code="create"/>" onclick="location.href='create.do?queryNodeId=${queryNodeId}&queryNodeType=${queryNodeType}&queryInfoPermType=${queryInfoPermType}&queryStatus=${queryStatus}&${searchstring}';"<c:if test="${oprt=='create'}"> disabled="disabled"</c:if>/></div>
			<div class="in-btn"></div>
			</shiro:hasPermission>
			<shiro:hasPermission name="core:info:copy">
			<div class="in-btn"><input type="button" value="<s:message code="copy"/>" onclick="location.href='create.do?id=${bean.id}&queryNodeId=${queryNodeId}&queryNodeType=${queryNodeType}&queryInfoPermType=${queryInfoPermType}&queryStatus=${queryStatus}&${searchstring}';"<c:if test="${oprt=='create'}"> disabled="disabled"</c:if>/></div>
			</shiro:hasPermission>
      <div class="in-btn"><input type="button" value="<s:message code="view"/>" onclick="location.href='view.do?id=${bean.id}&queryNodeId=${queryNodeId}&queryNodeType=${queryNodeType}&queryInfoPermType=${queryInfoPermType}&queryStatus=${queryStatus}&${searchstring}';"<c:if test="${oprt=='create'}"> disabled="disabled"</c:if>/></div>
      <div class="in-btn"><input type="button" value="<s:message code="info.foreView"/>" onclick="window.open('${bean.url}');"<c:if test="${oprt=='create'||bean.status ne 'A'}"> disabled="disabled"</c:if>/></div>
			<shiro:hasPermission name="core:info:delete">
			<div class="in-btn"><input type="button" value="<s:message code="delete"/>" onclick="if(confirmDelete()){location.href='delete.do?ids=${bean.id}&queryNodeId=${queryNodeId}&queryNodeType=${queryNodeType}&queryInfoPermType=${queryInfoPermType}&queryStatus=${queryStatus}&${searchstring}';}"<c:if test="${oprt=='create' || !bean.auditPerm}"> disabled="disabled"</c:if>/></div>
			</shiro:hasPermission>
			<div class="in-btn"></div>
			<shiro:hasPermission name="core:info:audit_pass">
			<div class="ls-btn"><input type="button" value="<s:message code="info.auditPass"/>" onclick="location.href='audit_pass.do?ids=${bean.id}&redirect=edit&queryNodeId=${queryNodeId}&queryNodeType=${queryNodeType}&queryInfoPermType=${queryInfoPermType}&queryStatus=${queryStatus}&position=${pagedList.number*pagedList.size+status.index}&${searchstring}';"<c:if test="${oprt=='create' || !bean.auditPerm}"> disabled="disabled"</c:if>/></div>
			</shiro:hasPermission>
			<shiro:hasPermission name="core:info:audit_reject">
			<div class="ls-btn"><input type="button" value="<s:message code="info.auditReject"/>" onclick="location.href='audit_reject.do?ids=${bean.id}&redirect=edit&queryNodeId=${queryNodeId}&queryNodeType=${queryNodeType}&queryInfoPermType=${queryInfoPermType}&queryStatus=${queryStatus}&position=${pagedList.number*pagedList.size+status.index}&${searchstring}';"<c:if test="${oprt=='create' || !bean.auditPerm}"> disabled="disabled"</c:if>/></div>
			</shiro:hasPermission>
			<shiro:hasPermission name="core:info:audit_return">
			<div class="ls-btn"><input type="button" value="<s:message code="info.auditReturn"/>" onclick="location.href='audit_return.do?ids=${bean.id}&redirect=edit&queryNodeId=${queryNodeId}&queryNodeType=${queryNodeType}&queryInfoPermType=${queryInfoPermType}&queryStatus=${queryStatus}&position=${pagedList.number*pagedList.size+status.index}&${searchstring}';"<c:if test="${oprt=='create' || !bean.auditPerm}"> disabled="disabled"</c:if>/></div>
			</shiro:hasPermission>
			<div class="in-btn"></div>
			<div class="in-btn"><input type="button" value="<s:message code="prev"/>" onclick="location.href='edit.do?id=${side.prev.id}&queryNodeId=${queryNodeId}&queryNodeType=${queryNodeType}&queryInfoPermType=${queryInfoPermType}&queryStatus=${queryStatus}&position=${position-1}&${searchstring}';"<c:if test="${empty side.prev}"> disabled="disabled"</c:if>/></div>
			<div class="in-btn"><input type="button" value="<s:message code="next"/>" onclick="location.href='edit.do?id=${side.next.id}&queryNodeId=${queryNodeId}&queryNodeType=${queryNodeType}&queryInfoPermType=${queryInfoPermType}&queryStatus=${queryStatus}&position=${position+1}&${searchstring}';"<c:if test="${empty side.next}"> disabled="disabled"</c:if>/></div>
			<div class="in-btn"></div>
			<div class="in-btn"><input type="button" value="<s:message code="return"/>" onclick="location.href='list.do?queryNodeId=${queryNodeId}&queryNodeType=${queryNodeType}&queryInfoPermType=${queryInfoPermType}&queryStatus=${queryStatus}&${searchstring}';"/></div>
      <div style="clear:both;"></div>
    </td>
  </tr>
	<c:set var="colCount" value="${0}"/>
  <c:forEach var="field" items="${model.normalFields}">
  <c:if test="${colCount%2==0||!field.dblColumn}"><tr></c:if>
  <td class="in-lab" width="15%"><c:if test="${field.required}"><em class="required">*</em></c:if><c:out value="${field.label}"/>:</td>
  <td<c:if test="${field.type!=50}"> class="in-ctt"</c:if><c:choose><c:when test="${field.dblColumn}"> width="35%"</c:when><c:otherwise> width="85%" colspan="3"</c:otherwise></c:choose>>
  <c:choose>
  <c:when test="${field.custom || field.innerType == 3}">
  	<tags:feild_custom bean="${bean}" field="${field}"/>
  </c:when>
  <c:otherwise>
  <c:choose>
  <c:when test="${field.name eq 'node'}">
    <f:hidden id="nodeId" name="nodeId" value="${node.id}"/>
    <f:hidden id="nodeIdNumber" value="${node.id}"/>
    <f:text id="nodeIdName" value="${node.displayName}" readonly="readonly" style="width:160px;"/><input id="nodeIdButton" type="button" value="<s:message code='choose'/>"/>
    <script type="text/javascript">
    $(function(){
    	Cms.f7.nodeInfoPerms("nodeId","nodeIdName",{
    		settings: {"title": "<s:message code='info.pleaseSelectNode'/>"},
    		params: {"isRealNode": true}
    	});
    });
    </script>
  </c:when>
  <c:when test="${field.name eq 'nodes'}">
  	<div id="nodeIds">
  	<c:set var="nodes" value="${bean.nodesExcludeMain}"/>
  	<c:forEach var="n" items="${nodes}">
  		<f:hidden name="nodeIds" value="${n.id}"/>
  	</c:forEach>
  	</div>
  	<div id="nodeIdsNumber">
  	<c:forEach var="n" items="${nodes}">
  		<f:hidden name="nodeIdsNumber" value="${n.id}"/>
  	</c:forEach>
  	</div>
  	<div id="nodeIdsName">
  	<c:forEach var="n" items="${nodes}">
  		<f:hidden name="nodeIdsName" value="${n.displayName}"/>
  	</c:forEach>
  	</div>
    <f:text id="nodeIdsNameDisplay" value="" readonly="readonly" style="width:160px;"/><input id="nodeIdsButton" type="button" value="<s:message code='choose'/>"/>
    <script type="text/javascript">
    $(function(){
    	Cms.f7.nodeMultiInfoPerms("nodeIds",{
    		settings: {"title": "<s:message code='info.pleaseSelectNodes'/>"},
    		params: {"isRealNode": true}
    	});
    });
    </script>
  </c:when>
  <c:when test="${field.name eq 'specials'}">
  	<div id="specialIds">
  	<c:forEach var="s" items="${bean.specials}">
  		<f:hidden name="specialIds" value="${s.id}"/>
  	</c:forEach>
  	</div>
  	<div id="specialIdsNumber">
  	<c:forEach var="s" items="${bean.specials}">
  		<f:hidden name="specialIdsNumber" value="${s.id}"/>
  	</c:forEach>
  	</div>
  	<div id="specialIdsName">
  	<c:forEach var="s" items="${bean.specials}">
  		<f:hidden name="specialIdsName" value="${s.title}"/>
  	</c:forEach>
  	</div>
    <f:text id="specialIdsNameDisplay" value="" readonly="readonly" style="width:160px;"/><input id="specialIdsButton" type="button" value="<s:message code='choose'/>"/>
    <script type="text/javascript">
    $(function(){
    	Cms.f7.specialMulti("specialIds",{
    		settings: {"title": "<s:message code='info.pleaseSelectSpecials'/>"}
    	});
    });
    </script>
  </c:when>
  <c:when test="${field.name eq 'title'}">
    <div>
      <c:set var="style_width">width:<c:out value="${field.customs['width']}" default="500"/>px;</c:set>
	    <f:text name="title" value="${bean.title}" class="required" maxlength="150" style="${style_width}"/>
	    <label><input id="linkCheck" type="checkbox" onclick="$('#linkDiv').toggle(this.checked);"<c:if test="${bean.linked}"> checked="checked"</c:if>/><s:message code="info.link"/></label>
	  </div>
    <div id="linkDiv" style="padding-top:2px;<c:if test="${!bean.linked}">display:none;</c:if>">
    	<f:text id="link" name="link" value="${bean.link}" maxlength="255" style="width:500px;" /><span class="in-prompt" title="<s:message code='info.link.prompt' htmlEscape='true'/>"></span>
  	</div>
  </c:when>
  <c:when test="${field.name eq 'color'}">
    <f:text id="color" name="color" value="${bean.color}" maxlength="50" style="width:70px;"/>
    <script type="text/javascript">
    	$(function() {
    		$("#color").colorPicker();
    	});
    </script>
    <label><f:checkbox name="strong" value="${bean.strong}"/><s:message code="info.strong"/></label>
    <label><f:checkbox name="em" value="${bean.em}"/><s:message code="info.em"/></label>
  </c:when>
  <c:when test="${field.name eq 'subtitle'}">
    <f:text name="subtitle" value="${bean.subtitle}" maxlength="150" style="width:500px;"/>
  </c:when>
  <c:when test="${field.name eq 'fullTitle'}">
    <f:textarea name="fullTitle" value="${bean.fullTitle}" maxlength="150" style="width:500px;height:46px" spellcheck="false"/>
  </c:when>
  <c:when test="${field.name eq 'tagKeywords'}">
    <f:text name="tagKeywords" value="${bean.tagKeywords}" maxlength="150" style="width:500px;"/>
    <%-- <input type="button" value="<s:message code='info.getTagKeywords'/>" onclick="var button=this;$(button).prop('disabled',true);$.get('get_keywords.do',{title:$('input[name=title]').val()},function(data){$('input[name=tagKeywords]').val(data);$(button).prop('disabled',false);})"/> --%>
  </c:when>
  <c:when test="${field.name eq 'metaDescription'}">
  	<f:hidden name="remainDescription" value="true"/>
    <f:textarea name="metaDescription" value="${bean.metaDescription}" class="{maxlength:450}" style="width:500px;height:80px;"/>
  </c:when>
  <c:when test="${field.name eq 'priority'}">
		<select name="priority" style="width:50px;">
  		<c:forEach var="i" begin="0" end="9">
  		<option<c:if test="${i==bean.priority}"> selected="selected"</c:if>>${i}</option>
  		</c:forEach>
  	</select>
  </c:when>
  <c:when test="${field.name eq 'publishDate'}">
    <input type="text" name="publishDate" value="<c:if test="${oprt=='edit'}"><fmt:formatDate value="${bean.publishDate}" pattern="yyyy-MM-dd'T'HH:mm:ss"/></c:if>" onclick="WdatePicker({dateFmt:'yyyy-MM-ddTHH:mm:ss'});" class="${oprt=='edit' ? 'required' : ''}" style="width:120px;"/>
    <s:message code="info.to"/>
    <input type="text" name="offDate" value="<c:if test="${oprt=='edit'}"><fmt:formatDate value="${bean.offDate}" pattern="yyyy-MM-dd'T'HH:mm:ss"/></c:if>" onclick="WdatePicker({dateFmt:'yyyy-MM-ddTHH:mm:ss'});" class="" style="width:120px;"/>    
  </c:when>
  <c:when test="${field.name eq 'infoPath'}">
    <f:text name="infoPath" value="${bean.infoPath}" maxlength="255" style="width:180px;"/>
  </c:when>
  <c:when test="${field.name eq 'infoTemplate'}">
    <f:text id="infoTemplate" name="infoTemplate" value="${bean.infoTemplate}" maxlength="255" style="width:160px;"/><input id="infoTemplateButton" type="button" value="<s:message code='choose'/>"/>
    <script type="text/javascript">
    $(function(){
    	Cms.f7.template("infoTemplate",{
    		settings: {"title": "<s:message code="webFile.chooseTemplate"/>"}
    	});
    });
    </script>
  </c:when>
  <c:when test="${field.name eq 'source'}">
    <f:text name="source" value="${bean.source}" maxlength="50" style="width:100px;"/>
    <s:message code="info.source.url"/>:<f:text name="sourceUrl" value="${bean.sourceUrl}" maxlength="255" style="width:100px;"/>
  </c:when>
  <c:when test="${field.name eq 'author'}">
    <f:text name="author" value="${bean.author}" maxlength="50" style="width:180px;"/>
  </c:when>
  <c:when test="${field.name eq 'allowComment'}">
    <select name="allowComment">
    	<option value=""><s:message code="defaultSelect"/></option>
    	<f:option value="true" selected="${bean.allowComment}"><s:message code="yes"/></f:option>
    	<f:option value="false" selected="${bean.allowComment}"><s:message code="no"/></f:option>
    </select>
  </c:when>
  <c:when test="${field.name eq 'viewGroups'}">
  	<s:message code="info.groups"/>:
  	<f:checkboxes name="viewGroupIds" checked="${bean.viewGroups}" items="${groupList}" itemValue="id" itemLabel="name"/>
  	<div id="viewOrgIds">
	  	<c:set var="viewOrgs" value="${bean.viewOrgs}"/>
	  	<c:forEach var="n" items="${viewOrgs}">
	  		<f:hidden name="viewOrgIds" value="${n.id}"/>
	  	</c:forEach>
  	</div>
  	<div id="viewOrgIdsNumber">
	  	<c:forEach var="n" items="${viewOrgs}">
	  		<f:hidden name="viewOrgIdsNumber" value="${n.id}"/>
	  	</c:forEach>
  	</div>
  	<div id="viewOrgIdsName">
	  	<c:forEach var="n" items="${viewOrgs}">
	  		<f:hidden name="viewOrgIdsName" value="${n.displayName}"/>
	  	</c:forEach>
	  </div>
  	<s:message code="info.orgs"/>:
    <f:text id="viewOrgIdsNameDisplay" value="" readonly="readonly" style="width:160px;"/><input id="viewOrgIdsButton" type="button" value="<s:message code='choose'/>"/>
    <script type="text/javascript">
    $(function(){
    	Cms.f7.orgMulti("viewOrgIds",{
    		settings: {"title": "<s:message code='org.f7.selectOrg'/>"},
    		params: {"treeNumber": "${orgTreeNumber}"}
    	});
    });
    </script>
  </c:when>
  <c:when test="${field.name eq 'attributes'}">
  	<c:set var="attrs" value="${bean.attrs}"/>
  	<c:forEach var="attr" items="${attrList}">
  	<label><input type="checkbox" name="attrIds" value="${attr.id}" onclick="$('#attr_img_${attr.id}').toggle(this.checked);"<c:if test="${fnx:contains_co(attrs,attr)}"> checked="checked"</c:if>/><c:out value="${attr.name}"/>(<c:out value="${attr.number}"/>)</label> &nbsp;
  	</c:forEach>
    <c:forEach var="attr" items="${attrList}">
    <c:if test="${attr.withImage}">
    	</td>
    </tr>
    <tr id="attr_img_${attr.id}"<c:if test="${!fnx:contains_co(attrs,attr)}"> style="display:none;"</c:if>>
    	<td class="in-lab" width="15%">
    		<em class="required">*</em>${attr.name}
    	</td>
    	<td class="in-ctt" width="85%" colspan="3">
    		<tags:image_upload name="attrImages_${attr.id}" value="${fnx:invoke1(bean,'getInfoAttr',attr).image}" required="true" scale="${attr.scale}" exact="${attr.exact}" watermark="${attr.watermark}" width="${attr.imageWidth}" height="${attr.imageHeight}"/>
    </c:if>
    </c:forEach>
  </c:when>
  <c:when test="${field.name eq 'smallImage'}">
    <tags:image_upload name="smallImage" value="${bean.smallImage}" width="${field.customs['imageWidth']}" height="${field.customs['imageHeight']}" watermark="${field.customs['imageWatermark']}" scale="${field.customs['imageScale']}"/>
  </c:when>
  <c:when test="${field.name eq 'largeImage'}">
    <tags:image_upload name="largeImage" value="${bean.largeImage}" width="${field.customs['imageWidth']}" height="${field.customs['imageHeight']}" watermark="${field.customs['imageWatermark']}" scale="${field.customs['imageScale']}"/>
  </c:when>
  <c:when test="${field.name eq 'file'}">
    <div>
      <span style="padding:0 7px;"><s:message code="fileName"/>:</span><f:text id="fileName" name="fileName" value="${bean.fileName}" maxlength="255" style="width:460px;"/>
    </div>
    <div style="padding-top:3px;">
      <span style="padding:0 7px;"><s:message code="fileUrl"/>:</span><f:text id="file" name="file" value="${bean.file}" maxlength="255" style="width:460px;"/>
    </div>
    <div style="padding-top:3px;">
      <span style="padding:0 7px;"><s:message code="fileLength"/>:</span><f:text id="fileLength" name="fileLength" value="${bean.fileLength}" class="{digits:true,max:2147483647}" maxlength="10" style="width:70px;"/>
      <input id="fileButton" type="button" value="<s:message code='choose'/>"/>
      <script type="text/javascript">
      $(function() {
        Cms.f7.uploads("file","fileName",{
          settings: {"title": "<s:message code="webFile.chooseUploads"/>"}
        });
      });
      </script>
      <span id="fileSwfButton"></span><input type="button" value="<s:message code="upload"/>" class="swfbutton"/>
      <input id="fileSwfCancel" type="button" value="<s:message code="cancel"/>" onclick="fileSwfUpload.cancelQueue();" disabled="disabled"/>
      <script type="text/javascript">
      var fileSwfUpload = Cms.swfUploadFile("file",{
        jsessionid: "<%=request.getSession().getId()%>",
    	  file_size_limit: "${GLOBAL.upload.fileLimit}",
    	  file_types: "${GLOBAL.upload.fileTypes}"
      });
      </script>
    </div>
    <div id="fileSwfProgress"></div>
  </c:when>
  <c:when test="${field.name eq 'video'}">
    <div>
      <span style="padding:0 7px;"><s:message code="fileName"/>:</span><f:text id="videoName" name="videoName" value="${bean.videoName}" maxlength="255" style="width:460px;"/>
    </div>
    <div style="padding-top:3px;">
      <span style="padding:0 7px;"><s:message code="fileUrl"/>:</span><f:text id="video" name="video" value="${bean.video}" maxlength="255" style="width:460px;"/>
    </div>
    <div style="padding-top:3px;">
      <span style="padding:0 7px;"><s:message code="fileLength"/>:</span><f:text id="videoLength" name="videoLength" value="${bean.videoLength}" class="{digits:true,max:2147483647}" maxlength="10" style="width:70px;"/>
      <span style="padding:0 7px;"><s:message code="videoTime"/>:</span><f:text id="videoTime" name="videoTime" value="${bean.videoTime}" maxlength="100" style="width:70px;"/> &nbsp;
      <input id="videoButton" type="button" value="<s:message code='choose'/>"/>
      <script type="text/javascript">
      $(function() {
        Cms.f7.uploads("video","videoName",{
          settings: {"title": "<s:message code="webFile.chooseUploads"/>"}
        });
      });
      </script>
      <span id="videoSwfButton"></span><input type="button" value="<s:message code="upload"/>" class="swfbutton"/>
      <input id="videoSwfCancel" type="button" value="<s:message code="cancel"/>" onclick="fileSwfUpload.cancelQueue();" disabled="disabled"/>
      <script type="text/javascript">
      var videoSwfUpload = Cms.swfUploadVideo("video",{
        jsessionid: "<%=request.getSession().getId()%>",
        file_size_limit: "${GLOBAL.upload.videoLimit}",
        file_types: "${GLOBAL.upload.videoTypes}"
      });
      </script>
      <div id="videoSwfProgress"></div>
    </div>
  </c:when>
  <c:when test="${field.name eq 'doc'}">
    <div>
      <span style="padding:0 7px;"><s:message code="fileName"/>:</span><f:text id="docName" name="docName" value="${bean.docName}" maxlength="255" style="width:460px;"/>
    </div>
    <div style="padding-top:3px;">
      <span style="padding:0 7px;"><s:message code="fileUrl"/>:</span><f:text id="doc" name="doc" value="${bean.doc}" maxlength="255" style="width:460px;"/>
    </div>
    <div style="padding-top:3px;">
      <span style="padding:0 7px;">PDF:</span><f:text id="docPdf" name="docPdf" value="${bean.docPdf}" maxlength="255" style="width:460px;"/>
    </div>
    <div style="padding-top:3px;">
      <span style="padding:0 7px;">SWF:</span><f:text id="docSwf" name="docSwf" value="${bean.docSwf}" maxlength="255" style="width:460px;"/>
    </div>
    <div style="padding-top:3px;">
      <span style="padding:0 7px;"><s:message code="fileLength"/>:</span><f:text id="docLength" name="docLength" value="${bean.docLength}" class="{digits:true,max:2147483647}" maxlength="10" style="width:70px;"/>
      <c:choose>
      <c:when test="${GLOBAL.docEnabled}">
      <span id="docSwfButton"></span><input type="button" value="<s:message code="upload"/>" class="swfbutton"/>
      <input id="docSwfCancel" type="button" value="<s:message code="cancel"/>" onclick="fileSwfUpload.cancelQueue();" disabled="disabled"/>
      <script type="text/javascript">
      var docSwfUpload = Cms.swfUploadDoc("doc",{
        jsessionid: "<%=request.getSession().getId()%>",
        file_size_limit: "${GLOBAL.upload.docLimit}",
        file_types: "${GLOBAL.upload.docTypes}"
      });
      </script>
      <div id="docSwfProgress"></div>
      </c:when>
      <c:otherwise>
      <span>文库功能未开启</span>
      </c:otherwise>
      </c:choose>
    </div>
  </c:when>
  <c:when test="${field.name eq 'files'}">
    <textarea id="filesTemplateArea" style="display:none;">
      <table width="100%" border="0" cellpadding="0" cellspacing="0" style="margin:5px 0;border-top:1px solid #ccc;">
        <tbody>
        <tr>
          <td>
            <div style="padding-top:3px;">
              <span style="padding:0 7px;"><s:message code="fileName"/>:</span><f:text id="files{0}Name" name="filesName" value="{2}" maxlength="255" style="width:460px;"/>
            </div>
            <div style="padding-top:3px;">
              <span style="padding:0 7px;"><s:message code="fileUrl"/>:</span><f:text id="files{0}" name="filesFile" value="{1}" maxlength="255" style="width:460px;"/>
            </div>
            <div style="padding-top:3px;">
              <span style="padding:0 7px;"><s:message code="fileLength"/>:</span><f:text id="files{0}Length" name="filesLength" value="{3}" class="{digits:true,max:2147483647}" maxlength="10" style="width:80px;"/>
              <input id="files{0}Button" type="button" value="<s:message code='choose'/>"/>
              <script type="text/javascript">
				      $(function() {
				        Cms.f7.uploads("files{0}","files{0}Name",{
				          settings: {"title": "<s:message code="webFile.chooseUploads"/>"}
				        });
				      });
				      </script>
            </div>
          </td>
          <td width="10%" align="center">
            <div><input type="button" value="<s:message code='delete'/>" onclick="filesRemove(this);"></div>
            <div><input type="button" value="<s:message code='moveUp'/>" onclick="filesMoveUp(this);"></div>
            <div><input type="button" value="<s:message code='moveDown'/>" onclick="filesMoveDown(this);"></div>
          </td>
        </tr>
        </tbody>
      </table>
    </textarea>  
    <script type="text/javascript">
    var fileRowIndex = 0;
    var fileRowTemplate = $.format($("#filesTemplateArea").val());
    function addFileRow(url, name, length) {
      $(fileRowTemplate(fileRowIndex++, url, name, length)).appendTo("#filesContainer");
    }
    $(function() {
        <c:forEach var="item" items="${bean.files}" varStatus="status">
        addFileRow("${fnx:escapeEcmaScript(item.file)}","${fnx:escapeEcmaScript(item.name)}","${fnx:escapeEcmaScript(item.length)}");
        </c:forEach>
    });
    function filesRemove(button) {
      var toMove = $(button).parent().parent().parent().parent().parent();
      toMove.remove();
    }
    function filesMoveUp(button) {
      var toMove = $(button).parent().parent().parent().parent().parent();
      toMove.prev().before(toMove);
    }
    function filesMoveDown(button) {
      var toMove = $(button).parent().parent().parent().parent().parent();
      toMove.next().after(toMove);
    }
    </script>
    <span id="filesSwfButton"></span><input type="button" value="<s:message code="upload"/>" class="swfbutton"/>
    <input id="filesSwfCancel" type="button" value="<s:message code="cancel"/>" onclick="filesSwfUpload.cancelQueue();" disabled="disabled"/>
    <input type="button" value="<s:message code='addRow'/>" onclick="addFileRow('','','');"/>
    <script type="text/javascript">
    var filesSwfUpload = Cms.swfUploadFiles("files",{
      jsessionid: "<%=request.getSession().getId()%>",
      file_size_limit: "${GLOBAL.upload.fileLimit}",
      file_types: "${GLOBAL.upload.fileTypes}"
    },addFileRow);
    </script>
    <div id="filesSwfProgress"></div>
    <div id="filesContainer"></div>
  </c:when>
  <c:when test="${field.name eq 'images'}">
  	<textarea id="imagesTemplateArea" style="display:none;">
		<table width="100%" border="0" cellpadding="0" cellspacing="0" style="margin:5px 0;border-top:1px solid #ccc;">
			<tbody>
			<tr>
				<td>
		  		<div style="margin-top:2px;">
		  		  <input type="hidden" name="imagesName"/>
            &lt;textarea id="imagesText{0}" name="imagesText" style="width:98%;height:45px;"&gt;{2}&lt;/textarea&gt;
          </div>
          <div style="margin-top:2px;">
		  			<f:text id="imagesImage{0}" name="imagesImage" value="{1}" onchange="fn_imagesImage{0}(this.value);" style="width:98%;"/>
		  		</div>
          <div style="margin-top:2px;">
            <s:message code="width"/>: <f:text id="w_imagesImage{0}" value="${field.customs['imageWidth']}" default="1500" style="width:70px;"/> &nbsp;
            <s:message code="height"/>: <f:text id="h_imagesImage{0}" value="${field.customs['imageHeight']}" default="" style="width:70px;"/> &nbsp;
            <input type="button" onclick="imgCrop('imagesImage{0}');" value="<s:message code='crop'/>"/> &nbsp;
            <input id="imagesImage{0}Button" type="button" value="<s:message code='choose'/>"/>
            <script type="text/javascript">
            $(function() {
              Cms.f7.uploads("imagesImage{0}","imagesText{0}",{
                settings: {"title": "<s:message code="webFile.chooseUploads"/>"},
                callbacks: {"ok": function(src){
                	fn_imagesImage{0}(src);
                }}
              });
            });
            </script>
          </div>
		    </td>
		    <td width="210" align="center" valign="middle">
					<img id="img_imagesImage{0}" style="display:none;"/>
				  <script type="text/javascript">
				    function fn_imagesImage{0}(src) {
				    	Cms.scaleImg("img_imagesImage{0}",200,100,src);
				    };
				    fn_imagesImage{0}("{1}");
				  </script>
				</td>
				<td width="80" align="center">
					<div><input type="button" value="<s:message code='delete'/>" onclick="imagesRemove(this);"></div>
					<div><input type="button" value="<s:message code='moveUp'/>" onclick="imagesMoveUp(this);"></div>
					<div><input type="button" value="<s:message code='moveDown'/>" onclick="imagesMoveDown(this);"></div>
          <div><input type="button" value="<s:message code='addRow'/>" onclick="addImageRow('','',this);"/></div>
				</td>
			</tr>
			</tbody>
		</table>
		</textarea>
		<script type="text/javascript">
		var imageRowIndex = 0;
		var imageRowTemplate = $.format($("#imagesTemplateArea").val());
		function addImageRow(image,text,button) {
			if(button) {
				$(imageRowTemplate(imageRowIndex++,image,text)).insertAfter($(button).parent().parent().parent().parent().parent());
			} else {
				$(imageRowTemplate(imageRowIndex++,image,text)).appendTo("#imagesContainer");
			}
		}
		$(function() {
	    <c:forEach var="item" items="${bean.images}" varStatus="status">
        addImageRow("${fnx:escapeEcmaScript(item.image)}","${fnx:escapeEcmaScript(item.text)}");
	    </c:forEach>
		});
		function imagesRemove(button) {
			var toMove = $(button).parent().parent().parent().parent().parent();
			toMove.remove();
		}
		function imagesMoveUp(button) {
			var toMove = $(button).parent().parent().parent().parent().parent();
			toMove.prev().before(toMove);
		}
		function imagesMoveDown(button) {
			var toMove = $(button).parent().parent().parent().parent().parent();
			toMove.next().after(toMove);
		}
		</script>
		<div>
      <span id="imagesSwfButton"></span><input type="button" value="<s:message code="upload"/>" class="swfbutton"/>
      <input id="imagesSwfCancel" type="button" value="<s:message code="cancel"/>" onclick="imagesSwfUpload.cancelQueue();" disabled="disabled"/>
      <input type="button" value="<s:message code='addRow'/>" onclick="addImageRow('','');"/>
      <script type="text/javascript">
      var imagesSwfUpload = Cms.swfUploadImages("images",{
        jsessionid: "<%=request.getSession().getId()%>",
        file_size_limit: "${GLOBAL.upload.imageLimit}",
        file_types: "${GLOBAL.upload.imageTypes}"
      }, addImageRow);
      </script>
			<s:message code="width"/>: <f:text id="w_images" value="${field.customs['imageWidth']}" default="1500" style="width:70px;"/> &nbsp;
	    <s:message code="height"/>: <f:text id="h_images" value="${field.customs['imageHeight']}" default="" style="width:70px;"/> &nbsp;
	    <label><input type="checkbox" id="s_images"<c:if test="${empty field.customs['imageScale'] || field.customs['imageScale']=='true'}"> checked="checked"</c:if>/><s:message code="scale"/></label>
	    <label><input type="checkbox" id="e_images"<c:if test="${!empty field.customs['imageExact'] && field.customs['imageExact']=='true'}"> checked="checked"</c:if>/><s:message code="exact"/></label>
	    <label><input type="checkbox" id="wm_images"<c:if test="${empty field.customs['imageWatermark'] || field.customs['imageWatermark']=='true'}"> checked="checked"</c:if>/><s:message code="watermark"/></label>&nbsp;
	    <f:hidden id="t_images" value="${(!empty field.customs['thumbnail']) ? field.customs['thumbnail'] : 'true'}"/>
	    <f:hidden id="tw_images" value="${(!empty field.customs['thumbnailWidth']) ? field.customs['thumbnailWidth'] : '116'}"/>
	    <f:hidden id="th_images" value="${(!empty field.customs['thumbnailHeight']) ? field.customs['thumbnailHeight'] : '77'}"/>
    </div>
    <div id="imagesSwfProgress"></div>
		<div id="imagesContainer"></div>
  </c:when>
    <%-- 
  <c:when test="${field.name eq 'text'}">
    <f:textarea id="clobs_text" name="clobs_text" value="${bean.text}"/>
    <div style="overflow:hidden;"><script id="editor" type="text/plain" style="overflow:hidden;"></script></div>
		<script type="text/javascript">
		$(function() {
			setTimeout(function() {
				var editor = UE.getEditor('editor',{
					imageUrl:"${ctx}${cmscp}/core/upload_image.do?ueditor=true",
					wordImageUrl:"${ctx}${cmscp}/core/upload_image.do?ueditor=true",
					fileUrl:"${ctx}${cmscp}/core/upload_file.do?ueditor=true"
				});
			},2000);
		});			  
		</script>

    <f:textarea id="clobs_text" name="clobs_text" value="${bean.text}"/>
    <script type="text/javascript">
			CKEDITOR.replace("clobs_text",{
				<c:if test="${!empty field.customs['width']}">width: ${field.customs['width']},</c:if>
				<c:if test="${!empty field.customs['height']}">height: ${field.customs['height']},</c:if>
				toolbar: "${(!empty field.customs['toolbar']) ? (field.customs['toolbar']) : 'Cms'}Page",        
				filebrowserUploadUrl: "../upload_file.do",
				filebrowserImageUploadUrl: "../upload_image.do",
				filebrowserFlashUploadUrl: "../upload_flash.do"
			});
    </script>
  </c:when>
     --%>
  <c:otherwise>
    System field not found: '${field.name}'
  </c:otherwise>
  </c:choose>
  </c:otherwise>
  </c:choose>
  </td><c:if test="${colCount%2==1||!field.dblColumn}"></tr></c:if>
  <c:if test="${field.dblColumn}"><c:set var="colCount" value="${colCount+1}"/></c:if>
  </c:forEach>
</table>
<c:forEach var="field" items="${model.editorFields}">
	<div style="padding:5px 3px;">
	  <c:if test="${field.required}"><em class="required">*</em></c:if><c:out value="${field.label}"/>:
	</div>
	<div>
	<c:choose>
	<c:when test="${field.name eq 'text'}">	
	  <f:textarea id="clobs_text" name="clobs_text" value="${bean.text}"/>
	  <script type="text/javascript">
	  var editor_clobs_text = null;
	  $(function() {
	    editor_clobs_text = UE.getEditor('clobs_text',{
	    	<c:if test="${!empty field.customs['toolbar']}">toolbars: window.UEDITOR_CONFIG.toolbars_${field.customs['toolbar']}Page,</c:if>
        <c:if test="${!empty field.customs['width']}">initialFrameWidth:${field.customs['width']},</c:if>
        <c:if test="${!empty field.customs['height']}">initialFrameHeight:${field.customs['height']},</c:if>
	      imageUrl:"${ctx}${cmscp}/core/upload_image.do?ueditor=true",
	      wordImageUrl:"${ctx}${cmscp}/core/upload_image.do?ueditor=true",
	      fileUrl:"${ctx}${cmscp}/core/upload_file.do;jsessionid=<%=request.getSession().getId()%>?ueditor=true",
	      videoUrl:"${ctx}${cmscp}/core/upload_video.do;jsessionid=<%=request.getSession().getId()%>?ueditor=true",
	      catcherUrl:"${ctx}${cmscp}/core/get_remote_image.do?ueditor=true",
	      imageManagerUrl:"${ctx}${cmscp}/core/image_manager.do",
	      getMovieUrl:"${ctx}${cmscp}/core/get_movie.do",
	    	localDomain:['${!empty GLOBAL.uploadsDomain ? GLOBAL.uploadsDomain : ""}']
	    });
	  });
	  </script>
	</c:when>
	<c:otherwise>
	 <tags:feild_custom bean="${bean}" field="${field}"/>
	</c:otherwise>
	</c:choose>
	</div>
</c:forEach>
<table border="0" cellpadding="0" cellspacing="0" class="in-tb">
  <tr>
    <td colspan="4" class="in-opt">
      <div class="in-btn"><input type="submit" value="<s:message code="save"/>"<c:if test="${oprt=='edit' && !bean.auditPerm}"> disabled="disabled"</c:if>/></div>
    	<c:if test="${oprt=='create'}">
    	<input type="hidden" id="draft" name="draft" value="false"/>
      <div class="in-btn"><input type="submit" value="<s:message code="info.saveAsDraft"/>" onclick="$('#draft').val('true');"/></div>
    	</c:if>
    	<c:if test="${oprt=='edit'}">
    	<input type="hidden" id="pass" name="pass" value="false"/>
      <div class="in-btn"><input type="submit" value="<s:message code="info.saveAndPass"/>" onclick="$('#pass').val('true');"<c:if test="${oprt=='edit' && !bean.auditPerm}"> disabled="disabled"</c:if>/></div>
    	</c:if>
      <div class="in-btn"><input type="submit" value="<s:message code="saveAndReturn"/>" onclick="$('#redirect').val('list');"<c:if test="${oprt=='edit' && !bean.auditPerm}"> disabled="disabled"</c:if>/></div>
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