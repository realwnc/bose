package com.mids.service;

import java.util.List;

import com.mids.mybatis.model.User;
import com.mids.mybatis.model.UserRole;
import com.mids.mybatis.util.PageHelper;
import com.mids.mybatis.vo.UserVo;

/**
 * @description：用户管理
 * @author：wncheng
 * @date：2015/10/1 14:51
 */
public interface UserService {
   
	User findByUsername(String username);

    User findById(Integer id);

    public PageHelper.Page<UserVo> findVoPage(UserVo userVo, int page, int rows, String sort) throws  Exception;

    void add(UserVo userVo);

    void updatePwdById(Integer userId, String pwd);

    UserVo findVoById(Integer id);

    void update(UserVo userVo);

    void deleteById(Integer id);
    
    
    List<UserRole> findListByUserId(Integer userId);
}
