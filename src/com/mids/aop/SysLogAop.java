package com.mids.aop;

import com.mids.mybatis.model.SysLog;
import com.mids.service.SysLogService;
import com.mids.util.Base64Util;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Enumeration;

/**
 * @description：AOP 日志
 * @author：wncheng
 * @date：2015/10/28 18:04
 */
@Aspect
@Component
public class SysLogAop {
    private static Logger LOGGER = LoggerFactory.getLogger(SysLogAop.class);

    @Autowired
    private SysLogService sysLogService;

    @Pointcut("within(@org.springframework.stereotype.Controller *)")
    	public void pointCut() {
    }

    @Around("pointCut()")
    public Object recordSysLog(ProceedingJoinPoint point) throws Throwable 
	{
        String method = point.getSignature().getName();
        String className = point.getTarget().getClass().getName();
        Object[] params = point.getArgs();
        StringBuffer sbParams = new StringBuffer();
        Enumeration<String> paraNames = null;
        HttpServletRequest request = null;
        if (params != null && params.length > 0) 
        {
            request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            paraNames = request.getParameterNames();//form表单
            String key;
            String value;
            while (paraNames.hasMoreElements()) {
                key = paraNames.nextElement();
                value = request.getParameter(key);
                sbParams.append(key).append("=").append(value).append("&");
            }
            if (StringUtils.isBlank(sbParams)) {
                sbParams.append(request.getQueryString());
            }
        }
        String strMessage = String.format("[类名]:%s,[方法]:%s,[参数]:%s", className, method, sbParams.toString());
        //LOGGER.info(strMessage);//comment by wncheng
        //if (isWriteLog(method)) 
        {
            try {
                Subject currentUser = SecurityUtils.getSubject();
                PrincipalCollection collection = currentUser.getPrincipals();
                if (null != collection) {
                    String loginName = collection.getPrimaryPrincipal().toString();
                    SysLog sysLog = new SysLog();
                    sysLog.setUsername(loginName);
                    sysLog.setRoleName(loginName);
                    sysLog.setOptContent(strMessage);
                    if (request != null) {
                        sysLog.setClientIp(request.getRemoteAddr());
                    }
                    LOGGER.info(sysLog.toString());
                    //sysLogService.insertLog(sysLog);//comment by wncheng
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return point.proceed();
    }
    private boolean isWriteLog(String method) {
        String[] pattern = {"login", "logout", "add", "edit", "delete", "grant"};
        for (String s : pattern) {
            if (method.indexOf(s) > -1) {
                return true;
            }
        }
        return false;
    }
}
