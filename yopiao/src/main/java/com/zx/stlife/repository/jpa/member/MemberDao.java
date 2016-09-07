package com.zx.stlife.repository.jpa.member;

import com.base.jpa.MyJpaRepository;
import com.zx.stlife.entity.member.Member;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemberDao extends MyJpaRepository<Member, Integer>{

	/**
	 * 获取会员资料
	 */
	@Query("select t from Member t where t.user.id=?1")
	public Member findMemberByUserId(Integer userId);

	/**
	 * 充值余额
	 */
	@Modifying
	@Query("update Member t set t.balance=t.balance+:balance where t.user.id=:userId")
	public int updateBalance(@Param("userId") Integer userId, @Param("balance") Integer balance);

	/**
	 * 改为删除状态并更新时间
	 */
	@Modifying
	@Query("update Member t set t.state=:state,t.editTime=now() where t.id in (:ids)")
	public int deleteByIds(@Param("state") Byte state, @Param("ids") List<Integer> ids);
	
	@Query("select t.headImg from Member t where t.user.id=?1")
	public String getHeadImgByUser(Integer userId);

	@Query("select t.balance from Member t where t.user.id=?1")
	public Integer getBalanceByUser(Integer userId);

}
