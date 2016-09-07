package com.zx.stlife.tools.quartz;

import com.zx.stlife.base.SpringContextHolder;
import org.quartz.*;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.text.ParseException;
import java.util.Map;

public abstract class QuartzUtils {
	public final static String CRON_DATE_FORMAT = "s m H d M ? yyyy";

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void registerJob(QuartzSupportEntity entity,
								   Class<? extends QuartzJobBean> jobClass,
								   Map<String, String> params) {
		try {
			String entityName = getEntityName(entity.getClass());
			String jobGroup = getJobGroupName(entityName);
			Scheduler quartzScheduler = getQuartzScheduler();

			String jobId = entity.getId();
			JobDetailImpl jobDetail = new JobDetailImpl();
			//jobDetail.setName(jobId);
			jobDetail.setJobClass(jobClass);
			//jobDetail.setGroup(jobGroup);
			JobKey jobKey = new JobKey(jobId, jobGroup);//同时赋值name, group, key
			jobDetail.setKey(jobKey);
			if(params != null){
				jobDetail.getJobDataMap().putAll(params);
			}

			if (null != quartzScheduler.getJobDetail(jobKey)) {
				removeJobDetail(jobDetail, quartzScheduler);	//已经存在相同设置移除调度
			}

			String key = getJobObjectName(entityName);
			jobDetail.getJobDataMap().put(key, entity);

			CronTriggerImpl trigger = new CronTriggerImpl();
			trigger.setKey(new TriggerKey(jobId, jobGroup));
			trigger.setJobKey(new JobKey(jobId, jobGroup));
			trigger.setCronExpression(entity.getCronExpression());

			quartzScheduler.addJob(jobDetail, true, true);
			quartzScheduler.scheduleJob(trigger);
		} catch (SchedulerException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	/**移除调度设置*/
	public static void removeJob(QuartzSupportEntity entity) {
		Scheduler quartzScheduler = getQuartzScheduler();
		try {
			JobDetailImpl jobDetail = new JobDetailImpl();
			jobDetail.setName(entity.getId());
			jobDetail.setGroup(getJobGroupName(entity));
			removeJobDetail(jobDetail, quartzScheduler);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}
	/**移除调度设置*/
	public static void removeJobDetail(JobDetail jobDetail) throws SchedulerException {
		Scheduler quartzScheduler = SpringContextHolder.getBean("clusterQuartzScheduler");
		removeJobDetail(jobDetail, quartzScheduler);
	}

	/**移除调度设置*/
	public static void removeJobDetail(JobDetail jobDetail, Scheduler quartzScheduler) throws SchedulerException {
		JobKey jobKey = jobDetail.getKey();
		quartzScheduler.pauseJob(jobKey);
		quartzScheduler.unscheduleJob(new TriggerKey(jobKey.getName(), jobKey.getGroup()));
	}
	/**获得Quartz服务*/
	private static Scheduler getQuartzScheduler() {
		return SpringContextHolder.getBean("clusterQuartzScheduler");
	}

	private static String getJobGroupName(QuartzSupportEntity entity) {
		String entityName = getEntityName(entity.getClass());
		return getJobGroupName(entityName);
	}

	private static String getJobGroupName(String entityName) {
		return entityName + "_schedule_group";
	}

	public static String getJobObjectName(Class<? extends QuartzSupportEntity> clz) {
		String entityName = getEntityName(clz);
		return getJobObjectName(entityName);
	}

	private static String getJobObjectName(String entityName) {
		return entityName + "Object";
	}
	/**要进行定时处理的业务实体名字*/
	private static String getEntityName(Class<? extends QuartzSupportEntity> clz) {
		return clz.getSimpleName().toLowerCase();
	}

}

