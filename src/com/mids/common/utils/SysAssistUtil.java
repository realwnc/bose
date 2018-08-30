package com.mids.common.utils;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import com.mids.common.properties.SystemPropertiesUtil;
import com.mids.util.MapUrlUtil;


public class SysAssistUtil {
	//private static final Logger LOGGER = LoggerFactory.getLogger(SysAssistUtil.class);
	private static String MOBILE_PREFIX = SystemPropertiesUtil.getInstance().getValue("system.mobile_prefix");
	private static String IMPORT_SUPPORT_FILES = SystemPropertiesUtil.getInstance().getValue("system.importfile.support");

	//比较器类  
	public static class MapKeyComparator implements Comparator<String>{
		public int compare(String str1, String str2) {
			return str1.compareTo(str2);
		}
	}
	
    public static int checkMobilePrefix(String mobile)
	{
    	int ret = 1;
        // 判断手机是否符合基本的11位数字
        if (mobile.matches("^\\d{11}$")) {
            // 判断手机号是否属于中国移动的号段
            String mobilePrefix = MOBILE_PREFIX;
            if (mobilePrefix != null && !"".equals(mobilePrefix)) 
            {
                String amobilePrefix[] = mobilePrefix.split(",");
                if (mobile != null && !"".equals(mobile)) 
                {
                    for (int i = 0; i < amobilePrefix.length; i++) 
                    {
                        String mp = amobilePrefix[i];
                        String prefix = mobile.substring(0, mp.length());
                        if (prefix.equals(mp)) {
                        	ret = 0;
                            break;
                        }
                    }
                }

            }
        }
        return ret;
	}
    
    public static int checkMobilePrefix(String mobile, String mobile_prefix)
   	{
       	int ret = 1;
           // 判断手机是否符合基本的11位数字
           if (mobile.matches("^\\d{11}$")) {
               // 判断手机号是否属于中国移动的号段              
        	   String amobilePrefix[] = mobile_prefix.split(",");
               if (mobile != null && !"".equals(mobile)) 
               {
                   for (int i = 0; i < amobilePrefix.length; i++) 
                   {
                       String mp = amobilePrefix[i];
                       String prefix = mobile.substring(0, mp.length());
                       if (prefix.equals(mp)) {
                       	ret = 0;
                           break;
                       }
                   }
               }
           }
           return ret;
   	}
    
    public static String generateMD5Sign(Map<String, String> map)
    {
    	Map<String, String> sortMap = new TreeMap<String, String>(new MapKeyComparator());
        sortMap.putAll(map);
        
        String str = MapUrlUtil.getUrlParamsByMap(map, false);        
        String md5 = DigestUtils.md5Hex(str);
        
        return md5;
    }
    
    public static boolean checkImportFileSupport(String filename)
    {
    	if(StringUtils.isBlank(IMPORT_SUPPORT_FILES) == true)
    	{
    		return false;
    	}
    	int pos = filename.lastIndexOf(".");
    	if(pos < 1)
    	{
    		return false;
    	}
    	String extname=filename.substring(pos+1).toLowerCase();
		if(extname.toLowerCase().startsWith("jsp"))
		{
			return false;
		}
		
    	String[] sufiles = IMPORT_SUPPORT_FILES.split("\\|");
    	for(String str: sufiles)
    	{
    		if(extname.equalsIgnoreCase(str) == true)
    		{
    			return true;
    		}
    	}
    	return false;
    }
    
    public static boolean findMatchUnity(String unit,String unity)
	{
		boolean re=false;
		String[] arr=unity.split("\\|",-1);
		for(int i=0;i<arr.length;i++)
		{
			if(unit.equalsIgnoreCase(arr[i])) 
			{
				return true;
			}
		}
		return re;
	}
	
	public static boolean findMatchUnityLike(String unit,String unity)
	{
		boolean re=false;
		String[] arr=unity.split("\\|",-1);
		for(int i=0;i<arr.length;i++)
		{
			//if(unit.equalsIgnoreCase(arr[i]))
			int nMatchPos = arr[i].indexOf("%");
			if(nMatchPos>0)
			{
				String temp = arr[i].substring(0, nMatchPos);
				if(unit.toLowerCase().indexOf(temp) == 0)
				{
					return true;
				}
			}
			else if(unit.equalsIgnoreCase(arr[i]) == true)
			{
				return true;
			}
		}
		return re;
	}
}