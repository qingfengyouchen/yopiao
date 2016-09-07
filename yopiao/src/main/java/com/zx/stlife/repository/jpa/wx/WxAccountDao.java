package com.zx.stlife.repository.jpa.wx;

import com.base.jpa.MyJpaRepository;
import com.zx.stlife.entity.wx.WxAccount;
import org.springframework.data.jpa.repository.Query;

public interface WxAccountDao extends MyJpaRepository<WxAccount, Integer> {
    @Query
    WxAccount findByEmail(String email);
}
