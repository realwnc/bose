package com.mids.common.utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mids.common.properties.SystemPropertiesUtil;
import com.mids.util.MapKeyComparator;
import com.mids.util.MapUrlUtil;


public class SystemUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(SystemUtil.class);
	
    public static int checkMobilePrefix(String mobile)
	{
    	int ret = 1;
        // 判断手机是否符合基本的11位数字
        if (mobile.matches("^\\d{11}$")) {
            // 判断手机号是否属于中国移动的号段
            String mobilePrefix = SystemPropertiesUtil.getInstance().getValue("system.mobile_prefix");
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
    
	/**
	 * 删除文件
	 * @param folderPath
	 * @param targetPath
	 * @sine 
	 */
	public static void removeFiles2Path(String folderPath, String targetPath) {
		File files = new File(folderPath);
		File[] filelist = files.listFiles();
		if(filelist == null) 
		{
			return;
		}
		System.out.println("该目录下文件个数：" + filelist.length);
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(new Date().getTime());
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String currentTime = dateFormat.format(c.getTime());
		String newFolderPath = targetPath + "/" + currentTime;
		// String newFolderPath = targetPath + currentTime;
		if (filelist.length > 0) {
			File file = new File(newFolderPath);
			if (!file.exists() && !file.isDirectory()) {
				System.out.println("//不存在");
				file.mkdir();
			} else {
				System.out.println("//目录存在");
			}
		}
		System.out.println("newFolderPath:" + newFolderPath);
		for (int i = 0; i < filelist.length; i++) {
			if (filelist[i].isFile()) {
				File moveFile = new File(newFolderPath + "/" + filelist[i].getName());
				// File moveFile = new File(newFolderPath + "\\" +
				// fileAry[i].getName());
				filelist[i].renameTo(moveFile);
			}
		}
	}
	
    public static String generateMD5Sign(Map<String, String> map)
    {
    	Map<String, String> sortMap = new TreeMap<String, String>(new MapKeyComparator());
        sortMap.putAll(map);
        
        String str = MapUrlUtil.getUrlParamsByMap(map, false);        
        String md5 = DigestUtils.md5Hex(str);
        
        return md5;
    }
}