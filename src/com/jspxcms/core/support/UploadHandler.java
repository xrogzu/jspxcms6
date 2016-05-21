package com.jspxcms.core.support;

import java.awt.image.BufferedImage;

import org.springframework.web.multipart.MultipartFile;

import com.jspxcms.common.upload.UploadResult;
import com.jspxcms.core.domain.Site;

public interface UploadHandler {
	public String copyImage(BufferedImage buff, String extension,
			String formatName, Site site, Boolean scale, Boolean exact,
			Integer width, Integer height, Boolean thumbnail,
			Integer thumbnailWidth, Integer thumbnailHeight, Boolean watermark,
			String ip, Integer userId, Integer siteId);

	public String storeImage(BufferedImage buff, String extension,
			String formatName, Site site, String ip, Integer userId);

	public void upload(String url, String type, Site site, Integer userId,
			String ip, UploadResult result);

	public void upload(MultipartFile partFile, String type, Site site,
			Integer userId, String ip, UploadResult result);

	public void upload(String url, String type, Site site, Integer userId,
			String ip, UploadResult result, Boolean scale, Boolean exact,
			Integer width, Integer height, Boolean thumbnail,
			Integer thumbnailWidth, Integer thumbnailHeight, Boolean watermark);

	public void upload(MultipartFile partFile, String type, Site site,
			Integer userId, String ip, UploadResult result, Boolean scale,
			Boolean exact, Integer width, Integer height, Boolean thumbnail,
			Integer thumbnailWidth, Integer thumbnailHeight, Boolean watermark);
}
