package com.mids.mybatis.mapper;

import com.mids.mybatis.model.Organization;

import java.util.List;

public interface OrganizationMapper {
    /**
     * 删除部门
     *
     * @param id
     * @return
     */
    int deleteById(Integer id);

    /**
     * 添加部门
     *
     * @param organization
     * @return
     */
    int insert(Organization organization);

    /**
     * 更新部门
     *
     * @param organization
     * @return
     */
    int update(Organization organization);

    /**
     * 查询一级部门
     *
     * @return
     */
    List<Organization> findAllByPid0();

    /**
     * 查询部门子集
     *
     * @param pid
     * @return
     */
    List<Organization> findAllByPid(Integer pid);

    /**
     * 查询所有部门集合
     *
     * @return
     */
    List<Organization> findAll();

    /**
     * 根据id查询部门
     *
     * @param id
     * @return
     */
    Organization findById(Integer id);
}