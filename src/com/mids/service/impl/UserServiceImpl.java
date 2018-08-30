package com.mids.service.impl;

import com.mids.mybatis.mapper.UserMapper;
import com.mids.mybatis.mapper.UserRoleMapper;
import com.mids.mybatis.model.User;
import com.mids.mybatis.model.UserRole;
import com.mids.mybatis.util.MySQLPageHelper;
import com.mids.mybatis.util.PageHelper;
import com.mids.mybatis.util.PageHelper.Page;
import com.mids.service.UserService;
import com.mids.mybatis.vo.UserVo;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private static Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;

    @Override
    public User findByUsername(String username) {
        return userMapper.findByUsername(username);
    }

    @Override
    public User findById(Integer id) {
        return userMapper.findById(id);
    }

    
    @Override
	public PageHelper.Page<UserVo> findVoPage(UserVo userVo, int pageNum, int rows, String sort) throws Exception {
        MySQLPageHelper pageHelper = new MySQLPageHelper();
        pageHelper.startPage(pageNum, rows, sort);
        userMapper.findVoList(userVo);
        Page page=pageHelper.endPage();
        Iterator<UserVo> iter=page.getResult().iterator();
        while(iter.hasNext())
        {
        	UserVo obj=iter.next();
        	List<UserRole> userRoleList=userRoleMapper.findListByUserId(obj.getId());
        	obj.setUserRoleList(userRoleList);
        }
        page.getResult();
        return page;
    }
    @Override
    public void add(UserVo userVo) {
        User user = new User();
        try {
            PropertyUtils.copyProperties(user, userVo);
        } catch (Exception e) {
            LOGGER.error("类转换异常：{}", e);
            throw new RuntimeException("类型转换异常：{}", e);
        }
        user.setInsertTime(new Date());
        user.setUpdateTime(new Date());
        userMapper.insert(user);

        Integer id = user.getId();
        String[] roles = userVo.getRoleIds().split(",");
        UserRole userRole = new UserRole();

        for (String string : roles) {
            userRole.setUserId(id);
            userRole.setRoleId(Integer.valueOf(string));
            userRole.setInsertTime(new Date());
            userRole.setUpdateTime(new Date());
            userRoleMapper.insert(userRole);
        }
    }

    @Override
    public void updatePwdById(Integer userId, String pwd) {
        userMapper.updatePwdById(userId, pwd);
    }

    @Override
    public UserVo findVoById(Integer id) {
        return userMapper.findVoById(id);
    }

    @Override
    public void update(UserVo userVo) {
        User user = new User();
        try {
            PropertyUtils.copyProperties(user, userVo);
        } catch (Exception e) {
            LOGGER.error("类转换异常：{}", e);
            throw new RuntimeException("类型转换异常：{}", e);
        }
        user.setUpdateTime(new Date());
        userMapper.update(user);
        Integer id = userVo.getId();
        List<UserRole> userRoles = userRoleMapper.findListByUserId(id);
        if (userRoles != null && (!userRoles.isEmpty())) {
            for (UserRole userRole : userRoles) {
                userRoleMapper.deleteById(userRole.getId());
            }
        }

        String[] roles = userVo.getRoleIds().split(",");
        UserRole userRole = new UserRole();
        for (String string : roles) {
            userRole.setUserId(id);
            userRole.setRoleId(Integer.valueOf(string));
            userRole.setInsertTime(new Date());
            userRole.setUpdateTime(new Date());
            userRoleMapper.insert(userRole);
        }

    }

    @Override
    public void deleteById(Integer id) {
        userMapper.deleteById(id);
        List<UserRole> userRoles = userRoleMapper.findListByUserId(id);
        if (userRoles != null && (!userRoles.isEmpty())) {
            for (UserRole userRole : userRoles) {
                userRoleMapper.deleteById(userRole.getId());
            }
        }
    }

    @Override
    public List<UserRole> findListByUserId(Integer userId)
    {
    	return userRoleMapper.findListByUserId(userId);
    }
}
