package com.sinco.mybatis.generator;

/**
 * 列
 * @author: dengwei
 * @date: 2018年10月31日 下午5:20:18
 *
 */
public class GeneratorColumn {

	/**
	 * 列名
	 */
	private String colume;
	
	private String jdbcType;
	
	private String javaType;
	
	private String  property;
	
	private String typeHandler;
	
	public GeneratorColumn(String colume, String jdbcType, String javaType, String property, String typeHandler) {
		super();
		this.colume = colume;
		this.jdbcType = jdbcType;
		this.javaType = javaType;
		this.property = property;
		this.typeHandler = typeHandler;
	}
	
	public GeneratorColumn(String colume, String jdbcType, String javaType, String property) {
		super();
		this.colume = colume;
		this.jdbcType = jdbcType;
		this.javaType = javaType;
		this.property = property;
	}

	public String getColume() {
		return colume;
	}

	public void setColume(String colume) {
		this.colume = colume;
	}

	public String getJdbcType() {
		return jdbcType;
	}

	public void setJdbcType(String jdbcType) {
		this.jdbcType = jdbcType;
	}

	public String getJavaType() {
		return javaType;
	}

	public void setJavaType(String javaType) {
		this.javaType = javaType;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public String getTypeHandler() {
		return typeHandler;
	}

	public void setTypeHandler(String typeHandler) {
		this.typeHandler = typeHandler;
	}
}
