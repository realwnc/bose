package com.mids.common.quartz;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.util.Map;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 类名：MyQuartzManager <br/>
 * 功能：<br/>
 * 详细：Quartz增、删、改工具类 <br/>
 * 作者： wncheng <br/>
 * 日期：2017-7-17 <br/>
 */
public class MyQuartzManager {  
	private static final Logger LOGGER = LoggerFactory.getLogger(MyQuartzManager.class);
  
	private static Scheduler scheduler = null;    
    public static final String DATA_KEY = "STATE_DATA";
     
    static {
        try {
            scheduler = new StdSchedulerFactory().getScheduler();
            LOGGER.info("初始化调度器 ");
        } catch (SchedulerException ex) {
        	LOGGER.error("初始化调度器=> [失败]:" + ex.getLocalizedMessage());
        }
    }   
    public static void addJob(String name, String group, Class<? extends Job> clazz, String cronExpression) {                 
        try {       
            //构造任务
            JobDetail job = newJob(clazz).withIdentity(name, group).build();             
            //构造任务触发器
            Trigger trg = newTrigger().withIdentity(name, group).withSchedule(cronSchedule(cronExpression)).build();       
             
            //将作业添加到调度器
            scheduler.scheduleJob(job, trg);
            LOGGER.info("创建作业=> [作业名称：" + name + " 作业组：" + group + "] ");
        } catch (SchedulerException e) {
            e.printStackTrace();
            LOGGER.error("创建作业=> [作业名称：" + name + " 作业组：" + group + "]=> [失败]");
        }
    }
    
    public static void addJob(String name, String group, Class<? extends Job> clazz, String cronExpression, Map<String, String> dataMap) {                 
        try {       
            //构造任务
        	JobDataMap jobDataMap = new JobDataMap();
        	if(dataMap != null)
        	{
        		for(Map.Entry<String, String> e: dataMap.entrySet())
        		{
            		jobDataMap.put(e.getKey(), e.getValue());
        		}        		
        	}
            JobDetail job = newJob(clazz).withIdentity(name, group).setJobData(jobDataMap).build();             
            //构造任务触发器
            Trigger trg = newTrigger().withIdentity(name, group).withSchedule(cronSchedule(cronExpression)).build();       
             
            //将作业添加到调度器
            scheduler.scheduleJob(job, trg);
            LOGGER.info("创建作业=> [作业名称：" + name + " 作业组：" + group + "] ");
        } catch (SchedulerException e) {
            e.printStackTrace();
            LOGGER.error("创建作业=> [作业名称：" + name + " 作业组：" + group + "]=> [失败]");
        }
    }
     
    public static void removeJob(String name, String group){
        try {
            TriggerKey tk = TriggerKey.triggerKey(name, group);
            scheduler.pauseTrigger(tk);//停止触发器  
            scheduler.unscheduleJob(tk);//移除触发器
            JobKey jobKey = JobKey.jobKey(name, group);
            scheduler.deleteJob(jobKey);//删除作业
            LOGGER.info("删除作业=> [作业名称：" + name + " 作业组：" + group + "] ");
        } catch (SchedulerException e) {
            e.printStackTrace();
            LOGGER.error("删除作业=> [作业名称：" + name + " 作业组：" + group + "]=> [失败]");
        }
    }
     
    public static void pauseJob(String name, String group){
        try {
            JobKey jobKey = JobKey.jobKey(name, group);
            scheduler.pauseJob(jobKey);
            LOGGER.info("暂停作业=> [作业名称：" + name + " 作业组：" + group + "] ");
        } catch (SchedulerException e) {
            e.printStackTrace();
            LOGGER.error("暂停作业=> [作业名称：" + name + " 作业组：" + group + "]=> [失败]");
        }
    }
     
    public static void resumeJob(String name, String group){
        try {
            JobKey jobKey = JobKey.jobKey(name, group);         
            scheduler.resumeJob(jobKey);
            LOGGER.info("恢复作业=> [作业名称：" + name + " 作业组：" + group + "] ");
        } catch (SchedulerException e) {
            e.printStackTrace();
            LOGGER.error("恢复作业=> [作业名称：" + name + " 作业组：" + group + "]=> [失败]");
        }
    }
     
    public static void modifyTime(String name, String group, String cronExpression){
        try {
            TriggerKey tk = TriggerKey.triggerKey(name, group);
            //构造任务触发器
            Trigger trg = newTrigger().withIdentity(name, group).withSchedule(cronSchedule(cronExpression)).build();       
            scheduler.rescheduleJob(tk, trg);
            LOGGER.info("修改作业触发时间=> [作业名称：" + name + " 作业组：" + group + "] ");
        } catch (SchedulerException e) {
            e.printStackTrace();
            LOGGER.error("修改作业触发时间=> [作业名称：" + name + " 作业组：" + group + "]=> [失败]");
        }
    }
 
    public static void start() {
        try {
            scheduler.start();
            LOGGER.info("启动调度器 ");
        } catch (SchedulerException e) {
            e.printStackTrace();
            LOGGER.error("启动调度器=> [失败]");
        }
    }
 
    public static void shutdown() {
        try {
            scheduler.shutdown();
            LOGGER.info("停止调度器 ");
        } catch (SchedulerException e) {
            e.printStackTrace();
            LOGGER.error("停止调度器=> [失败]");
        } 
    }
    
    //"0 15 10 ? * MON-FRI"每个周一、周二、周三、周四、周五的10：15触发
    public static void main(String[] args){
    	//MyQuartzManager.addJob("MyJob", "MyGroup1", MyJob.class, "0 0/5 14,18 * * ? ");//每天在下午2：00至2：59和6：00至6：59之间的每5分钟触发一次
    	MyQuartzManager.addJob("MyJob", "MyGroup1", MyJob.class, "0 15 10 ? * MON-FRI ");//"0 15 10 ? * MON-FRI"每个周一、周二、周三、周四、周五的10：15触发
	}
}  