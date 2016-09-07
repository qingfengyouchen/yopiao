package com.zx.stlife.entity.service;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.zx.stlife.entity.SuperContentContact;

/**
 * 天台微商
 */
@Entity
@Table(name = "service_micro_business")
public class MicroBusiness extends SuperContentContact {

	/**
	 * Default constructor
	 */
	public MicroBusiness() {
	}

	/**
	 * 微商名称
	 */
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}