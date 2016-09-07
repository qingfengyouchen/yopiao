package com.zx.stlife.entity.sys;

import com.base.jpa.model.SuperIntEntity;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 系统类别
 * 
 * @author micheal cao
 */
@Entity
@Table(name = "sys_category")
public class Category extends SuperIntEntity {

	private Byte category;
	private String name;
	private Byte sortNum;

	public Category() {
	}

	public Category(Integer id) {
		super(id);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Byte getSortNum() {
		return sortNum;
	}

	public void setSortNum(Byte sortNum) {
		this.sortNum = sortNum;
	}

	public Byte getCategory() {
		return category;
	}

	public void setCategory(Byte category) {
		this.category = category;
	}
}
