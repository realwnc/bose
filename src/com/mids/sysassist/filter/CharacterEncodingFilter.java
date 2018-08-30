package com.mids.sysassist.filter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

public class CharacterEncodingFilter implements Filter{
	private String encoding = "UTF-8";// 默认UTF-8编码
	
	public void destroy() {}

	public void doFilter(ServletRequest req, ServletResponse resp,  
            FilterChain chain) throws IOException, ServletException {  
        HttpServletRequest request = (HttpServletRequest) req;  
        HttpServletResponse response = (HttpServletResponse) resp;  
        // 设置request编码  
        request.setCharacterEncoding(encoding);  
        // 设置相应信息  
  //      response.setContentType("text/html;charset=" + encoding);  
   //     response.setCharacterEncoding(encoding);  
        chain.doFilter(request, response);  
    }  

	public void init(FilterConfig filterConfig) throws ServletException {
		  String encoding = filterConfig.getInitParameter("encoding");  
	        if (encoding != null) {  
	            this.encoding = encoding;  
	        }  
		
	}

}
	/** 
	 * 对Get方式传递的请求参数进行编码 
	 */  
	class CharacterEncodingRequest extends HttpServletRequestWrapper {  
	    private HttpServletRequest request = null;  
	  
	    public CharacterEncodingRequest(HttpServletRequest request) {  
	        super(request);  
	        this.request = request;  
	    }  
	  
	    /** 
	     * 对参数重新编码 
	     */  
	    @Override  
	    public String getParameter(String name) {  
	        String value = super.getParameter(name);  
	         if(value == null)  
	               return null;  
	         String method = request.getMethod();  
	        if ("get".equalsIgnoreCase(method)) {  
	            try {  
	                value = new String(value.getBytes("ISO-8859-1"),  
	                        request.getCharacterEncoding());  
	            } catch (UnsupportedEncodingException e) {  
	                e.printStackTrace();  
	            }  
	        }  
	        return value;  
	    }  
	  
	}
