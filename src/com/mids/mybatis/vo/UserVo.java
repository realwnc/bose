package com.mids.mybatis.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.mids.mybatis.model.User;
import com.mids.mybatis.model.UserRole;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @description：UserVo
 * @author：wncheng
 * @date：2015/10/1 14:51
 */
public class UserVo extends User {
	private static final long serialVersionUID = 1L;

	private boolean ck=false;
	
    private List<UserRole> userRoleList;

	private Integer organizationId;
    private String organizationName;

    private String roleIds;

	private Date startTime;

	private Date endTime;

    
    public boolean isCk() {
		return ck;
	}

	public void setCk(boolean ck) {
		this.ck = ck;
	}

    
    public Integer getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Integer organizationId) {
        this.organizationId = organizationId;
    }

    public List<UserRole> getUserRoleList() {
		return userRoleList;
	}

	public void setUserRoleList(List<UserRole> userRoleList) {
		this.userRoleList = userRoleList;
	}

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(String roleIds) {
        this.roleIds = roleIds;
    }
    
    public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
}