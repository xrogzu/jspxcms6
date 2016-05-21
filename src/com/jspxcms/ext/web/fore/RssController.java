package com.jspxcms.ext.web.fore;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jspxcms.common.web.Servlets;
import com.jspxcms.core.domain.Site;
import com.jspxcms.core.support.Context;
import com.jspxcms.core.support.ForeContext;

/**
 * RssSubscription
 * 
 * @author yangxing
 * 
 */
@Controller
public class RssController {
	public static final String RSS_CENTER_TEMPLATE = "sys_rss_center.html";
	public static final String RSS_TEMPLATE = "sys_rss.html";

	@RequestMapping(value = "/rss_center.jspx")
	public String rssList(HttpServletRequest request,
			org.springframework.ui.Model modelMap) {
		Site site = Context.getCurrentSite(request);
		Map<String, Object> data = modelMap.asMap();
		ForeContext.setData(data, request);
		return site.getTemplate(RSS_CENTER_TEMPLATE);
	}

	@RequestMapping(value = "/rss.jspx")
	public String list(Integer nodeId, HttpServletRequest request,
			HttpServletResponse response, org.springframework.ui.Model modelMap) {
		response.setContentType("text/xml;charset=utf-8");
		Servlets.setNoCacheHeader(response);
		Site site = Context.getCurrentSite(request);
		modelMap.addAttribute("nodeId", nodeId);
		Map<String, Object> data = modelMap.asMap();
		ForeContext.setData(data, request);
		return site.getTemplate(RSS_TEMPLATE);
	}
}
