package com.mids.controller.test;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mids.common.Result;
import com.mids.controller.BaseBusController;
import com.mids.util.LocalHostNameUtil;
import com.mids.util.MyFastjsonUtil;

/**
 * @description：
 * @author：wncheng
 * @date：2016年5月6日
 */
@Controller
@RequestMapping("test/")
public class TestingController extends BaseBusController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestingController.class);
    
    //@Autowired
    //private HadoopInterfaceService hadoopInterfaceService;
    
    @RequestMapping(value = "/test", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
	public Result test(@RequestBody String postData) {
    	Result result = new Result();    	
		try
		{
			Map<String, Object> reqMap = MyFastjsonUtil.json2Map(postData);
			String mobile = reqMap.get("mobile").toString();
			String product_id = reqMap.get("product_id").toString();
			String product_name = reqMap.get("product_name").toString();
			
			
			result.setSuccess(true);
			result.setObj(LocalHostNameUtil.getLocalHostName());
			
		} catch (Exception e) {
			LOGGER.error("---------->failed to subscribeProduct", e);
			e.printStackTrace();
			result.setMsg("Exception on subscribeProduct...");
		}
		//result.setObj(LocalHostNameUtil.getLocalHostName());
		return result;
	}
    
    
}
