package com.zx.stlife.base.scheduler;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.base.modules.util.Threads;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.support.TaskUtils;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 用JDKScheduledThreadPoolExecutor定时执行的任务。
 * 相比Spring的Task NameSpace配置方, 不需要反射調用，并强化了退出超时控制.
 * 
 * @author micheal
 */
public class JdkTimerJob implements Runnable {
	
	private static Logger logger = LoggerFactory.getLogger(JdkTimerJob.class);
	/**首次执行延期*/
	private int initialDelay = 0;
	/**每次执行间隔*/
	private int period = 0;
	/**shutdown时等待任务完成, 最多等待的时间*/
	private int shutdownTimeout = Integer.MAX_VALUE;

	private ScheduledExecutorService scheduledExecutorService;

	//@Autowired
	//private RedPackService redPackService;

	@PostConstruct
	public void start() throws Exception {

		// 任何异常不会中断schedule执行, 由Spring TaskUtils的LOG_AND_SUPPRESS_ERROR_HANDLER進行处理
		Runnable task = TaskUtils.decorateTaskWithErrorHandler(this, null, true);

		// 创建单线程的SechdulerExecutor,并用guava的ThreadFactoryBuilder设定生成线程的名称
		scheduledExecutorService = Executors.newSingleThreadScheduledExecutor(new ThreadFactoryBuilder().setNameFormat(
				"JdkTimerJob-%1$d").build());

		// scheduleAtFixedRatefixRate() 固定任务两次启动之间的时间间隔.
		// scheduleAtFixedDelay() 固定任务结束后到下一次启动间的时间间隔.
		scheduledExecutorService.scheduleAtFixedRate(task, initialDelay, period, TimeUnit.MILLISECONDS);
	}

	@PreDestroy
	public void stop() {
		Threads.normalShutdown(scheduledExecutorService, shutdownTimeout, TimeUnit.MILLISECONDS);
	}

	/**
	 * 定时打印当前用户数到日志.
	 */
	@Override
	public synchronized void run() {
		logger.info("GET Red Pack Start");
		
		//redPackService.getRedPackStatus();

		logger.info("GET Red Pack End");
	}

	/**
	 * 设置任务初始启动延时时间.
	 */
	public void setInitialDelay(int initialDelay) {
		this.initialDelay = initialDelay;
	}

	/**
	 * 设置任务间隔时间,单位毫秒.
	 */
	public void setPeriod(int period) {
		this.period = period;
	}

	/**
	 * 设置normalShutdown的等待时间, 单位毫秒.
	 */
	public void setShutdownTimeout(int shutdownTimeout) {
		this.shutdownTimeout = shutdownTimeout;
	}
}
