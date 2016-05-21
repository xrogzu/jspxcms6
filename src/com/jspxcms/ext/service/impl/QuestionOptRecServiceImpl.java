package com.jspxcms.ext.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jspxcms.ext.domain.QuestionOptRec;
import com.jspxcms.ext.domain.QuestionOption;
import com.jspxcms.ext.domain.QuestionRecord;
import com.jspxcms.ext.repository.QuestionOptRecDao;
import com.jspxcms.ext.service.QuestionOptRecService;

@Service
@Transactional(readOnly = true)
public class QuestionOptRecServiceImpl implements QuestionOptRecService {
	public QuestionOptRec get(Integer id) {
		return dao.findOne(id);
	}

	@Transactional
	public QuestionOptRec save(QuestionOptRec bean) {
		bean.applyDefaultValue();
		bean = dao.save(bean);
		return bean;
	}

	@Transactional
	public QuestionOptRec save(QuestionOption option, QuestionRecord record) {
		QuestionOptRec bean = new QuestionOptRec();
		bean.setOption(option);
		bean.setRecord(record);
		bean = save(bean);
		return bean;
	}

	@Transactional
	public QuestionOptRec update(QuestionOptRec bean) {
		bean.applyDefaultValue();
		bean = dao.save(bean);
		return bean;
	}

	@Transactional
	public QuestionOptRec delete(Integer id) {
		QuestionOptRec entity = dao.findOne(id);
		dao.delete(entity);
		return entity;
	}

	@Transactional
	public QuestionOptRec[] delete(Integer[] ids) {
		QuestionOptRec[] beans = new QuestionOptRec[ids.length];
		for (int i = 0; i < ids.length; i++) {
			beans[i] = delete(ids[i]);
		}
		return beans;
	}

	private QuestionOptRecDao dao;

	@Autowired
	public void setDao(QuestionOptRecDao dao) {
		this.dao = dao;
	}
}
