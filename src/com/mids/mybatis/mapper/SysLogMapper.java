package com.mids.mybatis.mapper;

import com.mids.mybatis.model.SysLog;

import java.util.List;

import org.springframework.dao.DataAccessException;

public interface SysLogMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SysLog record);

    int insertSelective(SysLog record);

    SysLog selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysLog record);

    int updateByPrimaryKey(SysLog record);

    List<SysLog> findList(SysLog sysLog) throws DataAccessException;

    int deleteExpired(int days);
}