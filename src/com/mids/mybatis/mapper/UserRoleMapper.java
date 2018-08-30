package com.mids.mybatis.mapper;

import java.util.List;

import com.mids.mybatis.model.UserRole;

public interface UserRoleMapper {

    int insert(UserRole userRole);

    int updateByPrimaryKeySelective(UserRole userRole);

    List<UserRole> findByUserId(Integer userId);

    int deleteById(Integer id);

    List<Integer> findRoleIdListByUserId(Integer userId);
    
    List<UserRole> findListByUserId(Integer userId);
}