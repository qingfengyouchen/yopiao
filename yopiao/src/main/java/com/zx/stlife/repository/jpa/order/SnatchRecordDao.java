package com.zx.stlife.repository.jpa.order;

import com.base.jpa.MyJpaRepository;
import com.zx.stlife.entity.order.SnatchRecord;
import com.zx.stlife.vo.snatch.SnatchRecord4GoodsTimesVo;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SnatchRecordDao extends MyJpaRepository<SnatchRecord, Integer>{

    @Query("select t from SnatchRecord t where t.user.id=?1 and t.goodsTimes.id=?2 and t.state=?3")
    SnatchRecord findByUserAndGoodsTimes(Integer userId, Integer goodsTimesId, Byte state);

    @Query("select t.buyTimes from SnatchRecord t where t.goodsTimes.id=?1 and t.user.id=?2 and t.state=?3")
    Integer getBuyTimesByGoodsTimesAndUser(Integer goodsTimeId, Integer userId, Byte state);

    @Query("select new com.zx.stlife.vo.snatch.SnatchRecord4GoodsTimesVo(" +
            "u.id, u.userName, u.mobileNo, u.nickName, t.buyTimes) " +
            "from SnatchRecord t left join t.user u  " +
            "where t.goodsTimes.id=?1 and t.state=?2 order by t.editTime desc")
    List<SnatchRecord4GoodsTimesVo> findByGoodsTimes(Integer goodsTimesId, Byte state);

    @Query("select count(t.id) from SnatchRecord t left join t.user u " +
            "where t.goodsTimes.id=?1 and u.isVirtual=true and t.state=?2")
    Integer getVirtualUserBuyAmount(Integer goodsTimesId, Byte state);
}
