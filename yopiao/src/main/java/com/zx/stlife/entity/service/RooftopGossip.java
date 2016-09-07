package com.zx.stlife.entity.service;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.zx.stlife.entity.SuperContent;

/**
 * 天台八卦
 */
@Entity
@Table(name = "service_rooftop_gossip")
public class RooftopGossip extends SuperContent {

	/**
	 * Default constructor
	 */
	public RooftopGossip() {
	}

}