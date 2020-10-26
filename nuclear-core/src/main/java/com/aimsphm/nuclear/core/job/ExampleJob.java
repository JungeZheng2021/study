package com.aimsphm.nuclear.core.job;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import com.aimsphm.nuclear.common.annotation.DistributedLock;
import com.aimsphm.nuclear.common.annotation.LogAop;
import com.google.gson.Gson;

//@Component
public class ExampleJob {
	private Logger logger = LoggerFactory.getLogger(ExampleJob.class);

	@Autowired
	Gson gson;

	@Scheduled(cron = "0 0 2 * * ?") //2:00 am
	@DistributedLock("testDistributeLock") //add a ditributeLock
	public void monitor() throws Exception {
		/* redisUtil.lockByExpires(DtConstant.AGV_LOCATION_REDIS_LOCK,4500l) */
//		if (redisClient.lock("testDistributeLock")) {
//			try {
//				logger.info("Start monitor agv at " + new Date());
		
//		List<Testformybatisplus> plus = testformybatisplusService.list();

//			} finally {
//				Thread.currentThread().sleep(3000);
//				redisUtil.unlock(DtConstant.AGV_LOCATION_REDIS_LOCK);				
//			}
//		}
	}

}
