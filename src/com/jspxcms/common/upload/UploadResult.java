package com.jspxcms.common.upload;

import java.util.Locale;

import org.springframework.context.MessageSource;

public class UploadResult {
	/**
	 * 错误状态码
	 */
	public static final int	ERROR_STATUS	= 500;

	private String			fileExtension;
	private long			fileLength;
	private String			fileName;
	private String			fileUrl;
	// 文库抽取文本,
	private String			fullText;
	private String			imageUrl;
	private Locale			locale;
	private String			message;
	private MessageSource	messageSource;
	private String			pdfUrl;

	private int				status			= 0;
	private String			swfUrl;

	public UploadResult() {
	}

	public UploadResult(String fileUrl, String fileName, String fileExtension, long fileLength) {
		set(fileUrl, fileName, fileExtension, fileLength);
	}

	public String getFileExtension() {
		return fileExtension;
	}

	public long getFileLength() {
		return fileLength;
	}

	public String getFileName() {
		return fileName;
	}

	public String getFileUrl() {
		return fileUrl;
	}

	public String getFullText() {
		return fullText;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public String getMessage() {
		return message;
	}

	public String getPdfUrl() {
		return pdfUrl;
	}

	public int getStatus() {
		return status;
	}

	public String getSwfUrl() {
		return swfUrl;
	}

	public boolean isError() {
		return status >= ERROR_STATUS;
	}

	public boolean isSuccess() {
		return !isError();
	}

	public void set(String fileUrl, String fileName, String fileExtension, long fileLength) {
		this.fileUrl = fileUrl;
		this.fileName = fileName;
		this.fileExtension = fileExtension;
		this.fileLength = fileLength;
	}

	public void set(String fileUrl, String fileName, String fileExtension, long fileLength, String pdfUrl,
			String swfUrl, String imageUrl, String fullText) {
		this.fileUrl = fileUrl;
		this.fileName = fileName;
		this.fileExtension = fileExtension;
		this.fileLength = fileLength;
		this.pdfUrl = pdfUrl;
		this.swfUrl = swfUrl;
		this.imageUrl = imageUrl;// add by javasuenlai@163.com
		this.fullText = fullText;// add by javasuenlai@163.com
	}

	public void setCode(String code) {
		setCode(code, null);
	}

	public void setCode(String code, String[] args) {
		if (messageSource != null && locale != null) {
			setMessage(messageSource.getMessage(code, args, code, locale));
		} else {
			this.message = code;
		}
	}

	public void setError(String message) {
		this.status = ERROR_STATUS;
		this.message = message;
	}

	public void setErrorCode(String code) {
		setErrorCode(code, null);
	}

	public void setErrorCode(String code, String[] args) {
		this.status = ERROR_STATUS;
		setCode(code, args);
	}

	public void setFileExtension(String fileExtension) {
		this.fileExtension = fileExtension;
	}

	public void setFileLength(long fileLength) {
		this.fileLength = fileLength;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

	public void setFullText(String fullText) {
		this.fullText = fullText;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setMessageSource(MessageSource messageSource, Locale locale) {
		this.messageSource = messageSource;
		this.locale = locale;
	}

	public void setPdfUrl(String pdfUrl) {
		this.pdfUrl = pdfUrl;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public void setSwfUrl(String swfUrl) {
		this.swfUrl = swfUrl;
	}

}