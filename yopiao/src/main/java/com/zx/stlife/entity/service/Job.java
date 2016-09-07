package com.zx.stlife.entity.service;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.zx.stlife.entity.SuperContentContact;

/**
 * 求职工作
 */
@Entity
@Table(name = "service_job")
public class Job extends SuperContentContact {

	/**
	 * Default constructor
	 */
	public Job() {
	}

	/**
	 * 姓名
	 */
	private String name;

	/**
	 * 性别
	 */
	private Byte gender;

	/**
	 * 年龄
	 */
	private Integer age;

	/**
	 * 学历
	 */
	private String education;

	/**
	 * 工作经验
	 */
	private String workExperience;

	/**
	 * 现居住地
	 */
	private String apartmentAddr;

	/**
	 * 户籍
	 */
	private String household;

	/**
	 * 求职意向
	 */
	private String jobIntension;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Byte getGender() {
		return gender;
	}

	public void setGender(Byte gender) {
		this.gender = gender;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getEducation() {
		return education;
	}

	public void setEducation(String education) {
		this.education = education;
	}

	public String getWorkExperience() {
		return workExperience;
	}

	public void setWorkExperience(String workExperience) {
		this.workExperience = workExperience;
	}

	public String getApartmentAddr() {
		return apartmentAddr;
	}

	public void setApartmentAddr(String apartmentAddr) {
		this.apartmentAddr = apartmentAddr;
	}

	public String getHousehold() {
		return household;
	}

	public void setHousehold(String household) {
		this.household = household;
	}

	public String getJobIntension() {
		return jobIntension;
	}

	public void setJobIntension(String jobIntension) {
		this.jobIntension = jobIntension;
	}

}