package com.zx.stlife.entity.service;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.zx.stlife.entity.SuperContentContact;

/**
 * 鲜花速递
 */
@Entity
@Table(name = "service_flower_artist")
public class FlowerArtist extends SuperContentContact {

	/**
	 * Default constructor
	 */
	public FlowerArtist() {
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