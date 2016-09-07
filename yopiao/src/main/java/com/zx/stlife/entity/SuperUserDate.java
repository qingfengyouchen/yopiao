package com.zx.stlife.entity;

import java.util.Date;

import javax.persistence.MappedSuperclass;

import com.base.jpa.model.SuperIntEntity;

/**
 * 用户时间超类
 */
@MappedSuperclass
public class SuperUserDate extends SuperIntEntity {

	/**
	 * Default constructor
	 */
	public SuperUserDate() {
	}

	/**
	 * 创建人
	 */
	private String creator;

	/**
	 * 修改人
	 */
	private String editor;

	/**
	 * 修改时间
	 */
	private Date editTime;

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getEditor() {
		return editor;
	}

	public void setEditor(String editor) {
		this.editor = editor;
	}

	public Date getEditTime() {
		return editTime;
	}

	public void setEditTime(Date editTime) {
		this.editTime = editTime;
	}

}