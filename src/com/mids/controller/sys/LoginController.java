package com.mids.controller.sys;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.mids.ConstantsCode;
import com.mids.common.Result;
import com.mids.util.CaptchaUtil;
import com.mids.util.MyFastjsonUtil;
import com.mids.util.RandomString;
import com.mids.util.RandomUtils;
import com.mids.util.crypt.MyAESUtils;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @description：登录退出
 * @author：wncheng
 * @date：2015/10/1 14:51
 */
@Controller
@RequestMapping("/sys")
public class LoginController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

    /**
     * 首页
     *
     * @return
     */
    /*
    @RequestMapping(value = "/")
    public String index() {
    	LOGGER.debug("redirect:/index.do");
        return "redirect:/index.do";
    }
	*/
    /**
     * 首页
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/index")
    public String index(Model model) {
    	LOGGER.debug("index");
    	
        return "/sys/main";
    }

    /**
     * GET 登录
     *
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(Model model, HttpServletRequest request) {
        LOGGER.info("--------->GET请求登录");
        String uuid = RandomUtils.generateUUID().toLowerCase();
        model.addAttribute("csrftoken", uuid);
        request.getSession().setAttribute("csrftoken", uuid);
        
        String aeskey = MyAESUtils.KEY;
        model.addAttribute("aeskey",  aeskey);
        request.getSession().setAttribute("aeskey", aeskey);
        //return "/sys/login";
        return "/sys/notlogin";//转到登录jsp,不用action是为了逃避shiro登录拦截
    }

    /**
     * POST 登录 shiro 写法
     *
     * @param username 用户名
     * @param password 密码
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public Result loginPost(String username, String password, String valcode, HttpServletRequest request, Model model, String csrftoken) {
        LOGGER.info("POST请求登录");
      
        Result result = new Result();
        if(StringUtils.isBlank(csrftoken) == true)
        {
        	result.setMsg("非法请求");
            return result;
        }
        String server_csrftoken = request.getSession().getAttribute("csrftoken").toString();
        if(server_csrftoken.equalsIgnoreCase(csrftoken) == false)
        {
        	result.setMsg("非法请求");
            return result;
        }
        if (StringUtils.isBlank(username)) {
            result.setMsg("用户名不能为空");
            return result;
        }
        if (StringUtils.isBlank(password)) {
            result.setMsg("密码不能为空");
            return result;
        }
        if (StringUtils.isBlank(valcode)) {
            result.setMsg("验证码不能为空");
            return result;
        }
        String sessvalcode = request.getSession(true).getAttribute("randomString").toString();
    	if(valcode.equalsIgnoreCase(sessvalcode) == false)
    	{
    		result.setMsg("验证码错误");
    		return result;
    	}
    	request.getSession(true).setAttribute("randomString", RandomString.GenerateRandStr(6));
    	
        try {
        	username = MyAESUtils.aesDecrypt(username);
        	Subject user = SecurityUtils.getSubject();
        	//String pwd=DigestUtils.md5Hex(password);
        	//char[] pwdCharArray=DigestUtils.md5Hex(password).toCharArray();
        	UsernamePasswordToken token = new UsernamePasswordToken(username, password.toCharArray());
        	token.setRememberMe(true);
            user.login(token);
            
            user.getSession().setAttribute("username", username);
        } catch (UnknownAccountException e) {
            LOGGER.error("账号不存在：{}", e);
            result.setMsg("用户名或密码错误");
            return result;
        } catch (DisabledAccountException e) {
            LOGGER.error("账号未启用：{}", e);
            result.setMsg("用户名或密码错误");
            return result;
        } catch (IncorrectCredentialsException e) {
            LOGGER.error("密码错误：{}", e);
            result.setMsg("用户名或密码错误");
            return result;
        } catch (Exception e) {
            LOGGER.error("未知错误,请联系管理员：{}", e);
            result.setMsg("未知错误,请联系管理员");
            return result;
        }
        
        
        result.setSuccess(true);
        return result;
    }

    /**
     * 未授权
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/unauth")
    public String unauth(Model model) {
        if (SecurityUtils.getSubject().isAuthenticated() == false) {
            return "redirect:/sys/login";
        }
        return "/unauth";
    }

    /**
     * 退出
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/logout")
    @ResponseBody
    public Result logout(HttpServletRequest request) {
        LOGGER.info("登出");
        Subject subject = SecurityUtils.getSubject();
        Result result = new Result();
        subject.logout();
        result.setSuccess(true);
        return result;
    }
    
	@RequestMapping(value = "/register", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
	public void register(HttpServletRequest request, HttpServletResponse response, @RequestBody String postData){
		
    	int retcode=ConstantsCode.RETCODE_SUCCEED;
		String retmsg=ConstantsCode.globalMapErrorCode.get(ConstantsCode.RETCODE_SUCCEED);
		
		Map<String, Object> rootmap = new HashMap<String, Object>();
		Map<String, Object> reqMap=null;
		try
		{
			reqMap = MyFastjsonUtil.json2Map(postData);
	    } catch (Exception e) {
			retcode=ConstantsCode.RETCODE_EXCEPTION;
			retmsg=ConstantsCode.globalMapErrorCode.get(ConstantsCode.RETCODE_EXCEPTION);
			LOGGER.error("===>weixin user login exception...", e);
		}
    	
    	//if(requestParamMap==null || !requestParamMap.containsKey("mobile"))
    	if(reqMap==null)
    	{
    		LOGGER.debug("==>reqeuest parameter is invalid!!!");
    		rootmap.put("retcode", ConstantsCode.RETCODE_ERROR_PARAMETER);
    		rootmap.put("retmsg", ConstantsCode.globalMapErrorCode.get(ConstantsCode.RETCODE_ERROR_PARAMETER));
    		MyFastjsonUtil.sendJsonToResponse(rootmap, response);
            return;
    	}
		try {
			
			
		} catch (Exception e) {
			retcode=ConstantsCode.RETCODE_EXCEPTION;
			retmsg=ConstantsCode.globalMapErrorCode.get(ConstantsCode.RETCODE_EXCEPTION);
			LOGGER.error("===>weixin user login exception...", e);
		}
		rootmap.put("retcode", retcode);
		rootmap.put("retmsg", retmsg);		
		
		MyFastjsonUtil.sendJsonToResponse(rootmap, response);
	}
	
	
	@RequestMapping(value = "/captcha", method = RequestMethod.GET)
    @ResponseBody
    public void captcha(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
    {
        CaptchaUtil.outputCaptcha(request, response);
    }
}
