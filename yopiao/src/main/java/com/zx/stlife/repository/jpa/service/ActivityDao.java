package com.zx.stlife.repository.jpa.service;

import com.base.jpa.MyJpaRepository;
import com.zx.stlife.entity.service.Activity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ActivityDao extends MyJpaRepository<Activity, Integer>{

	@Query("select a from Activity a where a.state=?1 order by a.id desc")
	public List<Activity> selListActivity(Byte state);
	
	@Modifying
	@Query("update Activity a set a.thumbImgUrl=null where a.id=?1")
	public void updateThumbImgUrl(Integer id);
	
	@Modifying
	@Query("update Activity a set a.imgUrl=null where a.id=?1")
	public void updateImgUrl(Integer id);
}
