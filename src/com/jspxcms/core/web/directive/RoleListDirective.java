package com.jspxcms.core.web.directive;

import java.io.IOException;
import java.util.Map;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
 * InfoListDirective
 *
 * @author suenlai
 * @date 2016年5月25日 上午10:06:51
 *
 */
public class RoleListDirective extends AbstractRoleListDirective implements TemplateDirectiveModel {
	@Override
	@SuppressWarnings("rawtypes")
	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body)
			throws TemplateException, IOException {
		super.doExecute(env, params, loopVars, body);
	}
}
