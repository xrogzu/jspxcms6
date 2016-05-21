package com.jspxcms.core.html;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import com.jspxcms.common.file.FileHandler;
import com.jspxcms.common.freemarker.Freemarkers;
import com.jspxcms.core.domain.Node;
import com.jspxcms.core.domain.Site;
import com.jspxcms.core.service.TaskService;
import com.jspxcms.core.support.ForeContext;

import freemarker.ext.servlet.FreemarkerServlet;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * PNode
 * 
 * @author liufang
 * 
 */
public abstract class PNode {
	public static void makeHtml(Node node, int max, Configuration config,
			FileHandler fileHandler, TaskService taskService, Integer taskId)
			throws IOException, TemplateException {
		if (node == null) {
			return;
		}
		deleteHtml(node, fileHandler);
		node.updateHtmlStatus();
		if (!node.getGenerate()) {
			return;
		}
		Site site = node.getSite();
		Template template = config.getTemplate(node.getTemplate());
		Map<String, Object> rootMap = new HashMap<String, Object>();
		rootMap.put(FreemarkerServlet.KEY_APPLICATION, Collections.EMPTY_MAP);
		rootMap.put(FreemarkerServlet.KEY_SESSION, Collections.EMPTY_MAP);
		rootMap.put(FreemarkerServlet.KEY_REQUEST, Collections.EMPTY_MAP);
		rootMap.put(Freemarkers.KEY_PARAMETERS, Collections.EMPTY_MAP);
		rootMap.put(Freemarkers.KEY_PARAMETER_VALUES, Collections.EMPTY_MAP);
		rootMap.put("node", node);
		Integer total = 1;
		int staticPage = node.getStaticPageOrDef();
		for (int page = 1; page <= max && page <= total && page <= staticPage
				&& taskService.isRunning(taskId); page++) {
			String filename = node.getUrlStatic(page, false, true);
			if (page == 1) {
				node.getDetail().setHtml(filename);
				if (max >= staticPage || max >= total) {
					node.setHtmlStatus(Node.HTML_GENERATED);
				} else {
					node.setHtmlStatus(Node.HTML_TOBE_UPDATE);
				}
			}
			// TODO like info:InfoText,title,text.
			rootMap.put("text", node.getText());
			String url = node.getUrlStatic(page);
			ForeContext.setData(rootMap, site, null, null, null, null, null,
					url);
			ForeContext.setPage(rootMap, page, node);
			fileHandler.storeFile(template, rootMap, filename);
			taskService.add(taskId, 1);
			total = ForeContext.getTotalPages();
			if (total == null || total < 1) {
				total = 1;
			}
		}
	}

	public static void deleteHtml(Node node, FileHandler fileHandler) {
		String html = node.getHtml();
		PNode.deleteHtml(html, fileHandler);
		node.getDetail().setHtml(null);
	}

	public static void deleteHtml(String html, FileHandler fileHandler) {
		if (StringUtils.isBlank(html)) {
			return;
		}
		String fullPath = FilenameUtils.getFullPath(html);
		String baseName = FilenameUtils.getBaseName(html);
		String extension = FilenameUtils.getExtension(html);
		int page = 2;
		while (StringUtils.isNotBlank(html)) {
			boolean success = fileHandler.delete(html);
			if (success) {
				// 删除成功，继续删除
				html = fullPath + baseName + "_" + page++ + "." + extension;
			} else {
				html = null;
			}
		}

	}
}
