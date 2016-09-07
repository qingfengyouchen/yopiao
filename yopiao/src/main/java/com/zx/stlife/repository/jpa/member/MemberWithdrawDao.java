package com.zx.stlife.repository.jpa.member;

import com.base.jpa.MyJpaRepository;
import com.zx.stlife.entity.member.MemberLevel;
import com.zx.stlife.entity.member.MemberWithdraw;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemberWithdrawDao extends MyJpaRepository<MemberWithdraw, Integer>{

//	@Modifying
//	@Query("update MemberWithdraw set iswithdraw=:iswithdraw,editTime=now() where id =:id")
//	public int updateById(@Param("iswithdraw") Integer iswithdraw, @Param("id") Integer id);

//	@Query("select t from MemberWithdraw t where t.MemberId=:MemberId")
//	public MemberWithdraw findMemberWithdrawByMemberId( @Param("MemberId") Integer MemberId);

}
