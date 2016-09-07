package com.zx.stlife.entity.wx;

import com.base.jpa.model.SuperIntEntity;
import com.google.common.collect.Lists;
import com.zx.stlife.constant.ConstWeixinH5;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.util.List;

/**
 * 微信公众号菜单
 * 
 * @author micheal cao
 */
@Entity
@Table(name = "wx_menu")
public class WxMenu extends SuperIntEntity {

	private String name;
	private String mkey;
	private String type = ConstWeixinH5.WXMenuType.CLICK;
	private String url;
	private String msgType;
	private Byte sortNum;
	private WxMenu father;

	private List<WxMenu> childrens = Lists.newArrayList();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMkey() {
		return mkey;
	}

	public void setMkey(String mkey) {
		this.mkey = mkey;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	public Byte getSortNum() {
		return sortNum;
	}

	public void setSortNum(Byte sortNum) {
		this.sortNum = sortNum;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "father_id")
	public WxMenu getFather() {
		return father;
	}

	public void setFather(WxMenu father) {
		this.father = father;
	}

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "father_id")
	@OrderBy("sort_num")
	public List<WxMenu> getChildrens() {
		return childrens;
	}

	public void setChildrens(List<WxMenu> childrens) {
		this.childrens = childrens;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
