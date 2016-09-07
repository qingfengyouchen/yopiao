package com.zx.stlife.repository.jpa.order;

import com.base.jpa.MyJpaRepository;
import com.zx.stlife.entity.order.PayRecord;
import org.springframework.data.jpa.repository.Query;

public interface PayRecordDao extends
        MyJpaRepository<PayRecord, Integer>{

    @Query("select t from PayRecord t where t.outTradeNo=?1")
    PayRecord getByOutTradeNo(String outTradeNo);
}
