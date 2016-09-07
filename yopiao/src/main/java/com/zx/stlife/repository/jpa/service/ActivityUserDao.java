package com.zx.stlife.repository.jpa.service;

import com.base.jpa.MyJpaRepository;
import com.zx.stlife.entity.service.ActivityUser;
import org.springframework.data.jpa.repository.Query;

public interface ActivityUserDao extends MyJpaRepository<ActivityUser, Integer>{

	@Query("select a from ActivityUser a where a.mobileNo=?1")
	public  ActivityUser findMobileNoActivityUser(String mobileNo);
}
