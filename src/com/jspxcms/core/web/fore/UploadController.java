package com.jspxcms.core.web.fore;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jspxcms.common.upload.Uploader;
import com.jspxcms.core.domain.Site;
import com.jspxcms.core.service.SiteService;
import com.jspxcms.core.support.Context;
import com.jspxcms.core.web.back.UploadControllerAbstract;

/**
 * UploadController
 * 
 * @author liufang
 * 
 */
@Controller
public class UploadController extends UploadControllerAbstract {

	@RequestMapping(value = "/upload_image.jspx", method = RequestMethod.POST)
	public void uploadImage(Boolean scale, Boolean exact, Integer width,
			Integer height, Boolean thumbnail, Integer thumbnailWidth,
			Integer thumbnailHeight, Boolean watermark,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		Site site = Context.getCurrentSite(request);
		upload(site, request, response, Uploader.IMAGE, scale, exact, width,
				height, thumbnail, thumbnailWidth, thumbnailHeight, watermark);
	}

	@RequestMapping(value = "/site_{siteNumber}/upload_image.jspx", method = RequestMethod.POST)
	public void uploadImageSite(@PathVariable String siteNumber, Boolean scale,
			Boolean exact, Integer width, Integer height, Boolean thumbnail,
			Integer thumbnailWidth, Integer thumbnailHeight, Boolean watermark,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		Site site = siteService.findByNumber(siteNumber);
		if (site != null) {
			Context.setCurrentSite(request, site);
			Context.setCurrentSite(site);
		} else {
			site = Context.getCurrentSite(request);
		}
		upload(site, request, response, Uploader.IMAGE, scale, exact, width,
				height, thumbnail, thumbnailWidth, thumbnailHeight, watermark);
	}

	@RequestMapping(value = "/upload_flash.jspx", method = RequestMethod.POST)
	public void uploadFlash(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		Site site = Context.getCurrentSite(request);
		upload(site, request, response, Uploader.FLASH);
	}

	@RequestMapping(value = "/site_{siteNumber}/upload_flash.jspx", method = RequestMethod.POST)
	public void uploadFlashSite(@PathVariable String siteNumber,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		Site site = siteService.findByNumber(siteNumber);
		if (site != null) {
			Context.setCurrentSite(request, site);
			Context.setCurrentSite(site);
		} else {
			site = Context.getCurrentSite(request);
		}
		upload(site, request, response, Uploader.FLASH);
	}

	@RequestMapping(value = "/upload_file.jspx", method = RequestMethod.POST)
	public void uploadFile(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		Site site = Context.getCurrentSite(request);
		upload(site, request, response, Uploader.FILE);
	}

	@RequestMapping(value = "/site_{siteNumber}/upload_file.jspx", method = RequestMethod.POST)
	public void uploadFileSite(@PathVariable String siteNumber,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		Site site = siteService.findByNumber(siteNumber);
		if (site != null) {
			Context.setCurrentSite(request, site);
			Context.setCurrentSite(site);
		} else {
			site = Context.getCurrentSite(request);
		}
		upload(site, request, response, Uploader.FILE);
	}

	@RequestMapping(value = "/upload_video.jspx", method = RequestMethod.POST)
	public void uploadVideo(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		Site site = Context.getCurrentSite(request);
		upload(site, request, response, Uploader.VIDEO);
	}

	@RequestMapping(value = "/site_{siteNumber}/upload_video.jspx", method = RequestMethod.POST)
	public void uploadVideoSite(@PathVariable String siteNumber,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		Site site = siteService.findByNumber(siteNumber);
		if (site != null) {
			Context.setCurrentSite(request, site);
			Context.setCurrentSite(site);
		} else {
			site = Context.getCurrentSite(request);
		}
		upload(site, request, response, Uploader.VIDEO);
	}

	@RequestMapping(value = "/get_remote_image.jspx")
	public void getRemoteImage(String upfile, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		Site site = Context.getCurrentSite(request);
		super.getRemoteImage(site, upfile, request, response);
	}

	@RequestMapping(value = "/site_{siteNumber}/get_remote_image.jspx")
	public void getRemoteImageSite(@PathVariable String siteNumber,
			String upfile, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		Site site = siteService.findByNumber(siteNumber);
		if (site != null) {
			Context.setCurrentSite(request, site);
			Context.setCurrentSite(site);
		} else {
			site = Context.getCurrentSite(request);
		}
		super.getRemoteImage(site, upfile, request, response);
	}

	@RequestMapping(value = { "/upload_image.jspx",
			"/site_{siteNumber}/upload_image.jspx" })
	public void imageSavePath(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		super.imageSavePath(request, response);
	}

	@RequestMapping(value = { "/image_manager.jspx",
			"/site_{siteNumber}/image_manager.jspx" })
	public void imageManager(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		super.imageManager(request, response);
	}

	@RequestMapping(value = { "/get_movie.jspx",
			"/site_{siteNumber}/get_movie.jspx" })
	public void getMovie(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		super.getMovie(request, response);
	}

	@Autowired
	private SiteService siteService;
}
