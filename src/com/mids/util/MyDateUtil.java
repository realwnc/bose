package com.mids.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * 日期处理工具类
 * 
 * @author wncheng
 * @date Mar 11, 2012
 * @modified by
 * @modified date
 * @since JDK1.6
 * @see com.util.DateUtil
 */

public class MyDateUtil {
	// ~ Static fields/initializers
	// =============================================

	/*
	 * private static String defaultDatePattern = null; private static String
	 * timePattern = "HH:mm"; private static Calendar cale =
	 * Calendar.getInstance(); public static final String TS_FORMAT =
	 * DateUtil.getDatePattern() + " HH:mm:ss.S";
	 * 
	 * private static final String MONTH_FORMAT = "yyyy-MM";
	 * 
	 * private static final String DATE_FORMAT = "yyyy-MM-dd";
	 * 
	 * private static final String HOUR_FORMAT = "HH:mm:ss";
	 * 
	 * private static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	 * 
	 * private static final String DAY_BEGIN_STRING_HHMMSS = " 00:00:00";
	 * 
	 * public static final String DAY_END_STRING_HHMMSS = " 23:59:59"; private
	 * static SimpleDateFormat sdf_date_format = new
	 * SimpleDateFormat(DATE_FORMAT); private static SimpleDateFormat
	 * sdf_hour_format = new SimpleDateFormat(HOUR_FORMAT); private static
	 * SimpleDateFormat sdf_datetime_format = new
	 * SimpleDateFormat(DATETIME_FORMAT);
	 */

	// ~ Methods
	// ================================================================

	public MyDateUtil() {
	}

	/**
	 * 获得当前日期及时间，以格式为：yyyy-MM-dd HH:mm:ss的日期字符串形式返回
	 * 
	 * @author wncheng
	 * @date Mar 11, 2012
	 * @return
	 */
	public static String getFormatCurrentDateTime() {
		try {
			Calendar cale = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return sdf.format(cale.getTime());
		} catch (Exception e) {			
			return "";
		}
	}

	/**
	 * 获得当前日期，以格式为：yyyy-MM-dd的日期字符串形式返回
	 * 
	 * @author wncheng
	 * @date Mar 11, 2012
	 * @return
	 */
	public static String getFormatCurrentDate() {
		try {
			Calendar cale = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			return sdf.format(cale.getTime());
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 获得当前时间，以格式为：HH:mm:ss的日期字符串形式返回
	 * 
	 * @author wncheng
	 * @date Mar 11, 2012
	 * @return
	 */
	public static String getFormatCurrentTime() {
		try {
			Calendar cale = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
			return sdf.format(cale.getTime());
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 返回指定年份中指定月份的天数
	 * 
	 * @param 年份year
	 * @param 月份month
	 * @return 指定月的总天数
	 */
	public static String getMonthLastDay(int year, int month) {
		int[][] day = { { 0, 30, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 },
				{ 0, 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 } };
		if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {
			return day[1][month] + "";
		} else {
			return day[0][month] + "";
		}
	}

	/**
	 * 判断是平年还是闰年
	 * 
	 * @author wncheng
	 * @date Mar 11, 2012
	 * @param year
	 * @return
	 */
	public static boolean isLeapyear(int year) {
		if ((year % 4 == 0 && year % 100 != 0) || (year % 400) == 0) {
			return true;
		} else {
			return false;
		}
	}

	// /////////////////////////by wncheng
	// /////////////////////////////////////////////////
	/**
	 * 将日期（java.util.Date, java.sql.Date, java.sql.Time,
	 * java.sql.TimeStamp）转换为字符串 格式为： yyyy-mm-dd hh:mm:ss
	 */
	/*
	public static String convertDate2String(Object obj) {
		try {
			if (obj != null) {
				Calendar c = Calendar.getInstance();
				if (obj instanceof java.util.Date) {
					java.util.Date dateTemp = (java.util.Date) obj;
					c.setTime(dateTemp);
				} else if (obj instanceof java.sql.Date) {
					java.sql.Date dateTemp = (java.sql.Date) obj;
					c.setTime(dateTemp);
				} else if (obj instanceof java.sql.Time) {
					java.sql.Date dateTemp = (java.sql.Date) obj;
					c.setTime(dateTemp);
				} else if (obj instanceof java.sql.Timestamp) {
					java.sql.Timestamp dateTemp = (java.sql.Timestamp) obj;
					c.setTime(dateTemp);
				} else {
					return "Bad date object";
				}

				return c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1)
						+ "-" + c.get(Calendar.DAY_OF_MONTH) + " "
						+ c.get(Calendar.HOUR_OF_DAY) + ":"
						+ c.get(Calendar.MINUTE) + ":" + c.get(Calendar.SECOND);
			} else {
				return "Null date object";
			}
		} catch (Exception e) {
			return e.toString();
		}
	}
	*/
	// /////////////////////////by wncheng
	// /////////////////////////////////////////////////
	/**
	 * 将日期（java.util.Date, java.sql.Date, java.sql.Time,
	 * java.sql.TimeStamp）转换为字符串 格式为： yyyy-mm-ddThh:mm:ss
	 */
	/*
	public static String convertDate2String_T(Object obj) {
		try {
			if (obj != null) {
				Calendar c = Calendar.getInstance();
				if (obj instanceof java.util.Date) {
					java.util.Date dateTemp = (java.util.Date) obj;
					c.setTime(dateTemp);
				} else if (obj instanceof java.sql.Date) {
					java.sql.Date dateTemp = (java.sql.Date) obj;
					c.setTime(dateTemp);
				} else if (obj instanceof java.sql.Time) {
					java.sql.Date dateTemp = (java.sql.Date) obj;
					c.setTime(dateTemp);
				} else if (obj instanceof java.sql.Timestamp) {
					java.sql.Timestamp dateTemp = (java.sql.Timestamp) obj;
					c.setTime(dateTemp);
				} else {
					return "Bad date object";
				}

				return c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1)
						+ "-" + c.get(Calendar.DAY_OF_MONTH) + "T"
						+ c.get(Calendar.HOUR_OF_DAY) + ":"
						+ c.get(Calendar.MINUTE) + ":" + c.get(Calendar.SECOND);
			} else {
				return "Null date object";
			}
		} catch (Exception e) {
			return e.toString();
		}
	}
	*/
	/**
	 * 将日期（java.util.Date, java.sql.Date, java.sql.Time,
	 * java.sql.TimeStamp）转换为字符串 格式为： yyyy-mm-dd hh:mm:ss
	 * （比convertDate2String方法多一个不足位补零功能，如2016-11-21 14:01:01）
	 * @param obj
	 * @return
	 */
	public static String convertDate2FormatString(Object obj) {
		try {
			if (obj != null) {
				Calendar c = Calendar.getInstance();
				if (obj instanceof java.util.Date) {
					java.util.Date dateTemp = (java.util.Date) obj;
					c.setTime(dateTemp);
				} else if (obj instanceof java.sql.Date) {
					java.sql.Date dateTemp = (java.sql.Date) obj;
					c.setTime(dateTemp);
				} else if (obj instanceof java.sql.Time) {
					java.sql.Date dateTemp = (java.sql.Date) obj;
					c.setTime(dateTemp);
				} else if (obj instanceof java.sql.Timestamp) {
					java.sql.Timestamp dateTemp = (java.sql.Timestamp) obj;
					c.setTime(dateTemp);
				} else {
					return "Bad date object";
				}

				String dateStr= c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1)
						+ "-" + c.get(Calendar.DAY_OF_MONTH) + " "
						+ c.get(Calendar.HOUR_OF_DAY) + ":"
						+ c.get(Calendar.MINUTE) + ":" + c.get(Calendar.SECOND);
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				return sdf.format(sdf.parse(dateStr)); 
			} else {
				return "Null date object";
			}
		} catch (Exception e) {
			return e.toString();
		}
	}

	
	/**
	 * 将字符串转化为日期（java.util.Date） 字符串格式要求为 yyyy-mm-ddThh:mm:ss
	 * **/
	public static Date convertFormatString2Date_T(String str) throws Exception {
		str = str.replaceAll("T", " ");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// TimeZone timezone = TimeZone.getTimeZone("GMT-0");
		// sdf.setTimeZone(timezone);
		java.util.Date date = sdf.parse(str);

		return date;
	}
	/**
	 * 将字符串转化为日期（java.util.Date） 字符串格式要求为 yyyy-mm-dd hh:mm:ss
	 * **/
	public static Date convertFormatString2Date(String str) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// TimeZone timezone = TimeZone.getTimeZone("GMT-0");
		// sdf.setTimeZone(timezone);
		java.util.Date date = sdf.parse(str);

		return date;
	}

	/**
	 * 将字符串转化为日期（java.util.Date） 字符串格式要求为 yyyyMMddHHmmss
	 * **/
	public static Date convertNoSpitString2Date(String str) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		java.util.Date date = sdf.parse(str);

		return date;
	}

	/**
	 * 
	 * 字符串格式要求为 yyyyMMddHHmmss
	 * **/
	public static String convertDate2FormatStringNoSplit(Date time) {
		SimpleDateFormat formatter;
		formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		String ctime = formatter.format(time);

		return ctime;
	}

	/**
	 * 
	 * 字符串格式要求为 yyyyMMddHHmmssSSSS
	 * **/
	public static String convertDate2FormatStringNoSplitMille(Date time) {
		SimpleDateFormat formatter;
		formatter = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String ctime = formatter.format(time);

		return ctime;
	}

	/**
	 * 
	 * 字符串格式要求为yyyy-MM-dd
	 * **/
	public static String convertDate2FormateStringNoTime(Date time) {
		SimpleDateFormat formatter;
		formatter = new SimpleDateFormat("yyyy-MM-dd");
		String ctime = formatter.format(time);

		return ctime;
	}

	/**
	 * 
	 * 字符串格式要求为 yyyyMMdd
	 * **/
	public static String convertDate2FormatStringNoSplitNoTime(Date time) {
		SimpleDateFormat formatter;
		formatter = new SimpleDateFormat("yyyyMMdd");
		String ctime = formatter.format(time);

		return ctime;
	}
	/**
	 * 将java.util.Date 格式转换为字符串格式'yyyy-MM-dd HH:mm:ss a'(12小时制)<br>
	 * 如Sat May 11 17:23:22 CST 2002 to '2002-05-11 05:23:22 下午'<br>
	 * 
	 * @param time
	 *            Date 日期<br>
	 * @param x
	 *            int 任意整数如：1<br>
	 * @return String 字符串<br>
	 */
	public static String convertDate2String_x(Date time, int x) {
		SimpleDateFormat formatter;
		formatter = new SimpleDateFormat("yyyy-MM-dd KK:mm:ss a");
		String ctime = formatter.format(time);

		return ctime;
	}
	/**
	 * 
	 * 字符串格式要求为 yyyyMMddHHmmss
	 * **/
	public static String convertDateMinute2StringNoSplit(Date time) {
		SimpleDateFormat formatter;
		formatter = new SimpleDateFormat("yyyyMMddHHmm");
		String ctime = formatter.format(time);

		return ctime;
	}
	
	/**
	 * 取系统当前时间:返回只值为如下形式 2002-10-30 08:28:56 下午
	 * 
	 * @param hour
	 *            为任意整数
	 * @return String
	 */

	public static String getCurrentHour() {
		SimpleDateFormat formatter;
		formatter = new SimpleDateFormat("H");
		String ctime = formatter.format(new Date());

		return ctime;
	}

	public static String getCurrentDay() {
		SimpleDateFormat formatter;
		formatter = new SimpleDateFormat("d");
		String ctime = formatter.format(new Date());
		return ctime;
	}

	public static String getCurrentMonth() {
		SimpleDateFormat formatter;
		formatter = new SimpleDateFormat("M");
		String ctime = formatter.format(new Date());
		return ctime;
	}

	public static String getCurrentYear() {
		SimpleDateFormat formatter;
		formatter = new SimpleDateFormat("yyyy");
		String ctime = formatter.format(new Date());
		return ctime;
	}

	public static String getCurrentWeek() {
		SimpleDateFormat formatter;
		formatter = new SimpleDateFormat("E");
		String ctime = formatter.format(new Date());
		return ctime;
	}
	
	public static String getChinaYear(int year)
	{
		final String[] Animals = new String[]{"鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊", "猴", "鸡", "狗", "猪"};  
        return Animals[(year - 4) % 12];
	}
	
	public static String cyclicalm(int year) {
		year = year - 1900 + 36;
        final String[] Gan = new String[]{"甲", "乙", "丙", "丁", "戊", "己", "庚", "辛", "壬", "癸"};  
        final String[] Zhi = new String[]{"子", "丑", "寅", "卯", "辰", "巳", "午", "未", "申", "酉", "戌", "亥"};  
        return (Gan[year % 10] + Zhi[year % 12]);  
    }
	
	public static void main(String[] args) {
		String cc = MyDateUtil.cyclicalm(2017);
		System.out.println("china year="+cc);
	}
}