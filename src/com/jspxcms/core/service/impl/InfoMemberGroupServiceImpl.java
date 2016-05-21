package com.jspxcms.core.service.impl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jspxcms.core.domain.Info;
import com.jspxcms.core.domain.InfoMemberGroup;
import com.jspxcms.core.domain.MemberGroup;
import com.jspxcms.core.listener.InfoDeleteListener;
import com.jspxcms.core.listener.MemberGroupDeleteListener;
import com.jspxcms.core.repository.InfoMemberGroupDao;
import com.jspxcms.core.service.InfoMemberGroupService;
import com.jspxcms.core.service.MemberGroupService;

@Service
@Transactional(readOnly = true)
public class InfoMemberGroupServiceImpl implements InfoMemberGroupService,
		InfoDeleteListener, MemberGroupDeleteListener {
	@Transactional
	public InfoMemberGroup save(Info info, Integer groupId, Boolean viewPerm) {
		InfoMemberGroup bean = new InfoMemberGroup();
		bean.setInfo(info);
		MemberGroup group = memberGroupService.get(groupId);
		bean.setGroup(group);
		bean.setViewPerm(viewPerm);
		bean.applyDefaultValue();
		bean = dao.save(bean);
		return bean;
	}

	@Transactional
	public void update(Info info, Integer[] viewGroupIds) {
		if (viewGroupIds == null) {
			viewGroupIds = new Integer[0];
		}
		Set<InfoMemberGroup> infoGroups = info.getInfoGroups();
		if (infoGroups == null) {
			infoGroups = new HashSet<InfoMemberGroup>();
			info.setInfoGroups(infoGroups);
		}
		// 先更新
		for (InfoMemberGroup infoGroup : infoGroups) {
			if (ArrayUtils.contains(viewGroupIds, infoGroup.getGroup().getId())) {
				infoGroup.setViewPerm(true);
			} else {
				infoGroup.setViewPerm(false);
			}
		}
		// 再新增
		for (Integer viewGroupId : viewGroupIds) {
			boolean contains = false;
			for (InfoMemberGroup infoGroup : infoGroups) {
				if (infoGroup.getGroup().getId().equals(viewGroupId)) {
					contains = true;
					break;
				}
			}
			if (!contains) {
				infoGroups.add(save(info, viewGroupId, true));
			}
		}
	}

	public void preInfoDelete(Integer[] ids) {
		if (ArrayUtils.isEmpty(ids)) {
			return;
		}
		dao.deleteByInfoId(Arrays.asList(ids));
	}

	public void preMemberGroupDelete(Integer[] ids) {
		if (ArrayUtils.isEmpty(ids)) {
			return;
		}
		dao.deleteByGroupId(Arrays.asList(ids));
	}

	private MemberGroupService memberGroupService;

	@Autowired
	public void setMemberGroupService(MemberGroupService memberGroupService) {
		this.memberGroupService = memberGroupService;
	}

	private InfoMemberGroupDao dao;

	@Autowired
	public void setDao(InfoMemberGroupDao dao) {
		this.dao = dao;
	}
}
