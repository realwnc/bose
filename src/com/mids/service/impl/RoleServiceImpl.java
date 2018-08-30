package com.mids.service.impl;

import com.mids.common.ServiceException;
import com.mids.mybatis.mapper.RoleMapper;
import com.mids.mybatis.mapper.RoleResourceMapper;
import com.mids.mybatis.mapper.UserRoleMapper;
import com.mids.mybatis.model.Role;
import com.mids.mybatis.model.RoleResource;
import com.mids.service.RoleService;
import com.mids.mybatis.util.MySQLPageHelper;
import com.mids.mybatis.util.PageHelper;
import com.mids.mybatis.vo.Tree;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class RoleServiceImpl implements RoleService {

    private static Logger LOGGER = LoggerFactory.getLogger(RoleServiceImpl.class);

    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private RoleResourceMapper roleResourceMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;

    @Override
   	public PageHelper.Page<Role> findPage(Role role, int page, int rows, String sort) throws Exception {
           MySQLPageHelper pageHelper = new MySQLPageHelper();
           pageHelper.startPage(page, rows, sort);
           roleMapper.findList(role);
           return pageHelper.endPage();
       }
    @Override
    public List<Tree> findTree() {
        List<Tree> trees = new ArrayList<Tree>();
        List<Role> roles = roleMapper.findAll();
        for (Role role : roles) {
            Tree tree = new Tree();
            tree.setId(role.getId());
            tree.setText(role.getName());

            trees.add(tree);
        }
        return trees;
    }

    @Override
    public void add(Role role) {
        int insert = roleMapper.insert(role);
        if (insert != 1) {
            LOGGER.warn("插入失败，参数：{}", role.toString());
            throw new ServiceException("插入失败");
        }
    }

    @Override
    public void deleteById(Integer id) {
        int update = roleMapper.deleteById(id);
        if (update != 1) {
            LOGGER.warn("删除失败，id：{}", id);
            throw new ServiceException("删除失败");
        }
    }

    @Override
    public Role findById(Integer id) {
        return roleMapper.findById(id);
    }

    @Override
    public void update(Role role) {
        int update = roleMapper.update(role);
        if (update != 1) {
            LOGGER.warn("更新失败，参数：{}", role.toString());
            throw new ServiceException("更新失败");
        }
    }

    @Override
    public List<Integer> findResourceIdListByRoleId(Integer id) {
        return roleMapper.findResourceIdListByRoleId(id);
    }

    @Override
    public void updateResource(Integer id, String resourceIds) {
        // 先删除后添加,有点爆力
        List<Integer> roleResourceIdList = roleMapper.findRoleResourceIdListByRoleId(id);
        if (roleResourceIdList != null && (!roleResourceIdList.isEmpty())) {
            for (Integer roleResourceId : roleResourceIdList) {
                roleResourceMapper.deleteById(roleResourceId);
            }
        }
        String[] resources = resourceIds.split(",");
        RoleResource roleResource = new RoleResource();
        for (String string : resources) {
            roleResource.setRoleId(id);
            roleResource.setResourceId(Integer.parseInt(string));
            roleResource.setInsertTime(new Date());
            roleResource.setUpdateTime(new Date());
            roleResourceMapper.insert(roleResource);
        }
    }

    @Override
    public List<Integer> findIdListByUserId(Integer userId) {
        return userRoleMapper.findRoleIdListByUserId(userId);
    }

    @Override
    public List<Map<Integer, String>> findResourceListByRoleId(Integer roleId) {
        return roleMapper.findRoleResourceListByRoleId(roleId);
    }

}
