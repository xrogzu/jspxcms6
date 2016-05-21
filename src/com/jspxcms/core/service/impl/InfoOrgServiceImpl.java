package com.jspxcms.core.service.impl;

import java.util.Arrays;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jspxcms.core.domain.Info;
import com.jspxcms.core.domain.InfoOrg;
import com.jspxcms.core.domain.InfoOrg.InfoOrgComparator;
import com.jspxcms.core.domain.Org;
import com.jspxcms.core.listener.InfoDeleteListener;
import com.jspxcms.core.listener.OrgDeleteListener;
import com.jspxcms.core.repository.InfoOrgDao;
import com.jspxcms.core.service.InfoOrgService;
import com.jspxcms.core.service.OrgService;

@Service
@Transactional(readOnly = true)
public class InfoOrgServiceImpl implements InfoOrgService, InfoDeleteListener,
		OrgDeleteListener {
	@Transactional
	public InfoOrg save(Info info, Integer orgId, Boolean viewPerm) {
		InfoOrg bean = new InfoOrg();
		bean.setInfo(info);
		Org org = orgService.get(orgId);
		bean.setOrg(org);
		bean.setViewPerm(viewPerm);
		bean.applyDefaultValue();
		bean = dao.save(bean);
		return bean;
	}

	@Transactional
	public void update(Info info, Integer[] viewOrgIds) {
		if (viewOrgIds == null) {
			viewOrgIds = new Integer[0];
		}
		SortedSet<InfoOrg> infoOrgs = info.getInfoOrgs();
		if (infoOrgs == null) {
			infoOrgs = new TreeSet<InfoOrg>(new InfoOrgComparator());
			info.setInfoOrgs(infoOrgs);
		}
		// 先更新
		for (InfoOrg infoOrg : infoOrgs) {
			if (ArrayUtils.contains(viewOrgIds, infoOrg.getOrg().getId())) {
				infoOrg.setViewPerm(true);
			} else {
				infoOrg.setViewPerm(false);
			}
		}
		// 再新增
		for (Integer viewOrgId : viewOrgIds) {
			boolean contains = false;
			for (InfoOrg infoOrg : infoOrgs) {
				if (infoOrg.getOrg().getId().equals(viewOrgId)) {
					contains = true;
					break;
				}
			}
			if (!contains) {
				infoOrgs.add(save(info, viewOrgId, true));
			}
		}
	}

	public void preInfoDelete(Integer[] ids) {
		if (ArrayUtils.isEmpty(ids)) {
			return;
		}
		dao.deleteByInfoId(Arrays.asList(ids));
	}

	public void preOrgDelete(Integer[] ids) {
		if (ArrayUtils.isEmpty(ids)) {
			return;
		}
		dao.deleteByOrgId(Arrays.asList(ids));
	}

	private OrgService orgService;

	@Autowired
	public void setOrgService(OrgService orgService) {
		this.orgService = orgService;
	}

	private InfoOrgDao dao;

	@Autowired
	public void setDao(InfoOrgDao dao) {
		this.dao = dao;
	}
}
