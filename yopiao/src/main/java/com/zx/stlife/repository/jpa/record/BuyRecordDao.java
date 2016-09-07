package com.zx.stlife.repository.jpa.record;

import com.base.jpa.MyJpaRepository;
import com.zx.stlife.entity.record.BuyRecord;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BuyRecordDao extends MyJpaRepository<BuyRecord, Integer>{

    @Query("select sum(t.timeValue) from BuyRecord t where t.goodsTimes.id=?1 and t.state=?2")
    Long sumTimeValue(Integer goodsTimesId, Byte state);

    @Query("select t from BuyRecord t where t.goodsTimes.id=?1 and t.state=?2 order by t.snatchTime desc")
    List<BuyRecord> findByGoodsTimes(Integer goodsTimesId, Byte state);

}
