package com.zx.stlife.entity.record;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.base.jpa.model.SuperIntEntity;
import com.zx.stlife.constant.Const;
import com.zx.stlife.entity.member.Member;
import com.zx.stlife.entity.sys.User;
import com.zx.stlife.tools.DateUtils;

/**
 * 积分
 */
@Entity
@Table(name = "record_jifen")
public class Jifen extends SuperIntEntity {

	/**
     * 
     */
	private Member member;

	/**
	 * 积分
	 */
	private Integer amount;

	/**
     *
     */
	private String descr;

	/**
     * 
     */
	private User user;

	/**
	 * Default constructor
	 */
	public Jifen() {
		super(Const.CommonState.ENABLE);
	}

	public Jifen(Member member, Integer amount, String descr, User user) {
		super(Const.CommonState.ENABLE);
		this.member = member;
		this.amount = amount;
		this.descr = descr;
		this.user = user;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	// #####非持久化属性#####
	private String abtainTime;

	@Transient
	public String getAbtainTime() {
		abtainTime = DateUtils.dateToYYYYMMDDHHMMSSSSSString(getCreateTime());
		return abtainTime;
	}

	public void setAbtainTime(String abtainTime) {
		this.abtainTime = abtainTime;
	}

}