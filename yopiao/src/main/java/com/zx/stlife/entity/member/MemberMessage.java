package com.zx.stlife.entity.member;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.zx.stlife.constant.Const;
import com.zx.stlife.entity.SuperIntVersion;
import com.zx.stlife.entity.member.Member;

/**
 * 会员提现
 */
@Entity
@Table(name = "sys_member_message")
public class MemberMessage extends SuperIntVersion {

    /**
     * Default constructor
     */
    public MemberMessage() {
    }

    public MemberMessage(Member member,String message) {
        super(Const.CommonState.ENABLE);
      this.member=member;
      this.message=message;

    }

   private Member member;
   
   private String message;
   
   private Integer isread;


   @ManyToOne(fetch= FetchType.LAZY)
   @JoinColumn(name = "member_id")
	public Member getMember() {
		return member;
	}
	
	public void setMember(Member member) {
		this.member = member;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Integer getIsread() {
		return isread;
	}

	public void setIsread(Integer isread) {
		this.isread = isread;
	}
	
	
}