package com.mids.common;

import java.io.Serializable;

/**
 * @description：操作结果集
 * @author：wncheng
 * @date：2015/10/1 14:51
 */
public class MyReturn implements Serializable {
	private static final long serialVersionUID = 1L;

    private int retcode = -1;    
	private String retmsg = "";
    private Object body = "";

    //-----------------------------------------------------------
    public int getRetcode() {
		return retcode;
	}
	public void setRetcode(int retcode) {
		this.retcode = retcode;
	}
	public String getRetmsg() {
		return retmsg;
	}
	public void setRetmsg(String retmsg) {
		this.retmsg = retmsg;
	}
	public Object getBody() {
		return body;
	}
	public void setBody(Object body) {
		this.body = body;
	}
}
