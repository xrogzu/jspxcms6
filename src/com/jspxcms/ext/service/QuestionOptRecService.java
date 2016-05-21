package com.jspxcms.ext.service;

import com.jspxcms.ext.domain.QuestionOptRec;
import com.jspxcms.ext.domain.QuestionOption;
import com.jspxcms.ext.domain.QuestionRecord;

public interface QuestionOptRecService {
	public QuestionOptRec save(QuestionOption option, QuestionRecord record);
}
