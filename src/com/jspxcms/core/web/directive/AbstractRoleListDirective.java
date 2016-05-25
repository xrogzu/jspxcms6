package com.jspxcms.core.web.directive;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.jspxcms.common.freemarker.Freemarkers;
import com.jspxcms.core.domain.Role;
import com.jspxcms.core.service.RoleService;
import com.jspxcms.core.support.ForeContext;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

/**
 * AbstractSpecialListPageDirective 角色列表指令
 *
 * @author suenlai
 *
 */
public abstract class AbstractRoleListDirective {
	public static final String SITE_ID = "siteId";

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void doExecute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body)
			throws TemplateException, IOException {
		if (loopVars.length < 1) {
			throw new TemplateModelException("Loop variable is required.");
		}
		if (body == null) {
			throw new RuntimeException("missing body");
		}

		Integer siteId = Freemarkers.getInteger(params, AbstractRoleListDirective.SITE_ID);
		if (siteId == null && params.get(AbstractRoleListDirective.SITE_ID) == null) {
			siteId = ForeContext.getSiteId(env);
		}
		List<Role> list = service.findList(siteId);
		loopVars[0] = env.getObjectWrapper().wrap(list);
		body.render(env.getOut());
	}

	@Autowired
	private RoleService service;
}
