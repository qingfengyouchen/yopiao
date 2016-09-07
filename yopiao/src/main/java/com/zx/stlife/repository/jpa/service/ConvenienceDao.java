package com.zx.stlife.repository.jpa.service;

import com.base.jpa.MyJpaRepository;
import com.zx.stlife.entity.service.Convenience;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ConvenienceDao extends MyJpaRepository<Convenience, Integer>{
	@Query("from Convenience where state=?1")
	public List<Convenience> findByState(Byte state);

	@Modifying
	@Query("delete from Convenience where id in (:ids)")
	int deleteByIds(@Param("ids") List<Integer> ids);

}
