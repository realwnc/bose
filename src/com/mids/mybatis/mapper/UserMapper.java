package com.mids.mybatis.mapper;

import com.mids.mybatis.model.User;
import com.mids.mybatis.vo.UserVo;

import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface UserMapper {
    int deleteById(Integer id);

    int insert(User user);

    int update(User user);

    User findByUsername(String username);

    User findById(Integer id);
    
    List<UserVo> findVoList(UserVo userVo) throws DataAccessException;

	void updatePwdById(@Param("userId") Integer userId, @Param("password") String password);

    UserVo findVoById(Integer id);
    
}