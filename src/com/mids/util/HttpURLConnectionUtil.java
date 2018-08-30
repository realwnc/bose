package com.mids.util;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpURLConnectionUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(HttpURLConnectionUtil.class);
	
	public static String downloadFile(String surl, String destPath)
	{
		try
		{
			URL url = new URL(surl);
	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	        conn.setConnectTimeout(60*1000);
	        conn.setReadTimeout(60*60*1000);
	        
	        conn.connect();
	        
	        // 打印HTTP header
	        Map headers = conn.getHeaderFields();
	        Set<String> keys = headers.keySet();
	        for(String key : keys) {
	            System.out.println(key + " ----------------------------- " + conn.getHeaderField(key));
	        }
	        String filename = surl;
	        int pos = filename.lastIndexOf("/");
	        if(pos > 0)
	        {
	        	filename = filename.substring(pos+1);
	        }
	        // 转换编码
	        String raw = conn.getHeaderField("Content-Disposition");
	        if (raw != null && raw.indexOf("=") != -1)
	        {
	        	/*
	        	 String contentDisposition = URLDecoder.decode(raw, "UTF-8");
	             System.out.println(contentDisposition);
	             // 匹配文件名
	             Pattern pattern = Pattern.compile(".*fileName=(.*)");
	             Matcher matcher = pattern.matcher(contentDisposition);
	             System.out.println(matcher.groupCount());
	             System.out.println(matcher.matches());
	             System.out.println(matcher.group(1));
	             filename = matcher.group(1);
	             */
	        	String[] args = raw.split(";");
	        	for (String arg:args) 
	        	{
		        	if (arg.trim().startsWith("filename=")) 
		        	{
		        		filename = arg.split("=")[1];
		        		filename = filename.replace("\"","");
		        		break;
		        	}
	        	}
	        }
	       
	        String extname = filename;
	        pos = extname.lastIndexOf(".");
	        extname = extname.substring(pos+1);
	        File dir = new File(destPath);
	        if(dir.exists() == false)
	        {
	        	dir.mkdirs();
	        }
	        // 写盘
	        //String newFilename = RandomUtils.generateUUID().toLowerCase()+"."+extname;
	        String dest = destPath + filename;
	        RandomAccessFile file = new RandomAccessFile(dest, "rw");
	        InputStream stream = conn.getInputStream();
	        byte buffer[] = new byte[1024];
	        while (true) {
	            int len = stream.read(buffer);
	            if (len == -1) {
	                break;
	            }
	            file.write(buffer, 0, len);
	        }
	        if (file != null) {
	            file.close();
	        }
	        if (stream != null) {
	            stream.close();
	        }
	        return filename;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
        return null;
    }

    public static void main(String[] args) throws Exception{
        HttpURLConnectionUtil.downloadFile("http://182.168.0.39:9099/fileupload/OA_FlowFJ_Download.do?tid=947810&tmp=1494918762208", "d://");
    }
}
