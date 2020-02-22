package com.sinco.mybatis.dal.core;

import java.sql.Connection;
import java.util.Map;
import java.util.Properties;

import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sinco.data.core.auth.AuthData;
import com.sinco.mybatis.dal.util.FieldUtils;

/**
 * 数据权限拦截器
 * @author james.deng
 *
 */
@Intercepts({ @Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class, Integer.class }) })
public class AuthHandlerInterceptor  implements Interceptor {
	
	private final Logger log=LoggerFactory.getLogger(AuthHandlerInterceptor.class);
	
	/**
	 * 得到授权
	 * @param param
	 * @return
	 */
	private AuthData getAuth(Object param){
		//得到page对象
		if(param instanceof AuthData){
			return (AuthData) param;
		}
		if(param instanceof Map){
			Map<String, Object> pageParamMap=((Map)param);
			for(Object val: pageParamMap.values()){
				if(val instanceof AuthData){
					return (AuthData) val;
				}
			}
		}
		return null;
	}
	
	@Override
	public Object intercept(Invocation invocation) throws Throwable {

		StatementHandler statement = getStatementHandler(invocation);

		BoundSql boundSql = statement.getBoundSql(); 
		Object param=boundSql.getParameterObject();
		AuthData auth=null;
		
		if(null != (auth=getAuth(param))){
			log.debug("auth data 不为空，进行数据权限处理");
			
			String sql = boundSql.getSql();
			
			//进行权限处理
			sql=auth.authHandle(sql);
			
			FieldUtils.setFieldValue(boundSql, "sql", sql);
			
			log.info("auth data sql:"+sql);
			
		}

		return invocation.proceed();
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
	
	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	@Override
	public void setProperties(Properties properties) {
		// TODO Auto-generated method stub
	}
}
