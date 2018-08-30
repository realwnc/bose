package com.mids.mybatis.mapper;

import com.mids.mybatis.model.Resource;
import com.mids.mybatis.model.Role;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

public interface RoleMapper {
    int insert(Role role);

    List<Role> findList(Role role) throws DataAccessException;

    //int findPageCount();

    List<Role> findAll();

    Role findById(Integer id);

    int update(Role role);

    int deleteById(Integer id);

    List<Integer> findResourceIdListByRoleId(Integer id);

    List<Integer> findRoleResourceIdListByRoleId(Integer id);

    List<Map<Integer, String>> findRoleResourceListByRoleId(Integer id);

    List<Resource> findResourceIdListByRoleIdAndType(Integer i);

}