package com.zx.stlife.repository.jpa.record;

import com.base.jpa.MyJpaRepository;
import com.zx.stlife.entity.record.RechargeRecord;
import org.springframework.data.jpa.repository.Query;

public interface RechargeRecordDao extends MyJpaRepository<RechargeRecord, Integer>{

    @Query("select t from RechargeRecord t where t.outTradeNo=?1")
    RechargeRecord getByOutTradeNo(String outTradeNo);
}
