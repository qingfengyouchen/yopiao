package com.zx.stlife.repository.jpa.member;

import com.base.jpa.MyJpaRepository;
import com.zx.stlife.entity.member.MemberMessage;


public interface MemberMessageDao extends MyJpaRepository<MemberMessage, Integer>{

//	@Modifying
//	@Query("update MemberWithdraw set iswithdraw=:iswithdraw,editTime=now() where id =:id")
//	public int updateById(@Param("iswithdraw") Integer iswithdraw, @Param("id") Integer id);

//	@Query("select t from MemberWithdraw t where t.MemberId=:MemberId")
//	public MemberWithdraw findMemberWithdrawByMemberId( @Param("MemberId") Integer MemberId);

}
