package com.mids.mybatis.mapper;

import com.mids.mybatis.model.Resource;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ResourceMapper {
    int insert(Resource resource);

    int update(Resource resource);

    List<Resource> findAllByTypeAndPid(@Param("resourceType") Integer resourceType, @Param("pid") Integer pid);

    List<Resource> findAll();

	List<Resource> findAllByTypeAndPid0(Integer resourceMenu);

    Resource findById(Integer id);

    int deleteById(Integer id);
}