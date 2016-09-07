package com.zx.stlife.repository.jpa.order;

import com.base.jpa.MyJpaRepository;
import com.zx.stlife.entity.order.SnatchNum;
import org.springframework.data.jpa.repository.Query;

public interface SnatchNumDao extends MyJpaRepository<SnatchNum, Integer> {

}
