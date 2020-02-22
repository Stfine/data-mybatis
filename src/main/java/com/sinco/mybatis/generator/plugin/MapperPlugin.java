package com.sinco.mybatis.generator.plugin;

import java.util.List;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;

import com.sinco.mybatis.page.Dialect;

/**
 * 
 * @author Administrator
 *
 */
public class MapperPlugin extends BasicPlugin {

	public static final String boMapId="BOResultMap";
	
	@Override
	public boolean validate(List<String> warnings) {
		// TODO Auto-generated method stub
		return true;
	}
	
	public String getBOPackage(){
		return this.getProperties().getProperty("boPackage",null);
	}
	
	public String getIdName(){
		return this.getProperties().getProperty("idName","id");
	}
	
	public Dialect getDialect(){
		return Dialect.of(this.getProperties().getProperty("dialect","mysql"));
	}

	private String getName( IntrospectedTable introspectedTable){
		String name=introspectedTable.getTableConfiguration().getDomainObjectName();
		if(name == null){
			name=introspectedTable.getTableConfiguration().getTableName();
		}
		return  makeJavaName(name);
	}
	
    public boolean modelExampleClassGenerated(TopLevelClass topLevelClass,
            IntrospectedTable introspectedTable) {
    	//添加 limit 
    	Field limitField=new Field("limit", FullyQualifiedJavaType.getStringInstance());
    	limitField.setVisibility(JavaVisibility.PROTECTED);
    	topLevelClass.addField(limitField);
    	
    	Method getLimit=new Method("getLimit");
    	getLimit.setReturnType(FullyQualifiedJavaType.getStringInstance());
    	getLimit.setVisibility(JavaVisibility.PUBLIC);
    	getLimit.addBodyLine("return limit;");
    	topLevelClass.addMethod(getLimit);
    	
    	Method setLimit=new Method("setLimit");
    	setLimit.addParameter(new Parameter(FullyQualifiedJavaType.getStringInstance(), "limit"));
    	setLimit.setVisibility(JavaVisibility.PUBLIC);
    	setLimit.addBodyLine("this.limit = limit;");
    	topLevelClass.addMethod(setLimit);
    	
    	Method setLimit1=new Method("setLimit");
    	setLimit1.addParameter(new Parameter(FullyQualifiedJavaType.getIntInstance(), "count"));
    	setLimit1.addBodyLine("this.limit = String.valueOf(count);");
    	setLimit1.setVisibility(JavaVisibility.PUBLIC);
    	topLevelClass.addMethod(setLimit1);
    	
    	Method setLimit2=new Method("setLimit");
    	setLimit2.addParameter(new Parameter(FullyQualifiedJavaType.getIntInstance(), "offset"));
    	setLimit2.addParameter(new Parameter(FullyQualifiedJavaType.getIntInstance(), "rows"));
    	//根据 Dialect 判断
    	switch (getDialect()) {
		case postgresql:
			setLimit2.addBodyLine("this.limit = new StringBuilder().append(String.valueOf(offset)).append(\" offset \").append(String.valueOf(rows)).toString();");
			break;
		default:
			setLimit2.addBodyLine("this.limit = new StringBuilder().append(String.valueOf(offset)).append(\",\").append(String.valueOf(rows)).toString();");
			break;
		}
    	setLimit2.setVisibility(JavaVisibility.PUBLIC);
    	topLevelClass.addMethod(setLimit2);
    	
    	String packageName=getBOPackage()+"."+getName(introspectedTable)+"BO";
    	topLevelClass.setSuperClass(new FullyQualifiedJavaType("AbstractExample<"+packageName+">"));
    	
    	topLevelClass.addImportedType("com.sinco.mybatis.dal.core.AbstractExample");
    	topLevelClass.addImportedType(packageName);
        return true;
    }
	
	@Override
	public boolean clientGenerated(Interface interfaze,
			TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		//添加引用类
		String packageName=getBOPackage()+"."+getName(introspectedTable)+"BO";
		FullyQualifiedJavaType  javaType= new FullyQualifiedJavaType(packageName);
		interfaze.addImportedType(javaType);
		
		return true;
	}

	@Override
	public boolean clientSelectByPrimaryKeyMethodGenerated(Method method,
			Interface interfaze, IntrospectedTable introspectedTable) {
		//改为返回BO
		String packageName=getBOPackage()+"."+getName(introspectedTable)+"BO";
		FullyQualifiedJavaType  javaType= new FullyQualifiedJavaType(packageName);
		method.setReturnType(javaType);
		return  true;
	}
	
	@Override
	public boolean clientSelectByExampleWithBLOBsMethodGenerated(Method method,
			Interface interfaze, IntrospectedTable introspectedTable) {
		//改为返回BO
		String packageName=getBOPackage()+"."+getName(introspectedTable)+"BO";
		FullyQualifiedJavaType  javaType= new FullyQualifiedJavaType("java.util.List");
		javaType.addTypeArgument(new FullyQualifiedJavaType(packageName));
		method.setReturnType(javaType);
		
		return super.clientSelectByExampleWithBLOBsMethodGenerated(method, interfaze,
				introspectedTable);
	}

	@Override
	public boolean clientSelectByExampleWithoutBLOBsMethodGenerated(
			Method method, Interface interfaze,
			IntrospectedTable introspectedTable) {
		
		//改为返回BO
		String packageName=getBOPackage()+"."+getName(introspectedTable)+"BO";
		FullyQualifiedJavaType  javaType= new FullyQualifiedJavaType("java.util.List");
		javaType.addTypeArgument(new FullyQualifiedJavaType(packageName));
		method.setReturnType(javaType);
		
		return super.clientSelectByExampleWithoutBLOBsMethodGenerated(method,
				interfaze, introspectedTable);
	}
	
	@Override
	public boolean sqlMapInsertElementGenerated(XmlElement element,
			IntrospectedTable introspectedTable) {
		
		 List<IntrospectedColumn> pks= introspectedTable.getPrimaryKeyColumns();
		 if(pks.size() < 2){
			 IntrospectedColumn column=pks.get(0);
			 
			 if(getIdName().equals(column.getActualColumnName())){
				 element.addAttribute(new Attribute("useGeneratedKeys", "true"));
				 element.addAttribute(new Attribute("keyProperty", getIdName()));
			 }
		 }
		
		return super.sqlMapInsertElementGenerated(element, introspectedTable);
	}

	@Override
	public boolean sqlMapInsertSelectiveElementGenerated(XmlElement element,
			IntrospectedTable introspectedTable) {
		 List<IntrospectedColumn> pks= introspectedTable.getPrimaryKeyColumns();
		 if(pks.size() < 2){
			 IntrospectedColumn column=pks.get(0);
			 if(getIdName().equals(column.getActualColumnName())){
				 element.addAttribute(new Attribute("useGeneratedKeys", "true"));
				 element.addAttribute(new Attribute("keyProperty", getIdName()));
			 }
		 }
		
		
		return super.sqlMapInsertSelectiveElementGenerated(element, introspectedTable);
	}

	/**
	 * 设置返回bo
	 * @param element
	 */
	private void setResultBO(XmlElement element){
		for (Attribute  a : element.getAttributes()) {
			if(a.getName().equals("resultMap")){
				element.getAttributes().remove(a);
				break;
			}
		}
		element.addAttribute(new Attribute("resultMap", boMapId));
	}

	/**
	 * 得到 limit 
	 * @return
	 */
	private XmlElement getLimitElement(){
		XmlElement limit= new XmlElement("if");
		limit.addAttribute(new Attribute("test", "limit != null"));
		limit.addElement(new TextElement("limit ${limit}"));
		return limit;
	}
	
	@Override
	public boolean sqlMapSelectByExampleWithoutBLOBsElementGenerated(
			XmlElement element, IntrospectedTable introspectedTable) {
		
		element.addElement(getLimitElement());
		
		setResultBO(element);
		
		return super.sqlMapSelectByExampleWithoutBLOBsElementGenerated(element,
				introspectedTable);
	}

	@Override
	public boolean sqlMapSelectByExampleWithBLOBsElementGenerated(
			XmlElement element, IntrospectedTable introspectedTable) {
		setResultBO(element);
		return super.sqlMapSelectByExampleWithBLOBsElementGenerated(element,
				introspectedTable);
	}

	@Override
	public boolean sqlMapSelectByPrimaryKeyElementGenerated(XmlElement element,
			IntrospectedTable introspectedTable) {
		setResultBO(element);
		return super.sqlMapSelectByPrimaryKeyElementGenerated(element,
				introspectedTable);
	}

	@Override
	public boolean sqlMapDocumentGenerated(Document document,
			IntrospectedTable introspectedTable) {
		//添加 bo map
		String packageName=getBOPackage()+"."+getName(introspectedTable)+"BO";
		XmlElement element=new XmlElement("resultMap");
		element.addAttribute(new Attribute("id", boMapId));
		element.addAttribute(new Attribute("type", packageName));
		element.addAttribute(new Attribute("extends", "BaseResultMap"));
		document.getRootElement().addElement(1, element);
		
		return super.sqlMapDocumentGenerated(document, introspectedTable);
	}
	
}
