package com.jspxcms.core.support;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.tomcat.util.http.fileupload.FileUtils;

import com.jspxcms.common.file.FileHandler;
import com.jspxcms.common.image.Images;
import com.jspxcms.common.image.ScaleParam;
import com.jspxcms.common.image.ThumbnailParam;
import com.jspxcms.common.image.WatermarkParam;
import com.jspxcms.common.upload.UploadResult;
import com.jspxcms.common.upload.Uploader;
import com.jspxcms.common.web.PathResolver;
import com.jspxcms.core.domain.Global;
import com.jspxcms.core.domain.GlobalUpload;
import com.jspxcms.core.domain.PublishPoint;
import com.jspxcms.core.domain.Site;
import com.jspxcms.core.domain.SiteWatermark;
import com.jspxcms.core.service.AttachmentService;

public class UploadHandlerImpl implements UploadHandler {
	@Autowired
	protected AttachmentService	attachmentService;

	protected final Logger		logger	= LoggerFactory.getLogger(UploadHandlerImpl.class);

	@Autowired
	protected PathResolver		pathResolver;

	@Override
	public String copyImage(BufferedImage buff, String extension, String formatName, Site site, Boolean scale,
			Boolean exact, Integer width, Integer height, Boolean thumbnail, Integer thumbnailWidth,
			Integer thumbnailHeight, Boolean watermark, String ip, Integer userId, Integer siteId) {
		GlobalUpload gu = site.getGlobal().getUpload();
		ScaleParam scaleParam = gu.getScaleParam(scale, exact, width, height);
		scale = scaleParam.getScale();

		ThumbnailParam thumbnailParam = new ThumbnailParam(thumbnail, thumbnailWidth, thumbnailHeight);
		thumbnail = thumbnailParam.getThumbnail();

		SiteWatermark sw = site.getWatermark();
		WatermarkParam watermarkParam = sw.getWatermarkParam(watermark);
		watermark = watermarkParam.getWatermark();

		PublishPoint point = site.getUploadsPublishPoint();
		String urlPrefix = point.getUrlPrefix();
		FileHandler fileHandler = point.getFileHandler(pathResolver);
		String pathname = UploadUtils.getUrl(site.getId(), Uploader.IMAGE, extension);
		try {
			storeImage(buff, scaleParam, thumbnailParam, watermarkParam, formatName, pathname, fileHandler, ip, userId,
					siteId);
			long length = buff.getWidth() * buff.getHeight() / 3;
			attachmentService.save(pathname, length, ip, userId, siteId);
			return urlPrefix + pathname;
		} catch (IOException e) {
			logger.error(null, e);
			return null;
		}
	}

	private UploadResult doUpload(File file, String fileName, String type, Site site, Integer userId, String ip,
			UploadResult result, Boolean scale, Boolean exact, Integer width, Integer height, Boolean thumbnail,
			Integer thumbnailWidth, Integer thumbnailHeight, Boolean watermark) throws Exception {
		Integer siteId = site.getId();
		long fileLength = file.length();
		String ext = FilenameUtils.getExtension(fileName).toLowerCase();
		GlobalUpload gu = site.getGlobal().getUpload();
		// 后缀名是否合法
		if (!validateExt(ext, type, gu, result)) {
			return result;
		}
		// 文库是否开启
		if (type == Uploader.DOC && !isDocEnabled(result, site.getGlobal())) {
			return result;
		}
		PublishPoint point = site.getUploadsPublishPoint();
		String urlPrefix = point.getUrlPrefix();
		FileHandler fileHandler = point.getFileHandler(pathResolver);

		String pathname = site.getSiteBase(Uploader.getQuickPathname(type, ext));
		String fileUrl = urlPrefix + pathname;
		String pdfUrl = null;
		String swfUrl = null;
		// @add by javasuenlai@163.com
		String imageUrl = null;
		StringBuilder extraTextBuilder = new StringBuilder();
		// end
		if (Uploader.IMAGE.equals(type)) {
			SiteWatermark sw = site.getWatermark();
			doUploadImage(fileHandler, file, pathname, scale, exact, width, height, thumbnail, thumbnailWidth,
					thumbnailHeight, watermark, gu, sw, ip, userId, siteId);
			imageUrl = fileUrl;
		} else if (Uploader.DOC == type) {
			if (!"swf".equals(ext)) {
				String swfPathname = site.getSiteBase(Uploader.getQuickPathname(type, "swf"));
				swfUrl = urlPrefix + swfPathname;
				String pdfPathname = null;
				if (!"pdf".equals(ext)) {
					pdfPathname = site.getSiteBase(Uploader.getQuickPathname(type, "pdf"));
					pdfUrl = urlPrefix + pdfPathname;
				} else {
					pdfUrl = fileUrl;
				}
				String imagePathname = site.getSiteBase(Uploader.getQuickPathname(Uploader.IMAGE, "png"));
				imageUrl = urlPrefix + imagePathname;
				extraTextBuilder = new StringBuilder();
				UploadDoc.exec(attachmentService, fileHandler, pathname, ext, pdfPathname, swfPathname, imagePathname,
						file, extraTextBuilder, ip, userId, siteId);
			} else {
				swfUrl = fileUrl;
				fileHandler.storeFile(file, pathname);
			}

		} else {
			fileHandler.storeFile(file, pathname);
		}
		attachmentService.save(pathname, fileLength, ip, userId, siteId);
		result.set(fileUrl, fileName, ext, fileLength, pdfUrl, swfUrl, imageUrl, extraTextBuilder.toString());
		return result;
	}

	private void doUploadImage(FileHandler fileHandler, File file, String pathname, Boolean scale, Boolean exact,
			Integer width, Integer height, Boolean thumbnail, Integer thumbnailWidth, Integer thumbnailHeight,
			Boolean watermark, GlobalUpload gu, SiteWatermark sw, String ip, Integer userId, Integer siteId)
			throws IOException {
		ScaleParam scaleParam = gu.getScaleParam(scale, exact, width, height);
		scale = scaleParam.getScale();

		ThumbnailParam thumbnailParam = new ThumbnailParam(thumbnail, thumbnailWidth, thumbnailHeight);
		thumbnail = thumbnailParam.getThumbnail();

		WatermarkParam watermarkParam = sw.getWatermarkParam(watermark);
		watermark = watermarkParam.getWatermark();

		String formatName = null;
		if (watermark || scale || thumbnail) {
			InputStream is = FileUtils.openInputStream(file);
			formatName = Images.getFormatName(is);
			IOUtils.closeQuietly(is);
		}
		if (StringUtils.isNotBlank(formatName)) {
			// 可以且需要处理的图片
			BufferedImage buff = ImageIO.read(file);
			storeImage(buff, scaleParam, thumbnailParam, watermarkParam, formatName, pathname, fileHandler, ip, userId,
					siteId);
		} else {
			// 不可处理的图片
			fileHandler.storeFile(file, pathname);
		}
	}

	private boolean isDocEnabled(UploadResult result, Global global) {
		if (!global.isDocEnabled()) {
			result.setError("DOC Converter is not available!");
			return false;
		}
		return true;
	}

	private void storeImage(BufferedImage buff, ScaleParam scaleParam, ThumbnailParam thumbnailParam,
			WatermarkParam watermarkParam, String formatName, String pathname, FileHandler fileHandler, String ip,
			Integer userId, Integer siteId) throws IOException {
		List<BufferedImage> images = new ArrayList<BufferedImage>();
		List<String> filenames = new ArrayList<String>();
		if (scaleParam.getScale()) {
			buff = Images.resize(buff, scaleParam);
		}
		BufferedImage thumbnailBuff = null;
		String thumbnailName = null;
		if (thumbnailParam.getThumbnail()) {
			Integer width = thumbnailParam.getWidth();
			Integer height = thumbnailParam.getHeight();
			thumbnailBuff = Scalr.resize(buff, Scalr.Method.QUALITY, Scalr.Mode.AUTOMATIC, width, height);
			thumbnailName = Uploader.getThumbnailName(pathname);
			images.add(thumbnailBuff);
			filenames.add(thumbnailName);
		}
		if (watermarkParam.getWatermark()) {
			String imagePath = watermarkParam.getImagePath();
			FileHandler handler = FileHandler.getFileHandler(pathResolver, Constants.TEMPLATE_STORE_PATH);
			BufferedImage watermarkBuff = handler.readImage(imagePath);
			if (watermarkBuff != null) {
				Images.watermark(buff, watermarkBuff, watermarkParam);
			}
		}
		images.add(buff);
		filenames.add(pathname);
		fileHandler.storeImages(images, formatName, filenames);
		if (thumbnailName != null) {
			long length = thumbnailBuff.getWidth() * thumbnailBuff.getHeight() / 3;
			attachmentService.save(thumbnailName, length, ip, userId, siteId);
		}
	}

	@Override
	public String storeImage(BufferedImage buff, String extension, String formatName, Site site, String ip,
			Integer userId) {
		PublishPoint point = site.getUploadsPublishPoint();
		FileHandler fileHandler = point.getFileHandler(pathResolver);
		String urlPrefix = point.getUrlPrefix();
		String filename = UploadUtils.getUrl(site.getId(), Uploader.IMAGE, extension);
		try {
			fileHandler.storeImage(buff, formatName, filename);
			long length = buff.getWidth() * buff.getHeight() / 3;
			attachmentService.save(filename, length, ip, userId, site.getId());
			return urlPrefix + filename;
		} catch (IOException e) {
			logger.error(null, e);
			return null;
		}
	}

	@Override
	public void upload(MultipartFile partFile, String type, Site site, Integer userId, String ip, UploadResult result) {
		upload(partFile, type, site, userId, ip, result, null, null, null, null, null, null, null, null);
	}

	@Override
	public void upload(MultipartFile partFile, String type, Site site, Integer userId, String ip, UploadResult result,
			Boolean scale, Boolean exact, Integer width, Integer height, Boolean thumbnail, Integer thumbnailWidth,
			Integer thumbnailHeight, Boolean watermark) {
		try {
			if (!validateFile(partFile, result)) {
				return;
			}
			String fileName = partFile.getOriginalFilename();
			String ext = FilenameUtils.getExtension(fileName);
			File temp = File.createTempFile("upload", "." + ext);
			partFile.transferTo(temp);
			try {
				doUpload(temp, fileName, type, site, userId, ip, result, scale, exact, width, height, thumbnail,
						thumbnailWidth, thumbnailHeight, watermark);
			} finally {
				FileUtils.deleteQuietly(temp);
			}
		} catch (Exception e) {
			result.setError(e.getMessage());
			logger.error(null, e);
		}
		return;
	}

	@Override
	public void upload(String url, String type, Site site, Integer userId, String ip, UploadResult result) {
		upload(url, type, site, userId, ip, result, null, null, null, null, null, null, null, null);
	}

	@Override
	public void upload(String url, String type, Site site, Integer userId, String ip, UploadResult result,
			Boolean scale, Boolean exact, Integer width, Integer height, Boolean thumbnail, Integer thumbnailWidth,
			Integer thumbnailHeight, Boolean watermark) {
		try {
			URL source = new URL(url);
			// file（下载）支持重定向支持，其他的不支持。
			if (Uploader.FILE.equals(type)) {
				HttpURLConnection.setFollowRedirects(true);
			} else {
				HttpURLConnection.setFollowRedirects(false);
			}
			HttpURLConnection conn = (HttpURLConnection) source.openConnection();
			conn.setRequestProperty("User-Agent", Constants.USER_ANGENT);
			int responseCode = conn.getResponseCode();
			if (responseCode != 200) {
				result.setError("URL response error:" + responseCode);
				return;
			}
			if (Uploader.IMAGE.equals(type)) {
				String contentType = conn.getContentType();
				if (!validateImageContentType(contentType, result)) {
					return;
				}
			}
			String disposition = conn.getHeaderField(HttpHeaders.CONTENT_DISPOSITION);
			String fileName = StringUtils.substringBetween(disposition, "filename=\"", "\"");
			if (StringUtils.isBlank(fileName)) {
				fileName = FilenameUtils.getName(source.getPath());
			}
			String ext = FilenameUtils.getExtension(fileName);
			File temp = File.createTempFile("upload", "." + ext);
			InputStream is = conn.getInputStream();
			FileUtils.copyInputStreamToFile(is, temp);
			IOUtils.closeQuietly(is);
			try {
				doUpload(temp, fileName, type, site, userId, ip, result, scale, exact, width, height, thumbnail,
						thumbnailWidth, thumbnailHeight, watermark);
			} finally {
				FileUtils.deleteQuietly(temp);
			}
		} catch (Exception e) {
			result.setError(e.getMessage());
		}
		return;
	}

	private boolean validateExt(String extension, String type, GlobalUpload gu, UploadResult result) {
		if (!gu.isExtensionValid(extension, type)) {
			logger.debug("image extension not allowed: " + extension);
			result.setErrorCode("imageExtensionNotAllowed", new String[] { extension });
			return false;
		}
		return true;
	}

	private boolean validateFile(MultipartFile partFile, UploadResult result) {
		if (partFile == null || partFile.isEmpty()) {
			logger.debug("file is empty");
			result.setError("no file upload!");
			return false;
		}
		return true;
	}

	private boolean validateImageContentType(String contentType, UploadResult result) {
		if (!StringUtils.contains(contentType, "image")) {
			logger.debug("ContentType not contain Image: " + contentType);
			result.setError("ContentType not contain Image: " + contentType);
			return false;
		}
		return true;
	}
}
