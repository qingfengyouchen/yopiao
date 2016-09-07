package com.zx.stlife.repository.jpa.service;

import com.base.jpa.MyJpaRepository;
import com.zx.stlife.entity.service.DelegateDrive;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**顺风代驾**/
public interface DelegateDriveDao  extends MyJpaRepository<DelegateDrive, Integer>{

	@Query("select d from DelegateDrive d where d.state=?1 order by d.id desc")
	public List<DelegateDrive> selListDelegateDrive(Byte state);
}
