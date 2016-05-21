package com.jspxcms.core.repository.impl;

import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.ArrayUtils;
import org.hibernate.ejb.QueryHints;

import com.jspxcms.core.domain.User;
import com.jspxcms.core.domaindsl.QUser;
import com.jspxcms.core.repository.UserDaoPlus;
import com.mysema.query.BooleanBuilder;
import com.mysema.query.jpa.impl.JPAQuery;

/**
 * UserDaoImpl
 * 
 * @author liufang
 * 
 */
public class UserDaoImpl implements UserDaoPlus {
	public List<User> findByUsername(String[] usernames) {
		if (ArrayUtils.isEmpty(usernames)) {
			return Collections.emptyList();
		}
		JPAQuery query = new JPAQuery(this.em);
		query.setHint(QueryHints.HINT_CACHEABLE, true);
		QUser user = QUser.user;
		query.from(user);
		BooleanBuilder exp = new BooleanBuilder();
		for (int i = 0, len = usernames.length; i < len; i++) {
			exp = exp.or(user.username.eq(usernames[i]));
		}
		query.where(exp);
		return query.list(user);
	}

	private EntityManager em;

	@PersistenceContext
	public void setEm(EntityManager em) {
		this.em = em;
	}
}
