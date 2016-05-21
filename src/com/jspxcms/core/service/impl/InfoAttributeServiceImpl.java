package com.jspxcms.core.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jspxcms.core.domain.Attribute;
import com.jspxcms.core.domain.Info;
import com.jspxcms.core.domain.InfoAttribute;
import com.jspxcms.core.repository.InfoAttributeDao;
import com.jspxcms.core.service.AttributeService;
import com.jspxcms.core.service.InfoAttributeService;

/**
 * InfoAttributeServiceImpl
 * 
 * @author liufang
 * 
 */
@Service
@Transactional(readOnly = true)
public class InfoAttributeServiceImpl implements InfoAttributeService {
	@Transactional
	public List<InfoAttribute> save(Info info, Integer[] attrIds,
			Map<String, String> attrImages) {
		List<InfoAttribute> infoAttrs = info.getInfoAttrs();
		if (infoAttrs == null) {
			infoAttrs = new ArrayList<InfoAttribute>();
			info.setInfoAttrs(infoAttrs);
		}
		if (ArrayUtils.isEmpty(attrIds)) {
			return infoAttrs;
		}
		String image;
		for (Integer attrId : attrIds) {
			image = attrImages.get(attrId.toString());
			infoAttrs.add(save(info, attrId, image));
		}
		return infoAttrs;
	}

	@Transactional
	private InfoAttribute save(Info info, Integer attrId, String image) {
		Attribute attr = attributeService.get(attrId);
		InfoAttribute bean = new InfoAttribute(info, attr, image);
		dao.save(bean);
		return bean;
	}

	@Transactional
	public List<InfoAttribute> update(Info info, Integer[] attrIds,
			Map<String, String> attrImages) {
		if (attrIds == null) {
			// 为null不更新。要设置为空，传空数组。
			return info.getInfoAttrs();
		}
		List<InfoAttribute> infoAttrs = info.getInfoAttrs();
		// 先删除
		Set<InfoAttribute> tobeDelete = new HashSet<InfoAttribute>();
		for (InfoAttribute infoAttr : infoAttrs) {
			if (!ArrayUtils.contains(attrIds, infoAttr.getAttribute().getId())) {
				tobeDelete.add(infoAttr);
			}
		}
		infoAttrs.removeAll(tobeDelete);
		dao.delete(tobeDelete);
		// 再更新、新增
		for (Integer attrId : attrIds) {
			boolean contains = false;
			String image;
			for (InfoAttribute infoAttr : infoAttrs) {
				if (infoAttr.getAttribute().getId().equals(attrId)) {
					// 更新
					image = attrImages.get(attrId.toString());
					infoAttr.setImage(image);
					contains = true;
					break;
				}
			}
			if (!contains) {
				// 新增
				image = attrImages.get(attrId.toString());
				infoAttrs.add(save(info, attrId, image));
			}
		}
		return infoAttrs;
	}

	public int deleteByInfoId(Integer infoId) {
		return dao.deleteByInfoId(infoId);
	}

	public int deleteByAttributeId(Integer attributeId) {
		return dao.deleteByAttributeId(attributeId);
	}

	private AttributeService attributeService;

	@Autowired
	public void setAttributeService(AttributeService attributeService) {
		this.attributeService = attributeService;
	}

	private InfoAttributeDao dao;

	@Autowired
	public void setDao(InfoAttributeDao dao) {
		this.dao = dao;
	}
}
