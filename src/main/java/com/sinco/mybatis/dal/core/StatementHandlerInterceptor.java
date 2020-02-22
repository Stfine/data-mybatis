package com.sinco.mybatis.dal.core;

import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.ibatis.executor.statement.PreparedStatementHandler;
import org.apache.ibatis.executor.statement.SimpleStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sinco.data.core.Page;
import com.sinco.mybatis.dal.util.FieldUtils;

/**
 * 分页拦截器
 * @author james.deng
 *
 */
public abstract class StatementHandlerInterceptor extends
			AbstractStatementHandlerInterceptor implements Interceptor {
	
	private final Logger log=LoggerFactory.getLogger(StatementHandlerInterceptor.class);

	
	public abstract SqlSession getMybatisSession();
	
	private Object prepare(Invocation invocation) throws Throwable {
		StatementHandler statement = getStatementHandler(invocation);
		
		if (statement instanceof SimpleStatementHandler 
				|| statement instanceof PreparedStatementHandler) {

			MappedStatement mappedStatement=getMappedStatement(statement);
			BoundSql boundSql = statement.getBoundSql();  
			Object param=boundSql.getParameterObject();
			Page page=null;
			if(null != (page=getPage(param))){
				log.debug("Page 不为空，进行分页处理");
				
				//用于count查询
				Object countParam=null; 
				
				//set countParam 
				if(param instanceof Page){
					countParam=null;
				}
				
				if(param instanceof Map){
					//将page对象排除，对于查询count
					Map<String, Object> pageParamMap=((Map)param);
					Map<String, Object> countParamMap=new HashMap<String, Object>();
					for(String key: pageParamMap.keySet()){
						Object mapValue=pageParamMap.get(key);
						if(!(mapValue instanceof Page)){
							countParamMap.put(key, mapValue);
						}
					}
					countParam=countParamMap;
				}
				SqlSession session= getMybatisSession();//getSqlSession();
				String namespace=mappedStatement.getId().substring(0, mappedStatement.getId().lastIndexOf("."));
				String pageSqlId=mappedStatement.getId().substring(mappedStatement.getId().lastIndexOf(".")+1,mappedStatement.getId().length());
				
				//创建 count sql id
				String countSqlId=createConutId(pageSqlId);
				
				//执行count sql
				Object obj=session.selectOne(namespace+"."+countSqlId, countParam);
				
				if(null != obj){
					page.setTotal(new Long(String.valueOf(obj)));
				}else{
					log.info(namespace+"."+countSqlId+" return null");
					page.setTotal(0);
					//return null;
				}
				
				
				String sql = boundSql.getSql();
				
				if (statement instanceof SimpleStatementHandler) {
					sql = dialect.getLimitString(sql,page.getOffset(),page.getSize());
				}
				else if (statement instanceof PreparedStatementHandler) {
					sql = dialect.getLimitString(sql, page.getOffset() > 0);
				}
				FieldUtils.setFieldValue(boundSql, "sql", sql);
				
				log.debug("page sql:"+sql);
				
			}
		}

		return invocation.proceed();
	}
	
	private Object parameterize(Invocation invocation) throws Throwable {
		Statement statement = (Statement) invocation.getArgs()[0];

		StatementHandler statementHandler = getStatementHandler(invocation);
		
		BoundSql boundSql = statementHandler.getBoundSql();
		Object rtn = invocation.proceed();

		if (statement instanceof PreparedStatement) {
			Object param=boundSql.getParameterObject();
			Page page=null;
			if(null != (page=getPage(param))){
				log.debug("Page 不为空，设置分页参数");
				PreparedStatement ps = (PreparedStatement) statement;
				int parameterSize = boundSql.getParameterMappings().size();
				dialect.setLimitParamters(ps, parameterSize,
						page.getOffset(), page.getSize());
			}
		}
		return rtn;
	}
	
	/**
	 * 创建select count sql id
	 * @param pageId
	 * @return
	 */
	private String createConutId(String pageId){
		String conutId="";
		
		if(pageId.indexOf("By") > 0 || pageId.indexOf("by") > 0 ){
			if(pageId.indexOf("By") > 0){
				conutId=pageId.replace("By", "CountBy");
			}else{
				conutId=pageId.replace("by", "Countby");
			}
		}else{
			conutId=pageId+"Count";
		}
		return conutId;
	}
	/**
	 * 得到分布查询对象
	 * @param param
	 * @return
	 */
	private Page getPage(Object param){
		//得到page对象
		if(param instanceof Page){
			return (Page) param;
		}else if (param instanceof AbstractExample){
			return ((AbstractExample)param).getPage();
		}
		if(param instanceof Map){
			Map<String, Object> pageParamMap=((Map)param);
			for(Object val: pageParamMap.values()){
				if(val instanceof Page){
					return (Page) val;
				}else if (val instanceof AbstractExample){
					return ((AbstractExample)param).getPage();
				}
			}
		}
		return null;
	}
	
	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		Method m = invocation.getMethod();
		if ("prepare".equals(m.getName())) {
			return prepare(invocation);
		}
		else if ("parameterize".equals(m.getName())) {
			return parameterize(invocation);
		}
		return invocation.proceed();
	}

	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	@Override
	public void setProperties(Properties properties) {
		//设置数据库方言
		setDialect(properties);
	}
}
