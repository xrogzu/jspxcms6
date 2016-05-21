package com.jspxcms.ext.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;

@Entity
@Table(name = "cms_question_item_rec")
public class QuestionItemRec implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	@Transient
	public void applyDefaultValue() {
	}

	private Integer id;
	private QuestionRecord record;
	private QuestionItem item;

	private String answer;

	@Id
	@Column(name = "f_questionitemrec_id", unique = true, nullable = false)
	@TableGenerator(name = "tg_cms_question_item_rec", pkColumnValue = "cms_question_item_rec", table = "t_id_table", pkColumnName = "f_table", valueColumnName = "f_id_value", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "tg_cms_question_item_rec")
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "f_questionrecord_id", nullable = false)
	public QuestionRecord getRecord() {
		return this.record;
	}

	public void setRecord(QuestionRecord record) {
		this.record = record;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "f_questionitem_id", nullable = false)
	public QuestionItem getItem() {
		return this.item;
	}

	public void setItem(QuestionItem item) {
		this.item = item;
	}

	@Column(name = "f_answer", length = 2000)
	public String getAnswer() {
		return this.answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

}
