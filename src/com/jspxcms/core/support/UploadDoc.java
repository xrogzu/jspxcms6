package com.jspxcms.core.support;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.OpenOfficeDocumentConverter;
import com.jspxcms.common.file.FileHandler;
import com.jspxcms.core.service.AttachmentService;

/**
 * openOffice连接池实现,以达到连接复用
 * 
 * @author suenlai
 *
 */
class OpenOfficeConnectionPool {
	private Queue<OpenOfficeConnection>	connectionQueue	= null;
	private int							initCount		= 5;

	public OpenOfficeConnectionPool(int initCount) {
		this.initCount = initCount;
		connectionQueue = new ArrayBlockingQueue<OpenOfficeConnection>(this.initCount);

		OpenOfficeConnection createdConnection = null;
		for (int i = 0; i < initCount; i++) {
			// connect to an OpenOffice.org instance running on port 8100
			createdConnection = createObject();
			connectionQueue.add(createdConnection);
		}
	}

	protected OpenOfficeConnection borrowObject() {
		OpenOfficeConnection borrowingConnection = connectionQueue.poll();
		boolean connected = validateObject(borrowingConnection);

		if (!connected) {
			borrowingConnection = createObject();
		}
		return borrowingConnection;
	}

	protected void close() {
		OpenOfficeConnection c = null;
		do {
			c = connectionQueue.poll();
			c.disconnect();
		} while (c != null);
	}

	public OpenOfficeConnection createObject() {
		OpenOfficeConnection createdConnection = new SocketOpenOfficeConnection(Constants.OPENOFFICE_HOST,
				Constants.OPENOFFICE_PORT);
		return createdConnection;
	}

	protected int getInitCount() {
		return initCount;
	}

	protected void returnObject(OpenOfficeConnection returningConnection) {
		boolean connected = validateObject(returningConnection);

		if (!connected) {
			returningConnection = createObject();
		}
		connectionQueue.offer(returningConnection);
	}

	protected boolean validateObject(OpenOfficeConnection connection) {
		if (connection == null) {
			return false;
		}
		return connection.isConnected();
	}
}

/****
 *
 * @author suenlai
 * @date 2016年5月22日 上午1:48:23
 */
class StreamPrinter implements Runnable {
	private InputStream	is;
	private Logger		logger;
	private String		type;

	public StreamPrinter(String type, InputStream is, Logger logger) {
		super();
		this.is = is;
		this.logger = logger;
	}

	public void asyncPrint() {
		new Thread(this).start();

	}

	@Override
	public void run() {
		try {

			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String line = null;
			while ((line = br.readLine()) != null) {
				logger.info(type + "=>" + line);
			}
		} catch (IOException ioe) {
			logger.error("获取子进程的" + type + "输出异常", ioe);
		}
	}
}

/**
 * 上传DOC文件。将doc文件转换成pdf，将pdf文件转化成swf文件。
 *
 * @author liufang
 *
 */
public class UploadDoc {

	protected static final Logger			logger						= LoggerFactory.getLogger(UploadDoc.class);

	private static OpenOfficeConnectionPool	openOfficeConnectionPool	= new OpenOfficeConnectionPool(10);

	public static void exec(AttachmentService attachmentService, FileHandler fileHandler, String pathname,
			String extension, String pdfPathname, String swfPathname, String imagePathname, File file,
			StringBuilder extraText, String ip, Integer userId, Integer siteId) throws Exception {
		// 本功能在商业版中提供

		// will be implements by javasuenlai@163.com

		File tempPdfFile = null;
		File tempSwfFile = null;
		File tempImageFile = null;
		try {
			if (!"pdf".equals(extension)) {
				tempPdfFile = office2PDF(attachmentService, fileHandler, pathname, extension, pdfPathname, swfPathname,
						file, ip, userId, siteId);
				tempSwfFile = pdf2Swf(attachmentService, fileHandler, pathname, extension, pdfPathname, swfPathname,
						tempPdfFile, ip, userId, siteId);
				// store attachement
				attachmentService.save(pdfPathname, tempPdfFile.length(), ip, userId, siteId);

				// use sending stream method to avoid moving tempFile
				fileHandler.storeFile(FileUtils.openInputStream(file), pathname);
			} else if ("pdf".equals(extension)) {
				tempSwfFile = pdf2Swf(attachmentService, fileHandler, pathname, extension, pdfPathname, swfPathname,
						file, ip, userId, siteId);
			} else {
				// do nothing
			}
			//
			tempImageFile = Pdf2CoverPng(attachmentService, fileHandler, pdfPathname, imagePathname, tempPdfFile, ip,
					userId, siteId);
			// 抽取PDF文本作为全文检索
			String tempText = pdf2text(tempPdfFile);
			extraText.append(tempText);

			if (tempImageFile != null) {
				attachmentService.save(imagePathname, tempImageFile.length(), ip, userId, siteId);
			}
			if (tempSwfFile != null) {
				attachmentService.save(swfPathname, tempSwfFile.length(), ip, userId, siteId);
			}
		} finally {
			// clear tempFile
			FileUtils.deleteQuietly(tempSwfFile);
			FileUtils.deleteQuietly(tempPdfFile);
			FileUtils.deleteQuietly(tempImageFile);
		}
	}

	public static File office2PDF(AttachmentService attachmentService, FileHandler fileHandler, String pathname,
			String extension, String pdfPathname, String swfPathname, File officeFile, String ip, Integer userId,
			Integer siteId) {
		OpenOfficeConnection connection = null;
		try {
			if (!officeFile.exists()) {
				return null;// 找不到源文件, 则返回-1
			}

			// 如果目标路径不存在, 则新建该路径
			// File outputFile = new File(destFile);
			File outputFile = File.createTempFile(
					String.valueOf(Thread.currentThread().getId()) + String.valueOf(System.currentTimeMillis()),
					".pdf");
			outputFile.deleteOnExit();
			if (!outputFile.getParentFile().exists()) {
				outputFile.getParentFile().mkdirs();
			}

			connection = openOfficeConnectionPool.borrowObject();

			// convert
			DocumentConverter converter = new OpenOfficeDocumentConverter(connection);
			converter.convert(officeFile, outputFile);

			// use sending stream parameter instead of sending File parameter to
			// avoid the
			// moving file invocation in fileHandler.storeFile(File,pathname)
			fileHandler.storeFile(FileUtils.openInputStream(outputFile), pdfPathname);
			// close the connection
			connection.disconnect();

			return outputFile;
		} catch (ConnectException e) {
			logger.error("连接openoffice 失败", e);
			return null;
		} catch (IOException e) {
			logger.error("读取openoffice IO失败", e);
			return null;
		} finally {
			openOfficeConnectionPool.returnObject(connection);
		}
	}

	// 上传并缩略封面图片
	public static File Pdf2CoverPng(AttachmentService attachmentService, FileHandler fileHandler, String pdfPathname,
			String imagePathname, File pdfFile, String ip, Integer userId, Integer siteId) {
		File tempOutputFile = null;
		try {
			tempOutputFile = File.createTempFile(
					String.valueOf(Thread.currentThread().getId()) + String.valueOf(System.currentTimeMillis()), "",
					null);
			tempOutputFile.deleteOnExit();
			StringBuilder fullCmd = new StringBuilder();

			fullCmd.append(Constants.PDF2PNG);
			fullCmd.append("\t");
			fullCmd.append(pdfFile.getAbsolutePath());
			fullCmd.append("\t");
			fullCmd.append(tempOutputFile.getAbsolutePath());
			fullCmd.append("\t");
			fullCmd.append("-f 1 -l 1 ");

			logger.info("run cmd:" + fullCmd);
			// 执行命令
			Process process = java.lang.Runtime.getRuntime().exec(new String[] { "sh", "-c", fullCmd.toString() });
			new StreamPrinter("STDOUT", process.getInputStream(), logger).asyncPrint();
			new StreamPrinter("STDERROR", process.getErrorStream(), logger).asyncPrint();
			process.waitFor();
			// use sending stream parameter instead of sending File parameter to
			// avoid the
			// moving file invocation in fileHandler.storeFile(File,pathname)
			File realImageFile = new File(tempOutputFile.getAbsolutePath() + "-000001.png");
			fileHandler.storeFile(FileUtils.openInputStream(realImageFile), imagePathname);
			return realImageFile;

		} catch (IOException e) {
			logger.error("pdftopng转换 IO失败", e);
			return null;
		} catch (InterruptedException e) {
			logger.error("xpdf->pdftopng命令执行中断", e);
			return null;
		} finally {
			FileUtils.deleteQuietly(tempOutputFile);
		}
	}

	public static File pdf2Swf(AttachmentService attachmentService, FileHandler fileHandler, String pathname,
			String extension, String pdfPathname, String swfPathname, File pdfFile, String ip, Integer userId,
			Integer siteId) {

		File tempOutputFile = null;
		try {
			tempOutputFile = File.createTempFile(
					String.valueOf(Thread.currentThread().getId()) + String.valueOf(System.currentTimeMillis()),
					".swf");
			tempOutputFile.deleteOnExit();
			StringBuilder fullCmd = new StringBuilder();

			fullCmd.append(Constants.SWFTOOLS_PDF2SWF);
			fullCmd.append("\t");
			fullCmd.append(pdfFile.getAbsolutePath());
			fullCmd.append("\t");
			fullCmd.append("-o ");
			fullCmd.append(tempOutputFile.getAbsolutePath());

			logger.info("run cmd:" + fullCmd);
			// 执行命令
			Process process = java.lang.Runtime.getRuntime().exec(new String[] { "sh", "-c", fullCmd.toString() });
			new StreamPrinter("STDOUT", process.getInputStream(), logger).asyncPrint();
			new StreamPrinter("STDERROR", process.getErrorStream(), logger).asyncPrint();
			process.waitFor(2000, TimeUnit.MILLISECONDS);
			process.waitFor();

			// use sending stream parameter instead of sending File parameter to
			// avoid the
			// moving file invocation in fileHandler.storeFile(File,pathname)
			fileHandler.storeFile(FileUtils.openInputStream(tempOutputFile), swfPathname);
			return tempOutputFile;

		} catch (IOException e) {
			logger.error("swftools转换 IO失败", e);
			return null;
		} catch (InterruptedException e) {
			logger.error("swftools->pdf2swf命令执行中断", e);
			return null;
		}
	}

	public static String pdf2text(File pdfFile) {
		File tempOutputFile = null;
		try {
			tempOutputFile = File.createTempFile(
					String.valueOf(Thread.currentThread().getId()) + String.valueOf(System.currentTimeMillis()),
					".txt");
			tempOutputFile.deleteOnExit();
			StringBuilder fullCmd = new StringBuilder();

			fullCmd.append(Constants.PDF2TEXT);
			fullCmd.append("\t");
			fullCmd.append(pdfFile.getAbsolutePath());
			fullCmd.append("\t");
			fullCmd.append(tempOutputFile.getAbsolutePath());
			fullCmd.append("\t");
			fullCmd.append("-enc UTF-8 ");

			logger.info("run cmd:" + fullCmd);
			// 执行命令
			Process process = java.lang.Runtime.getRuntime().exec(new String[] { "sh", "-c", fullCmd.toString() });
			new StreamPrinter("STDOUT", process.getInputStream(), logger).asyncPrint();
			new StreamPrinter("STDERROR", process.getErrorStream(), logger).asyncPrint();
			process.waitFor();

			String text = FileUtils.readFileToString(tempOutputFile);
			return text;

		} catch (IOException e) {
			logger.error("pdftotext转换 IO失败", e);
			return null;
		} catch (InterruptedException e) {
			logger.error("xpdf->pdftotext命令执行中断", e);
			return null;
		} finally {
			FileUtils.deleteQuietly(tempOutputFile);
		}
	}

}