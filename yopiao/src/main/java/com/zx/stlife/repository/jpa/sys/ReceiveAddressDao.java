package com.zx.stlife.repository.jpa.sys;

import com.base.jpa.MyJpaRepository;
import com.zx.stlife.entity.sys.ReceiveAddress;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ReceiveAddressDao extends MyJpaRepository<ReceiveAddress, Integer> {
	@Query("from ReceiveAddress r where r.user.id=?1 order by r.isDefault desc,r.createTime")
	public List<ReceiveAddress> findByUserId(Integer userId);
	
	@Query("from ReceiveAddress r where r.user.id=?1 and r.isDefault= true")
	public ReceiveAddress findByState(Integer userId);
	
	@Modifying
	@Query("delete from ReceiveAddress r where r.user.id=?1 and r.id=?2")
	public int deleteReceiveAddress(Integer userId,Integer id);

	@Modifying
	@Query("update ReceiveAddress set isDefault= false where id in (:ids)")
	public int updateSetAllFalse(@Param("ids")List<Integer> ids);

	@Modifying
	@Query("update ReceiveAddress set isDefault= true where id = :addressId")
	public int updateSetDefault(@Param("addressId")Integer addressId);
}
