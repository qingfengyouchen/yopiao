package com.zx.stlife.repository.jpa.service;

import com.base.jpa.MyJpaRepository;
import com.zx.stlife.entity.service.Tutoring;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TutoringDao extends MyJpaRepository<Tutoring, Integer>{

	@Query("from Tutoring where state=?1")
	public List<Tutoring> findByState(Byte state);

	@Modifying
	@Query("update Tutoring s set s.state=?1 where s.id in (?2)")
	public int deleteByIds(Byte state ,List<Integer> ids);
}
