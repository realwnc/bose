package com.mids.util;


import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;


public class MapUrlUtil {
    /**
	 * 将url参数转换成map
	 * @param param aa=11&bb=22&cc=33
	 * @return
	 */
	public static Map<String, Object> getUrlParams(String param, boolean bEncoded) {
		Map<String, Object> map = new HashMap<String, Object>(0);
		if (StringUtils.isBlank(param)) {
			return map;
		}
		if(bEncoded == true)
		{
			try{
				//param = URLEncoder.encode(param, "utf-8");
				param = URLDecoder.decode(param, "utf-8");
			}catch(UnsupportedEncodingException e)
			{
				e.printStackTrace();
			}
		}
		String[] params = param.split("&");
		for (int i = 0; i < params.length; i++) {
			String[] p = params[i].split("=");
			if (p.length == 2) {
				map.put(p[0], p[1]);
			}
		}
		return map;
	}

	/**
	 * 将map转换成url
	 * @param map
	 * @return
	 */
	public static String getUrlParamsByMap(Map<String, String> map, boolean bEncoded) {
		if (map == null) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		for (Map.Entry<String, String> entry : map.entrySet()) {
			sb.append(entry.getKey() + "=" + entry.getValue());
			sb.append("&");
		}
		String strurl = sb.toString();
		if (strurl.endsWith("&")) {
			strurl = StringUtils.substringBeforeLast(strurl, "&");
		}
		if(bEncoded == true)
		{
			try{
				strurl = URLEncoder.encode(strurl, "utf-8");
			}catch(UnsupportedEncodingException e)
			{
				e.printStackTrace();
			}
		}
		
		return strurl;
	}
	
	/**
	 * 将url参数转换成map
	 * @param param aa=11&bb=22&cc=33
	 * @return
	 */
	public static Map<String, Object> parseUrlParams(String url, boolean bEncoded) {
		Map<String, Object> map = new HashMap<String, Object>(0);
		if (StringUtils.isBlank(url)) {
			return map;
		}
		if(bEncoded == true)
		{
			try{
				//param = URLEncoder.encode(param, "utf-8");
				url = URLDecoder.decode(url, "utf-8");
			}catch(UnsupportedEncodingException e)
			{
				e.printStackTrace();
			}
		}
		int pos = url.indexOf("?");
		if(pos>1)
		{
			url = url.substring(pos+1);
		}
		String[] params = url.split("&");
		for (int i = 0; i < params.length; i++) {
			String[] p = params[i].split("=");
			if (p.length == 2) {
				map.put(p[0], p[1]);
			}
			else if (p.length == 1) {
				map.put(p[0], "");
			}
		}
		return map;
	}

}