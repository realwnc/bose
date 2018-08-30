package com.mids.mybatis;

import java.util.List;

public interface BaseMapper<T> {

    int insert(T obj);
    int insertSelective(T obj);

	int deleteByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(T obj);
    int updateByPrimaryKey(T record);

    T selectByPrimaryKey(Integer id);

	List<T> findList(T obj);
}