package com.zx.stlife.entity.sys;

import com.base.jpa.model.SuperIntEntity;
import com.google.common.collect.Lists;
import com.zx.stlife.constant.Const;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.List;

/**
 * 模块
 * 
 * @author micheal cao
 */
@Entity
@Table(name = "sys_module")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Module extends SuperIntEntity {

	private String name;
	private Byte sortNum;

	private List<Permission> permissionList = Lists.newArrayList();

	public Module() {
		super(Const.CommonState.ENABLE);
	}

	public Module(Integer id) {
		super(id, Const.CommonState.ENABLE);
	}

	public Module(Integer id, String name) {
		super(id, Const.CommonState.ENABLE);
		this.name = name;
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

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "module_id")
	@OrderBy("sort_num")
	public List<Permission> getPermissionList() {
		return permissionList;
	}

	public void setPermissionList(List<Permission> permissionList) {
		this.permissionList = permissionList;
	}
}
