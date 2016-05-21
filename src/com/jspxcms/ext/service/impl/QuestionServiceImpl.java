package com.jspxcms.ext.service.impl;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jspxcms.common.orm.Limitable;
import com.jspxcms.common.orm.RowSide;
import com.jspxcms.common.orm.SearchFilter;
import com.jspxcms.core.domain.Site;
import com.jspxcms.core.domain.User;
import com.jspxcms.core.listener.SiteDeleteListener;
import com.jspxcms.core.service.SiteService;
import com.jspxcms.core.service.UserService;
import com.jspxcms.core.support.DeleteException;
import com.jspxcms.ext.domain.Question;
import com.jspxcms.ext.domain.QuestionItem;
import com.jspxcms.ext.domain.QuestionOption;
import com.jspxcms.ext.domain.QuestionRecord;
import com.jspxcms.ext.listener.QuestionDeleteListener;
import com.jspxcms.ext.repository.QuestionDao;
import com.jspxcms.ext.service.QuestionItemRecService;
import com.jspxcms.ext.service.QuestionItemService;
import com.jspxcms.ext.service.QuestionOptRecService;
import com.jspxcms.ext.service.QuestionOptionService;
import com.jspxcms.ext.service.QuestionRecordService;
import com.jspxcms.ext.service.QuestionService;

@Service
@Transactional(readOnly = true)
public class QuestionServiceImpl implements QuestionService, SiteDeleteListener {
	public Page<Question> findAll(Map<String, String[]> params,
			Pageable pageable) {
		return dao.findAll(spec(params), pageable);
	}

	public RowSide<Question> findSide(Map<String, String[]> params,
			Question bean, Integer position, Sort sort) {
		if (position == null) {
			return new RowSide<Question>();
		}
		Limitable limit = RowSide.limitable(position, sort);
		List<Question> list = dao.findAll(spec(params), limit);
		return RowSide.create(list, bean);
	}

	private Specification<Question> spec(Map<String, String[]> params) {
		Collection<SearchFilter> filters = SearchFilter.parse(params).values();
		Specification<Question> sp = SearchFilter.spec(filters, Question.class);
		return sp;
	}

	public List<Question> findList(Integer userId, Integer historyUserId,
			Boolean inPeriod, Integer[] status, Limitable limitable) {
		return dao.findList(userId, historyUserId, inPeriod, status, limitable);
	}

	public Page<Question> findPage(Integer userId, Integer historyUserId,
			Boolean inPeriod, Integer[] status, Pageable pageable) {
		return dao.findPage(userId, historyUserId, inPeriod, status, pageable);
	}

	public Question findLatest(Integer[] status, Integer siteId) {
		return dao.findLatest(status, siteId);
	}

	public Question get(Integer id) {
		return dao.findOne(id);
	}

	@Transactional
	public Question save(Question bean, Integer[] orgIds, String[] itemTitle,
			Boolean[] itemInput, Integer siteId) {
		Site site = siteService.get(siteId);
		bean.setSite(site);
		bean.applyDefaultValue();
		bean = dao.save(bean);

		itemService.save(itemTitle, itemInput, bean);
		return bean;
	}

	@Transactional
	public Question update(Question bean, Integer[] orgIds, Integer[] itemId,
			String[] itemTitle, Boolean[] itemInput) {
		bean.applyDefaultValue();
		bean = dao.save(bean);

		itemService.update(itemId, itemTitle, itemInput, bean);
		return bean;
	}

	private Question doDelete(Integer id) {
		Question entity = dao.findOne(id);
		if (entity != null) {
			dao.delete(entity);
		}
		return entity;
	}

	@Transactional
	public Question delete(Integer id) {
		firePreDelete(new Integer[] { id });
		return doDelete(id);
	}

	@Transactional
	public Question[] delete(Integer[] ids) {
		firePreDelete(ids);
		Question[] beans = new Question[ids.length];
		for (int i = 0; i < ids.length; i++) {
			beans[i] = doDelete(ids[i]);
		}
		return beans;
	}

	@Transactional
	public void answer(Integer id, Integer userId, String ip, String cookie,
			Integer[] selects, Map<Integer, String> inputs) {
		Question question = get(id);
		question.setTotal(question.getTotal() + 1);
		User user = userService.get(userId);
		QuestionRecord record = recordService.save(question, user, ip, cookie);
		QuestionOption option;
		for (Integer select : selects) {
			option = optionService.get(select);
			option.setCount(option.getCount() + 1);
			optRecService.save(option, record);
		}
		QuestionItem item;
		for (Integer input : inputs.keySet()) {
			item = itemService.get(input);
			itemRecService.save(item, record, inputs.get(input));
		}
	}

	public void preSiteDelete(Integer[] ids) {
		if (ArrayUtils.isNotEmpty(ids)) {
			if (dao.countBySiteId(Arrays.asList(ids)) > 0) {
				throw new DeleteException("question.management");
			}
		}
	}

	private void firePreDelete(Integer[] ids) {
		if (!CollectionUtils.isEmpty(deleteListeners)) {
			for (QuestionDeleteListener listener : deleteListeners) {
				listener.preQuestionDelete(ids);
			}
		}
	}

	private List<QuestionDeleteListener> deleteListeners;

	@Autowired(required = false)
	public void setDeleteListeners(List<QuestionDeleteListener> deleteListeners) {
		this.deleteListeners = deleteListeners;
	}

	private QuestionItemRecService itemRecService;
	private QuestionRecordService recordService;
	private QuestionOptionService optionService;
	private QuestionOptRecService optRecService;

	private QuestionItemService itemService;
	private UserService userService;
	private SiteService siteService;

	@Autowired
	public void setIrecordService(QuestionItemRecService itemRecService) {
		this.itemRecService = itemRecService;
	}

	@Autowired
	public void setRecordService(QuestionRecordService recordService) {
		this.recordService = recordService;
	}

	@Autowired
	public void setItemService(QuestionItemService itemService) {
		this.itemService = itemService;
	}

	@Autowired
	public void setOptionService(QuestionOptionService optionService) {
		this.optionService = optionService;
	}

	@Autowired
	public void setOptRecService(QuestionOptRecService optRecService) {
		this.optRecService = optRecService;
	}

	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@Autowired
	public void setSiteService(SiteService siteService) {
		this.siteService = siteService;
	}

	private QuestionDao dao;

	@Autowired
	public void setDao(QuestionDao dao) {
		this.dao = dao;
	}

}
