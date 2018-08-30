package com.mids.mybatis.mapper;

import com.mids.mybatis.model.RoleResource;

import java.util.List;

public interface RoleResourceMapper {
    
	int insert(RoleResource roleResource);

    List<RoleResource> findIdListByRoleId(Integer id);

    int deleteById(Integer roleResourceId);
}