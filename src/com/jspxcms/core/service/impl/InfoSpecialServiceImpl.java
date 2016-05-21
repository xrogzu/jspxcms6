package com.jspxcms.core.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jspxcms.core.domain.Info;
import com.jspxcms.core.domain.InfoSpecial;
import com.jspxcms.core.domain.Special;
import com.jspxcms.core.repository.InfoSpecialDao;
import com.jspxcms.core.service.InfoSpecialService;
import com.jspxcms.core.service.SpecialService;

@Service
@Transactional(readOnly = true)
public class InfoSpecialServiceImpl implements InfoSpecialService {
	@Transactional
	public List<InfoSpecial> save(Info info, Integer[] specialIds) {
		List<InfoSpecial> infoSpecials = info.getInfoSpecials();
		if (infoSpecials == null) {
			infoSpecials = new ArrayList<InfoSpecial>();
			info.setInfoSpecials(infoSpecials);
		}
		if (ArrayUtils.isEmpty(specialIds)) {
			return infoSpecials;
		}
		for (int i = 0, len = specialIds.length; i < len; i++) {
			infoSpecials.add(save(info, specialIds[i], i));
		}
		return infoSpecials;
	}

	private InfoSpecial save(Info info, Integer specialId, int index) {
		InfoSpecial bean = new InfoSpecial();
		Special special = specialService.refer(specialId);
		bean.setSpecial(special);
		bean.setInfo(info);
		bean.setSpecialIndex(index);
		dao.save(bean);
		return bean;
	}

	@Transactional
	public List<InfoSpecial> update(Info info, Integer[] specialIds) {
		if (specialIds == null) {
			// 为null不更新，要设置为空，传空数组。
			return info.getInfoSpecials();
		}
		List<InfoSpecial> infoSpecials = info.getInfoSpecials();
		// 先删除
		Set<InfoSpecial> tobeDelete = new HashSet<InfoSpecial>();
		for (InfoSpecial infoSpecial : infoSpecials) {
			Special special = infoSpecial.getSpecial();
			if (!ArrayUtils.contains(specialIds, special.getId())) {
				specialService.derefer(special);
				tobeDelete.add(infoSpecial);
			}
		}
		infoSpecials.removeAll(tobeDelete);
		dao.delete(tobeDelete);
		// 再新增
		for (int i = 0, len = specialIds.length; i < len; i++) {
			boolean contains = false;
			for (InfoSpecial infoSpecial : infoSpecials) {
				if (infoSpecial.getSpecial().getId().equals(specialIds[i])) {
					infoSpecial.setSpecialIndex(i);
					infoSpecials.remove(infoSpecial);
					infoSpecials.add(i, infoSpecial);
					contains = true;
					break;
				}
			}
			if (!contains) {
				// 新增
				infoSpecials.add(i, save(info, specialIds[i], i));
			}
		}
		return infoSpecials;
	}

	@Transactional
	public int deleteByInfoId(Integer infoId) {
		return dao.deleteByInfoId(infoId);
	}

	@Transactional
	public int deleteBySpecialId(Integer specialId) {
		return dao.deleteBySpecialId(specialId);
	}

	private SpecialService specialService;

	@Autowired
	public void setSpecialService(SpecialService specialService) {
		this.specialService = specialService;
	}

	private InfoSpecialDao dao;

	@Autowired
	public void setDao(InfoSpecialDao dao) {
		this.dao = dao;
	}
}
