package com.sinco.mybatis.dal.core;

import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.session.RowBounds;

import com.sinco.data.core.dialect.Dialect;
import com.sinco.mybatis.dal.util.FieldUtils;

/**
 * 拦截器
 * @author james.deng
 *
 */
public abstract class AbstractStatementHandlerInterceptor implements Interceptor{
	
	
	protected Dialect dialect;

	/**
	 * 设置数据库方言
	 * @param properties
	 */
	public void setDialect(Properties properties) {
		String classPath=properties.getProperty("dialectClassPath");
		if(StringUtils.isBlank(classPath)){
			throw new RuntimeException("dialectClassPath is null");
		}
		try {
			Class dialectClass = Class.forName(classPath);
			this.dialect = (Dialect) dialectClass.newInstance();
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(classPath+"not find",e);
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	
	}

	protected StatementHandler getStatementHandler(Invocation invocation) {
		StatementHandler statement = (StatementHandler) invocation.getTarget();
		if (statement instanceof RoutingStatementHandler) {
			try {
				statement = (StatementHandler) FieldUtils.getFieldValue(statement,
						"delegate");
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}
		return statement;
	}
	
	protected MappedStatement getMappedStatement(StatementHandler delegate) {
		try {
			return (MappedStatement) FieldUtils.getFieldValue(delegate, "mappedStatement");
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}  
	}
	
	protected RowBounds getRowBounds(StatementHandler statement) {
		try {
			return (RowBounds) FieldUtils.getFieldValue(statement, "rowBounds");
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
	
	protected boolean hasBounds(RowBounds rowBounds) {
		return (rowBounds != null 
				&& rowBounds.getLimit() > 0 
				&& rowBounds.getLimit() < RowBounds.NO_ROW_LIMIT);
	}

}
