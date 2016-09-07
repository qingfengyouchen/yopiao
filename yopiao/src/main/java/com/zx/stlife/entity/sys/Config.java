package com.zx.stlife.entity.sys;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

/**
 * 系统配置信息
 * 
 * @author micheal cao
 */
@Entity
@Table(name = "sys_config")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Config{

	private String key;
	private String value;

	public Config() {
	}

	public Config(String key, String value) {
		this.key = key;
		this.value = value;
	}

	@Column(name="mvalue")
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Id
	@Column(name="mkey")
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
