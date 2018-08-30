package com.mids;


public class Constants
{
	public static String CHARSET = "UTF-8";
	public static String COUNTRY_CODE = "86";
	
	public static String PREFIX_ECOP_ORDERID = "ECOPER_";

	public static int APP_TOKEN_EXPIRE_TIME = 7200;//second
	public static int MAX_REDIS_TIMEOUT = 25*3600;//second =8 hour

	public static final int APP_TYPE_ANDROID=0;
	public static final int APP_TYPE_IOS=1;

	public static int DEFAULT_PAGE_SIZE = 6;
	
	public static String[] IMAGE_FILE_SUFFIX = { ".jpg", ".bmp", ".jpeg", ".png", ".ico", ".gif" };

	public static String CRMRES_PATH_ROOT ="/crmres";
	
}
