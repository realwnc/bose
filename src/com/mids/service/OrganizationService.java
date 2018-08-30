package com.mids.service;

import com.mids.mybatis.model.Organization;
import com.mids.mybatis.vo.Tree;

import java.util.List;

/**
 * @description：部门管理
 * @author：wncheng
 * @date：2015/10/1 14:51
 */
public interface OrganizationService {
    /**
     * 查询部门资源树
     *
     * @return
     */
    List<Tree> findTree();

    /**
     * 查询部门数据表格
     *
     * @return
     */
    List<Organization> findTreeGrid();

    /**
     * 添加部门
     *
     * @param organization
     */
    void add(Organization organization);

    /**
     * 根据id查找部门
     *
     * @param id
     * @return
     */
    Organization findById(Integer id);

    /**
     * 更新部门
     *
     * @param organization
     */
    void update(Organization organization);

    /**
     * 根据id删除部门
     *
     * @param id
     */
    void deleteById(Integer id);

}
