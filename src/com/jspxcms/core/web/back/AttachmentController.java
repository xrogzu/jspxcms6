package com.jspxcms.core.web.back;

import static com.jspxcms.core.support.Constants.DELETE_SUCCESS;
import static com.jspxcms.core.support.Constants.MESSAGE;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jspxcms.common.web.Servlets;
import com.jspxcms.core.domain.Attachment;
import com.jspxcms.core.service.AttachmentService;
import com.jspxcms.core.service.OperationLogService;
import com.jspxcms.core.support.Constants;

@Controller
@RequestMapping("/core/attachment")
public class AttachmentController {
	private static final Logger logger = LoggerFactory
			.getLogger(AttachmentController.class);

	@RequiresPermissions("core:attachment:list")
	@RequestMapping("list.do")
	public String list(
			@RequestParam(defaultValue = "false") boolean queryUsed,
			@PageableDefaults(sort = "id", sortDir = Direction.DESC) Pageable pageable,
			HttpServletRequest request, org.springframework.ui.Model modelMap) {
		Map<String, String[]> params = Servlets.getParameterValuesMap(request,
				Constants.SEARCH_PREFIX);
		Page<Attachment> pagedList = service.findAll(queryUsed, params,
				pageable);
		modelMap.addAttribute("queryUsed", queryUsed);
		modelMap.addAttribute("pagedList", pagedList);
		return "core/attachment/attachment_list";
	}

	@RequiresPermissions("core:attachment:delete")
	@RequestMapping("delete.do")
	public String delete(Integer[] ids,
			@RequestParam(defaultValue = "false") boolean queryUsed,
			HttpServletRequest request, RedirectAttributes ra) {
		List<Attachment> beans = service.delete(ids);
		for (Attachment bean : beans) {
			logService.operation("opr.attachment.delete", bean.getName(), null,
					bean.getId(), request);
			logger.info("delete Attachment, name={}.", bean.getName());
		}
		ra.addAttribute("queryUsed", queryUsed);
		ra.addFlashAttribute(MESSAGE, DELETE_SUCCESS);
		return "redirect:list.do";
	}

	@ModelAttribute("bean")
	public Attachment preloadBean(@RequestParam(required = false) Integer oid) {
		return oid != null ? service.get(oid) : null;
	}

	@Autowired
	private OperationLogService logService;
	@Autowired
	private AttachmentService service;
}
