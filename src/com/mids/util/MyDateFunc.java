package com.mids.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MyDateFunc {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("当天24点时间：" + getTimesnight().toLocaleString());
		System.out.println("当前时间：" + new Date().toLocaleString());
		System.out.println("当天0点时间：" + getTimesmorning().toLocaleString());
		System.out.println("昨天0点时间：" + getYesterdaymorning().toLocaleString());
		System.out.println("近7天时间：" + getWeekFromNow().toLocaleString());
		System.out.println("本周周一0点时间：" + getTimesWeekmorning().toLocaleString());
		System.out.println("本周周日24点时间：" + getTimesWeeknight().toLocaleString());
		System.out.println("本月初0点时间：" + getTimesMonthmorning().toLocaleString());
		System.out.println("本月未24点时间：" + getTimesMonthnight().toLocaleString());
		System.out.println("上月初0点时间：" + getLastMonthStartMorning().toLocaleString());
		if(getCurrentQuarterStartTime()!=null){
		System.out.println("本季度开始点时间：" + getCurrentQuarterStartTime().toLocaleString());
		}
		System.out.println("本季度结束点时间：" + getCurrentQuarterEndTime().toLocaleString());
		System.out.println("本年开始点时间：" + getCurrentYearStartTime().toLocaleString());
		System.out.println("本年结束点时间：" + getCurrentYearEndTime().toLocaleString());
		System.out.println("上年开始点时间：" + getLastYearStartTime().toLocaleString());
	}

	// 获得当天0点时间
	public static Date getTimesmorning() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();


	}
	// 获得当天24点时间
	public static Date getTimesnight() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 24);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}
	
	// 获得当天23:59:59点时间
	public static Date getTimesnightLastSecond() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	// 获得昨天0点时间
	public static Date getYesterdaymorning() {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(getTimesmorning().getTime()-3600*24*1000);
		return cal.getTime();
	}
	// 获得当天近7天时间
	public static Date getWeekFromNow() {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis( getTimesmorning().getTime()-3600*24*1000*7);
		return cal.getTime();
	}

	// 获得本周一0点时间
	public static Date getTimesWeekmorning() {
		Calendar cal = Calendar.getInstance();
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		return cal.getTime();
	}

	// 获得本周日24点时间
	public static Date getTimesWeeknight() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(getTimesWeekmorning());
		cal.add(Calendar.DAY_OF_WEEK, 7);
		return cal.getTime();
	}

	// 获得本月第一天0点时间
	public static Date getTimesMonthmorning() {
		Calendar cal = Calendar.getInstance();
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
		return cal.getTime();
	}

	// 获得本月最后一天24点时间
	public static Date getTimesMonthnight() {
		Calendar cal = Calendar.getInstance();
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		cal.set(Calendar.HOUR_OF_DAY, 24);
		return cal.getTime();
	}

	// 获得本年第一天0点时间 //wncheng
	public static Date getTimesYearmorning() {
		Calendar cal = Calendar.getInstance();
		cal.set(cal.get(Calendar.YEAR), 1, cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
		return cal.getTime();
	}

	// 获得本年最后一天24点时间 //wncheng
	public static Date getTimesYearnight() {
		Calendar cal = Calendar.getInstance();
		cal.set(cal.get(Calendar.YEAR), 12, cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		cal.set(Calendar.HOUR_OF_DAY, 24);
		return cal.getTime();
	}

	public static Date getLastMonthStartMorning() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(getTimesMonthmorning());
		cal.add(Calendar.MONTH, -1);
		return cal.getTime();
	}
	//季度
	public static Date getCurrentQuarterStartTime() {
		Calendar c = Calendar.getInstance();
		int currentMonth = c.get(Calendar.MONTH) + 1;
		SimpleDateFormat longSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat shortSdf = new SimpleDateFormat("yyyy-MM-dd");
		Date now = null;
		try {
			if (currentMonth >= 1 && currentMonth <= 3)
				c.set(Calendar.MONTH, 0);
			else if (currentMonth >= 4 && currentMonth <= 6)
				c.set(Calendar.MONTH, 3);
			else if (currentMonth >= 7 && currentMonth <= 9)
				c.set(Calendar.MONTH, 4);
			else if (currentMonth >= 10 && currentMonth <= 12)
				c.set(Calendar.MONTH, 9);
			c.set(Calendar.DATE, 1);
			now = longSdf.parse(shortSdf.format(c.getTime()) + " 00:00:00");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return now;
	}

	/**
	 * 当前季度的结束时间，即2012-03-31 23:59:59
	 *
	 * @return
	 */
	public static Date getCurrentQuarterEndTime() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(getCurrentQuarterStartTime());
		cal.add(Calendar.MONTH, 3);
		return cal.getTime();
	}


	public static Date getCurrentYearStartTime() {
		Calendar cal = Calendar.getInstance();
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.YEAR));
		return cal.getTime();
	}

	public static Date getCurrentYearEndTime() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(getCurrentYearStartTime());
		cal.add(Calendar.YEAR, 1);
		return cal.getTime();
	}

	public static Date getLastYearStartTime() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(getCurrentYearStartTime());
		cal.add(Calendar.YEAR, -1);
		return cal.getTime();
	}

	
	// 获得几天前的0点时间 //wncheng
	public static Date getTimesmorningBeforeDays(int days) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, days);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}
	// 获得几天前的23点时间 //wncheng
	public static Date getTimesnightedBeforeDays(int days) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, days);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.MILLISECOND, 999);
		return cal.getTime();
	}
	
	// 获得当天24点时间
	public static Date getEndTime4Date(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}
	
	// 获得几天前的0点时间 //wncheng
	public static Date getTimesSecondZero(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}
	// 获得几月后的最后一天
	public static Date getEndTime4AfterMonth(int month) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.MONTH, month);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.MILLISECOND, 0);
	       
		return cal.getTime();
		
	}

	
	/*
	 * 前N个月的时间
	 */
	public static Date MonthBeforeDate(Date dateNow, int months) {
		//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar cl = Calendar.getInstance();
		cl.setTime(dateNow);
		cl.add(Calendar.MONTH, months);
		return cl.getTime();
	}

	/*
	 * 前N个年的时间
	 */
	public static Date YearBeforeDate(Date dateNow, int years) {
		//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar cl = Calendar.getInstance();
		cl.setTime(dateNow);
		cl.add(Calendar.YEAR, years);
		return cl.getTime();
	}
	
	/**
	 * 计算N小时之前的日期
	 * 
	 * @param bgdate
	 * @param days
	 * @return
	 */
	public static Date HourBeforeDate(Date dateNow, int hour) {
		//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar cl = Calendar.getInstance();
		cl.setTime(dateNow);
		cl.add(Calendar.HOUR, hour);
		return cl.getTime();
	}
	
	/**
	 * 计算N分钟之前的日期
	 * 
	 * @param bgdate
	 * @param days
	 * @return
	 */
	public static Date MinuteBeforeDate(Date dateNow, int minute) {
		//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar cl = Calendar.getInstance();
		cl.setTime(dateNow);
		cl.add(Calendar.MINUTE, minute);
		return cl.getTime();
	}
	
	/**
	 * 计算N秒之前的日期
	 * 
	 * @param bgdate
	 * @param days
	 * @return
	 */
	public static Date SecondBeforeDate(Date dateNow, int second) {
		//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar cl = Calendar.getInstance();
		cl.setTime(dateNow);
		cl.add(Calendar.SECOND, second);
		return cl.getTime();
	}
	

	/**
	 * 计算N天之后的日期
	 * 
	 * @param bgdate
	 * @param days
	 * @return
	 */
	public static Date DateAfterDays(Date bgdate, int days) {
		long Time = (bgdate.getTime() / 1000) + 60 * 60 * 24 * days;
		Date ret = new Date();
		ret.setTime(Time * 1000);
		return ret;
	}

	/**
	 * 计算N天之前的日期
	 * 
	 * @param bgdate
	 * @param days
	 * @return
	 */
	public static Date DateBeforeDays(Date bgdate, int days) {
		long Time = (bgdate.getTime() / 1000) - 60 * 60 * 24 * days;
		Date ret = new Date();
		ret.setTime(Time * 1000);
		return ret;
	}

	/**
	 * 得到二个日期这之间的天数相隔
	 * 
	 * @param rq1
	 * @param rq2
	 * @return
	 */
	public static long DaysBetween(Date bgdate, Date enddate) {
		long beginTime = bgdate.getTime();
		long endTime = enddate.getTime();
		long days = (long) ((endTime - beginTime) / (1000 * 60 * 60 * 24) + 0.5);
		return days;
	}

}
