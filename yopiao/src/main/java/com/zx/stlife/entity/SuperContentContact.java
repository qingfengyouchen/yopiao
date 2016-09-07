package com.zx.stlife.entity;

import javax.persistence.MappedSuperclass;

/**
 * 联系信息超类
 */
@MappedSuperclass
public class SuperContentContact extends SuperContent {

	/**
	 * Default constructor
	 */
	public SuperContentContact() {
	}

	/**
	 * 联系人
	 */
	private String linker;

	/**
	 * 联系电话
	 */
	private String tel;

	public String getLinker() {
		return linker;
	}

	public void setLinker(String linker) {
		this.linker = linker;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

}