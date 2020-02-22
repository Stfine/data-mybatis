package com.sinco.mybatis.generator;

import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.api.ShellCallback;
import org.mybatis.generator.config.ColumnOverride;
import org.mybatis.generator.config.CommentGeneratorConfiguration;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.config.JDBCConnectionConfiguration;
import org.mybatis.generator.config.JavaClientGeneratorConfiguration;
import org.mybatis.generator.config.JavaModelGeneratorConfiguration;
import org.mybatis.generator.config.ModelType;
import org.mybatis.generator.config.PluginConfiguration;
import org.mybatis.generator.config.SqlMapGeneratorConfiguration;
import org.mybatis.generator.config.TableConfiguration;
import org.mybatis.generator.exception.InvalidConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.sinco.mybatis.dal.model.BaseModel;
import com.sinco.mybatis.generator.config.JdbcConfig;
import com.sinco.mybatis.generator.plugin.ApiCommentGenerator;
import com.sinco.mybatis.generator.plugin.MapperPlugin;
import com.sinco.mybatis.generator.plugin.SpringDataDaoPlugin;
import com.sinco.mybatis.generator.plugin.VoPlugin;
import com.sinco.mybatis.page.Dialect;

/**
 * 用于产生基本 dao 类
 * @author Administrator
 *
 */
public class MyBatisGeneratorTool2 {
	
	private static final  Logger log=LoggerFactory.getLogger(MyBatisGeneratorTool2.class);
	
	private Class generatorBOClass=VoPlugin.class;
	
	private Class generatorDaoClass=SpringDataDaoPlugin.class;
	
	private JdbcConfig jdbcConfig;
	
	private Dialect dialect;
	
	private String itemPath;
	
	private String rootPackage;
	
	/**
	 * 增量id的name
	 */
	private String incrementIdName="id";
	
	/**
	 * 产生dao
	 */
	private boolean generatorDao = false;
	
	/**
	 * 产生bo
	 */
	private boolean generatorBO = false;
	
	/**
	 * 产生 mapper java 类
	 */
	private boolean generatorMapperJava = true;
	
	
	public MyBatisGeneratorTool2(JdbcConfig jdbcConfig,String itemPath,String rootPackage){
		this.jdbcConfig=jdbcConfig;
		this.itemPath=itemPath;
		this.rootPackage=rootPackage;
		if(jdbcConfig.getDriverClass().indexOf("mysql") > -1) {
			dialect=Dialect.mysql;
		}else if(jdbcConfig.getDriverClass().indexOf("postgresql") > -1) {
			dialect=Dialect.postgresql;
		}else {
			throw new RuntimeException("jdbc driver class["+jdbcConfig.getDriverClass()+"] 不支持");
		}
	}
	
	public void generator(List<GeneratorTable> tables){
		Context context=getContext();
		
		//设置 table
		for (GeneratorTable table : tables) {
			parseTable(context, table);
		}
		
		List<String> warnings = new ArrayList<String>();
		boolean overwrite = true;
		
		Configuration config = new Configuration();
		config.addContext(context);
		
		ShellCallback callback = new MyShellCallback(overwrite,this.generatorMapperJava);
		MyBatisGenerator myBatisGenerator = null;
		try {
			myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
		} catch (InvalidConfigurationException e) {
			e.printStackTrace();
		}
		try {
			myBatisGenerator.generate(null);
			StringBuilder sb=new StringBuilder();
			for (GeneratorTable table : tables) {
				sb.append(table.getTableName()).append(",");
			}
			log.info("生成 [{}] 成功",sb.toString());
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void generator(GeneratorTable table){
		generator(Lists.newArrayList(table));
	}
	
	 private void parseTable(Context context, GeneratorTable table) {
	        TableConfiguration tc = new TableConfiguration(context);
	        context.addTableConfiguration(tc);

	        String catalog = null;
	        String schema = table.getSchema();
	        String tableName = table.getTableName(); //$NON-NLS-1$
	        String domainObjectName =table.getDomainObjectName(); //$NON-NLS-1$
	        String alias = table.getAlias(); //$NON-NLS-1$
//	        String enableInsert = attributes.getProperty("enableInsert"); //$NON-NLS-1$
//	        String enableSelectByPrimaryKey = attributes
//	                .getProperty("enableSelectByPrimaryKey"); //$NON-NLS-1$
//	        String enableSelectByExample = attributes
//	                .getProperty("enableSelectByExample"); //$NON-NLS-1$
//	        String enableUpdateByPrimaryKey = attributes
//	                .getProperty("enableUpdateByPrimaryKey"); //$NON-NLS-1$
//	        String enableDeleteByPrimaryKey = attributes
//	                .getProperty("enableDeleteByPrimaryKey"); //$NON-NLS-1$
//	        String enableDeleteByExample = attributes
//	                .getProperty("enableDeleteByExample"); //$NON-NLS-1$
//	        String enableCountByExample = attributes
//	                .getProperty("enableCountByExample"); //$NON-NLS-1$
//	        String enableUpdateByExample = attributes
//	                .getProperty("enableUpdateByExample"); //$NON-NLS-1$
//	        String selectByPrimaryKeyQueryId = attributes
//	                .getProperty("selectByPrimaryKeyQueryId"); //$NON-NLS-1$
//	        String selectByExampleQueryId = attributes
//	                .getProperty("selectByExampleQueryId"); //$NON-NLS-1$
//	        String modelType = attributes.getProperty("modelType"); //$NON-NLS-1$
//	        String escapeWildcards = attributes.getProperty("escapeWildcards"); //$NON-NLS-1$
//	        String delimitIdentifiers = attributes
//	                .getProperty("delimitIdentifiers"); //$NON-NLS-1$
//	        String delimitAllColumns = attributes.getProperty("delimitAllColumns"); //$NON-NLS-1$

	        if (stringHasValue(catalog)) {
	            tc.setCatalog(catalog);
	        }

	        if (stringHasValue(schema)) {
	            tc.setSchema(schema);
	        }

	        if (stringHasValue(tableName)) {
	            tc.setTableName(tableName);
	        }

	        if (stringHasValue(domainObjectName)) {
	            tc.setDomainObjectName(domainObjectName);
	        }

	        if (stringHasValue(alias)) {
	            tc.setAlias(alias);
	        }
	        //添加列配制
	       if(table.getColumns() != null) {
	    	   table.getColumns().forEach(e -> {
	    		   parseColumnOverride(tc, e);
	    	   });
	       }
	 }
	
	 
	 private void parseColumnOverride(TableConfiguration tc, GeneratorColumn column) {

	        ColumnOverride co = new ColumnOverride(column.getColume());

	        String property = column.getProperty(); //$NON-NLS-1$
	        if (stringHasValue(property)) {
	            co.setJavaProperty(property);
	        }

	        String javaType = column.getJavaType(); //$NON-NLS-1$
	        if (stringHasValue(javaType)) {
	            co.setJavaType(javaType);
	        }

	        String jdbcType = column.getJdbcType(); //$NON-NLS-1$
	        if (stringHasValue(jdbcType)) {
	            co.setJdbcType(jdbcType);
	        }

	        String typeHandler = column.getTypeHandler(); //$NON-NLS-1$
	        if (stringHasValue(typeHandler)) {
	            co.setTypeHandler(typeHandler);
	        }

//	        String delimitedColumnName = attributes
//	                .getProperty("delimitedColumnName"); //$NON-NLS-1$
//	        if (stringHasValue(delimitedColumnName)) {
//	            co.setColumnNameDelimited(isTrue(delimitedColumnName));
//	        }
//
//	        String isGeneratedAlways = attributes.getProperty("isGeneratedAlways"); //$NON-NLS-1$
//	        if (stringHasValue(isGeneratedAlways)) {
//	            co.setGeneratedAlways(Boolean.parseBoolean(isGeneratedAlways));
//	        }

	        tc.addColumnOverride(co);
	    }
	
	/**
	 * 得到 context
	 * @return
	 */
	public Context getContext(){
		Context context=new Context(ModelType.FLAT);
		context.setId("context1");
		context.setJdbcConnectionConfiguration(parseJdbcConnection(jdbcConfig));
		parseCommentGenerator(context);
		parseJavaClientGenerator(context);
		parseJavaModelGenerator(context);
		parsePlugin(context);
		parseSqlMapGenerator(context);
		return context;
	}
	
	/**
	 * 绑定插件
	 * @param context
	 */
    private void parsePlugin(Context context) {
    	//添加 dao 层插件
    	if(generatorDao){
    		PluginConfiguration pluginConfiguration = new PluginConfiguration();
    		pluginConfiguration.setConfigurationType(generatorDaoClass.getName());
    		pluginConfiguration.addProperty("targetRootPackage",rootPackage);
    		pluginConfiguration.addProperty("targetProject",itemPath);
    		pluginConfiguration.addProperty("idName",getIncrementIdName());
    		context.addPluginConfiguration(pluginConfiguration);
    	}
    	
    	//添加 bo 层插件
    	if(generatorBO){
    		PluginConfiguration pluginConfiguration = new PluginConfiguration();
    		pluginConfiguration.setConfigurationType(generatorBOClass.getName());
    		pluginConfiguration.addProperty("targetRootPackage",rootPackage);
    		pluginConfiguration.addProperty("targetProject",itemPath);
    		pluginConfiguration.addProperty("isApi","false");
    		context.addPluginConfiguration(pluginConfiguration);
    	}
    	
    	//mapper 插件
    	PluginConfiguration mapperPluginConfiguration = new PluginConfiguration();
    	mapperPluginConfiguration.setConfigurationType(MapperPlugin.class.getName());
    	mapperPluginConfiguration.addProperty("boPackage",rootPackage+".bo");
    	mapperPluginConfiguration.addProperty("idName",getIncrementIdName());
    	mapperPluginConfiguration.addProperty("dialect",dialect.name());
    	context.addPluginConfiguration(mapperPluginConfiguration);
    }
	
    /**
     * 绑定注释
     * @param context
     */
    private void parseCommentGenerator(Context context) {
        CommentGeneratorConfiguration commentGeneratorConfiguration = new CommentGeneratorConfiguration();
        context.setCommentGeneratorConfiguration(commentGeneratorConfiguration);
        commentGeneratorConfiguration.setConfigurationType(ApiCommentGenerator.class.getName());
        commentGeneratorConfiguration.addProperty("isApi", "false");
    }
    
    /**
     * 绑定 model
     * @param context
     */
    private void parseJavaModelGenerator(Context context) {
        JavaModelGeneratorConfiguration javaModelGeneratorConfiguration = new JavaModelGeneratorConfiguration();

        context.setJavaModelGeneratorConfiguration(javaModelGeneratorConfiguration);
        javaModelGeneratorConfiguration.setTargetPackage(rootPackage+".model");
        javaModelGeneratorConfiguration.setTargetProject(itemPath);
        javaModelGeneratorConfiguration.addProperty("rootClass", BaseModel.class.getName());
    }
    
    /**
     * 绑定 map xml
     * @param context
     */
    private void parseSqlMapGenerator(Context context) {
        SqlMapGeneratorConfiguration sqlMapGeneratorConfiguration = new SqlMapGeneratorConfiguration();

        context.setSqlMapGeneratorConfiguration(sqlMapGeneratorConfiguration);
        sqlMapGeneratorConfiguration.setTargetPackage(rootPackage+".mapper");
        sqlMapGeneratorConfiguration.setTargetProject(itemPath);
    }
    
    /**
     * 绑定map 接口
     * @param context
     * @param node
     */
    private void parseJavaClientGenerator(Context context) {
        JavaClientGeneratorConfiguration javaClientGeneratorConfiguration = new JavaClientGeneratorConfiguration();

        context.setJavaClientGeneratorConfiguration(javaClientGeneratorConfiguration);

        javaClientGeneratorConfiguration.setConfigurationType("XMLMAPPER");
        javaClientGeneratorConfiguration.setTargetPackage(rootPackage+".mapper");
        javaClientGeneratorConfiguration.setTargetProject(itemPath);
       // javaClientGeneratorConfiguration.setImplementationPackage(implementationPackage);
    }
    
	/**
	 * 产生config
	 * @param jdbcConfig
	 * @return
	 */
    private JDBCConnectionConfiguration parseJdbcConnection(JdbcConfig jdbcConfig) {
        JDBCConnectionConfiguration jdbcConnectionConfiguration = new JDBCConnectionConfiguration();

        if(StringUtils.isNotBlank(jdbcConfig.getUsername())){
        	jdbcConnectionConfiguration.setUserId(jdbcConfig.getUsername());
        }
        
        if(StringUtils.isNotBlank(jdbcConfig.getPassword())){
        	jdbcConnectionConfiguration.setPassword(jdbcConfig.getPassword());
        }
        
        jdbcConnectionConfiguration.setDriverClass(jdbcConfig.getDriverClass());
        jdbcConnectionConfiguration.setConnectionURL(jdbcConfig.getUrl());
        return jdbcConnectionConfiguration;
    }

	public Class getGeneratorBOClass() {
		return generatorBOClass;
	}

	public void setGeneratorBOClass(Class generatorBOClass) {
		this.generatorBOClass = generatorBOClass;
	}

	public Class getGeneratorDaoClass() {
		return generatorDaoClass;
	}

	public void setGeneratorDaoClass(Class generatorDaoClass) {
		this.generatorDaoClass = generatorDaoClass;
	}

	public boolean isGeneratorDao() {
		return generatorDao;
	}

	public void setGeneratorDao(boolean generatorDao) {
		this.generatorDao = generatorDao;
	}

	public boolean isGeneratorBO() {
		return generatorBO;
	}

	public void setGeneratorBO(boolean generatorBO) {
		this.generatorBO = generatorBO;
	}

	public String getIncrementIdName() {
		return incrementIdName;
	}

	public void setIncrementIdName(String incrementIdName) {
		this.incrementIdName = incrementIdName;
	}

	public boolean isGeneratorMapperJava() {
		return generatorMapperJava;
	}

	public void setGeneratorMapperJava(boolean generatorMapperJava) {
		this.generatorMapperJava = generatorMapperJava;
	}
}
