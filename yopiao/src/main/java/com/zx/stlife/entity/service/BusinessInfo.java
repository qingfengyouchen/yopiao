package com.zx.stlife.entity.service;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.zx.stlife.entity.SuperContentContact;

/**
 * 商业信息
 */
@Entity
@Table(name = "service_business_info")
public class BusinessInfo extends SuperContentContact {

	/**
	 * Default constructor
	 */
	public BusinessInfo() {
	}

	/**
	 * 类型，分为出售和出租
	 */
	private Byte type;

	public Byte getType() {
		return type;
	}

	public void setType(Byte type) {
		this.type = type;
	}

}