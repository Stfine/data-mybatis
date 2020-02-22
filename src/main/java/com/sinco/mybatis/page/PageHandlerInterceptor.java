package com.sinco.mybatis.page;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sinco.data.core.Page;
import com.sinco.mybatis.dal.core.AbstractExample;
import com.sinco.mybatis.page.parser.impl.AbstractParser;
import com.sinco.mybatis.page.util.MSUtils;

/**
 * 分布拦截器
 * @author james.deng
 *
 */
@SuppressWarnings({"rawtypes", "unchecked"})
@Intercepts(@Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}))
public class PageHandlerInterceptor implements Interceptor {

	private final Logger log=LoggerFactory.getLogger(PageHandlerInterceptor.class);
	
	//处理MS
    private MSUtils msUtils;
	
	/**
	 * 得到分布查询对象
	 * @param param
	 * @return
	 */
	private Page getPage(Object param){
		//得到page对象
		if(param instanceof Page){
			return (Page) param;
		}
		if(param instanceof AbstractExample){
			AbstractExample ae=(AbstractExample)param;
			return ae.getPage();
		}
		if(param instanceof Map){
			Map<String, Object> pageParamMap=((Map)param);
			for(Object val: pageParamMap.values()){
				if(val instanceof Page){
					return (Page) val;
				}
				if(val instanceof AbstractExample){
					AbstractExample ae=(AbstractExample)param;
					return ae.getPage();
				}
			}
		}
		return null;
	}
	
	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		
		 final Object[] args = invocation.getArgs();
		 
		
		Object param=args[1];
		
		Page page=null;
		
		if(null != (page=getPage(param))){
			log.debug("Page 不为空，进行分页处理");
			//获取原始的ms
			MappedStatement ms = (MappedStatement) args[0];
			
            SqlSource sqlSource = ms.getSqlSource();
			
			//将参数中的MappedStatement替换为新的qs
            msUtils.processCountMappedStatement(ms, sqlSource, args);
            //查询总数
            Object result = invocation.proceed();
            //设置总数
            page.setTotal((Integer) ((List) result).get(0));
            
            if (page.getTotalElements() == 0) {
                return new ArrayList();
            }
            List list=new ArrayList();
            if (page.getPageSize() > 0){
            	//将参数中的MappedStatement替换为新的qs
            	msUtils.processPageMappedStatement(ms, sqlSource, page, args);
	            //执行分页查询
            	list = (List) invocation.proceed();
            	page.setContent(list);
			}
			return list;
		}
		return invocation.proceed();
	}

	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	@Override
	public void setProperties(Properties properties) {
		Object dialect=properties.get("dialect");
		if(dialect != null) {
			msUtils = new MSUtils(AbstractParser.newParser(
					com.sinco.mybatis.page.Dialect.valueOf((String)dialect)));
		}else {
			msUtils = new MSUtils(AbstractParser.newParser(com.sinco.mybatis.page.Dialect.mysql));
		}
	}
}
