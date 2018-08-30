package com.mids.util;


import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import javafx.animation.KeyValue;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.JSONLibDataFormatSerializer;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;

public class MyFastjsonUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(MyFastjsonUtil.class);
	private static boolean bBase64Flag = false;
	
	private static final SerializeConfig config;
	private static String dateFormat = "yyyy-MM-dd HH:mm:ss";  
	static{
		config = new SerializeConfig();
        //config.put(java.util.Date.class, new JSONLibDataFormatSerializer()); // 使用和json-lib兼容的日期输出格式  
        //config.put(java.sql.Date.class, new JSONLibDataFormatSerializer()); // 使用和json-lib兼容的日期输出格式
        config.put(java.util.Date.class, new SimpleDateFormatSerializer(dateFormat)); 
        config.put(java.sql.Date.class, new SimpleDateFormatSerializer(dateFormat));
	}
	
	private static final SerializerFeature[] features = {SerializerFeature.WriteMapNullValue, // 输出空置字段  
        SerializerFeature.WriteNullListAsEmpty, // list字段如果为null，输出为[]，而不是null
        SerializerFeature.WriteNullNumberAsZero, // 数值字段如果为null，输出为0，而不是null
        SerializerFeature.WriteNullBooleanAsFalse, // Boolean字段如果为null，输出为false，而不是null
        SerializerFeature.WriteNullStringAsEmpty // 字符类型字段如果为null，输出为""，而不是null
	};
	
	@SuppressWarnings("unchecked")
    public static Map<String, Object> json2Map(String json) {
    	return JSON.parseObject(json, Map.class);
    }
	
	public static String map2Json(Map<String, Object> map) {
		return JSONObject.toJSONString(map);
	}
	public static String obj2JsonString(Object obj)
	{
		return JSON.toJSONString(obj, config, features);
	}

	public static String obj2JsonStringNoFeatures(Object obj)
	{
		return JSON.toJSONString(obj, config);
	}
	
	/**  
     * 将string转化为序列化的json字符串  
     * @param keyvalue  
     * @return  
     */  
    public static Object json2Object(String json) {
    	return JSON.parse(json);
    }
	
	public static Object json2Bean(String json) {
        return JSON.parse(json);
    }
  
    public static <T> T json2Bean(String json, Class<T> clazz) {
        return JSON.parseObject(json, clazz);
    }

    // 转换为数组  
    public static <T> Object[] json2Array(String json) {
        return json2Array(json, null);
    }

    // 转换为数组  
    public static <T> Object[] json2Array(String json, Class<T> clazz) {
        return JSON.parseArray(json, clazz).toArray();
    }
  
    // 转换为List  
    public static <T> List<T> json2List(String json, Class<T> clazz) {
        return JSON.parseArray(json, clazz);
    }
  
    /**  
     * 将javabean转化为序列化的json字符串  
     * @param keyvalue  
     * @return  
     */  
    public static Object bean2Json(KeyValue keyvalue) {
        String textJson = JSON.toJSONString(keyvalue);
        Object objectJson  = JSON.parse(textJson);
        return objectJson;
    }

    //------------------------------------------------------------------------------------
    public static int sendJsonToResponse(Map<String, Object> map, HttpServletResponse response) 
    {
    	int nRet = 0;
    	String json = "{'retcode':'0'}";
    	try  
        {  
            json = map2Json(map);       
            
            String output = json;
            if(bBase64Flag==true)
            {
            	output = Base64Util.encode(json, "UTF-8");
            }
            //方案二  
            //ServletOutputStream os = response.getOutputStream(); //获取输出流  
            //os.write(json.getBytes(Charset.forName("GBK"))); //将json数据写入流中  
            //os.flush();  
  
            //方案一  
            response.setCharacterEncoding("UTF-8"); //设置编码格式  
            response.setContentType("text/html");   //设置数据格式  
            PrintWriter out = response.getWriter(); //获取写入对象  
            out.print(output); //将json数据写入流中  
            out.flush();
            LOGGER.info("--------->Platform send response data="+output);
        }
    	catch (IOException e) {
        	LOGGER.error("--------->error on transform map to json, exception:", e);
        	e.printStackTrace();
        	nRet = -1;
        }
        catch(Exception e)
        {
        	LOGGER.error("--------->error on output response, exception:", e);
            e.printStackTrace();
            nRet = -2;
        }
    	
    	return nRet;
    }
}