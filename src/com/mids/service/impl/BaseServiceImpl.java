package com.mids.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.mids.mybatis.BaseMapper;
import com.mids.mybatis.util.MySQLPageHelper;
import com.mids.mybatis.util.PageHelper;
import com.mids.service.BaseService;

public class BaseServiceImpl<T> implements BaseService<T>
{
	@Autowired
	private BaseMapper baseMapper;
	
	@Override
	public int add(T obj)
	{
		return baseMapper.insert(obj);
	}

	@Override
	public int addSelective(T obj)
	{
		return baseMapper.insertSelective(obj);
	}
	
	@Override
    public int deleteById(Integer id){
		return baseMapper.deleteByPrimaryKey(id);
    }

	@Override
	public int update(T obj){
		return baseMapper.updateByPrimaryKey(obj);		
    }
	@Override
	public int updateSelective(T obj){
		return baseMapper.updateByPrimaryKeySelective(obj);		
    }
    
	@Override
	public T findById(Integer id){
		return (T) baseMapper.selectByPrimaryKey(id);
    }

	@Override
	public List<T> findList(T obj){
		return baseMapper.findList(obj);
    }
    
	@Override
    public PageHelper.Page<T> findPage(T obj, int page, int rows, String sort) throws  Exception
    {
		MySQLPageHelper pageHelper = new MySQLPageHelper();
        pageHelper.startPage(page, rows, sort);
        baseMapper.findList(obj);
        return pageHelper.endPage();
    }
	
}