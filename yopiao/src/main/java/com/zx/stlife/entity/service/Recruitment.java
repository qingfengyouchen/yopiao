package com.zx.stlife.entity.service;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.zx.stlife.entity.SuperContentContact;

/**
 * 招聘工作
 */
@Entity
@Table(name = "service_recruitment")
public class Recruitment extends SuperContentContact {

	/**
	 * Default constructor
	 */
	public Recruitment() {
	}

	/**
	 * 薪资
	 */
	private String salary;

	/**
	 * 地址
	 */
	private String address;

	/**
	 * 职位
	 */
	private String position;

	/**
	 * 要求
	 */
	private String requirement;

	/**
	 * 公司简介
	 */
	private String companyIntroduce;

	public String getSalary() {
		return salary;
	}

	public void setSalary(String salary) {
		this.salary = salary;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getRequirement() {
		return requirement;
	}

	public void setRequirement(String requirement) {
		this.requirement = requirement;
	}

	public String getCompanyIntroduce() {
		return companyIntroduce;
	}

	public void setCompanyIntroduce(String companyIntroduce) {
		this.companyIntroduce = companyIntroduce;
	}

}