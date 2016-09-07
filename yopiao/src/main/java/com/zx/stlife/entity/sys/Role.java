package com.zx.stlife.entity.sys;

import java.util.List;

import javax.persistence.*;

import com.base.jpa.model.SuperIntEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import com.base.modules.util.Collections3;
import com.zx.stlife.constant.Const;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 * 角色.
 * 
 * @author micheal cao
 */
@Entity
@Table(name = "sys_role")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Role extends SuperIntEntity {

	private String name;

	private Byte sortNum;

	private List<Permission> permissionList = Lists.newArrayList(); // 有序的关联对象集合

	public Role() {
		super(Const.CommonState.ENABLE);
	}

	public Role(Integer id, String name) {
		super(id);
		this.name = name;
	}

	public Role(Integer id) {
		super(id);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Byte getSortNum() {
		return sortNum;
	}

	public void setSortNum(Byte sortNum) {
		this.sortNum = sortNum;
	}

	// 多对多定义
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "sys_role_permission", joinColumns = { @JoinColumn(name = "role_id") }, inverseJoinColumns = { @JoinColumn(name = "permission_id") })
	// Fecth策略定义
	@Fetch(FetchMode.SUBSELECT)
	// 集合按id排序
	@OrderBy("id ASC")
	// 缓存策略
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	public List<Permission> getPermissionList() {
		return permissionList;
	}

	public void setPermissionList(List<Permission> permissionList) {
		this.permissionList = permissionList;
	}

	@Transient
	@JsonIgnore
	public List<String> getPermissionCodes(){
		return Collections3.extractToList(permissionList, "code");
	}

	/*#################非持久化属性####################*/
	private String hasSelected;

	@Transient
	public String getHasSelected() {
		return hasSelected;
	}

	public void setHasSelected(String hasSelected) {
		this.hasSelected = hasSelected;
	}
}
