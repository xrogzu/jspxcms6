package com.jspxcms.core.web.fore;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jspxcms.common.web.Servlets;
import com.jspxcms.core.domain.Site;
import com.jspxcms.core.service.SiteService;
import com.jspxcms.core.support.Context;
import com.jspxcms.core.support.ForeContext;
import com.jspxcms.core.support.Response;

/**
 * AppController
 * 
 * @author liufang
 * 
 */
@Controller
public class AppController {
	@RequestMapping(value = "/site_{siteNumber}/app.jspx")
	public String searchSite(@PathVariable String siteNumber, Integer page,
			HttpServletRequest request, HttpServletResponse response,
			org.springframework.ui.Model modelMap) {
		Site site = siteService.findByNumber(siteNumber);
		Context.setCurrentSite(request, site);
		Context.setCurrentSite(site);
		return doSearch(page, site, request, response, modelMap);
	}

	@RequestMapping(value = "/app.jspx")
	public String search(Integer page, HttpServletRequest request,
			HttpServletResponse response, org.springframework.ui.Model modelMap) {
		Site site = Context.getCurrentSite(request);
		return doSearch(page, site, request, response, modelMap);
	}

	private String doSearch(Integer page, Site site,
			HttpServletRequest request, HttpServletResponse response,
			org.springframework.ui.Model modelMap) {
		Response resp = new Response(request, response, modelMap);
		String template = Servlets.getParameter(request, "template");
		if (StringUtils.isBlank(template)) {
			return resp.badRequest("parameter 'template' is required.");
		}
		template = "app_" + template + ".html";

		Map<String, Object> data = modelMap.asMap();
		ForeContext.setData(data, request);
		ForeContext.setPage(data, page);
		return site.getTemplate(template);
	}

	@Autowired
	private SiteService siteService;
}
