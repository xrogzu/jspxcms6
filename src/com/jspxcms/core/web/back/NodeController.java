package com.jspxcms.core.web.back;

import static com.jspxcms.core.support.Constants.CREATE;
import static com.jspxcms.core.support.Constants.DELETE_SUCCESS;
import static com.jspxcms.core.support.Constants.EDIT;
import static com.jspxcms.core.support.Constants.MESSAGE;
import static com.jspxcms.core.support.Constants.OPRT;
import static com.jspxcms.core.support.Constants.SAVE_SUCCESS;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jspxcms.common.orm.RowSide;
import com.jspxcms.common.web.Servlets;
import com.jspxcms.core.domain.Info;
import com.jspxcms.core.domain.MemberGroup;
import com.jspxcms.core.domain.Model;
import com.jspxcms.core.domain.Node;
import com.jspxcms.core.domain.NodeDetail;
import com.jspxcms.core.domain.Role;
import com.jspxcms.core.domain.Site;
import com.jspxcms.core.domain.User;
import com.jspxcms.core.domain.Workflow;
import com.jspxcms.core.service.MemberGroupService;
import com.jspxcms.core.service.ModelService;
import com.jspxcms.core.service.NodeQueryService;
import com.jspxcms.core.service.NodeService;
import com.jspxcms.core.service.OperationLogService;
import com.jspxcms.core.service.RoleService;
import com.jspxcms.core.service.WorkflowService;
import com.jspxcms.core.support.CmsException;
import com.jspxcms.core.support.Constants;
import com.jspxcms.core.support.Context;

/**
 * NodeController
 * 
 * @author liufang
 * 
 */
@Controller
@RequestMapping("/core/node")
public class NodeController {
	private static final Logger logger = LoggerFactory
			.getLogger(NodeController.class);

	@RequiresPermissions("core:node:left")
	@RequestMapping("left.do")
	public String left(HttpServletRequest request,
			org.springframework.ui.Model modelMap) {
		User user = Context.getCurrentUser(request);
		Integer siteId = Context.getCurrentSiteId(request);
		List<Node> list = query.findList(siteId, null);
		list = user.getNodePermList(siteId, list);
		modelMap.addAttribute("list", list);
		return "core/node/node_left";
	}

	@RequiresPermissions("core:node:list")
	@RequestMapping("list.do")
	public String list(
			Integer queryParentId,
			@RequestParam(defaultValue = "false") boolean showDescendants,
			@PageableDefaults(sort = { "treeNumber", "id" }, sortDir = Direction.ASC) Pageable pageable,
			HttpServletRequest request, org.springframework.ui.Model modelMap) {
		User user = Context.getCurrentUser(request);
		Integer siteId = Context.getCurrentSiteId(request);
		Node parent = null;
		if (queryParentId != null) {
			parent = query.get(queryParentId);
		}
		if (parent == null) {
			parent = query.findRoot(siteId);
			if (parent != null) {
				queryParentId = parent.getId();
			}
		}
		String treeNumber = null;
		if (showDescendants && parent != null) {
			treeNumber = parent.getTreeNumber();
		}
		boolean isAllNodePerm = user.getAllNodePerm(siteId);
		Map<String, String[]> params = Servlets.getParameterValuesMap(request,
				Constants.SEARCH_PREFIX);
		List<Node> list = query.findList(siteId, treeNumber, queryParentId,
				user.getId(), isAllNodePerm, params, pageable.getSort());
		List<Model> nodeModelList = modelService.findList(siteId,
				Node.NODE_MODEL_TYPE);
		modelMap.addAttribute("parent", parent);
		modelMap.addAttribute("list", list);
		modelMap.addAttribute("nodeModelList", nodeModelList);
		modelMap.addAttribute("queryParentId", queryParentId);
		modelMap.addAttribute("showDescendants", showDescendants);
		return "core/node/node_list";
	}

	@RequiresPermissions("core:node:create")
	@RequestMapping("create.do")
	public String create(Integer cid, Integer parentId, Integer modelId,
			Integer infoModelId, Integer queryParentId,
			Boolean showDescendants, HttpServletRequest request,
			org.springframework.ui.Model modelMap) {
		Site site = Context.getCurrentSite(request);
		Integer siteId = site.getId();
		// 复制节点
		Node bean = null;
		if (cid != null) {
			bean = query.get(cid);
		}
		// 父节点
		Node parent = null;
		if (bean != null) {
			parent = bean.getParent();
		} else if (parentId != null) {
			parent = query.get(parentId);
		} else {
			parent = query.findRoot(siteId);
		}
		// 模型
		Model model = null;
		if (modelId != null) {
			// 指定模型
			model = modelService.get(modelId);
		} else if (bean != null) {
			// 复制节点
			model = bean.getNodeModel();
		} else if (parent != null && parent.getParent() != null) {
			// 指定了父节点，且父节点不是根节点。
			model = parent.getNodeModel();
		} else if (query.findRoot(siteId) != null) {
			// 根节点存在，获取默认节点模型
			model = modelService.findDefault(siteId, Node.NODE_MODEL_TYPE);
			// 没有定义节点模型
			if (model == null) {
				throw new CmsException("node.error.nodeModelNotFound");
			}
		} else {
			// 根节点不存在，获取默认首页模型
			model = modelService.findDefault(siteId, Node.HOME_MODEL_TYPE);
			// 没有定义首页模型
			if (model == null) {
				throw new CmsException("node.error.nodeHomeModelNotFound");
			}
		}
		// 权限、信息模型
		if (parent != null && bean == null) {
			bean = new Node();
			bean.setNodeRoles(parent.getNodeRoles());
			bean.setNodeGroups(parent.getNodeGroups());
			bean.setNodeOrgs(parent.getNodeOrgs());
		}
		List<Role> roleList = roleService.findList(siteId);
		List<MemberGroup> groupList = memberGroupService.findList();
		String orgTreeNumber = site.getOrg().getTreeNumber();
		String modelType = model.getType();
		List<Model> nodeModelList = modelService.findList(siteId, modelType);
		List<Model> infoModelList = modelService.findList(siteId,
				Info.MODEL_TYPE);
		Model infoModel = null;
		if (infoModelId != null) {
			infoModel = modelService.get(infoModelId);
		} else if (bean != null && bean.getInfoModel() != null) {
			infoModel = bean.getInfoModel();
		} else if (parent != null) {
			infoModel = parent.getInfoModel();
		} else if (!infoModelList.isEmpty()) {
			infoModel = infoModelList.get(0);
		}
		List<Workflow> workflowList = workflowService.findList(siteId);

		modelMap.addAttribute("workflowList", workflowList);
		modelMap.addAttribute("cid", cid);
		modelMap.addAttribute("bean", bean);
		modelMap.addAttribute("parent", parent);
		modelMap.addAttribute("model", model);
		modelMap.addAttribute("roleList", roleList);
		modelMap.addAttribute("groupList", groupList);
		modelMap.addAttribute("orgTreeNumber", orgTreeNumber);
		modelMap.addAttribute("nodeModelList", nodeModelList);
		modelMap.addAttribute("infoModel", infoModel);
		modelMap.addAttribute("infoModelList", infoModelList);
		modelMap.addAttribute("queryParentId", queryParentId);
		modelMap.addAttribute("showDescendants", showDescendants);
		modelMap.addAttribute(OPRT, CREATE);
		return "core/node/node_form";
	}

	@RequiresPermissions("core:node:edit")
	@RequestMapping("edit.do")
	public String edit(
			Integer id,
			Integer modelId,
			Integer queryParentId,
			@RequestParam(defaultValue = "false") boolean showDescendants,
			Integer position,
			@PageableDefaults(sort = { "treeNumber", "id" }, sortDir = Direction.ASC) Pageable pageable,
			HttpServletRequest request, org.springframework.ui.Model modelMap) {
		User user = Context.getCurrentUser(request);
		Site site = Context.getCurrentSite(request);
		Integer siteId = site.getId();
		Node bean = query.get(id);
		if (!bean.isDataPerm(user)) {
			throw new CmsException("accessDenied");
		}
		Node parent = bean.getParent();
		String treeNumber = null;
		if (showDescendants && parent != null) {
			treeNumber = parent.getTreeNumber();
		}
		boolean isAllNodePerm = user.getAllNodePerm(siteId);
		Map<String, String[]> params = Servlets.getParameterValuesMap(request,
				Constants.SEARCH_PREFIX);
		RowSide<Node> side = query.findSide(siteId, treeNumber, queryParentId,
				user.getId(), isAllNodePerm, params, bean, position,
				pageable.getSort());
		modelMap.addAttribute("bean", bean);
		modelMap.addAttribute("side", side);
		modelMap.addAttribute("position", position);
		List<Model> infoModelList = modelService.findList(siteId,
				Info.MODEL_TYPE);
		List<Model> modelList = modelService.findList(siteId,
				Node.NODE_MODEL_TYPE);
		List<Model> nodeModelList;
		if (bean.getParent() == null) {
			// 首页模型
			nodeModelList = modelService.findList(siteId, Node.HOME_MODEL_TYPE);
		} else {
			nodeModelList = modelList;
		}
		Model model;
		// 允许修改栏目模型
		if (modelId != null) {
			model = modelService.get(modelId);
		} else {
			model = bean.getNodeModel();
		}
		List<Role> roleList = roleService.findList(siteId);
		List<MemberGroup> groupList = memberGroupService.findList();
		String orgTreeNumber = site.getOrg().getTreeNumber();
		List<Workflow> workflowList = workflowService.findList(siteId);
		modelMap.addAttribute("workflowList", workflowList);

		modelMap.addAttribute("model", model);
		modelMap.addAttribute("parent", parent);
		modelMap.addAttribute("bean", bean);
		modelMap.addAttribute("modelList", modelList);
		modelMap.addAttribute("roleList", roleList);
		modelMap.addAttribute("groupList", groupList);
		modelMap.addAttribute("orgTreeNumber", orgTreeNumber);
		modelMap.addAttribute("nodeModelList", nodeModelList);
		modelMap.addAttribute("infoModelList", infoModelList);
		modelMap.addAttribute("infoModel", bean.getInfoModel());
		modelMap.addAttribute("queryParentId", queryParentId);
		modelMap.addAttribute("showDescendants", showDescendants);
		modelMap.addAttribute(OPRT, EDIT);
		return "core/node/node_form";
	}

	@RequiresPermissions("core:node:save")
	@RequestMapping("save.do")
	public String save(Integer cid, Node bean, NodeDetail detail,
			Integer[] infoPermIds, Integer[] nodePermIds,
			Integer[] viewGroupIds, Integer[] contriGroupIds,
			Integer[] commentGroupIds, Integer[] viewOrgIds, Integer parentId,
			Integer nodeModelId, Integer infoModelId, Integer workflowId,
			@RequestParam(defaultValue = "false") boolean infoPermIdsExist,
			@RequestParam(defaultValue = "false") boolean nodePermIdsExist,
			@RequestParam(defaultValue = "false") boolean viewGroupIdsExist,
			@RequestParam(defaultValue = "false") boolean contriGroupIdsExist,
			@RequestParam(defaultValue = "false") boolean commentGroupIdsExist,
			Integer queryParentId, Boolean showDescendants, String redirect,
			HttpServletRequest request, RedirectAttributes ra) {
		Integer siteId = Context.getCurrentSiteId(request);
		Integer userId = Context.getCurrentUserId(request);
		if (!infoPermIdsExist) {
			infoPermIds = getInfoPermIds(siteId, parentId);
		}
		if (!nodePermIdsExist) {
			nodePermIds = getNodePermIds(siteId, parentId);
		}
		if (!viewGroupIdsExist) {
			viewGroupIds = getViewGroupIds(siteId, parentId);
		}
		if (!contriGroupIdsExist) {
			contriGroupIds = getContriGroupIds(siteId, parentId);
		}
		if (!commentGroupIdsExist) {
			commentGroupIds = getCommentGroupIds(siteId, parentId);
		}
		Map<String, String> customs = Servlets.getParameterMap(request,
				"customs_");
		Map<String, String> clobs = Servlets.getParameterMap(request, "clobs_");
		service.save(bean, detail, customs, clobs, infoPermIds, nodePermIds,
				viewGroupIds, contriGroupIds, commentGroupIds, viewOrgIds,
				parentId, nodeModelId, infoModelId, workflowId, userId, siteId);
		String ip = Servlets.getRemoteAddr(request);
		logService.operation("opr.node.add", bean.getTitle(), null,
				bean.getId(), ip, userId, siteId);
		logger.info("save Node, name={}.", bean.getName());
		ra.addAttribute("queryParentId", queryParentId);
		ra.addAttribute("showDescendants", showDescendants);
		ra.addFlashAttribute(MESSAGE, SAVE_SUCCESS);
		ra.addFlashAttribute("refreshLeft", true);
		if (Constants.REDIRECT_LIST.equals(redirect)) {
			return "redirect:list.do";
		} else if (Constants.REDIRECT_CREATE.equals(redirect)) {
			ra.addAttribute("parentId", parentId);
			return "redirect:create.do";
		} else {
			ra.addAttribute("id", bean.getId());
			return "redirect:edit.do";
		}
	}

	@RequiresPermissions("core:node:update")
	@RequestMapping("update.do")
	public String update(@ModelAttribute("bean") Node bean,
			@ModelAttribute("detail") NodeDetail detail, Integer[] infoPermIds,
			Integer[] nodePermIds, Integer[] viewGroupIds,
			Integer[] contriGroupIds, Integer[] commentGroupIds,
			Integer[] viewOrgIds, Integer parentId, Integer nodeModelId,
			Integer infoModelId, Integer workflowId,
			@RequestParam(defaultValue = "false") boolean infoPermIdsExist,
			@RequestParam(defaultValue = "false") boolean nodePermIdsExist,
			@RequestParam(defaultValue = "false") boolean viewGroupIdsExist,
			@RequestParam(defaultValue = "false") boolean contriGroupIdsExist,
			@RequestParam(defaultValue = "false") boolean commentGroupIdsExist,
			Integer position, Integer queryParentId, Boolean showDescendants,
			String redirect, HttpServletRequest request, RedirectAttributes ra) {
		User user = Context.getCurrentUser(request);
		if (!bean.isDataPerm(user)) {
			throw new CmsException("accessDenied");
		}
		if (infoPermIdsExist && infoPermIds == null) {
			infoPermIds = new Integer[0];
		}
		if (nodePermIdsExist && nodePermIds == null) {
			nodePermIds = new Integer[0];
		}
		if (viewGroupIdsExist && viewGroupIds == null) {
			viewGroupIds = new Integer[0];
		}
		if (contriGroupIdsExist && contriGroupIds == null) {
			contriGroupIds = new Integer[0];
		}
		if (commentGroupIdsExist && commentGroupIds == null) {
			commentGroupIds = new Integer[0];
		}
		if (viewOrgIds == null) {
			viewOrgIds = new Integer[0];
		}
		Integer siteId = Context.getCurrentSiteId(request);
		Map<String, String> customs = Servlets.getParameterMap(request,
				"customs_");
		Map<String, String> clobs = Servlets.getParameterMap(request, "clobs_");
		service.update(bean, detail, customs, clobs, infoPermIds, nodePermIds,
				viewGroupIds, contriGroupIds, commentGroupIds, viewOrgIds,
				nodeModelId, infoModelId, workflowId);
		Integer userId = Context.getCurrentUserId(request);
		String ip = Servlets.getRemoteAddr(request);
		logService.operation("opr.node.edit", bean.getTitle(), null,
				bean.getId(), ip, userId, siteId);
		logger.info("update Node, name={}.", bean.getName());
		// 移动节点
		if (parentId != null && bean.getParent() != null
				&& !parentId.equals(bean.getParent().getId())) {
			service.move(new Integer[] { bean.getId() }, parentId, siteId);
		}
		logger.info("update Node, name={}.", bean.getName());
		ra.addAttribute("queryParentId", queryParentId);
		ra.addAttribute("showDescendants", showDescendants);
		ra.addFlashAttribute(MESSAGE, SAVE_SUCCESS);
		ra.addFlashAttribute("refreshLeft", true);
		if (Constants.REDIRECT_LIST.equals(redirect)) {
			return "redirect:list.do";
		} else {
			ra.addAttribute("id", bean.getId());
			ra.addAttribute("position", position);
			return "redirect:edit.do";
		}
	}

	@RequiresPermissions("core:node:batch_update")
	@RequestMapping("batch_update.do")
	public String batchUpdate(Integer[] id, String[] name, String[] number,
			Integer[] views, Boolean[] hidden, Integer queryParentId,
			Boolean showDescendants, Pageable pageable,
			HttpServletRequest request, RedirectAttributes ra) {
		Integer siteId = Context.getCurrentSiteId(request);
		if (ArrayUtils.isNotEmpty(id)) {
			// 有排序的情况下不更新树结构，以免引误操作。
			boolean isUpdateTree = pageable.getSort() == null;
			Node[] beans = service.batchUpdate(id, name, number, views, hidden,
					siteId, isUpdateTree);
			Integer userId = Context.getCurrentUserId(request);
			String ip = Servlets.getRemoteAddr(request);
			for (Node bean : beans) {
				logService.operation("opr.node.batchEdit", bean.getTitle(),
						null, bean.getId(), ip, userId, siteId);
				logger.info("batch update Node, name={}.", bean.getName());
			}
		}
		ra.addAttribute("queryParentId", queryParentId);
		ra.addAttribute("showDescendants", showDescendants);
		ra.addFlashAttribute(MESSAGE, SAVE_SUCCESS);
		ra.addFlashAttribute("refreshLeft", true);
		return "redirect:list.do";
	}

	@RequiresPermissions("core:node:delete")
	@RequestMapping("delete.do")
	public String delete(Integer[] ids, Integer queryParentId,
			Boolean showDescendants, HttpServletRequest request,
			RedirectAttributes ra) {
		User user = Context.getCurrentUser(request);
		for (Integer id : ids) {
			Node bean = query.get(id);
			if (!bean.isDataPerm(user)) {
				throw new CmsException("accessDenied");
			}
		}
		Node[] beans = service.delete(ids);
		Integer siteId = Context.getCurrentSiteId(request);
		Integer userId = Context.getCurrentUserId(request);
		String ip = Servlets.getRemoteAddr(request);
		for (Node bean : beans) {
			logService.operation("opr.node.delete", bean.getTitle(), null,
					bean.getId(), ip, userId, siteId);
			logger.info("delete Node, name={}.", bean.getName());
		}
		ra.addAttribute("queryParentId", queryParentId);
		ra.addAttribute("showDescendants", showDescendants);
		ra.addFlashAttribute(MESSAGE, DELETE_SUCCESS);
		ra.addFlashAttribute("refreshLeft", true);
		return "redirect:list.do";
	}

	@RequiresPermissions("core:node:move_form")
	@RequestMapping("move_form.do")
	public String moveForm(Integer[] ids, Boolean noChecked,
			Integer queryParentId, Boolean showDescendants,
			HttpServletRequest request, org.springframework.ui.Model modelMap) {
		User user = Context.getCurrentUser(request);
		Integer siteId = Context.getCurrentSiteId(request);
		List<Node> selectedList = query.findByIds(ids);
		// 获得已选择节点的所有父节点ID，用于展开树。
		Set<Integer> selectedPids = new HashSet<Integer>();
		// 获得选中节点的ID
		Set<Integer> selectedIds = new HashSet<Integer>();
		for (Node node : selectedList) {
			selectedIds.add(node.getId());
			Node parent = node.getParent();
			while (parent != null) {
				selectedPids.add(parent.getId());
				parent = parent.getParent();
			}
		}
		List<Node> list = query.findList(siteId, null);
		modelMap.addAttribute("nodePerms", user.getNodePerms(siteId));
		modelMap.addAttribute("list", list);
		modelMap.addAttribute("noChecked", noChecked);
		modelMap.addAttribute("selectedList", selectedList);
		modelMap.addAttribute("selectedIds", selectedIds.toArray());
		modelMap.addAttribute("selectedPids", selectedPids.toArray());
		modelMap.addAttribute("queryParentId", queryParentId);
		modelMap.addAttribute("showDescendants", showDescendants);
		return "core/node/node_move";
	}

	@RequiresPermissions("core:node:move_submit")
	@RequestMapping("move_submit.do")
	public String moveSubmit(Integer[] ids, Integer id, Integer queryParentId,
			Boolean showDescendants, HttpServletRequest request,
			RedirectAttributes ra) {
		Integer siteId = Context.getCurrentSiteId(request);
		service.move(ids, id, siteId);
		Integer userId = Context.getCurrentUserId(request);
		String ip = Servlets.getRemoteAddr(request);
		Node bean;
		for (Integer bid : ids) {
			bean = query.get(bid);
			logService.operation("opr.node.move", bean.getTitle(), null,
					bean.getId(), ip, userId, siteId);
			logger.info("move Node, name={}.", bean.getName());
		}
		// bug??
		// ra.addAttribute("ids", ids);
		ra.addAttribute("noChecked", true);
		ra.addAttribute("queryParentId", queryParentId);
		ra.addAttribute("showDescendants", showDescendants);
		ra.addFlashAttribute(MESSAGE, SAVE_SUCCESS);
		ra.addFlashAttribute("refreshLeft", true);
		return "redirect:move_form.do?ids=" + StringUtils.join(ids, "&ids=");
	}

	@RequiresPermissions("core:node:merge_form")
	@RequestMapping("merge_form.do")
	public String mergeForm(
			@RequestParam(defaultValue = "true") boolean deleteMergedNode,
			Boolean noChecked, Integer queryParentId, Boolean showDescendants,
			HttpServletRequest request, org.springframework.ui.Model modelMap) {
		User user = Context.getCurrentUser(request);
		Integer siteId = Context.getCurrentSiteId(request);
		// 获得已选择节点的所有父节点ID，用于展开树。
		Set<Integer> selectedPids = new HashSet<Integer>();
		// 获得选中节点的ID
		List<Node> list = query.findList(siteId, null);
		modelMap.addAttribute("nodePerms", user.getNodePerms(siteId));
		modelMap.addAttribute("list", list);
		modelMap.addAttribute("deleteMergedNode", deleteMergedNode);
		modelMap.addAttribute("noChecked", noChecked);
		modelMap.addAttribute("selectedPids", selectedPids.toArray());
		modelMap.addAttribute("queryParentId", queryParentId);
		modelMap.addAttribute("showDescendants", showDescendants);
		return "core/node/node_merge";
	}

	@RequiresPermissions("core:node:merge_submit")
	@RequestMapping("merge_submit.do")
	public String mergeSubmit(Integer[] ids, Integer id,
			@RequestParam(defaultValue = "true") boolean deleteMergedNode,
			Integer queryParentId, Boolean showDescendants,
			HttpServletRequest request, RedirectAttributes ra) {
		service.merge(ids, id, deleteMergedNode);
		Integer siteId = Context.getCurrentSiteId(request);
		Integer userId = Context.getCurrentUserId(request);
		String ip = Servlets.getRemoteAddr(request);
		Node bean;
		for (Integer bid : ids) {
			bean = query.get(bid);
			logService.operation("opr.node.merge", bean.getTitle(), null,
					bean.getId(), ip, userId, siteId);
			logger.info("merge Node, name={}.", bean.getName());
		}
		ra.addAttribute("deleteMergedNode", deleteMergedNode);
		ra.addAttribute("noChecked", true);
		ra.addAttribute("queryParentId", queryParentId);
		ra.addAttribute("showDescendants", showDescendants);
		ra.addFlashAttribute(MESSAGE, SAVE_SUCCESS);
		ra.addFlashAttribute("refreshLeft", true);
		return "redirect:merge_form.do";
	}

	@ModelAttribute
	public void preloadBean(@RequestParam(required = false) Integer oid,
			org.springframework.ui.Model modelMap) {
		if (oid != null) {
			Node bean = query.get(oid);
			if (bean != null) {
				modelMap.addAttribute("bean", bean);
				NodeDetail detail = bean.getDetail();
				if (detail == null) {
					detail = new NodeDetail();
					detail.setId(oid);
					detail.setNode(bean);
				}
				modelMap.addAttribute("detail", detail);
			}
		}
	}

	private Integer[] getInfoPermIds(Integer siteId, Integer parentId) {
		Collection<Role> roles = null;
		if (parentId != null) {
			Node node = query.get(parentId);
			if (node != null) {
				roles = node.getInfoPerms();
			}
		}
		if (roles == null) {
			roles = roleService.findList(siteId);
		}
		int len = roles.size();
		Integer[] ids = new Integer[len];
		int i = 0;
		for (Role role : roles) {
			ids[i++] = role.getId();
		}
		return ids;
	}

	private Integer[] getNodePermIds(Integer siteId, Integer parentId) {
		Collection<Role> roles = null;
		if (parentId != null) {
			Node node = query.get(parentId);
			if (node != null) {
				roles = node.getNodePerms();
			}
		}
		if (roles == null) {
			roles = roleService.findList(siteId);
		}
		int len = roles.size();
		Integer[] ids = new Integer[len];
		int i = 0;
		for (Role role : roles) {
			ids[i++] = role.getId();
		}
		return ids;
	}

	private Integer[] getViewGroupIds(Integer siteId, Integer parentId) {
		Collection<MemberGroup> groups = null;
		if (parentId != null) {
			Node node = query.get(parentId);
			if (node != null) {
				groups = node.getViewGroups();
			}
		}
		if (groups == null) {
			groups = memberGroupService.findList();
		}
		int len = groups.size();
		Integer[] ids = new Integer[len];
		int i = 0;
		for (MemberGroup group : groups) {
			ids[i++] = group.getId();
		}
		return ids;
	}

	private Integer[] getContriGroupIds(Integer siteId, Integer parentId) {
		Collection<MemberGroup> groups = null;
		if (parentId != null) {
			Node node = query.get(parentId);
			if (node != null) {
				groups = node.getContriGroups();
			}
		}
		if (groups == null) {
			groups = memberGroupService.findList();
		}
		int len = groups.size();
		Integer[] ids = new Integer[len];
		int i = 0;
		for (MemberGroup group : groups) {
			ids[i++] = group.getId();
		}
		return ids;
	}

	private Integer[] getCommentGroupIds(Integer siteId, Integer parentId) {
		Collection<MemberGroup> groups = null;
		if (parentId != null) {
			Node node = query.get(parentId);
			if (node != null) {
				groups = node.getCommentGroups();
			}
		}
		if (groups == null) {
			groups = memberGroupService.findList();
		}
		int len = groups.size();
		Integer[] ids = new Integer[len];
		int i = 0;
		for (MemberGroup group : groups) {
			ids[i++] = group.getId();
		}
		return ids;
	}

	@Autowired
	private OperationLogService logService;
	@Autowired
	private RoleService roleService;
	@Autowired
	private MemberGroupService memberGroupService;
	@Autowired
	private WorkflowService workflowService;
	@Autowired
	private ModelService modelService;
	@Autowired
	private NodeService service;
	@Autowired
	private NodeQueryService query;
}
