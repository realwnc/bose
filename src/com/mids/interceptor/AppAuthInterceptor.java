package com.mids.interceptor;

import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.mids.util.MyDateUtil;
import com.mids.util.crypt.MyAESUtils;


public class AppAuthInterceptor implements HandlerInterceptor {
	//private final String ADMINSESSION = "adminsession";
	private static final Logger LOGGER = LoggerFactory.getLogger(AppAuthInterceptor.class);
	private static final long MAX_TIMEOUT = 10*60*1000 *6*24;
	//拦截前处理
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object obj) throws Exception {
		String url = request.getRequestURL().toString();
		String param = request.getQueryString();
		LOGGER.debug("---------->AppAuthInterceptor:request url={}", url+"?"+param);
		String signstr = request.getHeader("sign");
		if(StringUtils.isBlank(signstr) == true)
		{
			LOGGER.error("-------->AppAuthInterceptor:http header sign is invalid, sign={}", signstr);
			writeResponse(response, "invalid sign", null);
			return false;
		}

		return true;
	}
	//拦截后处理
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object obj, ModelAndView mav) throws Exception { }
	//全部完成后处理
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object obj, Exception e) throws Exception { }
	
	private void writeResponse(HttpServletResponse response, String retmsg, Map<String, Object> map) 
    {
    	try  
    	{
    		Document doc = DocumentHelper.createDocument();
        	Element rootElement = doc.addElement("info");
        	rootElement.addElement("retcode").addText(9978+"");
			rootElement.addElement("retmsg").addText(retmsg);
			Element bodyElement = rootElement.addElement("body");
			if(map!=null)
			{
				for (Map.Entry<String, Object> entry : map.entrySet())
				{
					bodyElement.addElement(entry.getKey()).addText(entry.getValue().toString());
				}
			}
			
			String respxml = doc.asXML();;
            response.setCharacterEncoding("UTF-8"); //设置编码格式  
            response.setContentType("text/html");   //设置数据格式  
            PrintWriter out = response.getWriter(); //获取写入对象  
            out.print(respxml); 
            out.flush();
            LOGGER.info("--------->AppAuthInterceptor:write response data=\n"+respxml);
        }
        catch(Exception e)
        {
        	LOGGER.error("--------->AppAuthInterceptor:Exception on write response data="+retmsg+", exception:", e);
            e.printStackTrace();
        }    	
    }
	
}