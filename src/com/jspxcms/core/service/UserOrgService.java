package com.jspxcms.core.service;

import java.util.List;

import com.jspxcms.core.domain.User;
import com.jspxcms.core.domain.UserOrg;

public interface UserOrgService {
	public List<UserOrg> save(User user, Integer[] orgIds, Integer orgId);

	public List<UserOrg> update(User user, Integer[] orgIds, Integer orgId,
			Integer topOrgId);
}
