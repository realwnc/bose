package com.mids.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;


public class LocalHostNameUtil {
	private static String hostname="";
	static
	{
		hostname = getLocalHostNameByInet();
	}
	public static String getLocalHostName() {
		return hostname;
	}
	private static String getLocalHostNameByInet() {
		String hostname = "";
		try{  
            InetAddress inetAddress = InetAddress.getLocalHost();
			if(null != inetAddress){
				hostname = inetAddress.getHostName(); //get the host address
			}
			System.out.println("-------->getLocalHostNmae hostname="+hostname);
        
        }catch(UnknownHostException e){
        	String host = e.getMessage(); // host = "hostname: hostname"  
            if (host != null) {
                int colon = host.indexOf(':');
                if (colon > 0) {
                    hostname = host.substring(0, colon);
                    colon = hostname.indexOf('.');
                    if (colon > 0) {
                        hostname = hostname.substring(0, colon);
                    }
                }
            }
            System.out.println("-------->UnknownHostException getLocalHostNmae hostname="+hostname);
        }
		
		return hostname;
	}
}
