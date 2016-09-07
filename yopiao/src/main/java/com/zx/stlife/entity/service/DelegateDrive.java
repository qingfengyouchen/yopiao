package com.zx.stlife.entity.service;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.zx.stlife.entity.SuperContentContact;

/**
 * 顺风代驾
 */
@Entity
@Table(name = "service_delegate_drive")
public class DelegateDrive extends SuperContentContact {

	/**
	 * Default constructor
	 */
	public DelegateDrive() {
	}

}