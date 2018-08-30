package com.mids;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class ConstantsCode 
{
	private static final long serialVersionUID = 1L;
	
	
	public static final int RETCODE_UNKNOWN=-1;
	public static final int RETCODE_SUCCEED=0;
	public static final int RETCODE_FAILURE=1;
	public static final int RETCODE_EXCEPTION=400;
	public static final int RETCODE_ERROR_DATAFORMAT=401;
	public static final int RETCODE_ERROR_PARAMETER=402;
	public static final int RETCODE_ERROR_SIGNATURE=403;
	public static final int RETCODE_ERROR_SERVERBUSY=405;
	
	public static final int RETCODE_ERROR_ACCESS_DENY=409;
	
	
	public static Map<Integer, String> globalMapErrorCode = new ConcurrentHashMap<Integer, String>() {
		{
	    	put(RETCODE_SUCCEED, "成功");
			put(RETCODE_FAILURE, "失败");
	        put(RETCODE_EXCEPTION, "系统忙");
	        put(RETCODE_ERROR_DATAFORMAT, "请求数据格式错误");
	        put(RETCODE_ERROR_PARAMETER, "请求参数错误");
	        put(RETCODE_ERROR_SIGNATURE, "数据签名错误");
	        put(RETCODE_ERROR_SERVERBUSY, "服务器忙，请稍后重试");
	        put(RETCODE_ERROR_ACCESS_DENY, "未经授权的访问");

	    }
	};
	
}
