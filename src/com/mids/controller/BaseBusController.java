package com.mids.controller;

import com.mids.Constants;
import com.mids.util.Base64Util;
import com.mids.util.StringEscapeEditor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.servlet.support.RequestContext;


public class BaseBusController {
	private static final Logger LOGGER = LoggerFactory.getLogger(BaseBusController.class);
	@Autowired
    private HttpServletRequest request;
	
	
	@InitBinder
    public void initBinder(ServletRequestDataBinder binder) {
        /**
         * 自动转换日期类型的字段格式
         */
        binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"), true));

        /**
         * 防止XSS攻击
         */
        binder.registerCustomEditor(String.class, new StringEscapeEditor(true, false));
    }

	protected String getBasePath(HttpServletRequest request) {
		return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
	}
	
	protected String getClientInternetIP(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		
		return ip;
	}
	
	protected String readRequest(HttpServletRequest request, boolean bBase64Flag)
	{
		BufferedReader bf = null;
		String retStr = "";
        try 
        {
        	StringBuffer sb = new StringBuffer();
        	String temp = null;
            bf = request.getReader();
            while ((temp = bf.readLine()) != null) {
                sb.append(temp);
            }
            temp = null;
            retStr = sb.toString();
            if(bBase64Flag==true)
            {
            	retStr = new String(Base64Util.decode(retStr), "utf-8");
            }
            
            //LOGGER.info("--------->read request data=\n" + retStr);          
        }
        catch (IOException e) {
        	if(bf!=null)
            LOGGER.error("--------->Exception on read request data=" + bf.toString() + ", exception:", e);
        }
        finally {
            if (bf != null) {
                try {
                    bf.close();
                }
                catch (IOException e) {
                	LOGGER.error("--------->error on close input stream, exception:", e);
                }
            }
        }
        return retStr;
	}
	protected int writeResponse(HttpServletResponse response, String data, boolean bBase64Flag) 
    {
    	int nRet = 0;
    	try  
    	{
    		String output = data;
            if(bBase64Flag==true)
            {
            	output = Base64Util.encode(output, "UTF-8");
            } 
            response.setCharacterEncoding("UTF-8"); //设置编码格式  
            response.setContentType("text/html");   //设置数据格式  
            PrintWriter out = response.getWriter(); //获取写入对象  
            out.print(output); 
            out.flush();
            LOGGER.info("--------->write response data=\n"+output);
        }
        catch(Exception e)
        {
        	LOGGER.error("--------->Exception on write response data=" + data + ", exception:", e);
            e.printStackTrace();
            nRet = -1;
        }
    	
    	return nRet;
    }
	
	protected int write2Response(HttpServletResponse response, String data) {
		int nRetCode=0;
		try {
			response.setCharacterEncoding(Constants.CHARSET);
			response.setContentType("text/html;charset=utf-8");//is or not
			PrintWriter printWriter = response.getWriter();
			printWriter.print(data);
			printWriter.flush();
			printWriter.close();
		} catch (Exception e) {
			nRetCode=-1;
			LOGGER.error("--------->write2Response exception=" + e);			
		}
		return nRetCode;
	}


	protected String getLocateMessage(Model model, HttpServletRequest request, String item)
	{
		String message = "";
		//if(!model.containsAttribute("contentModel"))
		{
			//从后台代码获取国际化信息
			RequestContext requestContext = new RequestContext(request);
			message = requestContext.getMessage(item);
		}
		
		return message;
	}
	
	//String projectName = super.getLocateMessage(request, "project.name");
	protected String getLocateMessage(HttpServletRequest request, String item)
	{
		String message = "";
		//if(!model.containsAttribute("contentModel"))
		{
			//从后台代码获取国际化信息
			RequestContext requestContext = new RequestContext(request);
			message = requestContext.getMessage(item);
		}
		
		return message;
	}
	
	protected String getLocateMessage(String item)
	{
		String message = "";
		//if(!model.containsAttribute("contentModel"))
		{
			//从后台代码获取国际化信息
			RequestContext requestContext = new RequestContext(request);
			message = requestContext.getMessage(item);
		}
		
		return message;
	}
	
	protected HttpSession getSession(HttpServletRequest request)
	{
		return request.getSession();
	}
}
