package com.zx.stlife.repository.jpa.service;

import com.base.jpa.MyJpaRepository;
import com.zx.stlife.entity.service.Job;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**求职工作**/
public interface JobDao extends MyJpaRepository<Job, Integer>{

	@Query("select j from Job j where j.state=?1 order by j.id desc")
	public List<Job> selListJob(Byte state);
}
