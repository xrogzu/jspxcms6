package com.jspxcms.ext.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import com.jspxcms.common.orm.Limitable;
import com.jspxcms.ext.domain.QuestionOptRec;

public interface QuestionOptRecDao extends Repository<QuestionOptRec, Integer>,
		QuestionOptRecDaoPlus {
	public Page<QuestionOptRec> findAll(Specification<QuestionOptRec> spec,
			Pageable pageable);

	public List<QuestionOptRec> findAll(Specification<QuestionOptRec> spec,
			Limitable limitable);

	public QuestionOptRec findOne(Integer id);

	public QuestionOptRec save(QuestionOptRec bean);

	public void delete(QuestionOptRec bean);

	// --------------------

	@Modifying
	@Query("delete from QuestionOptRec bean where bean.option.id in (?1)")
	public int deleteBySiteId(Collection<Integer> optionIds);
}
