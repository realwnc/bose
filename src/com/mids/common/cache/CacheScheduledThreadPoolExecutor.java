package com.mids.common.cache;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CacheScheduledThreadPoolExecutor
{
	private static final Logger LOGGER = LoggerFactory.getLogger(CacheScheduledThreadPoolExecutor.class);
	
	private static int MAX_POOL_SIZE = 10;	
	private static ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor(MAX_POOL_SIZE);
	
	private static int Test_Thread_Timeval = 30;
	
	public static int startCacheScheduledTask()
	{
		Test_Thread test_Thread = new Test_Thread();
		exec.scheduleAtFixedRate(test_Thread, 1, Test_Thread_Timeval, TimeUnit.SECONDS);
		
		
		return 0;
	}
	
	private static class Test_Thread implements Runnable{
		public Test_Thread(){}
		
		@Override
	    public void run() {
			LOGGER.info("---------->Thread Test_Thread begin ....................");
			
			LOGGER.info("---------->Thread Test_Thread endxx ....................");
		}
		
		@Override
	    public String toString(){
	        return "";
	    }
	}
	
}


