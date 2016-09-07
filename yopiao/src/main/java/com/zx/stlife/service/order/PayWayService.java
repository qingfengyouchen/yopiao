package com.zx.stlife.service.order;

import com.zx.stlife.constant.Const;
import com.zx.stlife.entity.order.PayRecord;
import com.zx.stlife.entity.order.PayWay;
import com.zx.stlife.repository.jpa.order.PayWayDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author micheal cao
 */
// Spring Service Bean的标识.
@Component
@Transactional(readOnly = true)
public class PayWayService {

	private static Logger logger = LoggerFactory.getLogger(PayWayService.class);

	@Autowired
	private PayWayDao payWayDao;

	@Transactional
	public void save(PayWay entity){
		entity = payWayDao.save(entity);
	}

	@Transactional
	public void delete(PayWay entity){
		entity.setState(Const.CommonState.DELETED);
		save(entity);
		//payWayDao.delete(entity);
	}
}
