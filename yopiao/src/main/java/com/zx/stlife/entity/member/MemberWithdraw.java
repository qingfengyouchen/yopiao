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
@Table(name = "sys_member_withdraw")
public class MemberWithdraw extends SuperIntVersion {

    /**
     * Default constructor
     */
    public MemberWithdraw() {
    }

    public MemberWithdraw(Member member,Integer jifen,float price,Date editTime) {
        super(Const.CommonState.ENABLE);
      this.member=member;
      this.jifen=jifen;
      this.price=price;
      this.editTime=editTime;
    }

   private Member member;
   
   private Integer jifen;
   
   private float price;
   
   private Date editTime;
   
   private Integer isdone;
   


   @ManyToOne(fetch= FetchType.LAZY)
   @JoinColumn(name = "member_id")
	public Member getMember() {
		return member;
	}
	
	public void setMember(Member member) {
		this.member = member;
	}
	
	public Integer getJifen() {
		return jifen;
	}
	
	public void setJifen(Integer jifen) {
		this.jifen = jifen;
	}
	
	public float getPrice() {
		return price;
	}
	
	public void setPrice(float price) {
		this.price = price;
	}


	public Date getEditTime() {
		return editTime;
	}

	public void setEditTime(Date editTime) {
		this.editTime = editTime;
	}
	
	public Integer getIsdone() {
		return isdone;
	}

	public void setIsdone(Integer isdone) {
		this.isdone = isdone;
	}
}