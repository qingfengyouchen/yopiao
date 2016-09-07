package com.base.jpa.model;

import com.base.modules.util.DateUtilsEx;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@MappedSuperclass
public abstract class SuperIntEntity implements Serializable {

	private Integer id;

	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 状态
	 */
	private Byte state;

	public SuperIntEntity(){
		this.createTime = DateUtilsEx.getNow();
	}

	public SuperIntEntity(Integer id){
		this.id = id;
		this.createTime = DateUtilsEx.getNow();
	}

	public SuperIntEntity(Byte state) {
		this.state = state;
		if(this.createTime == null){
			this.createTime = DateUtilsEx.getNow();
		}
	}

	public SuperIntEntity(Integer id, Byte state) {
		this.id = id;
		this.state = state;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Byte getState() {
		return state;
	}

	public void setState(Byte state) {
		this.state = state;
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

		SuperIntEntity idEntity = (SuperIntEntity) o;

		return !(id != null ? !id.equals(idEntity.id) : idEntity.id != null);

	}

	@Override
	public int hashCode() {
		return id != null ? id.hashCode() : 0;
	}

}
