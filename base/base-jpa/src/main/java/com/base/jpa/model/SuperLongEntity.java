/*******************************************************************************
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package com.base.jpa.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

@MappedSuperclass
public abstract class SuperLongEntity implements Serializable {

	private Long id;

	public SuperLongEntity(){

	}

	public SuperLongEntity(Long id){
		this.id = id;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean hasId(){
		return id != null;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		SuperLongEntity idEntity = (SuperLongEntity) o;

		return !(id != null ? !id.equals(idEntity.id) : idEntity.id != null);

	}

	@Override
	public int hashCode() {
		return id != null ? id.hashCode() : 0;
	}

}
