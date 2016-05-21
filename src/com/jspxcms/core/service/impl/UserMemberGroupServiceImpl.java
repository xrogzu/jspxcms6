package com.jspxcms.core.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jspxcms.core.domain.MemberGroup;
import com.jspxcms.core.domain.User;
import com.jspxcms.core.domain.UserMemberGroup;
import com.jspxcms.core.listener.MemberGroupDeleteListener;
import com.jspxcms.core.repository.UserMemberGroupDao;
import com.jspxcms.core.service.MemberGroupService;
import com.jspxcms.core.service.UserMemberGroupService;

@Service
@Transactional(readOnly = true)
public class UserMemberGroupServiceImpl implements UserMemberGroupService,
		MemberGroupDeleteListener {

	@Transactional
	public List<UserMemberGroup> save(User user, Integer[] groupIds,
			Integer groupId) {
		if (groupIds == null) {
			groupIds = new Integer[0];
		} else {
			groupIds = ArrayUtils.removeElement(groupIds, groupId);
		}
		int size = ArrayUtils.getLength(groupIds) + 1;
		List<UserMemberGroup> userGroups = user.getUserGroups();
		if (userGroups == null) {
			userGroups = new ArrayList<UserMemberGroup>(size);
			user.setUserGroups(userGroups);
		}
		userGroups.add(save(user, groupId, 0));
		if (size > 1) {
			for (int i = 0, len = groupIds.length; i < len; i++) {
				userGroups.add(save(user, groupIds[i], i++));
			}
		}
		return userGroups;
	}

	@Transactional
	private UserMemberGroup save(User user, Integer groupId, int index) {
		UserMemberGroup userGroup = new UserMemberGroup();
		MemberGroup group = memberGroupService.get(groupId);
		userGroup.setGroup(group);
		userGroup.setUser(user);
		userGroup.setGroupIndex(index);
		dao.save(userGroup);
		return userGroup;
	}

	@Transactional
	public List<UserMemberGroup> update(User user, Integer[] groupIds,
			Integer groupId) {
		// 主会员组为null，不更新。
		if (groupId == null) {
			return user.getUserGroups();
		}
		if (groupIds == null) {
			groupIds = new Integer[0];
		} else {
			groupIds = ArrayUtils.removeElement(groupIds, groupId);
		}
		List<UserMemberGroup> userGroups = user.getUserGroups();
		// 处理主会员组
		if (!userGroups.isEmpty()) {
			UserMemberGroup userGroup = userGroups.get(0);
			if (!userGroup.getGroup().getId().equals(groupId)) {
				userGroups.set(0, save(user, groupId, 0));
				dao.delete(userGroup);
			}
		} else {
			userGroups.add(0, save(user, groupId, 0));
		}
		// 先删除
		Set<UserMemberGroup> tobeDelete = new HashSet<UserMemberGroup>();
		// 从第二个会员组开始，第一个是主会员组。
		for (int i = 1, len = userGroups.size(); i < len; i++) {
			UserMemberGroup userGroup = userGroups.get(i);
			Integer gid = userGroup.getGroup().getId();
			if (!ArrayUtils.contains(groupIds, gid) || groupId.equals(gid)) {
				tobeDelete.add(userGroup);
			}
		}
		userGroups.removeAll(tobeDelete);
		dao.delete(tobeDelete);
		// 再新增
		for (int i = 0, len = groupIds.length; i < len; i++) {
			boolean contains = false;
			for (UserMemberGroup userGroup : userGroups) {
				if (userGroup.getGroup().getId().equals(groupIds[i])) {
					userGroup.setGroupIndex(i + 1);
					userGroups.remove(userGroup);
					userGroups.add(i + 1, userGroup);
					contains = true;
					break;
				}
			}
			if (!contains) {
				userGroups.add(save(user, groupIds[i], i + 1));
			}
		}
		return userGroups;
	}

	public void preMemberGroupDelete(Integer[] ids) {
		if (ids == null) {
			return;
		}
		for (Integer id : ids) {
			dao.deleteByGroupId(id);
		}
	}

	private MemberGroupService memberGroupService;

	@Autowired
	public void setMemberGroupService(MemberGroupService memberGroupService) {
		this.memberGroupService = memberGroupService;
	}

	private UserMemberGroupDao dao;

	@Autowired
	public void setDao(UserMemberGroupDao dao) {
		this.dao = dao;
	}
}
