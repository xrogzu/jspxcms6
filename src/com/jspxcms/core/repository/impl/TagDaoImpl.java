package com.jspxcms.core.repository.impl;

import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.ArrayUtils;
import org.hibernate.ejb.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.jspxcms.common.orm.Limitable;
import com.jspxcms.common.orm.QuerydslUtils;
import com.jspxcms.core.domain.Tag;
import com.jspxcms.core.domaindsl.QInfo;
import com.jspxcms.core.domaindsl.QInfoTag;
import com.jspxcms.core.domaindsl.QTag;
import com.jspxcms.core.repository.TagDaoPlus;
import com.mysema.query.BooleanBuilder;
import com.mysema.query.jpa.impl.JPAQuery;

/**
 * TagDaoImpl
 * 
 * @author liufang
 * 
 */
public class TagDaoImpl implements TagDaoPlus {
	public List<Tag> findList(Integer[] siteId, String[] node,
			Integer[] nodeId, Integer refers, Limitable limitable) {
		JPAQuery query = new JPAQuery(this.em);
		query.setHint(QueryHints.HINT_CACHEABLE, true);
		QTag tag = QTag.tag;
		predicate(query, tag, siteId, node, nodeId, refers);
		return QuerydslUtils.list(query, tag, limitable);
	}

	public Page<Tag> findPage(Integer[] siteId, String[] node,
			Integer[] nodeId, Integer refers, Pageable pageable) {
		JPAQuery query = new JPAQuery(this.em);
		query.setHint(QueryHints.HINT_CACHEABLE, true);
		QTag tag = QTag.tag;
		predicate(query, tag, siteId, node, nodeId, refers);
		return QuerydslUtils.page(query, tag, pageable);
	}

	private void predicate(JPAQuery query, QTag tag, Integer[] siteId,
			String[] node, Integer[] nodeId, Integer refers) {
		query.from(tag);
		BooleanBuilder exp = new BooleanBuilder();
		if (ArrayUtils.isNotEmpty(siteId)) {
			exp = exp.and(tag.site.id.in(siteId));
		}
		if (ArrayUtils.isNotEmpty(node)) {
			QInfoTag infoTag = QInfoTag.infoTag;
			QInfo info = QInfo.info;
			query.innerJoin(tag.infoTags, infoTag);
			query.innerJoin(infoTag.info, info);
			exp = exp.and(info.node.number.in(node));
			query.distinct();
		}
		if (ArrayUtils.isNotEmpty(nodeId)) {
			QInfoTag infoTag = QInfoTag.infoTag;
			QInfo info = QInfo.info;
			query.innerJoin(tag.infoTags, infoTag);
			query.innerJoin(infoTag.info, info);
			exp = exp.and(info.node.id.in(nodeId));
			query.distinct();
		}
		if (refers != null) {
			exp = exp.and(tag.refers.goe(refers));
		}
		query.where(exp);
	}

	public List<Tag> findByName(String[] names, Integer[] siteIds) {
		if (ArrayUtils.isEmpty(names)) {
			return Collections.emptyList();
		}
		JPAQuery query = new JPAQuery(this.em);
		query.setHint(QueryHints.HINT_CACHEABLE, true);
		QTag tag = QTag.tag;
		query.from(tag);
		BooleanBuilder exp = new BooleanBuilder();
		if (ArrayUtils.isNotEmpty(names)) {
			BooleanBuilder e = new BooleanBuilder();
			for (int i = 0, len = names.length; i < len; i++) {
				e = e.or(tag.name.eq(names[i]));
			}
			exp = exp.and(e);
		}
		if (ArrayUtils.isNotEmpty(siteIds)) {
			BooleanBuilder e = new BooleanBuilder();
			for (int i = 0, len = siteIds.length; i < len; i++) {
				e = e.or(tag.site.id.eq(siteIds[i]));
			}
			exp = exp.and(e);
		}
		query.where(exp);
		return query.list(tag);
	}

	private EntityManager em;

	@PersistenceContext
	public void setEm(EntityManager em) {
		this.em = em;
	}

}
