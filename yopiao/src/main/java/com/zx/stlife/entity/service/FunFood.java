package com.zx.stlife.entity.service;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.zx.stlife.entity.SuperContentContact;

/**
 * 休闲美食
 */
@Entity
@Table(name = "service_fun_food")
public class FunFood extends SuperContentContact {

	/**
	 * Default constructor
	 */
	public FunFood() {
	}

	/**
	 * 送货范围
	 */
	private String sendRange;

	public String getSendRange() {
		return sendRange;
	}

	public void setSendRange(String sendRange) {
		this.sendRange = sendRange;
	}

}