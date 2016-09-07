package com.zx.stlife.entity.service;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.zx.stlife.entity.SuperContentContact;

/**
 * 私教补习
 */
@Entity
@Table(name = "service_tutoring")
public class Tutoring extends SuperContentContact {

	/**
	 * Default constructor
	 */
	public Tutoring() {
	}

	/**
	 * 类型，分为私教和补习
	 */
	private Byte type;

	public Byte getType() {
		return type;
	}

	public void setType(Byte type) {
		this.type = type;
	}

}