package com.zx.stlife.repository.jpa.order;

import com.base.jpa.MyJpaRepository;
import com.zx.stlife.entity.order.SnatchRecordDetail;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SnatchRecordDetailDao extends MyJpaRepository<SnatchRecordDetail, Integer>{

	@Modifying
	@Query("update SnatchRecordDetail t set t.userHeadImg=?1 where t.user.id=?2")
	public int updateUserHeadImg(String userHeadImg, Integer userId);
	
    @Query("select t from SnatchRecordDetail t where t.payRecord.id=?1")
    List<SnatchRecordDetail> findByPayRecord(Integer payRecordId);

}
