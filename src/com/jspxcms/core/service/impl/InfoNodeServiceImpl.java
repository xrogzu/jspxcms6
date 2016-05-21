package com.jspxcms.core.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jspxcms.core.domain.Info;
import com.jspxcms.core.domain.InfoNode;
import com.jspxcms.core.domain.Node;
import com.jspxcms.core.repository.InfoNodeDao;
import com.jspxcms.core.service.InfoNodeService;
import com.jspxcms.core.service.NodeQueryService;

@Service
@Transactional(readOnly = true)
public class InfoNodeServiceImpl implements InfoNodeService {
	@Transactional
	public List<InfoNode> save(Info info, Integer[] nodeIds, Integer nodeId) {
		if (nodeIds == null) {
			nodeIds = new Integer[0];
		} else {
			nodeIds = ArrayUtils.removeElement(nodeIds, nodeId);
		}
		int size = ArrayUtils.getLength(nodeIds) + 1;
		List<InfoNode> infoNodes = info.getInfoNodes();
		if (infoNodes == null) {
			infoNodes = new ArrayList<InfoNode>(size);
			info.setInfoNodes(infoNodes);
		}
		infoNodes.add(save(info, nodeId, 0));
		if (size > 1) {
			for (int i = 0, len = nodeIds.length; i < len; i++) {
				infoNodes.add(save(info, nodeIds[i], i + 1));
			}
		}
		return infoNodes;
	}

	@Transactional
	private InfoNode save(Info info, Integer nodeId, int index) {
		InfoNode infoNode = new InfoNode();
		Node node = nodeQueryService.get(nodeId);
		infoNode.setNode(node);
		infoNode.setInfo(info);
		infoNode.setNodeIndex(index);
		dao.save(infoNode);
		return infoNode;
	}

	@Transactional
	public List<InfoNode> update(Info info, Integer[] nodeIds, Integer nodeId) {
		// 主栏目为null，不更新。
		if (nodeId == null) {
			return info.getInfoNodes();
		}
		if (nodeIds == null) {
			nodeIds = new Integer[0];
		} else {
			nodeIds = ArrayUtils.removeElement(nodeIds, nodeId);
		}
		List<InfoNode> infoNodes = info.getInfoNodes();
		// 处理主栏目
		if (!infoNodes.isEmpty()) {
			InfoNode infoNode = infoNodes.get(0);
			if (!infoNode.getNode().getId().equals(nodeId)) {
				infoNodes.set(0, save(info, nodeId, 0));
				dao.delete(infoNode);
			}
		} else {
			infoNodes.add(0, save(info, nodeId, 0));
		}
		// 先删除
		Set<InfoNode> tobeDelete = new HashSet<InfoNode>();
		// 从第二个栏目开始，第一个是主栏目。
		for (int i = 1, len = infoNodes.size(); i < len; i++) {
			InfoNode infoNode = infoNodes.get(i);
			Integer nid = infoNode.getNode().getId();
			if (!ArrayUtils.contains(nodeIds, nid) || nodeId.equals(nid)) {
				tobeDelete.add(infoNode);
			}
		}
		infoNodes.removeAll(tobeDelete);
		dao.delete(tobeDelete);
		// 再新增
		for (int i = 0, len = nodeIds.length; i < len; i++) {
			boolean contains = false;
			for (InfoNode infoNode : infoNodes) {
				if (infoNode.getNode().getId().equals(nodeIds[i])) {
					infoNode.setNodeIndex(i + 1);
					infoNodes.remove(infoNode);
					infoNodes.add(i + 1, infoNode);
					contains = true;
					break;
				}
			}
			if (!contains) {
				infoNodes.add(save(info, nodeIds[i], i + 1));
			}
		}
		return infoNodes;
	}

	@Transactional
	public int moveByNodeId(Collection<Integer> nodeIds, Integer nodeId) {
		return dao.moveByNodeId(nodeIds, nodeId);
	}

	@Transactional
	public int deleteByNodeId(Integer nodeId) {
		return dao.deleteByNodeId(nodeId);
	}

	@Transactional
	public int deleteByInfoId(Integer infoId) {
		return dao.deleteByNodeId(infoId);
	}

	private NodeQueryService nodeQueryService;

	@Autowired
	public void setNodeQueryService(NodeQueryService nodeQueryService) {
		this.nodeQueryService = nodeQueryService;
	}

	private InfoNodeDao dao;

	@Autowired
	public void setDao(InfoNodeDao dao) {
		this.dao = dao;
	}
}
