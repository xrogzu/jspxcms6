package com.jspxcms.core.web.back;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.shiro.util.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.jspxcms.common.file.FileHandler;
import com.jspxcms.common.upload.UploadResult;
import com.jspxcms.common.upload.Uploader;
import com.jspxcms.common.util.JsonMapper;
import com.jspxcms.common.web.PathResolver;
import com.jspxcms.common.web.Servlets;
import com.jspxcms.core.domain.GlobalUpload;
import com.jspxcms.core.domain.PublishPoint;
import com.jspxcms.core.domain.Site;
import com.jspxcms.core.support.Context;
import com.jspxcms.core.support.UploadHandler;

/**
 * UploadControllerAbstract
 * 
 * @author liufang
 * 
 */
public abstract class UploadControllerAbstract {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected void imageSavePath(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		if (request.getParameter("fetch") != null) {
			response.setHeader("Content-Type", "text/javascript");
			response.getWriter().print("updateSavePath( ['image'] );");
			return;
		}
	}

	protected void imageManager(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		response.getWriter().print("");
	}

	protected void getMovie(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		StringBuffer readOneLineBuff = new StringBuffer();
		String content = "";
		String searchkey = request.getParameter("searchKey");
		String videotype = request.getParameter("videoType");
		try {
			searchkey = URLEncoder.encode(searchkey, "utf-8");
			URL url = new URL(
					"http://api.tudou.com/v3/gw?method=item.search&appKey=myKey&format=json&kw="
							+ searchkey + "&pageNo=1&pageSize=20&channelId="
							+ videotype + "&inDays=7&media=v&sort=s");
			URLConnection conn = url.openConnection();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					conn.getInputStream(), "utf-8"));
			String line = "";
			while ((line = reader.readLine()) != null) {
				readOneLineBuff.append(line);
			}
			content = readOneLineBuff.toString();
			reader.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		response.getWriter().print(content);
	}

	protected void getRemoteImage(Site site, String upfile,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		GlobalUpload gu = site.getGlobal().getUpload();
		PublishPoint point = site.getUploadsPublishPoint();
		FileHandler fileHandler = point.getFileHandler(pathResolver);
		String urlPrefix = point.getUrlPrefix();

		String state = "SUCCESS";
		String[] arr = upfile.split("ue_separate_ue");
		List<String> urls = new ArrayList<String>();
		List<String> srcs = new ArrayList<String>();
		for (int i = 0; i < arr.length; i++) {
			String extension = FilenameUtils.getExtension(arr[i]);
			// 格式验证
			if (!gu.isExtensionValid(extension, Uploader.IMAGE)) {
				state = "Extension Invalid";
				continue;
			}
			HttpURLConnection.setFollowRedirects(false);
			HttpURLConnection conn = (HttpURLConnection) new URL(arr[i])
					.openConnection();
			if (conn.getContentType().indexOf("image") == -1) {
				state = "ContentType Invalid";
				continue;
			}
			if (conn.getResponseCode() != 200) {
				state = "Request Error";
				continue;
			}
			String pathname = site.getSiteBase(Uploader.getQuickPathname(
					Uploader.IMAGE, extension));
			fileHandler.storeFile(conn.getInputStream(), pathname);
			urls.add(urlPrefix + pathname);
			srcs.add(arr[i]);
		}
		StringBuilder outstr = new StringBuilder();
		for (String url : urls) {
			outstr.append(url).append("ue_separate_ue");
		}
		StringBuilder srcUrl = new StringBuilder();
		for (String src : srcs) {
			srcUrl.append(src).append("ue_separate_ue");
		}
		int sepLength = "ue_separate_ue".length();
		if (outstr.length() > sepLength) {
			outstr.setLength(outstr.length() - sepLength);
		}
		if (srcUrl.length() > sepLength) {
			srcUrl.setLength(srcUrl.length() - sepLength);
		}
		response.getWriter().print(
				"{'url':'" + outstr + "','tip':'" + state + "','srcUrl':'"
						+ srcUrl + "'}");
	}

	protected void upload(Site site, HttpServletRequest request,
			HttpServletResponse response, String type) throws IOException {
		upload(site, request, response, type, null, null, null, null, null,
				null, null, null);
	}

	protected void upload(Site site, HttpServletRequest request,
			HttpServletResponse response, String type, Boolean scale,
			Boolean exact, Integer width, Integer height, Boolean thumbnail,
			Integer thumbnailWidth, Integer thumbnailHeight, Boolean watermark)
			throws IOException {
		UploadResult result = new UploadResult();
		Locale locale = RequestContextUtils.getLocale(request);
		result.setMessageSource(messageSource, locale);

		Integer userId = Context.getCurrentUserId(request);
		String ip = Servlets.getRemoteAddr(request);
		MultipartFile partFile = getMultipartFile(request);
		uploadHandler.upload(partFile, type, site, userId, ip, result, scale,
				exact, width, height, thumbnail, thumbnailWidth,
				thumbnailHeight, watermark);

		String ckeditor = request.getParameter("CKEditor");
		String ueditor = request.getParameter("ueditor");
		if (ckeditor != null) {
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html");
			response.setHeader("Cache-Control", "no-cache");
			PrintWriter out = response.getWriter();
			String callback = request.getParameter("CKEditorFuncNum");
			out.println("<script type=\"text/javascript\">");
			out.println("(function(){var d=document.domain;while (true){try{var A=window.parent.document.domain;break;}catch(e) {};d=d.replace(/.*?(?:\\.|$)/,'');if (d.length==0) break;try{document.domain=d;}catch (e){break;}}})();\n");
			if (result.isError()) {
				out.println("window.parent.CKEDITOR.tools.callFunction("
						+ callback + ",'" + result.getFileUrl() + "',''" + ");");
			} else {
				out.println("alert('" + result.getMessage() + "');");
			}
			out.print("</script>");
			out.flush();
			out.close();
		} else if (ueditor != null) {
			Map<String, String> umap = new HashMap<String, String>();
			String title = request.getParameter("pictitle");
			umap.put("title", title);
			umap.put("state", "SUCCESS");
			umap.put("original", result.getFileName());
			umap.put("url", result.getFileUrl());
			umap.put("fileType", "." + result.getFileExtension());
			JsonMapper mapper = new JsonMapper();
			String json = mapper.toJson(umap);
			logger.debug(json);
			Servlets.writeHtml(response, json);
		} else {
			JsonMapper mapper = new JsonMapper();
			String json = mapper.toJson(result);
			logger.debug(json);
			Servlets.writeHtml(response, json);
		}
	}

	private MultipartFile getMultipartFile(HttpServletRequest request) {
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		if (CollectionUtils.isEmpty(fileMap)) {
			throw new IllegalStateException("No upload file found!");
		}
		return fileMap.entrySet().iterator().next().getValue();
	}

	@Autowired
	protected MessageSource messageSource;
	@Autowired
	protected PathResolver pathResolver;
	@Autowired
	protected UploadHandler uploadHandler;

}
