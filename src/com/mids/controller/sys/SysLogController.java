package com.mids.controller.sys;

//import com.google.common.collect.Maps;
import com.mids.mybatis.model.SysLog;
import com.mids.mybatis.util.PageHelper;
import com.mids.service.SysLogService;
import com.mids.util.MyFastjsonUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

/**
 * @description：日志管理
 * @author：wncheng
 * @date：2015/10/30 18:06
 */
@Controller
@RequestMapping("/sys/sysLog")
public class SysLogController {
    private static final Logger LOGGER = LoggerFactory.getLogger(SysLogController.class);

    @Autowired
    private SysLogService SysLogService;


    @RequestMapping(value = "/manager", method = RequestMethod.GET)
    public String manager() {
        return "/sys/admin/syslog";
    }


    @RequestMapping("findSysLogPage")
	public void findSysLogPage(HttpServletResponse response, SysLog sysLog,
			@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "rows", defaultValue = "10") int rows) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			//user.setUsername("");
			PageHelper.Page<SysLog> sysLogPage = SysLogService.findSysLogPage(sysLog, page, rows, "");
			// 得到套餐产品所有的国家
			
			map.put("total", sysLogPage.getTotal());
			map.put("rows", sysLogPage.getResult());
		} catch (Exception e) {
			map.put("total", 0);
			map.put("rows", "");
			LOGGER.error("===>获取系统日志分页数据出错 :", e);
		}
		MyFastjsonUtil.sendJsonToResponse(map, response);
	}
}
