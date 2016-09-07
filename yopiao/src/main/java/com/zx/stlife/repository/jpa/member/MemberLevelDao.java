package com.zx.stlife.repository.jpa.member;

import com.base.jpa.MyJpaRepository;
import com.zx.stlife.entity.member.MemberLevel;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemberLevelDao extends MyJpaRepository<MemberLevel, Integer>{

	@Modifying
	@Query("update MemberLevel set state=:state where id in (:ids)")
	public int deleteByIds(@Param("state") Byte state, @Param("ids") List<Integer> ids);

	@Query("select t from MemberLevel t where t.state=:state and t.minValue=:minValue")
	public MemberLevel findMemberLevelByMinValue(@Param("state") Byte state, @Param("minValue") Integer minValue);

}
