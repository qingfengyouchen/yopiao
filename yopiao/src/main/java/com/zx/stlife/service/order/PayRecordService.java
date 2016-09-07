package com.zx.stlife.service.order;

import com.zx.stlife.entity.order.PayRecord;
import com.zx.stlife.repository.jpa.order.PayRecordDao;
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
public class PayRecordService {

	private static Logger logger = LoggerFactory.getLogger(PayRecordService.class);

	@Autowired
	private PayRecordDao payRecordDao;

	public PayRecord get(Integer id){
		return payRecordDao.findOne(id);
	}

	@Transactional
	public void save(PayRecord entity){
		entity = payRecordDao.save(entity);
	}

	public PayRecord getByOutTradeNo(String outTradeNo){
		return payRecordDao.getByOutTradeNo(outTradeNo);
	}
}
