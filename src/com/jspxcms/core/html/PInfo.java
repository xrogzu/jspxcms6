package com.jspxcms.core.html;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.jspxcms.common.file.FileHandler;
import com.jspxcms.common.freemarker.Freemarkers;
import com.jspxcms.core.domain.Info;
import com.jspxcms.core.domain.Node;
import com.jspxcms.core.domain.Site;
import com.jspxcms.core.service.TaskService;
import com.jspxcms.core.support.ForeContext;
import com.jspxcms.core.support.TitleText;

import freemarker.ext.servlet.FreemarkerServlet;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * PInfo
 * 
 * @author liufang
 * 
 */
public abstract class PInfo {
	public static void makeHtml(Info info, Configuration config,
			FileHandler fileHandler, TaskService taskService, Integer taskId)
			throws IOException, TemplateException {
		if (info == null) {
			return;
		}
		deleteHtml(info, fileHandler);
		info.updateHtmlStatus();
		if (!info.isNormal() || !info.getGenerate()) {
			// 不需要生成
			return;
		}
		Site site = info.getSite();
		Node node = info.getNode();
		List<TitleText> textList = info.getTextList();
		Template template = config.getTemplate(info.getTemplate());
		Map<String, Object> rootMap = new HashMap<String, Object>();
		rootMap.put(FreemarkerServlet.KEY_APPLICATION, Collections.EMPTY_MAP);
		rootMap.put(FreemarkerServlet.KEY_SESSION, Collections.EMPTY_MAP);
		rootMap.put(FreemarkerServlet.KEY_REQUEST, Collections.EMPTY_MAP);
		rootMap.put(Freemarkers.KEY_PARAMETERS, Collections.EMPTY_MAP);
		rootMap.put(Freemarkers.KEY_PARAMETER_VALUES, Collections.EMPTY_MAP);
		rootMap.put("info", info);
		rootMap.put("node", node);
		TitleText titleText;
		List<String> items = new ArrayList<String>(1);
		items.add(null);
		Pageable pa;
		int total = textList.size();
		for (int page = 1; page <= total && taskService.isRunning(taskId); page++) {
			titleText = textList.get(page - 1);
			String filename = info.getUrlStatic(page, false, true);
			if (page == 1) {
				info.getDetail().setHtml(filename);
				info.setHtmlStatus(Node.HTML_GENERATED);
			}
			rootMap.put("title", titleText.getTitle());
			rootMap.put("text", titleText.getText());
			items.set(0, titleText.getText());
			pa = new PageRequest(page, 1);
			Page<String> pagedList = new PageImpl<String>(items, pa, total);
			String url = info.getUrlStatic(page);
			ForeContext.setData(rootMap, site, null, null, null, null, null,
					url);
			ForeContext.setPage(rootMap, page, info, pagedList);
			fileHandler.storeFile(template, rootMap, filename);
			taskService.add(taskId, 1);
		}
	}

	public static void deleteHtml(Info info, FileHandler fileHandler) {
		String html = info.getHtml();
		PNode.deleteHtml(html, fileHandler);
		info.getDetail().setHtml(null);
	}
}
