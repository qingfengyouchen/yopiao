package com.zx.stlife.repository.jpa.sys;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.base.jpa.MyJpaRepository;
import com.zx.stlife.entity.sys.User;

public interface UserDao extends MyJpaRepository<User, Integer> {

	public User findByUserName(String userName);

	public User findByMobileNo(String mobileNo);

	public User findByMobileNoAndSource(String mobileNo, Byte source);
	
	public User findByOpenId(String openId);
	
	public List<User> findByIsVirtual(Boolean isVirtual);

	@Modifying
	@Query("update User set state=:state, editor=:editor, editTime=now() where id in (:ids)")
	@Transactional
	public int deleteByIds(@Param("state")Byte state, @Param("editor")String editor, @Param("ids")List<Integer> ids);

	@Query("select t.user from SnatchNum t where t.goodsTimes.id=?1 and num=?2")
	public User getByGoodsTimesAndNum(Integer goodsTimesId, Integer num);

	@Query("select t.mobileNo from User t where t.id=?1")
	public String getMobileNo(Integer id);
	
	@Query("select t.mobileNo from User t where id in (?1)")
	public List<String> getMobileNo(List<Integer> userIds);

}
