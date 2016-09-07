package com.zx.stlife.repository.jpa.record;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.base.jpa.MyJpaRepository;
import com.zx.stlife.entity.record.RedPack;

public interface RedPackDao extends MyJpaRepository<RedPack, Integer>{

	/**
	 * 查询积分抽红包平均值
	 */
	@Query("select avg(t.total) from RedPack t where t.sourceType=?1")
    public Double findRedPackAvgValue(Byte sourceType);
	
	/**
	 * 查询用户当天分享抽夺宝红包
	 */
	@Query("select t from RedPack t where t.user.id=?1 and t.category=?2 and t.createTime between ?3 and ?4")
    public RedPack findRedPackByShare(Integer userId, Byte category, Date startDate, Date endDate);
	
	/**
	 * 查询用户生活类红包
	 */
	@Query("select t from RedPack t where t.user.id=?1 and t.category=?2 and t.sourceType=?3")
    public RedPack findRedPackByLife(Integer userId, Byte category, Byte sourceType);
	
	/**
	 * 查询用户推荐码获得的夺宝红包
	 */
	@Query("select t from RedPack t where t.user.id=?1 and t.category=?2 and t.sourceType=?3 order by t.createTime desc")
    public List<RedPack> findRedPackListByReferral(Integer userId, Byte category, Byte sourceType);
	
    @Query("select t from RedPack t where t.user.id=?1 and t.state=?2 and t.category=?3")
    public List<RedPack> findCanUseByUser(Integer userId, Byte canUseState, Byte category);

    @Query("select t from RedPack t where " +
            "t.user.id=:userId and t.id in (:ids) and t.state=:canUseState " +
            "and (t.balance is not null and t.balance>0)")
    public List<RedPack> findCanUseRedPackByUserAndIds(@Param("userId") Integer userId,
                                                @Param("ids") List<Integer> ids,
                                                @Param("canUseState") Byte canUseState);
}
