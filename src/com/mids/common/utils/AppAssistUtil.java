package com.mids.common.utils;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mids.ConstantsCode;

public class AppAssistUtil
{
	private static final Logger LOGGER = LoggerFactory.getLogger(AppAssistUtil.class);

	public static boolean checkJsonParamExist(Map<String, Object> map, String... strArray)
    {
		if(map == null)
		{
			return false;
		}
    	for (String obj : strArray)
    	{
    		String str = obj;
			if(map.containsKey(str) == false)
    		{
    			return false;
    		}
    	}
    	return true;
    }
	
	public static int checkRequestParamValid(Map<String, Object> map, boolean bTokenFlag, String... strArray)
	{
		try
		{
			if(map == null)
			{
				return ConstantsCode.RETCODE_ERROR_PARAMETER;
			}
			if(bTokenFlag == true)
			{
				String token = map.get("token")==null?"noset":map.get("token").toString();
				//if(AppAuthInfoCacheContainer.get(token) == null)
				{
					return ConstantsCode.RETCODE_ERROR_ACCESS_DENY;
				}
			}
	    	for (String obj : strArray)
	    	{
	    		String str = obj;
				if(map.containsKey(str) == false || StringUtils.isBlank(str) == true)
	    		{
	    			return ConstantsCode.RETCODE_ERROR_PARAMETER;
	    		}
	    	}
		}
		catch(Exception e)
		{
			LOGGER.error("--------->reqeuest parameter exception!!!");
			e.printStackTrace();
			
			return ConstantsCode.RETCODE_ERROR_PARAMETER; 
		}
    	return ConstantsCode.RETCODE_SUCCEED;
	}

}