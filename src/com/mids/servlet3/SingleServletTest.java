package com.mids.servlet3;

import javax.servlet.http.HttpServlet;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
//import com.mids.db.*;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.support.RequestContext;

import com.mids.mybatis.model.User;
import com.mids.service.UserService;

@WebServlet(name="SingleServletTest",urlPatterns="/SingleServletTest")
public class SingleServletTest extends HttpServlet 
{
	@Autowired(required = true)
	private UserService userService;
	
	
	public UserService getUserService() {
		return userService;
	}


	public void setUserService(UserService userService) {
		this.userService = userService;
	}


	public void init(ServletConfig config) throws ServletException {  
	    super.init(config);  
	    SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, config.getServletContext());
	}  
	
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		//DBDataCtrl.executeUpdate("update tb_ids_terminal set dt_registertime=now() where vc_terminalid='test01@gzecsr'");
		
        response.getWriter().write("Hello Servlet3.0");
        User user=new User();
        //user.setId(101);
        //user.setUsername("wncheng");
        user.setPassword("123");
        try
        {
        	//userService.addUser(user);
        }
        catch (Exception e)
        {
        	e.printStackTrace();
        }
    }
	
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
    {
        this.doGet(request, response);
    }
    /*
     * 完成了一个使用注解描述的Servlet程序开发。
    　　使用@WebServlet将一个继承于javax.servlet.http.HttpServlet的类定义为Servlet组件。
    　　@WebServlet有很多的属性：
        　　1、asyncSupported：    声明Servlet是否支持异步操作模式。
        　　2、description：　　    Servlet的描述。
        　　3、displayName：       Servlet的显示名称。
        　　4、initParams：        Servlet的init参数。
        　　5、name：　　　　       Servlet的名称。
        　　6、urlPatterns：　　   Servlet的访问URL。
        　　7、value：　　　        Servlet的访问URL。
    　　Servlet的访问URL是Servlet的必选属性，可以选择使用urlPatterns或者value定义。
    　　像上面的Servlet3Demo可以描述成@WebServlet(name="Servlet3Demo",value="/Servlet3Demo")。
    　　也定义多个URL访问：
    　　如@WebServlet(name="Servlet3Demo",urlPatterns={"/Servlet3Demo","/Servlet3Demo2"})
    　　或者@WebServlet(name="AnnotationServlet",value={"/Servlet3Demo","/Servlet3Demo2"})
     *
     */
}
