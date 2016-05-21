package com.jspxcms.core.web.back;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jspxcms.common.upload.Uploader;
import com.jspxcms.core.domain.Site;
import com.jspxcms.core.support.Context;

/**
 * UploadController
 * 
 * @author liufang
 * 
 */
@Controller
@RequestMapping("/core")
public class UploadController extends UploadControllerAbstract {
	@RequestMapping(value = "upload_image.do", method = RequestMethod.POST)
	public void uploadImage(Boolean scale, Boolean exact, Integer width,
			Integer height, Boolean thumbnail, Integer thumbnailWidth,
			Integer thumbnailHeight, Boolean watermark,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		Site site = Context.getCurrentSite(request);
		upload(site, request, response, Uploader.IMAGE, scale, exact, width,
				height, thumbnail, thumbnailWidth, thumbnailHeight, watermark);
	}

	@RequestMapping(value = "upload_flash.do", method = RequestMethod.POST)
	public void uploadFlash(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		Site site = Context.getCurrentSite(request);
		upload(site, request, response, Uploader.FLASH);
	}

	@RequestMapping(value = "upload_file.do", method = RequestMethod.POST)
	public void uploadFile(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		Site site = Context.getCurrentSite(request);
		upload(site, request, response, Uploader.FILE);
	}

	@RequestMapping(value = "upload_video.do", method = RequestMethod.POST)
	public void uploadVideo(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		Site site = Context.getCurrentSite(request);
		upload(site, request, response, Uploader.VIDEO);
	}

	@RequestMapping(value = "upload_doc.do", method = RequestMethod.POST)
	public void uploadDocument(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		Site site = Context.getCurrentSite(request);
		upload(site, request, response, Uploader.DOC);
	}

	@RequestMapping(value = "get_remote_image.do")
	public void getRemoteImage(String upfile, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		Site site = Context.getCurrentSite(request);
		super.getRemoteImage(site, upfile, request, response);
	}

	@RequestMapping(value = "upload_image.do")
	public void imageSavePath(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		super.imageSavePath(request, response);
	}

	@RequestMapping(value = "image_manager.do")
	public void imageManager(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		super.imageManager(request, response);
	}

	@RequestMapping(value = "get_movie.do")
	public void getMovie(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		super.getMovie(request, response);
	}
}
