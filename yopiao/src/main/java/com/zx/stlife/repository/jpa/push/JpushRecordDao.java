package com.zx.stlife.repository.jpa.push;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.base.jpa.MyJpaRepository;
import com.zx.stlife.entity.push.JpushRecord;

public interface JpushRecordDao extends MyJpaRepository<JpushRecord, Integer> {

	/**
	 * 查询推送记录
	 */
	@Query("select t from JpushRecord t where t.userId=?1 and t.createTime between ?2 and ?3 and t.isResultOk=?4")
	public JpushRecord findJpushRecordByUser(Integer userId, Date startDate, Date endDate, Boolean isResultOk);
	
	/**
	 * 查询推送总次数
	 */
	@Query("select count(t.id) from JpushRecord t where t.userId=?1 and t.createTime>=?2 and t.isResultOk=?3")
	public Long findJpushCountByUser(Integer userId, Date startDate, Boolean isResultOk);
	
	/**
	 * 查询当天的推送用户
	 */
	@Query("select t.userId from JpushRecord t where t.createTime between ?1 and ?2 and t.isResultOk=?3")
	public List<Integer> findJpushUserByDate(Date startDate, Date endDate, Boolean isResultOk);

}
