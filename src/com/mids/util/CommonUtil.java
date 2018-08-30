package com.mids.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;


public class CommonUtil {
	/**
	 * @return String
	 */
	public static String NewUUID() {
		//String id = String.valueOf(System.currentTimeMillis());
		String id = UUID.randomUUID().toString();
		return id;
	}
	
	public static long NewLongId() {
		long id=System.currentTimeMillis();
		return id;
	}

	/**
	 * 
	 * 
	 * @return String
	 
	public static String getRemoteIP(HttpServletRequest request) {
		if (request.getHeader("x-forwarded-for") == null) {
			return request.getRemoteAddr();
		}
		return request.getHeader("x-forwarded-for");
	}
	*/
	public static boolean checkEmail(String email){
    	Pattern pattern = Pattern.compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
    	Matcher matcher = pattern.matcher(email);
    	return matcher.matches();
    }
	/**
	 * 
	 * 
	 * @return String
	 */
	public static String getIPAddressByDomain(String domain) {
		String ipaddr = "";
		try
		{
			String ipdd = InetAddress.getByName(domain).toString();
	        System.out.println("demain["+domain+"]="+ipdd);
	        //获取www.baidu.com的真实IP地址 
	        ipaddr = InetAddress.getByName(domain).getHostAddress();
	        System.out.println("demain["+domain+"]="+ipaddr);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			ipaddr = "";
		}
        return ipaddr;
	}
	/**
	 * 
	 * 
	 * @return String
	 */
	public static boolean checkIpValid(String ipAddress)  
	{
		String  ip="(2[5][0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})";
        Pattern pattern = Pattern.compile(ip);
        Matcher matcher = pattern.matcher(ipAddress);
        return matcher.matches();
	}
	
	
	public static String filterOnlyDigit(String str)
    {
    	String regEx="[^0-9]";
    	Pattern p = Pattern.compile(regEx);
    	Matcher m = p.matcher(str);

    	String result = m.replaceAll("").trim();
    	
    	return result;
    }
    
    public static boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }
    
}
