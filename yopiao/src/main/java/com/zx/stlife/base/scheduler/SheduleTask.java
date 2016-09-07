package com.zx.stlife.base.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 被Spring的Scheduler反射调用的Service POJO.
 * 
 * @author micheal
 */
@Component
public class SheduleTask {

	private static Logger logger = LoggerFactory.getLogger(SheduleTask.class);
	/*@Autowired
	private RedPackService redPackService;*/

	/**
	 * 每天上午9点重发失败的红包
	 */
	public void resendFailRedPack(){
		logger.info("resendFailRedPack start...............");
		//redPackService.resendFailRedPack();
		logger.info("resendFailRedPack finish...............");
	}
	
}
