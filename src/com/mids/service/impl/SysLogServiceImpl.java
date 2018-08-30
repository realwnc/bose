package com.mids.service.impl;

import com.mids.mybatis.mapper.SysLogMapper;
import com.mids.mybatis.model.SysLog;
import com.mids.mybatis.util.MySQLPageHelper;
import com.mids.mybatis.util.PageHelper;
import com.mids.service.SysLogService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @description：
 * @author：wncheng
 * @date：2015/10/30 10:40
 */
@Service
public class SysLogServiceImpl implements SysLogService {
    @Autowired
    private SysLogMapper sysLogMapper;

    @Override
    public void insertLog(SysLog sysLog) {
        sysLogMapper.insert(sysLog);
    }

    @Override
	public PageHelper.Page<SysLog> findSysLogPage(SysLog sysLog, int page, int rows, String sort) throws Exception {
        MySQLPageHelper pageHelper = new MySQLPageHelper();
        pageHelper.startPage(page, rows, sort);
        sysLogMapper.findList(sysLog);
        return pageHelper.endPage();
    }
    
    @Override
    public int deleteExpired(int days)
    {
    	return sysLogMapper.deleteExpired(days);
    }
}
