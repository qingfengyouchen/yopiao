package com.zx.stlife.repository.jpa.service;

import com.base.jpa.MyJpaRepository;
import com.zx.stlife.entity.service.Recruitment;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RecruitmentDao extends MyJpaRepository<Recruitment, Integer>{

	@Query("select r from Recruitment r where r.state=?1 order by r.id")
	public List<Recruitment> selListRecruitment(Byte state);
}
