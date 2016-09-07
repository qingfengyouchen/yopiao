package com.zx.stlife.repository.jpa.service;

import com.base.jpa.MyJpaRepository;
import com.zx.stlife.entity.service.RooftopGossip;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RooftopGossipDao extends MyJpaRepository<RooftopGossip, Integer>{

	@Query("from RooftopGossip where state=?1")
	public List<RooftopGossip> findByState(Byte state);

	@Modifying
	@Query("update RooftopGossip s set s.state=?1 where s.id in (?2)")
	public int deleteByIds(Byte state ,List<Integer> ids);

}
