package com.jspxcms.core.web.back;

import static com.jspxcms.core.support.Constants.CREATE;
import static com.jspxcms.core.support.Constants.DELETE_SUCCESS;
import static com.jspxcms.core.support.Constants.EDIT;
import static com.jspxcms.core.support.Constants.MESSAGE;
import static com.jspxcms.core.support.Constants.OPRT;
import static com.jspxcms.core.support.Constants.SAVE_SUCCESS;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jspxcms.common.orm.RowSide;
import com.jspxcms.common.web.Servlets;
import com.jspxcms.core.domain.MemberGroup;
import com.jspxcms.core.domain.Site;
import com.jspxcms.core.service.MemberGroupService;
import com.jspxcms.core.service.OperationLogService;
import com.jspxcms.core.support.Constants;
import com.jspxcms.core.support.Context;

/**
 * MemberGroupController
 * 
 * @author liufang
 * 
 */
@Controller
@RequestMapping("/core/member_group")
public class MemberGroupController {
	private static final Logger logger = LoggerFactory
			.getLogger(MemberGroupController.class);

	@RequestMapping("list.do")
	@RequiresPermissions("core:member_group:list")
	public String list(
			@PageableDefaults(sort = { "seq", "id" }) Pageable pageable,
			HttpServletRequest request, org.springframework.ui.Model modelMap) {
		Map<String, String[]> params = Servlets.getParameterValuesMap(request,
				Constants.SEARCH_PREFIX);
		List<MemberGroup> list = service.findList(params, pageable.getSort());
		modelMap.addAttribute("list", list);
		return "core/member_group/member_group_list";
	}

	@RequestMapping("create.do")
	@RequiresPermissions("core:member_group:create")
	public String create(Integer id, org.springframework.ui.Model modelMap) {
		if (id != null) {
			MemberGroup bean = service.get(id);
			modelMap.addAttribute("bean", bean);
		}
		modelMap.addAttribute(OPRT, CREATE);
		return "core/member_group/member_group_form";
	}

	@RequestMapping("edit.do")
	@RequiresPermissions("core:member_group:edit")
	public String edit(Integer id, Integer position, @PageableDefaults(sort = {
			"seq", "id" }) Pageable pageable, HttpServletRequest request,
			org.springframework.ui.Model modelMap) {
		Site site = Context.getCurrentSite(request);
		MemberGroup bean = service.get(id);
		Map<String, String[]> params = Servlets.getParameterValuesMap(request,
				Constants.SEARCH_PREFIX);
		RowSide<MemberGroup> side = service.findSide(params, bean, position,
				pageable.getSort());

		modelMap.addAttribute("viewNodes", bean.getViewNodes(site.getId()));
		modelMap.addAttribute("contriNodes", bean.getContriNodes(site.getId()));
		modelMap.addAttribute("commentNodes",
				bean.getCommentNodes(site.getId()));

		modelMap.addAttribute("bean", bean);
		modelMap.addAttribute("side", side);
		modelMap.addAttribute("position", position);
		modelMap.addAttribute(OPRT, EDIT);
		return "core/member_group/member_group_form";
	}

	@RequestMapping("save.do")
	@RequiresPermissions("core:member_group:save")
	public String save(MemberGroup bean, Integer[] viewNodeIds,
			Integer[] contriNodeIds, Integer[] commentNodeIds, String redirect,
			HttpServletRequest request, RedirectAttributes ra) {
		Integer siteId = Context.getCurrentSiteId(request);
		service.save(bean, viewNodeIds, contriNodeIds, commentNodeIds, siteId);
		logService.operation("opr.memberGroup.add", bean.getName(),
				null, bean.getId(), request);
		logger.info("save MemberGroup, name={}.", bean.getName());
		ra.addFlashAttribute(MESSAGE, SAVE_SUCCESS);
		if (Constants.REDIRECT_LIST.equals(redirect)) {
			return "redirect:list.do";
		} else if (Constants.REDIRECT_CREATE.equals(redirect)) {
			return "redirect:create.do";
		} else {
			ra.addAttribute("id", bean.getId());
			return "redirect:edit.do";
		}
	}

	@RequestMapping("update.do")
	@RequiresPermissions("core:member_group:update")
	public String update(@ModelAttribute("bean") MemberGroup bean,
			Integer[] viewNodeIds, Integer[] contriNodeIds,
			Integer[] commentNodeIds, Integer position, String redirect,
			HttpServletRequest request, RedirectAttributes ra) {
		if (viewNodeIds == null) {
			viewNodeIds = new Integer[0];
		}
		if (contriNodeIds == null) {
			contriNodeIds = new Integer[0];
		}
		if (commentNodeIds == null) {
			commentNodeIds = new Integer[0];
		}

		Integer siteId = Context.getCurrentSiteId(request);
		service.update(bean, viewNodeIds, contriNodeIds, commentNodeIds, siteId);
		logService.operation("opr.memberGroup.edit", bean.getName(), null,
				bean.getId(), request);
		logger.info("update MemberGroup, name={}.", bean.getName());
		ra.addFlashAttribute(MESSAGE, SAVE_SUCCESS);
		if (Constants.REDIRECT_LIST.equals(redirect)) {
			return "redirect:list.do";
		} else {
			ra.addAttribute("id", bean.getId());
			ra.addAttribute("position", position);
			return "redirect:edit.do";
		}
	}

	@RequestMapping("delete.do")
	@RequiresPermissions("core:member_group:delete")
	public String delete(Integer[] ids, HttpServletRequest request,
			RedirectAttributes ra) {
		MemberGroup[] beans = service.delete(ids);
		for (MemberGroup bean : beans) {
			logService.operation("opr.memberGroup.delete", bean.getName(),
					null, bean.getId(), request);
			logger.info("delete MemberGroup, name={}.", bean.getName());
		}
		ra.addFlashAttribute(MESSAGE, DELETE_SUCCESS);
		return "redirect:list.do";
	}

	@ModelAttribute("bean")
	public MemberGroup preloadBean(@RequestParam(required = false) Integer oid) {
		return oid != null ? service.get(oid) : null;
	}

	@Autowired
	private OperationLogService logService;
	@Autowired
	private MemberGroupService service;
}
