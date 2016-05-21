package com.jspxcms.core.web.back;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jspxcms.common.file.FileHandler;
import com.jspxcms.common.image.Images;
import com.jspxcms.common.image.WatermarkParam;
import com.jspxcms.common.web.PathResolver;
import com.jspxcms.common.web.Servlets;
import com.jspxcms.core.domain.PublishPoint;
import com.jspxcms.core.domain.Site;
import com.jspxcms.core.support.Constants;
import com.jspxcms.core.support.Context;
import com.jspxcms.core.support.UploadHandler;

/**
 * ImageCropController
 * 
 * @author liufang
 * 
 */
@Controller
@RequestMapping("/commons")
public class ImageCropController {
	@RequiresPermissions("commons:img_crop:select")
	@RequestMapping(value = "img_area_select.do")
	public String imgAreaSelect(String src, Integer targetWidth,
			Integer targetHeight, String targetFrame, String name,
			org.springframework.ui.Model modelMap) {
		String srcNoCache = src;
		if (StringUtils.isNotBlank(src)) {
			srcNoCache += src.indexOf("?") == -1 ? "?" : "&";
			srcNoCache += "d=" + System.currentTimeMillis();
		}
		modelMap.addAttribute("src", src);
		modelMap.addAttribute("srcNoCache", srcNoCache);
		modelMap.addAttribute("targetWidth", targetWidth);
		modelMap.addAttribute("targetHeight", targetHeight);
		modelMap.addAttribute("targetFrame", targetFrame);
		modelMap.addAttribute("name", name);
		return "commons/img_area_select";
	}

	@RequiresPermissions("commons:img_crop:submit")
	@RequestMapping(value = "img_crop.do")
	public String imgCrop(String src, Float scale, Integer top, Integer left,
			Integer width, Integer height, Integer targetWidth,
			Integer targetHeight, String name, Boolean watermark,
			HttpServletRequest request, org.springframework.ui.Model modelMap)
			throws IOException {
		Site site = Context.getCurrentSite(request);
		Integer userId = Context.getCurrentUserId(request);
		PublishPoint point = site.getUploadsPublishPoint();
		String urlPrefix = point.getUrlPrefix();
		FileHandler fileHandler = point.getFileHandler(pathResolver);

		if (!src.startsWith(urlPrefix)) {
			return null;
		}
		String id = src.substring(urlPrefix.length());
		String extension = FilenameUtils.getExtension(id);
		String formatName = fileHandler.getFormatName(id);
		if (formatName == null) {
			return null;
		}

		BufferedImage buff = fileHandler.readImage(id);
		buff = Scalr.crop(buff, left, top, width, height);
		if (targetWidth < width || targetHeight < height) {
			buff = Scalr.resize(buff, Scalr.Method.QUALITY, targetWidth,
					targetHeight);
		}
		if (watermark != null && watermark) {
			WatermarkParam winfo = site.getWatermark().getWatermarkParam(null);
			if (winfo.getWatermark()) {
				String imagePath = winfo.getImagePath();
				FileHandler handler = FileHandler.getFileHandler(pathResolver,
						Constants.TEMPLATE_STORE_PATH);
				BufferedImage watermarkBuff = handler.readImage(imagePath);
				if (watermarkBuff != null) {
					Images.watermark(buff, watermarkBuff, winfo);
				}
			}
		}
		String ip = Servlets.getRemoteAddr(request);
		String url = uploadHandler.storeImage(buff, extension, formatName,
				site, ip, userId);
		fileHandler.delete(id);
		modelMap.addAttribute("name", name);
		modelMap.addAttribute("url", url);
		return "commons/img_crop";
	}

	private UploadHandler uploadHandler;
	private PathResolver pathResolver;

	@Autowired
	public void setUploadHandler(UploadHandler uploadHandler) {
		this.uploadHandler = uploadHandler;
	}

	@Autowired
	public void setPathResolver(PathResolver pathResolver) {
		this.pathResolver = pathResolver;
	}
}
