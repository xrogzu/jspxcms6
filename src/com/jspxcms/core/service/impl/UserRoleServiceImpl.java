package com.jspxcms.core.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jspxcms.core.domain.Role;
import com.jspxcms.core.domain.User;
import com.jspxcms.core.domain.UserRole;
import com.jspxcms.core.listener.RoleDeleteListener;
import com.jspxcms.core.repository.UserRoleDao;
import com.jspxcms.core.service.RoleService;
import com.jspxcms.core.service.UserRoleService;

@Service
@Transactional(readOnly = true)
public class UserRoleServiceImpl implements UserRoleService, RoleDeleteListener {
	@Transactional
	public List<UserRole> save(User user, Integer[] roleIds) {
		List<UserRole> userRoles = user.getUserRoles();
		if (userRoles == null) {
			userRoles = new ArrayList<UserRole>();
			user.setUserRoles(userRoles);
		}
		if (ArrayUtils.isEmpty(roleIds)) {
			return userRoles;
		}
		for (int i = 0, len = roleIds.length; i < len; i++) {
			userRoles.add(save(user, roleIds[i], i));
		}
		return userRoles;
	}

	@Transactional
	public UserRole save(User user, Role role, Integer index) {
		UserRole bean = new UserRole(user, role, index);
		bean.applyDefaultValue();
		dao.save(bean);
		return bean;
	}

	@Transactional
	private UserRole save(User user, Integer roleId, int index) {
		Role role = roleService.get(roleId);
		return save(user, role, index);
	}

	@Transactional
	public List<UserRole> update(User user, Integer[] roleIds, Integer siteId) {
		if (roleIds == null) {
			roleIds = new Integer[0];
		}
		List<UserRole> userRoles = user.getUserRoles();
		// 先删除
		Set<UserRole> tobeDelete = new HashSet<UserRole>();
		for (UserRole userRole : userRoles) {
			Role role = userRole.getRole();
			Integer rid = role.getId();
			Integer sid = role.getSite().getId();
			if ((siteId == null || sid.equals(siteId))
					&& !ArrayUtils.contains(roleIds, rid)) {
				tobeDelete.add(userRole);
			}
		}
		userRoles.removeAll(tobeDelete);
		dao.delete(tobeDelete);
		// 再新增
		for (int i = 0, len = roleIds.length; i < len; i++) {
			boolean contains = false;
			for (UserRole userRole : userRoles) {
				if (userRole.getRole().getId().equals(roleIds[i])) {
					userRole.setRoleIndex(i);
					userRoles.remove(userRole);
					userRoles.add(i, userRole);
					contains = true;
					break;
				}
			}
			if (!contains) {
				userRoles.add(save(user, roleIds[i], i));
			}
		}
		return userRoles;
	}

	@Transactional
	public int deleteByUserId(Integer userId) {
		return dao.deleteByUserId(userId);
	}

	@Transactional
	public int deleteByRoleId(Integer roleId) {
		return dao.deleteByRoleId(roleId);
	}

	public void preRoleDelete(Integer[] ids) {
		if (ids != null) {
			for (Integer id : ids) {
				dao.deleteByRoleId(id);
			}
		}
	}

	private RoleService roleService;

	@Autowired
	public void setRoleService(RoleService roleService) {
		this.roleService = roleService;
	}

	private UserRoleDao dao;

	@Autowired
	public void setDao(UserRoleDao dao) {
		this.dao = dao;
	}
}
