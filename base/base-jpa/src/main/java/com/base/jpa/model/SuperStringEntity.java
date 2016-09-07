/*******************************************************************************
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package com.base.jpa.model;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.Set;
import java.util.TreeSet;

@MappedSuperclass
public abstract class SuperStringEntity implements Serializable {

	private String id;

	public SuperStringEntity(){

	}

	public SuperStringEntity(String id){
		this.id = id;
	}

	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(columnDefinition = "varchar(32)")
	@Id
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean hasId(){
		return StringUtils.isNotBlank(id);
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		SuperStringEntity idEntity = (SuperStringEntity) o;

		return !(id != null ? !id.equals(idEntity.id) : idEntity.id != null);

	}

	@Override
	public int hashCode() {
		return id != null ? id.hashCode() : 0;
	}
}
