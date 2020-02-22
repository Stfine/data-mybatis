package com.sinco.mybatis.dal.core;

import java.sql.PreparedStatement;
import java.util.Properties;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;

import com.sinco.data.core.entity.PrePersistEntity;
import com.sinco.data.core.entity.PreUpdateEntity;

/**
 * 拦截器用于执行 Pre 方法
 * @author james.deng
 *
 */
@Intercepts( {  
    @Signature(method = "update", type = Executor.class, args = {  
           MappedStatement.class, Object.class }) })  
public class PreHandlerInterceptor implements Interceptor {

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		MappedStatement ms= (MappedStatement) invocation.getArgs()[0];
		Object param=invocation.getArgs()[1];
		if("UPDATE".equals(ms.getSqlCommandType().name())){
			if(param instanceof PreUpdateEntity){
				PreUpdateEntity preUpdate=(PreUpdateEntity) param;
				preUpdate.preUpdate();
			}
		}else if("INSERT".equals(ms.getSqlCommandType().name())){
			if(param instanceof PrePersistEntity){
				PrePersistEntity prePersis=(PrePersistEntity) param;
				prePersis.prePersist();
			}
		}
	    Object result = invocation.proceed();  
	    return result;  
	}

	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	@Override
	public void setProperties(Properties properties) {
		
	}
	
}
