package com.mids.mybatis.util;

import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

/**
 * Mybatis - 通用分页拦截器 支持MYSQL数据库分页
 * 
 * @author wncheng
 */

//@Intercepts({ @Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class }),
@Intercepts({ @Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class, Integer.class }),
			@Signature(type = ResultSetHandler.class, method = "handleResultSets", args = { Statement.class }) })
public class MySQLPageHelper extends PageHelper {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MySQLPageHelper.class);

	/**
	 * 修改原SQL为分页SQL
	 * 
	 * @param sql
	 * @param page
	 * @return
	 */
	@Override
	public String buildPageSql(String sql, Page page) {
		StringBuilder pageSql = new StringBuilder(200);

		pageSql.append(sql);
		pageSql.append(" limit   ").append(page.getStartRow());
		pageSql.append(", ").append(page.getPageSize());
		return pageSql.toString();
	}

	/**
	 * 修改原SQL为mysql分页SQL并排序
	 * 
	 * @param sql
	 * @param page
	 * @return
	 */
	@Override
	public String buildPageSql(String sql, Page page, String sort) {
		StringBuilder pageSql = new StringBuilder(200);

		pageSql.append(sql);
		if (null != sort)
			pageSql.append(" order by " + sort+" ");
		pageSql.append(" limit   ").append(page.getStartRow());
		pageSql.append(" , ").append(page.getPageSize());
		LOGGER.debug("================>>>>分页sql语句 : " +pageSql);
		return pageSql.toString();
	}

}
