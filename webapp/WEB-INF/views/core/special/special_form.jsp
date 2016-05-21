<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fnx" uri="http://java.sun.com/jsp/jstl/functionsx"%>
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
  $("#validForm").validate();
  $("input[name='name']").focus();
});
function confirmDelete() {
  return confirm("<s:message code='confirmDelete'/>");
}
function uploadFile(name,button) {
  if($("#f_"+name).val()=="") {alert("<s:message code='pleaseSelectTheFile'/>");return;}
  Cms.uploadFile("../upload_file.do",name,button);
}
function uploadVideo(name,button) {
  if($("#f_"+name).val()=="") {alert("<s:message code='pleaseSelectTheFile'/>");return;}
  Cms.uploadFile("../upload_video.do",name,button);
}
function uploadImg(name,button) {
  if($("#f_"+name).val()=="") {alert("<s:message code='pleaseSelectTheFile'/>");return;}
  Cms.uploadImg("../upload_image.do",name,button);
}
function imgCrop(name) {
  if($("#"+name).val()=="") {alert("<s:message code='noImageToCrop'/>");return;}
  Cms.imgCrop("../../commons/img_area_select.do",name);
}
</script>
</head>
<body class="c-body">
<jsp:include page="/WEB-INF/views/commons/show_message.jsp"/>
<div class="c-bar margin-top5">
  <span class="c-position"><s:message code="special.management"/> - <s:message code="${oprt=='edit' ? 'edit' : 'create'}"/></span>
</div>
<form id="validForm" action="${oprt=='edit' ? 'update' : 'save'}.do" method="post">
<tags:search_params/>
<f:hidden name="oid" value="${bean.id}"/>
<f:hidden name="position" value="${position}"/>
<input type="hidden" id="redirect" name="redirect" value="edit"/>
<table border="0" cellpadding="0" cellspacing="0" class="in-tb margin-top5">
  <tr>
    <td colspan="4" class="in-opt">
      <shiro:hasPermission name="core:special:create">
      <div class="in-btn"><input type="button" value="<s:message code="create"/>" onclick="location.href='create.do?modelId=${model.id}&categoryId=${category.id}&${searchstring}';"<c:if test="${oprt=='create'}"> disabled="disabled"</c:if>/></div>
      <div class="in-btn"></div>
      </shiro:hasPermission>
      <shiro:hasPermission name="core:special:copy">
      <div class="in-btn"><input type="button" value="<s:message code="copy"/>" onclick="location.href='create.do?id=${bean.id}&${searchstring}';"<c:if test="${oprt=='create'}"> disabled="disabled"</c:if>/></div>
      </shiro:hasPermission>
      <shiro:hasPermission name="core:info:list">
      <c:url var="infoListUrl" value="../info/list.do">
        <c:param name="search_CONTAIN_JinfoSpecials.Jspecial.title" value="${bean.title}"/>
      </c:url>
      <div class="in-btn"><input type="button" value="<s:message code="special.infoList"/>" onclick="location.href='${infoListUrl}';"<c:if test="${oprt=='create'}"> disabled="disabled"</c:if>/></div>
      </shiro:hasPermission>
      <shiro:hasPermission name="core:special:delete">
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
  <c:set var="colCount" value="${0}"/>
  <c:forEach var="field" items="${model.enabledFields}">
  <c:if test="${colCount%2==0||!field.dblColumn}"><tr></c:if>
  <td class="in-lab" width="15%"><c:if test="${field.required}"><em class="required">*</em></c:if><c:out value="${field.label}"/>:</td>
  <td<c:if test="${field.type!=50}"> class="in-ctt"</c:if><c:choose><c:when test="${field.dblColumn}"> width="35%"</c:when><c:otherwise> width="85%" colspan="3"</c:otherwise></c:choose>>
  <c:choose>
  <c:when test="${field.custom}">
    <tags:feild_custom bean="${bean}" field="${field}"/>
  </c:when>
  <c:otherwise>
  <c:choose>
  <c:when test="${field.name eq 'title'}">
    <c:set var="style_width">width:<c:out value="${field.customs['width']}" default="500"/>px;</c:set>
    <f:text name="title" value="${bean.title}" class="required" maxlength="150" style="${style_width}"/>
  </c:when>
  <c:when test="${field.name eq 'metaKeywords'}">
    <c:set var="style_width">width:<c:out value="${field.customs['width']}" default="500"/>px;</c:set>
    <f:text name="metaKeywords" value="${bean.metaKeywords}" maxlength="150" style="${style_width}"/>
  </c:when>
  <c:when test="${field.name eq 'metaDescription'}">
    <c:set var="style_width">width:<c:out value="${field.customs['width']}" default="500"/>px;</c:set>
    <c:set var="style_height">height:<c:out value="${field.customs['height']}" default="80"/>px;</c:set>
    <f:textarea name="metaDescription" value="${bean.metaDescription}" maxlength="450" style="${style_width}${style_height}"/>
  </c:when>
  <c:when test="${field.name eq 'category'}">
    <select name="categoryId" class="required">
      <f:options items="${categoryList}" itemValue="id" itemLabel="name" selected="${category.id}"/>
    </select>
  </c:when>
  <c:when test="${field.name eq 'creationDate'}">
    <input type="text" name="creationDate" value="<fmt:formatDate value="${bean.creationDate}" pattern="yyyy-MM-dd'T'HH:mm:ss"/>" onclick="WdatePicker({dateFmt:'yyyy-MM-ddTHH:mm:ss'});" class="${oprt=='edit' ? 'required' : ''}" style="width:180px;"/></td>
  </c:when>
  <c:when test="${field.name eq 'model'}">
    <select name="modelId" class="required" onchange="location.href='${oprt=='edit' ? 'edit' : 'create'}.do?id=${bean.id}&modelId='+$(this).val()+'&${searchstring}';">
      <f:options items="${modelList}" itemValue="id" itemLabel="name" selected="${model.id}"/>
    </select>
  </c:when>
  <c:when test="${field.name eq 'specialTemplate'}">
    <f:text id="specialTemplate" name="specialTemplate" value="${bean.specialTemplate}" maxlength="255" style="width:160px;"/><input id="specialTemplateButton" type="button" value="<s:message code='choose'/>"/>
    <script type="text/javascript">
    $(function(){
      Cms.f7.template("specialTemplate",{
        settings: {"title": "<s:message code="webFile.chooseTemplate"/>"}
      });
    });
    </script>
  </c:when>
  <c:when test="${field.name eq 'recommend'}">
    <label><f:radio name="recommend" value="true" checked="${bean.recommend}" class="required" /><s:message code="yes"/></label>
    <label><f:radio name="recommend" value="false" checked="${bean.recommend}" default="false" class="required" /><s:message code="no"/></label>
  </c:when>
  <c:when test="${field.name eq 'views'}">
    <f:text name="views" value="${oprt=='edit' ? bean.views : '0'}" class="required digit" style="width:180px;"/></td>
  </c:when>
  <c:when test="${field.name eq 'smallImage'}">
    <tags:image_upload name="smallImage" value="${bean.smallImage}" width="${field.customs['imageWidth']}" height="${field.customs['imageHeight']}" watermark="${field.customs['imageWatermark']}" scale="${field.customs['imageScale']}"/>
  </c:when>
  <c:when test="${field.name eq 'largeImage'}">
    <tags:image_upload name="largeImage" value="${bean.largeImage}" width="${field.customs['imageWidth']}" height="${field.customs['imageHeight']}" watermark="${field.customs['imageWatermark']}" scale="${field.customs['imageScale']}"/>
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
  <c:otherwise>
    System field not found: '${field.name}'
  </c:otherwise>
  </c:choose>
  </c:otherwise>
  </c:choose>
  </td><c:if test="${colCount%2==1||!field.dblColumn}"></tr></c:if>
  <c:if test="${field.dblColumn}"><c:set var="colCount" value="${colCount+1}"/></c:if>
  </c:forEach>
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