package com.zx.stlife.entity.sys;

import com.base.jpa.model.SuperIntEntity;
import com.google.common.collect.Lists;
import com.zx.stlife.constant.Const;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;

/**
 * 权限
 * 
 * @author micheal cao
 */
@Entity
@Table(name = "sys_permission")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Permission extends SuperIntEntity {

	private String name;
	private String code;
	private Byte sortNum;
	private Module module;

	private List<Role> roleList = Lists.newArrayList(); // 有序的关联对象集合

	public Permission() {
		super(Const.CommonState.ENABLE);
	}

	public Permission(Integer id) {
		super(id);
	}

	public Permission(Integer id, String name) {
		super(id);
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Byte getSortNum() {
		return sortNum;
	}

	public void setSortNum(Byte sortNum) {
		this.sortNum = sortNum;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "module_id")
	public Module getModule() {
		return module;
	}

	public void setModule(Module module) {
		this.module = module;
	}

	// 多对多定义
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "sys_role_permission", joinColumns = { @JoinColumn(name = "permission_id") }, inverseJoinColumns = { @JoinColumn(name = "role_id") })
	// Fecth策略定义
	@Fetch(FetchMode.SUBSELECT)
	// 集合按id排序
	@OrderBy("id ASC")
	// 缓存策略
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	public List<Role> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<Role> roleList) {
		this.roleList = roleList;
	}


	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	/*#################非持久化属性####################*/
	private Integer moduleId;
	private String moduleName;
	private boolean hasSelected;

	@Transient
	public Integer getModuleId() {
		if(getModule() != null){
			moduleId = getModule().getId();
		}
		return moduleId;
	}

	public void setModuleId(Integer moduleId) {
		if(moduleId != null){
			setModule(new Module(moduleId));
		}
		this.moduleId = moduleId;
	}


	@Transient
	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	@Transient
	public boolean isHasSelected() {
		return hasSelected;
	}

	public void setHasSelected(boolean hasSelected) {
		this.hasSelected = hasSelected;
	}
}
