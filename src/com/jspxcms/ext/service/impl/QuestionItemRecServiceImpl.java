package com.jspxcms.ext.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jspxcms.ext.domain.QuestionItemRec;
import com.jspxcms.ext.domain.QuestionItem;
import com.jspxcms.ext.domain.QuestionRecord;
import com.jspxcms.ext.repository.QuestionItemRecDao;
import com.jspxcms.ext.service.QuestionItemRecService;

@Service
@Transactional(readOnly = true)
public class QuestionItemRecServiceImpl implements QuestionItemRecService {
	public QuestionItemRec get(Integer id) {
		return dao.findOne(id);
	}

	@Transactional
	public QuestionItemRec save(QuestionItem item, QuestionRecord record,
			String answer) {
		QuestionItemRec bean = new QuestionItemRec();
		bean.setAnswer(answer);
		bean.setItem(item);
		bean.setRecord(record);
		bean.applyDefaultValue();
		bean = dao.save(bean);
		return bean;
	}

	@Transactional
	public QuestionItemRec update(QuestionItemRec bean) {
		bean.applyDefaultValue();
		bean = dao.save(bean);
		return bean;
	}

	@Transactional
	public QuestionItemRec delete(Integer id) {
		QuestionItemRec entity = dao.findOne(id);
		dao.delete(entity);
		return entity;
	}

	@Transactional
	public QuestionItemRec[] delete(Integer[] ids) {
		QuestionItemRec[] beans = new QuestionItemRec[ids.length];
		for (int i = 0; i < ids.length; i++) {
			beans[i] = delete(ids[i]);
		}
		return beans;
	}

	private QuestionItemRecDao dao;

	@Autowired
	public void setDao(QuestionItemRecDao dao) {
		this.dao = dao;
	}
}
