package com.mids.common.quartz;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException; 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ContextLoader;

import com.mids.common.properties.SystemPropertiesUtil;
import com.mids.sysassist.SpringContextHolder;

//JobDetail jobDetail = new JobDetail("job1_1","jGroup1", MyJob.class);

public class MyJob implements Job {  
	private static final Logger LOGGER = LoggerFactory.getLogger(MyJob.class);
	//private static BusServerService busServerService = (BusServerService)SpringContextHolder.getBean(BusServerService.class);
	
	public void execute(JobExecutionContext jobCtx)throws JobExecutionException
	{
		JobDataMap data = jobCtx.getJobDetail().getJobDataMap();
		String serverId = data.getString("sid");
		Integer sid = Integer.parseInt(serverId);
		
		LOGGER.info("--------->job execute begin...");
	}
	
}  