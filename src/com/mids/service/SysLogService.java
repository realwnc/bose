package com.mids.service;

import com.mids.mybatis.model.SysLog;
import com.mids.mybatis.util.PageHelper;

/**
 * @description：操作日志
 * @author：wncheng
 * @date：2015/10/30 10:35
 */
public interface SysLogService {

    void insertLog(SysLog sysLog);

    public PageHelper.Page<SysLog> findSysLogPage(SysLog sysLog, int page, int rows, String sort) throws  Exception;
    
    public int deleteExpired(int days);
}
