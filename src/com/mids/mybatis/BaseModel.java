package com.mids.mybatis;

import java.io.Serializable;


public class BaseModel implements Serializable {
	private static final long serialVersionUID = 1L;
	
    private Integer mchtId;
    private String sort;
	private String order;

	public Integer getMchtId() {
		return mchtId;
	}
	public void setMchtId(Integer mchtId) {
		this.mchtId = mchtId;
	}
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}

}