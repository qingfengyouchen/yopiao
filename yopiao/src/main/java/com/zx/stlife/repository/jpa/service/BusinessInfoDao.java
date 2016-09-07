package com.zx.stlife.repository.jpa.service;

import com.base.jpa.MyJpaRepository;
import com.zx.stlife.entity.service.BusinessInfo;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BusinessInfoDao extends MyJpaRepository<BusinessInfo, Integer>{

	@Query("from BusinessInfo where state=?1")
	public List<BusinessInfo> findByState(Byte state);

	@Modifying
	@Query("update BusinessInfo s set s.state=?1 where s.id in (?2)")
	public int deleteByIds(Byte state ,List<Integer> ids);
	
}
