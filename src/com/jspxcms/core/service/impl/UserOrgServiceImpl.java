package com.jspxcms.core.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jspxcms.core.domain.Org;
import com.jspxcms.core.domain.User;
import com.jspxcms.core.domain.UserOrg;
import com.jspxcms.core.listener.OrgDeleteListener;
import com.jspxcms.core.repository.UserOrgDao;
import com.jspxcms.core.service.OrgService;
import com.jspxcms.core.service.UserOrgService;

@Service
@Transactional(readOnly = true)
public class UserOrgServiceImpl implements UserOrgService, OrgDeleteListener {
	@Transactional
	public List<UserOrg> save(User user, Integer[] orgIds, Integer orgId) {
		if (orgIds == null) {
			orgIds = new Integer[0];
		} else {
			orgIds = ArrayUtils.removeElement(orgIds, orgId);
		}
		int size = ArrayUtils.getLength(orgIds) + 1;
		List<UserOrg> userOrgs = user.getUserOrgs();
		if (userOrgs == null) {
			userOrgs = new ArrayList<UserOrg>(size);
			user.setUserOrgs(userOrgs);
		}
		userOrgs.add(save(user, orgId, 0));
		if (size > 1) {
			for (int i = 0, len = orgIds.length; i < len; i++) {
				userOrgs.add(save(user, orgIds[i], i + 1));
			}
		}
		return userOrgs;
	}

	@Transactional
	private UserOrg save(User user, Integer orgId, int index) {
		UserOrg userOrg = new UserOrg();
		Org org = orgService.get(orgId);
		userOrg.setUser(user);
		userOrg.setOrg(org);
		userOrg.setOrgIndex(index);
		dao.save(userOrg);
		return userOrg;
	}

	@Transactional
	public List<UserOrg> update(User user, Integer[] orgIds, Integer orgId,
			Integer topOrgId) {
		// 主组织ID不存在，不更新。有时候更新用户其他信息，但不更新组织，这时组织ID可以传null。
		if (orgId == null) {
			return user.getUserOrgs();
		}
		if (orgIds == null) {
			orgIds = new Integer[0];
		} else {
			orgIds = ArrayUtils.removeElement(orgIds, orgId);
		}
		List<UserOrg> userOrgs = user.getUserOrgs();

		// 处理主组织
		if (!userOrgs.isEmpty()) {
			UserOrg userOrg = userOrgs.get(0);
			if (!userOrg.getOrg().getId().equals(orgId)) {
				userOrgs.set(0, save(user, orgId, 0));
				dao.delete(userOrg);
			}
		} else {
			userOrgs.add(0, save(user, orgId, 0));
		}

		String treeNumber = null;
		if (topOrgId != null) {
			treeNumber = orgService.get(topOrgId).getTreeNumber();
		}

		// 先删除
		// 从第二个组织开始，第一个是主组织。
		Set<UserOrg> tobeDelete = new HashSet<UserOrg>();
		for (int i = 1, len = userOrgs.size(); i < len; i++) {
			UserOrg userOrg = userOrgs.get(i);
			Org org = userOrg.getOrg();
			Integer oid = org.getId();
			String otreeNumber = org.getTreeNumber();
			if (orgId.equals(oid)) {
				tobeDelete.add(userOrg);
			} else if (treeNumber == null || otreeNumber.startsWith(treeNumber)) {
				if (!ArrayUtils.contains(orgIds, oid)) {
					tobeDelete.add(userOrg);
				}
			}
		}

		userOrgs.removeAll(tobeDelete);
		dao.delete(tobeDelete);

		// 再新增
		for (int i = 0, len = orgIds.length; i < len; i++) {
			boolean contains = false;
			for (UserOrg userOrg : userOrgs) {
				if (userOrg.getOrg().getId().equals(orgIds[i])) {
					userOrg.setOrgIndex(i + 1);
					userOrgs.remove(userOrg);
					userOrgs.add(i + 1, userOrg);
					contains = true;
					break;
				}
			}
			if (!contains) {
				userOrgs.add(save(user, orgIds[i], i + 1));
			}
		}
		return userOrgs;
	}

	@Transactional
	public int deleteByUserId(Integer userId) {
		return dao.deleteByUserId(userId);
	}

	@Transactional
	public int deleteByOrgId(Integer orgId) {
		return dao.deleteByOrgId(orgId);
	}

	@Transactional
	public void preOrgDelete(Integer[] ids) {
		if (ids != null) {
			for (Integer id : ids) {
				dao.deleteByOrgId(id);
			}
		}
	}

	private OrgService orgService;

	@Autowired
	public void setOrgService(OrgService orgService) {
		this.orgService = orgService;
	}

	private UserOrgDao dao;

	@Autowired
	public void setDao(UserOrgDao dao) {
		this.dao = dao;
	}
}
