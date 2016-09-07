package com.zx.stlife.repository.jpa.order;

import com.base.jpa.MyJpaRepository;
import com.zx.stlife.entity.order.WinngUserReceiveInfo;
import org.springframework.data.jpa.repository.Query;

public interface WinngUserReceiveInfoDao extends MyJpaRepository<WinngUserReceiveInfo, Integer>{

    @Query("select t from WinngUserReceiveInfo t where t.goodsTimes.id=?1")
    WinngUserReceiveInfo getByGoodsTimes(Integer goodsTimesId);

    @Query("select t.mobileNo from WinngUserReceiveInfo t where t.goodsTimes.id=?1")
    String getMobileNoByGoodsTimes(Integer goodsTimesId);

}
