package com.sinco.mybatis.generator.plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.DefaultJavaFormatter;
import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;

import com.sinco.mybatis.generator.GeneratorJavaFile;

public class TestPlugin  extends BasicPlugin {

	
	@Override
	public boolean validate(List<String> arg0) {
		// TODO Auto-generated method stub
		return true;
	}
	
	public String getTargetPackage() {
		return getTargetRootPackage()+".test";
	}
	
    public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(
            IntrospectedTable introspectedTable) {
    	List<GeneratedJavaFile> output=new ArrayList<GeneratedJavaFile>();
    	
    	MyTestCompilationUnit vo=new MyTestCompilationUnit(introspectedTable);
    	output.add(new GeneratedJavaFile(vo,getTargetProject(), new DefaultJavaFormatter()));
    	
        return output;
    }
    
    private class MyTestCompilationUnit implements CompilationUnit{
    	
    	public MyTestCompilationUnit(IntrospectedTable introspectedTable){
    		this.introspectedTable=introspectedTable;
    	}
    	
    	private IntrospectedTable introspectedTable;
    	
    	@Override
    	public String getFormattedContent() {
    		Map<String, Object> param=new HashMap<>();
    		String name=getName();
    		param.put("name", name);
    		param.put("javaName", name+"DaoTest");
    		param.put("packagePath", getTargetPackage());
			param.put("packageRootPath", getTargetRootPackage());
    		
    		try {
    			return GeneratorJavaFile.generatorJava("test.ftl", param);
    		} catch (Exception e) {
    			throw new RuntimeException();
    		}
    	}
    	
    	@Override
    	public Set<FullyQualifiedJavaType> getImportedTypes() {
    		Set<FullyQualifiedJavaType> output=new HashSet<FullyQualifiedJavaType>();
    		
    		return output;
    	}
    	
    	@Override
    	public Set<String> getStaticImports() {
    		// TODO Auto-generated method stub
    		return null;
    	}
    	
    	@Override
    	public FullyQualifiedJavaType getSuperClass() {
    		
    		return null;
    	}
    	
    	@Override
    	public boolean isJavaInterface() {
    		// TODO Auto-generated method stub
    		return false;
    	}
    	
    	@Override
    	public boolean isJavaEnumeration() {
    		// TODO Auto-generated method stub
    		return false;
    	}
    	
		private String getName(){
			String name=introspectedTable.getTableConfiguration().getDomainObjectName();
			if(name == null){
				name=introspectedTable.getTableConfiguration().getTableName();
			}
			return  makeJavaName(name);
		}
    	
    	@Override
    	public Set<FullyQualifiedJavaType> getSuperInterfaceTypes() {
    		return null;
    	}
    	
    	@Override
    	public FullyQualifiedJavaType getType() {
    		// TODO Auto-generated method stub
    		String name= getName();
    		name+="DaoTest";
    		
    		FullyQualifiedJavaType java=new FullyQualifiedJavaType(getTargetPackage()+"."+name);
    		
    		return java;
    	}
    	
    	@Override
    	public void addImportedType(FullyQualifiedJavaType importedType) {
    		// TODO Auto-generated method stub
    		
    	}
    	
    	@Override
    	public void addImportedTypes(Set<FullyQualifiedJavaType> importedTypes) {
    		// TODO Auto-generated method stub
    		
    	}
    	
    	@Override
    	public void addStaticImport(String staticImport) {
    		// TODO Auto-generated method stub
    		
    	}
    	
    	@Override
    	public void addStaticImports(Set<String> staticImports) {
    		// TODO Auto-generated method stub
    		
    	}
    	
    	@Override
    	public void addFileCommentLine(String commentLine) {
    		// TODO Auto-generated method stub
    		
    	}
    	
    	@Override
    	public List<String> getFileCommentLines() {
    		// TODO Auto-generated method stub
    		return null;
    	}
    	
    }
}
