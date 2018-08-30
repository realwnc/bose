package com.mids.springtask;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service; 

import com.mids.service.SysLogService;

@Service
public class CronTaskJob { 
	private static final Logger LOGGER = LoggerFactory.getLogger(CronTaskJob.class);
	
	@Autowired(required = true)
    private SysLogService sysLogService;
	
    
	private static int SYSLOG_EXPIRED_DAYS = 90;//DAY
	public void deleteExpiredSysLog()
    {
    	LOGGER.info("--------->SpringTaskServiceImpl delete expired syslog begin......");
    	Date beginDate = new Date();
    	int n = sysLogService.deleteExpired(SYSLOG_EXPIRED_DAYS);
    	long consumeTime = (new Date()).getTime() - beginDate.getTime();
    	LOGGER.info("--------->SpringTaskServiceImpl delete expired syslog begin......, consume time={} ms", consumeTime);
    }
	
}