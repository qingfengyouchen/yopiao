package com.zx.stlife.entity.sys;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.base.jpa.model.SuperIntEntity;
import com.base.modules.util.Collections3;
import com.base.modules.util.DateUtilsEx;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import com.zx.stlife.base.UserUtils;
import com.zx.stlife.constant.Const;

/**
 * 用户.
 * 
 * @author micheal cao
 */
@Entity
@Table(name = "sys_user")
// 默认的缓存策略.
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class User extends SuperIntEntity {
	/**用户名*/
	private String userName;
	/**昵称*/
	private String nickName;
	/**真实姓名*/
	private String trueName;
	private String plainPassword;
	private String password;
	private String salt;
	private String mobileNo;
	private Byte gender;
	private String creator;
	private Date editTime;
	private String editor;
	/**是否虚拟用户*/
	private Boolean isVirtual = false;
	/**第三方openId*/
	private String openId;
	/**来源*/
	private Byte source;

	private List<Role> roleList = Lists.newArrayList(); // 有序的关联对象集合

	public User() {
		super(Const.CommonState.ENABLE);
		this.source = Const.UserSource.REGISTER;
	}

	public User(Integer id) {
		super(id, Const.CommonState.ENABLE);
	}

	public User(Integer id, String trueName, String mobileNo) {
		super(id, Const.CommonState.ENABLE);
		this.trueName = trueName;
		this.mobileNo = mobileNo;
	}

	public User(String userName, String nickName,
				String plainPassword, Byte gender, Boolean isVirtual) {
		super(Const.CommonState.ENABLE);
		this.userName = userName;
		this.nickName = nickName;
		this.plainPassword = plainPassword;
		this.gender = gender;
		this.isVirtual = isVirtual;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getNickName() {
		if(StringUtils.isBlank(nickName)){
			nickName = getUserName();
		}
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getTrueName() {
		return trueName;
	}

	public void setTrueName(String trueName) {
		this.trueName = trueName;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public Byte getGender() {
		return gender;
	}

	public void setGender(Byte gender) {
		this.gender = gender;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Date getEditTime() {
		return editTime;
	}

	public void setEditTime(Date editTime) {
		this.editTime = editTime;
	}

	public String getEditor() {
		return editor;
	}

	public void setEditor(String editor) {
		this.editor = editor;
	}

	public void fillOperateAndTime(){
		User user = UserUtils.getCurrentUser();
		if(hasId()){
			if(user != null) {
				this.setEditor(user.getName());
			}
			this.setEditTime(DateUtilsEx.getNow());
		}else{
			if(user != null) {
				this.setCreator(user.getName());
			}
			this.setCreateTime(DateUtilsEx.getNow());
		}
	}

	public Boolean getIsVirtual() {
		return isVirtual;
	}

	public void setIsVirtual(Boolean isVirtual) {
		this.isVirtual = isVirtual;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public Byte getSource() {
		return source;
	}

	public void setSource(Byte source) {
		this.source = source;
	}

	// 多对多定义
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "sys_user_role", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = { @JoinColumn(name = "role_id") })
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

	@Transient
	@JsonIgnore
	public String getRoleNames() {
		return Collections3.extractToString(roleList, "name", ", ");
	}

	//########非持久化属性
	private String name;
	/**是否来源第三方登录*/
	private boolean isFromThirdpart;

	@Transient
	public String getName() {
		if(StringUtils.isBlank(name)){
			name = StringUtils.isBlank(trueName) ?
					(StringUtils.isBlank(nickName) ? userName : nickName) : trueName;
		}
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Transient
	public String getPlainPassword() {
		return plainPassword;
	}

	public void setPlainPassword(String plainPassword) {
		this.plainPassword = plainPassword;
	}

	@Transient
	public String getUserInfo(){
		StringBuffer sb = new StringBuffer(mobileNo == null ? "" : (mobileNo + "#") )
				.append(getName());
		if(sb.length() > 32){
			return sb.substring(0, 32);
		}

		return sb.toString();
	}

	@Transient
	public boolean isFromThirdpart() {
		return StringUtils.isNotBlank(getOpenId());
	}

	public void setIsFromThirdpart(boolean isFromThirdpart) {
		this.isFromThirdpart = isFromThirdpart;
	}
}