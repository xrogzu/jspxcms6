package com.jspxcms.core.service.impl;

import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jspxcms.core.domain.Node;
import com.jspxcms.core.domain.NodeOrg;
import com.jspxcms.core.domain.NodeOrg.NodeOrgComparator;
import com.jspxcms.core.domain.Org;
import com.jspxcms.core.listener.NodeDeleteListener;
import com.jspxcms.core.listener.OrgDeleteListener;
import com.jspxcms.core.repository.NodeOrgDao;
import com.jspxcms.core.service.NodeOrgService;
import com.jspxcms.core.service.OrgService;

@Service
@Transactional(readOnly = true)
public class NodeOrgServiceImpl implements NodeOrgService, NodeDeleteListener,
		OrgDeleteListener {
	@Transactional
	public NodeOrg save(Node node, Integer orgId, Boolean viewPerm) {
		NodeOrg bean = new NodeOrg();
		bean.setNode(node);
		Org org = orgService.get(orgId);
		bean.setOrg(org);
		bean.setViewPerm(viewPerm);
		bean.applyDefaultValue();
		bean = dao.save(bean);
		return bean;
	}

	@Transactional
	public void update(Node node, Integer[] viewOrgIds) {
		if (viewOrgIds == null) {
			viewOrgIds = new Integer[0];
		}
		SortedSet<NodeOrg> nodeOrgs = node.getNodeOrgs();
		if (nodeOrgs == null) {
			nodeOrgs = new TreeSet<NodeOrg>(new NodeOrgComparator());
			node.setNodeOrgs(nodeOrgs);
		}
		// 先更新
		for (NodeOrg nodeOrg : nodeOrgs) {
			if (ArrayUtils.contains(viewOrgIds, nodeOrg.getOrg().getId())) {
				nodeOrg.setViewPerm(true);
			} else {
				nodeOrg.setViewPerm(false);
			}
		}
		// 再新增
		for (Integer viewOrgId : viewOrgIds) {
			boolean contains = false;
			for (NodeOrg nodeOrg : nodeOrgs) {
				if (nodeOrg.getOrg().getId().equals(viewOrgId)) {
					contains = true;
					break;
				}
			}
			if (!contains) {
				nodeOrgs.add(save(node, viewOrgId, true));
			}
		}
	}

	public void preNodeDelete(Integer[] ids) {
		if (ArrayUtils.isEmpty(ids)) {
			return;
		}
		for (Integer id : ids) {
			dao.deleteByNodeId(id);
		}
	}

	public void preOrgDelete(Integer[] ids) {
		if (ArrayUtils.isEmpty(ids)) {
			return;
		}
		for (Integer id : ids) {
			dao.deleteByOrgId(id);
		}
	}

	private OrgService orgService;

	@Autowired
	public void setOrgService(OrgService orgService) {
		this.orgService = orgService;
	}

	private NodeOrgDao dao;

	@Autowired
	public void setDao(NodeOrgDao dao) {
		this.dao = dao;
	}
}
