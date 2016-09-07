package com.zx.stlife.repository.jpa.service;

import com.base.jpa.MyJpaRepository;
import com.zx.stlife.entity.service.MicroBusiness;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MicroBusinessDao extends MyJpaRepository<MicroBusiness, Integer> {

	@Query("from MicroBusiness where state=?1")
	public List<MicroBusiness> findByState(Byte state);

	@Modifying
	@Query("update MicroBusiness s set s.state=?1 where s.id in (?2)")
	public int deleteByIds(Byte state ,List<Integer> ids);
}
