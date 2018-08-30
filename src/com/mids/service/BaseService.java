package com.mids.service;

import java.util.List;

import com.mids.mybatis.util.PageHelper;

public interface BaseService<T> {
	
    int add(T obj);
    int addSelective(T obj);

    int deleteById(Integer id);

    int update(T obj);
    int updateSelective(T obj);
    
    T findById(Integer id);

    List<T> findList(T obj);
    public PageHelper.Page<T> findPage(T obj, int page, int rows, String sort) throws  Exception;
}
