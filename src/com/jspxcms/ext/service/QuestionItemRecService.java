package com.jspxcms.ext.service;

import com.jspxcms.ext.domain.QuestionItemRec;
import com.jspxcms.ext.domain.QuestionItem;
import com.jspxcms.ext.domain.QuestionRecord;

public interface QuestionItemRecService {
	public QuestionItemRec get(Integer id);

	public QuestionItemRec save(QuestionItem item, QuestionRecord record,
			String answer);

	public QuestionItemRec update(QuestionItemRec bean);

	public QuestionItemRec delete(Integer id);

	public QuestionItemRec[] delete(Integer[] ids);
}
