package com.zx.stlife.entity.service;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.zx.stlife.entity.SuperContentContact;

/**
 * 便民服务
 */
@Entity
@Table(name = "service_convenience")
public class Convenience extends SuperContentContact {

	/**
	 * Default constructor
	 */
	public Convenience() {
	}

}