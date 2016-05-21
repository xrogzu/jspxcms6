package com.jspxcms.ext.repository.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.ArrayUtils;
import org.hibernate.ejb.QueryHints;

import com.jspxcms.common.orm.Limitable;
import com.jspxcms.common.orm.QuerydslUtils;
import com.jspxcms.ext.domain.Ad;
import com.jspxcms.ext.domaindsl.QAd;
import com.jspxcms.ext.repository.AdDaoPlus;
import com.mysema.query.BooleanBuilder;
import com.mysema.query.jpa.impl.JPAQuery;

/**
 * AdDaoImpl
 * 
 * @author liufang
 * 
 */
public class AdDaoImpl implements AdDaoPlus {
	public List<Ad> findList(Integer[] siteId, String[] slot, Integer[] slotId,
			Limitable limitable) {
		JPAQuery query = new JPAQuery(this.em);
		query.setHint(QueryHints.HINT_CACHEABLE, true);
		QAd ad = QAd.ad;
		query.from(ad);
		BooleanBuilder exp = new BooleanBuilder();
		if (ArrayUtils.isNotEmpty(siteId)) {
			exp = exp.and(ad.site.id.in(siteId));
		}
		if (ArrayUtils.isNotEmpty(slot)) {
			exp = exp.and(ad.slot.number.in(slot));
		}
		if (ArrayUtils.isNotEmpty(slotId)) {
			exp = exp.and(ad.slot.id.in(slotId));
		}
		query.where(exp);
		return QuerydslUtils.list(query, ad, limitable);
	}

	private EntityManager em;

	@PersistenceContext
	public void setEm(EntityManager em) {
		this.em = em;
	}
}
