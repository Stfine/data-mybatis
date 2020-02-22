package com.sinco.mybatis.dal.core;

import java.util.Properties;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 分表拦截器
 * @author james.deng
 *
 */
@Intercepts({@Signature(
		  type= Executor.class,
		  method = "update",
		  args = {MappedStatement.class,Object.class})})
public class PartitionTableNameInterceptor extends
			AbstractStatementHandlerInterceptor implements Interceptor {
	
	private final Logger log=LoggerFactory.getLogger(PartitionTableNameInterceptor.class);
	
	private final String paramName="partitionTableName";
	
//	private PartitionTableName getObject(Object [] param){
//		for (Object obj : param) {
//			if(obj instanceof PartitionTableName){
//				return (PartitionTableName) obj;
//			}
//		}
//		return null;
//	}
	
	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		MappedStatement ms = (MappedStatement) invocation.getArgs()[0];

//		PartitionTableName tableName=null;
//		if(null != (tableName=getObject(invocation.getArgs()))){
//			log.debug("PartitionTableName 不为空，产生表名");
//			boundSql.setAdditionalParameter(paramName, tableName.makeTableName());
//		}
		
		return invocation.proceed();
	}

	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	@Override
	public void setProperties(Properties properties) {
	}
}
