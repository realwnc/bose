package com.mids.sysassist;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.mids.common.cache.CacheScheduledThreadPoolExecutor;


@WebServlet(name = "initStartupData", urlPatterns = "/initStartupData", loadOnStartup = 4)
public class InitStartupDataServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(InitStartupDataServlet.class);
   
    //@Autowired(required = true)
    //private LotMobileInfoService lotMobileInfoService;

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, config.getServletContext());
        initXXXData();
    }

    public void initXXXData() {
    	LOGGER.info("----------------init data while startup-------------------");
        
		CacheScheduledThreadPoolExecutor.startCacheScheduledTask();
    }
    
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // DBDataCtrl.executeUpdate("update tb_ids_terminal set dt_registertime=now() where vc_terminalid='test01@gzecsr'");
        response.getWriter().write("Hello Servlet3.0");
        // User user = new User();
        // // user.setId("101");
        // user.setUsername("wncheng");
        // user.setPassword("123");
        // user.setStsDate("2015-04-13 10:38:22");
        String result = null;// JSON.toJSONString(user);
        response.getWriter().write(result);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request, response);
    }
}