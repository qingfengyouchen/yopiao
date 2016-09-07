package com.zx.stlife.tools.quartz.job;

import com.base.modules.util.SimpleUtils;
import com.base.modules.util.Threads;
import com.zx.stlife.service.order.SnatchRecordService;
import com.zx.stlife.tools.RandomUitls;
import com.zx.stlife.tools.quartz.QuartzUtils;
import org.hibernate.StaleObjectStateException;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.impl.JobDetailImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import javax.persistence.LockTimeoutException;

/**
 * 被Spring的Quartz JobDetailBean定时执行的Job类, 支持持久化到数据库实现Quartz集群.
 * 
 * 因为需要被持久化, 不能有用XXService等不能被持久化的成员变量, 
 * 只能在每次调度时从QuartzJobBean注入的applicationContext中动态取出.
 * @author Micheal
 */
public class JobRecharge extends QuartzJobBean {

	private static Logger logger = LoggerFactory.getLogger(JobRecharge.class);

	private ApplicationContext applicationContext;

	/**
	 * 从SchedulerFactoryBean注入的applicationContext.
	 */
	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}
	
	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
		JobDetailImpl jobDetail = (JobDetailImpl)context.getJobDetail();
		JobDataMap jobDataMap = jobDetail.getJobDataMap();
		String outTradeNo = jobDataMap.getString("outTradeNo");
		Boolean isH5Pay = jobDataMap.getBooleanFromString("isH5Pay");

		boolean isRemoveJobDetail = true;
		if( null == outTradeNo) {
			logger.error("outTradeNo不能为空");
		}else{
			try {
				SnatchRecordService snatchRecordService =
						applicationContext.getBean(SnatchRecordService.class);

				while(true){
					try{
						isRemoveJobDetail = snatchRecordService.queryWeixinRechargeResult(
								outTradeNo, isH5Pay!=null && isH5Pay );
						break;
					} catch (ObjectOptimisticLockingFailureException | StaleObjectStateException ex) {
						logger.info("查询微信充值订单执行 - 出现并发，商户订单号：{}", outTradeNo);
						Threads.sleep(RandomUitls.randomInt(100));
					} catch(CannotAcquireLockException | LockTimeoutException ex){
						logger.info("查询微信充值订单执行 - 出现死锁，等待继续执行，商户订单号：{}", outTradeNo);
						Threads.sleep(RandomUitls.randomInt(300));
					} catch (Exception ex) {
						logger.error("查询微信充值订单执行 - 出错，商户订单号：" + outTradeNo, ex);
						break;
					}
				}
			} catch (Exception ex) {
				logger.error("查询微信充值订单执行 - 出现异常，商户订单号：" + outTradeNo, ex);
			}
		}

		try {
			if(isRemoveJobDetail) {
				QuartzUtils.removeJobDetail(jobDetail);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
}
