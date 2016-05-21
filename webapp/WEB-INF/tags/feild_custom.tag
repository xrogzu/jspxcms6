<%@ tag pageEncoding="UTF-8"%><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%><%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%><%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%><%@ taglib prefix="fnx" uri="http://java.sun.com/jsp/jstl/functionsx"%><%@ taglib prefix="s" uri="http://www.springframework.org/tags" %><%@ taglib prefix="f" uri="http://www.jspxcms.com/tags/form"%><%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ attribute name="field" type="java.lang.Object" required="true" rtexprvalue="true"%>
<%@ attribute name="bean" type="java.lang.Object" required="true" rtexprvalue="true"%>
  <c:choose>
    <c:when test="${field.innerType==3}">
      <c:set var="c_name" value="${field.name}"/>
    </c:when>
    <c:otherwise>
    	<c:set var="c_name"><c:out value="${(field.clob) ? ('clobs_') : ('customs_')}${field.name}"/></c:set>
    </c:otherwise>
  </c:choose>
  <c:choose>
    <c:when test="${field.innerType==3}">
      <c:set var="c_value" value="${bean[field.name]}"/>
    </c:when>
    <c:when test="${field.clob}">
      <c:set var="c_value" value="${bean.clobs[field.name]}"/>
    </c:when>
    <c:otherwise>
      <c:set var="c_value" value="${bean.customs[field.name]}"/>
    </c:otherwise>
  </c:choose>
	<%--<c:set var="c_value"><c:out value="${(field.clob) ? (bean.clobs[field.name]) : (bean.customs[field.name])}"/></c:set>--%>
	<c:if test="${oprt=='create' && empty c_value}"><c:set var="c_value" value="${field.defValue}"/></c:if>
	<c:set var="style_width"><c:if test="${!empty field.customs['width']}">width:${field.customs['width']}px;</c:if></c:set>
	<c:set var="style_height"><c:if test="${!empty field.customs['height']}">height:${field.customs['height']}px;</c:if></c:set>
	<c:set var="attr_maxlength"><c:if test="${!empty field.customs['maxLength']}">maxlength="${field.customs['maxLength']}"</c:if></c:set>
	<c:set var="attr_class"><c:choose><c:when test="${!empty field.customs['validation']}">class="${field.customs['validation']}"</c:when><c:otherwise><c:if test="${field.required}">class="required"</c:if></c:otherwise></c:choose></c:set>
	<c:choose>
 		<c:when test="${field.type==1}">
 			<input type="text" name="${c_name}" value="${c_value}" ${attr_class} ${attr_maxlength} style="${style_width}"/>
 		</c:when>
 		<c:when test="${field.type==2}">
 			<c:if test="${oprt=='create' && c_value=='now'}"><c:set var="c_value"><fmt:formatDate value="${fnx:now()}" pattern="${field.customs['datePattern']}"/></c:set></c:if>
 			<input type="text" name="${c_name}" value="${c_value}" onclick="WdatePicker({dateFmt:'${field.customs['datePattern']}'});" ${attr_class} ${attr_maxlength} style="${style_width}"/>
 		</c:when>
 		<c:when test="${field.type==3}">
      <c:set var="c_value" value="${fnx:split_sc(c_value,',')}"/>
 			<c:forEach var="option" items="${fnx:invoke(field.options,'entrySet')}">
 				<label><input type="checkbox" name="${c_name}" value="${option.key}"<c:if test="${fnx:contains_oxo(c_value,option.key)}"> checked="checked"</c:if>/>${option.value}</label>
 			</c:forEach>
 		</c:when>
    <c:when test="${field.type==4 || field.type==100}">
      <c:forEach var="option" items="${fnx:invoke(field.options,'entrySet')}">
        <label><input type="radio" name="${c_name}" value="${option.key}"<c:if test="${c_value eq option.key}"> checked="checked"</c:if>/>${option.value}</label>
      </c:forEach>
    </c:when>
 		<c:when test="${field.type==5 || field.type==101}">
 			<select name="${c_name}" style="${style_width}">
      <c:forEach var="option" items="${fnx:invoke(field.options,'entrySet')}">
 				<option value="${option.key}"<c:if test="${c_value eq option.key}"> selected="selected"</c:if>>${option.value}</option>
 			</c:forEach>
 			</select>
 		</c:when>
 		<c:when test="${field.type==6}">
 			<textarea name="${c_name}" ${attr_class} ${attr_maxlength} style="${style_width}${style_height}">${c_value}</textarea>
 		</c:when>
 		<c:when test="${field.type==7}">
 			<tags:image_upload name="${c_name}" value="${c_value}" width="${field.customs['imageWidth']}" height="${field.customs['imageHeight']}" watermark="${field.customs['imageWatermark']}" scale="${field.customs['imageScale']}"/>
 		</c:when>  	
 		<c:when test="${field.type==8}">
 			<c:set var="c_valueName">${field.name}Name</c:set>
 			<c:set var="c_valueName"><c:out value="${(field.clob) ? (bean.clobs[c_valueName]) : (bean.customs[c_valueName])}"/></c:set>
      <c:set var="c_valueLength">${field.name}Length</c:set>
      <c:set var="c_valueLength"><c:out value="${(field.clob) ? (bean.clobs[c_valueLength]) : (bean.customs[c_valueLength])}"/></c:set>
      <c:set var="c_valueTime">${field.name}Time</c:set>
      <c:set var="c_valueTime"><c:out value="${(field.clob) ? (bean.clobs[c_valueTime]) : (bean.customs[c_valueTime])}"/></c:set>
	    <%--
	    <div>
	      <span style="padding:0 7px;"><s:message code="fileName"/>:</span><f:text id="${c_name}Name" name="${c_name}Name" value="${c_valueName}" maxlength="255" style="width:420px;"/>
	    </div>
	    <div style="padding-top:3px;">
	      <span style="padding:0 7px;"><s:message code="fileUrl"/>:</span><f:text id="${c_name}" name="${c_name}" value="${c_value}" maxlength="255" style="width:420px;"/>
	    </div>
	    <div style="padding-top:3px;">
	      <span style="padding:0 7px;"><input id="f_${c_name}" name="f_${c_name}" type="file" size="23" style="width:235px;"/></span> <input type="button" onclick="uploadVideo('${c_name}',this)" value="<s:message code="upload"/>"/>
	    </div>
	    --%>
	  <div>
      <span style="padding:0 7px;"><s:message code="fileName"/>:</span><f:text id="${c_name}Name" name="${c_name}Name" value="${c_valueName}" maxlength="255" style="width:460px;"/>
    </div>
    <div style="padding-top:3px;">
      <span style="padding:0 7px;"><s:message code="fileUrl"/>:</span><f:text id="${c_name}" name="${c_name}" value="${c_value}" maxlength="255" style="width:460px;"/>
    </div>
    <div style="padding-top:3px;">
      <span style="padding:0 7px;"><s:message code="fileLength"/>:</span><f:text id="${c_name}Length" name="${c_name}Length" value="${c_valueLength}" class="{digits:true,max:2147483647}" maxlength="10" style="width:70px;"/>
      <span style="padding:0 7px;"><s:message code="videoTime"/>:</span><f:text id="${c_name}Time" name="${c_name}Time" value="${c_valueTime}" maxlength="100" style="width:70px;"/> &nbsp;
      <input id="${c_name}Button" type="button" value="<s:message code='choose'/>"/>
      <script type="text/javascript">
      $(function() {
        Cms.f7.uploads("${c_name}","${c_name}Name",{
          settings: {"title": "<s:message code="webFile.chooseUploads"/>"}
        });
      });
      </script>
      <span id="${c_name}SwfButton"></span><input type="button" value="<s:message code="upload"/>" class="swfbutton"/>
      <input id="${c_name}SwfCancel" type="button" value="<s:message code="cancel"/>" onclick="${c_name}SwfUpload.cancelQueue();" disabled="disabled"/>
      <script type="text/javascript">
      var ${c_name}SwfUpload = Cms.swfUploadVideo("${c_name}",{
        jsessionid: "<%=request.getSession().getId()%>",
        file_size_limit: "${GLOBAL.upload.videoLimit}",
        file_types: "${GLOBAL.upload.videoTypes}"
      });
      </script>
      <div id="${c_name}SwfProgress"></div>
    </div>
		</c:when>
 		<c:when test="${field.type==9}">
 		  <c:set var="c_valueName">${field.name}Name</c:set>
      <c:set var="c_valueName"><c:out value="${(field.clob) ? (bean.clobs[c_valueName]) : (bean.customs[c_valueName])}"/></c:set>
      <c:set var="c_valueLength">${field.name}Length</c:set>
      <c:set var="c_valueLength"><c:out value="${(field.clob) ? (bean.clobs[c_valueLength]) : (bean.customs[c_valueLength])}"/></c:set>
	    <div>
	      <span style="padding:0 7px;"><s:message code="fileName"/>:</span><f:text id="${c_name}Name" name="${c_name}Name" value="${c_valueName}" maxlength="255" style="width:460px;"/>
	    </div>
	    <div style="padding-top:3px;">
	      <span style="padding:0 7px;"><s:message code="fileUrl"/>:</span><f:text id="${c_name}" name="${c_name}" value="${c_value}" maxlength="255" style="width:460px;"/>
	    </div>
	    <div style="padding-top:3px;">
	      <span style="padding:0 7px;"><s:message code="fileLength"/>:</span><f:text id="${c_name}Length" name="${c_name}Length" value="${c_valueLength}" class="{digits:true,max:2147483647}" maxlength="10" style="width:70px;"/>
	      <input id="${c_name}Button" type="button" value="<s:message code='choose'/>"/>
	      <script type="text/javascript">
	      $(function() {
	        Cms.f7.uploads("${c_name}","${c_name}Name",{
	          settings: {"title": "<s:message code="webFile.chooseUploads"/>"}
	        });
	      });
	      </script>
	      <span id="${c_name}SwfButton"></span><input type="button" value="<s:message code="upload"/>" class="swfbutton"/>
	      <input id="${c_name}SwfCancel" type="button" value="<s:message code="cancel"/>" onclick="${c_name}SwfUpload.cancelQueue();" disabled="disabled"/>
	      <script type="text/javascript">
	      var ${c_name}SwfUpload = Cms.swfUploadFile("${c_name}",{
	        jsessionid: "<%=request.getSession().getId()%>",
	        file_size_limit: "${GLOBAL.upload.fileLimit}",
	        file_types: "${GLOBAL.upload.fileTypes}"
	      });
	      </script>
	    </div>
	    <div id="${c_name}SwfProgress"></div>
   	</c:when>
 		<c:when test="${field.type==50}">
 			<textarea id="${c_name}" name="${c_name}">${c_value}</textarea>
			<script type="text/javascript">
	    $(function() {
	      var editor_${c_name} = UE.getEditor('${c_name}',{
	    	  <c:if test="${!empty field.customs['toolbar']}">toolbars: window.UEDITOR_CONFIG.toolbars_${field.customs['toolbar']},</c:if>
          <c:if test="${!empty field.customs['width']}">initialFrameWidth:${field.customs['width']},</c:if>
          <c:if test="${!empty field.customs['height']}">initialFrameHeight:${field.customs['height']},</c:if>
	        imageUrl: "${ctx}${cmscp}/core/upload_image.do?ueditor=true",
	        wordImageUrl: "${ctx}${cmscp}/core/upload_image.do?ueditor=true",
	        fileUrl: "${ctx}${cmscp}/core/upload_file.do;jsessionid=<%=request.getSession().getId()%>?ueditor=true",
	        videoUrl: "${ctx}${cmscp}/core/upload_video.do;jsessionid=<%=request.getSession().getId()%>?ueditor=true",
	        catcherUrl: "${ctx}${cmscp}/core/get_remote_image.do?ueditor=true",
	        imageManagerUrl: "${ctx}${cmscp}/core/image_manager.do",
	        getMovieUrl: "${ctx}${cmscp}/core/get_movie.do",
	        localDomain: ['${!empty GLOBAL.uploadsDomain ? GLOBAL.uploadsDomain : ""}']
	      });
	    });
			</script>  			
 		</c:when>
  	<c:otherwise>
  		Unknown Filed Type: ${field.type}
  	</c:otherwise>
	</c:choose>
	<c:if test="${!empty field.prompt}"><span class="in-prompt" title="<c:out value='${field.prompt}'/>">&nbsp;</span></c:if>