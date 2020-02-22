package com.sinco.mybatis.generator;

import java.util.List;

/**
 * 表
 * @author: dengwei
 * @date: 2018年10月31日 下午5:19:56
 *
 */
public class GeneratorTable {

	private String schema;
	private String tableName;
	private String domainObjectName;
	private String alias;
	private String idName;
	private List<GeneratorColumn> columns;
	
	public GeneratorTable(String schema, String tableName, String domainObjectName) {
		this.schema = schema;
		this.tableName = tableName;
		this.domainObjectName = domainObjectName;
	}
	public GeneratorTable(String schema, String tableName, String domainObjectName,String idName) {
		this.schema = schema;
		this.tableName = tableName;
		this.domainObjectName = domainObjectName;
		this.idName = idName;
	}
	public GeneratorTable(String schema, String tableName, String domainObjectName,List<GeneratorColumn> columns) {
		this.schema = schema;
		this.tableName = tableName;
		this.domainObjectName = domainObjectName;
		this.columns = columns;
	}
	
	public List<GeneratorColumn> getColumns() {
		return columns;
	}
	public void setColumns(List<GeneratorColumn> columns) {
		this.columns = columns;
	}
	public String getIdName() {
		return idName;
	}

	public void setIdName(String idName) {
		this.idName = idName;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getSchema() {
		return schema;
	}
	public void setSchema(String schema) {
		this.schema = schema;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getDomainObjectName() {
		return domainObjectName;
	}
	public void setDomainObjectName(String domainObjectName) {
		this.domainObjectName = domainObjectName;
	}
}
