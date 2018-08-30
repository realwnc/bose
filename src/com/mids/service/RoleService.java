package com.mids.service;

import com.mids.mybatis.model.Role;
import com.mids.mybatis.util.PageHelper;
import com.mids.mybatis.vo.Tree;

import java.util.List;
import java.util.Map;

/**
 * @description：权限管理
 * @author：wncheng
 * @date：2015/10/1 14:51
 */
public interface RoleService {
    /**
     * 查询权限列表
     *
     * @param 
     */
	
    public PageHelper.Page<Role> findPage(Role role, int page, int rows, String sort) throws  Exception;
    
    /**
     * 查询权限树
     *
     * @return
     */
    List<Tree> findTree();

    /**
     * 添加权限
     *
     * @param role
     */
    void add(Role role);

    /**
     * 根据id删除权限
     *
     * @param id
     */
    void deleteById(Integer id);

    /**
     * 根据id查询权限
     *
     * @param id
     * @return
     */
    Role findById(Integer id);

    /**
     * 更新权限
     *
     * @param role
     */
    void update(Role role);

    /**
     * 根据权限id查询资源集合
     *
     * @param id
     * @return
     */
    List<Integer> findResourceIdListByRoleId(Integer id);

    /**
     * 更新权限和资源的关联关系
     *
     * @param id
     * @param resourceIds
     */
    void updateResource(Integer id, String resourceIds);

    /**
     * 根据用户查询id查询权限集合
     *
     * @param userId
     * @return
     */
    List<Integer> findIdListByUserId(Integer userId);

    /**
     * 根据权限查询id查询资源路径集合
     *
     * @param roleId
     * @return
     */
    List<Map<Integer, String>> findResourceListByRoleId(Integer roleId);

}
