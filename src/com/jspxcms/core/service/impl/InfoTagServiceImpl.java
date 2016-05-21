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
import com.jspxcms.core.domain.InfoTag;
import com.jspxcms.core.domain.Tag;
import com.jspxcms.core.repository.InfoTagDao;
import com.jspxcms.core.service.InfoTagService;
import com.jspxcms.core.service.TagService;

@Service
@Transactional(readOnly = true)
public class InfoTagServiceImpl implements InfoTagService {
	@Transactional
	public List<InfoTag> save(Info info, String[] tagNames) {
		List<InfoTag> infoTags = info.getInfoTags();
		if (infoTags == null) {
			infoTags = new ArrayList<InfoTag>();
			info.setInfoTags(infoTags);
		}
		if (ArrayUtils.isEmpty(tagNames)) {
			return infoTags;
		}
		for (int i = 0, len = tagNames.length; i < len; i++) {
			infoTags.add(save(info, tagNames[i], i));
		}
		return infoTags;
	}

	private InfoTag save(Info info, String tagName, int index) {
		InfoTag bean = new InfoTag();
		Tag tag = tagService.refer(tagName, info.getSite().getId());
		bean.setTag(tag);
		bean.setInfo(info);
		bean.setTagIndex(index);
		dao.save(bean);
		return bean;
	}

	@Transactional
	public List<InfoTag> update(Info info, String[] tagNames) {
		if (tagNames == null) {
			// 为null不更新。要设置为空，传空数组。
			return info.getInfoTags();
		}
		List<InfoTag> infoTags = info.getInfoTags();
		// 先删除
		Set<InfoTag> tobeDelete = new HashSet<InfoTag>();
		for (InfoTag infoTag : infoTags) {
			Tag tag = infoTag.getTag();
			if (!ArrayUtils.contains(tagNames, tag.getName())) {
				tagService.derefer(tag);
				tobeDelete.add(infoTag);
			}
		}
		infoTags.removeAll(tobeDelete);
		dao.delete(tobeDelete);
		// 再新增
		for (int i = 0, len = tagNames.length; i < len; i++) {
			boolean contains = false;
			for (InfoTag infoTag : infoTags) {
				if (infoTag.getTag().getName().equals(tagNames[i])) {
					infoTag.setTagIndex(i);
					infoTags.remove(infoTag);
					infoTags.add(i, infoTag);
					contains = true;
					break;
				}
			}
			if (!contains) {
				// 新增
				infoTags.add(i, save(info, tagNames[i], i));
			}
		}
		return infoTags;
	}

	@Transactional
	public int deleteByInfoId(Integer infoId) {
		return dao.deleteByInfoId(infoId);
	}

	@Transactional
	public int deleteByTagId(Integer tagId) {
		return dao.deleteByTagId(tagId);
	}

	private TagService tagService;

	@Autowired
	public void setTagService(TagService tagService) {
		this.tagService = tagService;
	}

	private InfoTagDao dao;

	@Autowired
	public void setDao(InfoTagDao dao) {
		this.dao = dao;
	}
}
