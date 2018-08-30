package com.mids.interceptor;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;


public class LoginInterceptor implements HandlerInterceptor {
	//private final String ADMINSESSION = "adminsession";
	//拦截前处理
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object obj) throws Exception 
	{
		/*
		Object sesslang = request.getSession().getAttribute("language");
		if(sesslang == null)
		{
			Locale ll = request.getLocale();        
	        String language = "zh_CN";
	     	if(ll.getLanguage().equals("zh")){
	             Locale locale = new Locale("zh", "CN");
	             request.getSession().setAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME,locale);
	             language = "zh_CN";
	         }
	         else if(ll.getLanguage().equals("en")){
	             Locale locale = new Locale("en", "US");
	             request.getSession().setAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME,locale);
	             language = "en_US";
	         }
	         else 
	             request.getSession().setAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME,LocaleContextHolder.getLocale());
	     	
	     	request.getSession().setAttribute("language", language);
		}
		if(request.getSession(true).getAttribute("username")!=null && 
				request.getSession(true).getAttribute("mchtid")!=null && 
				request.getSession(true).getAttribute("mchtcode")!=null && 
				request.getSession(true).getAttribute("userid")!=null && 
				request.getSession(true).getAttribute("administrator")!=null)
		{
		  return true;
		}		
		response.sendRedirect(request.getContextPath()+"/sys/index.do");
		*/
		return true;
	}
	//拦截后处理
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object obj, ModelAndView mav) throws Exception { }
	//全部完成后处理
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object obj, Exception e) throws Exception { }
}