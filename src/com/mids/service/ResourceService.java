package com.mids.service;

import com.mids.mybatis.model.Resource;
import com.mids.mybatis.model.User;
import com.mids.mybatis.vo.Tree;

import java.util.List;

/**
 * @description：资源管理
 * @author：wncheng
 * @date：2015/10/1 14:51
 */
public interface ResourceService {

    /**
     * 根据用户查询树形菜单列表
     *
     * @param currentUser
     * @return
     */
    List<Tree> findTree(User currentUser);

    /**
     * 查询所有资源
     *
     * @return
     */
    List<Resource> findAll();

    /**
     * 添加资源
     *
     * @param resource
     */
    void add(Resource resource);

    /**
     * 查询二级数
     *
     * @return
     */
    List<Tree> findAllTree();

    /**
     * 查询三级数
     *
     * @return
     */
    List<Tree> findAllTrees();

    /**
     * 更新资源
     *
     * @param resource
     */
    void update(Resource resource);

    /**
     * 根据id查询资源
     *
     * @param id
     * @return
     */
    Resource findById(Integer id);

    /**
     * 根据id删除资源
     *
     * @param id
     */
    void deleteById(Integer id);

}
