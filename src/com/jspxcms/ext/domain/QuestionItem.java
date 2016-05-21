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
@Table(name = "cms_question_item")
public class QuestionItem implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	@Transient
	public void applyDefaultValue() {
		if (getMaxSelected() == null) {
			setMaxSelected(1);
		}
		if (getSeq() == null) {
			setSeq(Integer.MAX_VALUE);
		}
		if (getEssay() == null) {
			setEssay(false);
		}
	}

	private Integer id;
	private List<QuestionOption> options = new ArrayList<QuestionOption>(0);
	private List<QuestionItemRec> itemRecs = new ArrayList<QuestionItemRec>(0);

	private Question question;

	private String title;
	private Integer maxSelected;
	private Integer seq;
	private Boolean essay;

	@Id
	@Column(name = "f_questionitem_id", unique = true, nullable = false)
	@TableGenerator(name = "tg_cms_question_item", pkColumnValue = "cms_question_item", table = "t_id_table", pkColumnName = "f_table", valueColumnName = "f_id_value", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "tg_cms_question_item")
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.REMOVE }, mappedBy = "item")
	@OrderBy(value = "seq asc, id asc")
	public List<QuestionOption> getOptions() {
		return this.options;
	}

	public void setOptions(List<QuestionOption> options) {
		this.options = options;
	}

	@OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.REMOVE }, mappedBy = "item")
	@OrderBy(value = "id asc")
	public List<QuestionItemRec> getItemRecs() {
		return this.itemRecs;
	}

	public void setItemRecs(List<QuestionItemRec> itemRecs) {
		this.itemRecs = itemRecs;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "f_question_id", nullable = false)
	public Question getQuestion() {
		return this.question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}

	@Column(name = "f_title", nullable = false, length = 150)
	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "f_max_selected", nullable = false)
	public Integer getMaxSelected() {
		return this.maxSelected;
	}

	public void setMaxSelected(Integer maxSelected) {
		this.maxSelected = maxSelected;
	}

	@Column(name = "f_seq", nullable = false)
	public Integer getSeq() {
		return this.seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}

	@Column(name = "f_is_essay", nullable = false)
	public Boolean getEssay() {
		return this.essay;
	}

	public void setEssay(Boolean essay) {
		this.essay = essay;
	}
}
