package com.mids.mybatis.model;

import java.io.Serializable;
import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @description：角色
 * @author：wncheng
 * @date：2015/10/1 14:51
 */
public class Role implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String name;

    private Integer seq;

    private String description;

    private Integer statusx;

	@JSONField (format="yyyy-MM-dd HH:mm:ss")
    private Date insertTime;

	@JSONField (format="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public Integer getStatusx() {
        return statusx;
    }

    public void setStatusx(Integer statusx) {
        this.statusx = statusx;
    }
    
    public Date getInsertTime() {
		return insertTime;
	}

	public void setInsertTime(Date insertTime) {
		this.insertTime = insertTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}


    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", seq=" + seq +
                ", description='" + description + '\'' +
                ", statusx=" + statusx +
                ", insertTime=" + insertTime +
                ", updateTime=" + updateTime +
                '}';
    }
}