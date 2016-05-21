package com.jspxcms.ext.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;

@Entity
@Table(name = "cms_question_option")
public class QuestionOption implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 获取选项的票数比例。范围从0-1。
	 * 
	 * @return 浮点型
	 */
	@Transient
	public float getProportion() {
		Integer total = getItem().getQuestion().getTotal();
		if (total != null && total > 0) {
			return (float) getCount() / total;
		} else {
			return 0;
		}
	}

	@Transient
	public void applyDefaultValue() {
		if (getCount() == null) {
			setCount(0);
		}
		if (getSeq() == null) {
			setSeq(Integer.MAX_VALUE);
		}
	}

	private Integer id;
	private List<QuestionOptRec> optRecs = new ArrayList<QuestionOptRec>(0);
	private QuestionItem item;

	private String title;
	private Integer count;
	private Integer seq;

	@Id
	@Column(name = "f_questionoption_id", unique = true, nullable = false)
	@TableGenerator(name = "tg_cms_question_option", pkColumnValue = "cms_question_option", table = "t_id_table", pkColumnName = "f_table", valueColumnName = "f_id_value", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "tg_cms_question_option")
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.REMOVE }, mappedBy = "option")
	@OrderBy(value = "id asc")
	public List<QuestionOptRec> getOptRecs() {
		return optRecs;
	}

	public void setOptRecs(List<QuestionOptRec> optRecs) {
		this.optRecs = optRecs;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "f_questionitem_id", nullable = false)
	public QuestionItem getItem() {
		return this.item;
	}

	public void setItem(QuestionItem item) {
		this.item = item;
	}

	@Column(name = "f_title", nullable = false, length = 150)
	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "f_count", nullable = false)
	public Integer getCount() {
		return this.count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	@Column(name = "f_seq", nullable = false)
	public Integer getSeq() {
		return this.seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}

}
