package com.zx.stlife.repository.jpa.service;

import com.base.jpa.MyJpaRepository;
import com.zx.stlife.entity.service.FunFood;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FunFoodDao extends MyJpaRepository<FunFood, Integer>{

	@Query("from FunFood where state=?1")
	public List<FunFood> findByState(Byte state);

	@Modifying
	@Query("update FunFood s set s.state=?1 where s.id in (?2)")
	public int deleteByIds(Byte state ,List<Integer> ids);

}
