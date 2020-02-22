package com.sinco.mybatis.generator.plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.DefaultJavaFormatter;
import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;

import com.sinco.mybatis.generator.GeneratorJavaFile;

public class VoPlugin  extends PluginAdapter{

	public String getTargetProject() {
		return this.getProperties().getProperty("targetProject",null);
	}
	
	public String getTargetRootPackage() {
		return this.getProperties().getProperty("targetRootPackage",null);
	}

	public String getTargetBOPackage() {
		return getTargetRootPackage()+".bo";
	}
	

	public String getTargetVOPackage() {
		return getTargetRootPackage()+".vo";
	}
	
	public String getIsApi() {
		return this.getProperties().getProperty("isApi","false");
	}
	
	
	@Override
	public boolean validate(List<String> arg0) {
		// TODO Auto-generated method stub
		return true;
	}

	private String makeJavaName(String name){
		String [] names=name.split("_");
		
		StringBuffer sb=new StringBuffer();
		for (String s : names) {
			sb.append(toUpperCaseFirstOne(s));
		}
		return sb.toString();
	}
	
	private  String toUpperCaseFirstOne(String s)
    {
        if(Character.isUpperCase(s.charAt(0)))
            return s;
        else
            return (new StringBuilder()).append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).toString();
    }
	
	
    public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(
            IntrospectedTable introspectedTable) {
    	List<GeneratedJavaFile> output=new ArrayList<GeneratedJavaFile>();
    	
    	MyBOCompilationUnit bo=new MyBOCompilationUnit(introspectedTable);
    	output.add(new GeneratedJavaFile(bo, this.getTargetProject(), new DefaultJavaFormatter()));
    	
    	MyVOCompilationUnit vo=new MyVOCompilationUnit(introspectedTable);
    	output.add(new GeneratedJavaFile(vo, getTargetProject(), new DefaultJavaFormatter()));
    	
        return output;
    }
    
    private class MyBOCompilationUnit implements CompilationUnit{
    	
    	public MyBOCompilationUnit(IntrospectedTable introspectedTable){
    		this.introspectedTable=introspectedTable;
    	}
    	
    	private IntrospectedTable introspectedTable;

		@Override
		public String getFormattedContent() {
			Map<String, Object> param=new HashMap<>();
			String name=getName();
			param.put("name", name);
			param.put("isApi", getIsApi());
			param.put("javaName", name+"BO");
			param.put("superClassName",  name);
			param.put("packagePath", getTargetBOPackage());
			param.put("packageRootPath", getTargetRootPackage());
			
			try {
				return GeneratorJavaFile.generatorJava("bo.ftl", param);
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
	    	name+="BO";
	    	
			FullyQualifiedJavaType java=new FullyQualifiedJavaType(getTargetBOPackage()+"."+name);
			
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
    
    private class MyVOCompilationUnit implements CompilationUnit{
    	
    	public MyVOCompilationUnit(IntrospectedTable introspectedTable){
    		this.introspectedTable=introspectedTable;
    	}
    	
    	private IntrospectedTable introspectedTable;
    	
    	@Override
    	public String getFormattedContent() {
    		Map<String, Object> param=new HashMap<>();
    		String name=getName();
    		param.put("name", name);
    		param.put("javaName", name+"VO");
    		param.put("packagePath", getTargetVOPackage());
			param.put("packageRootPath", getTargetRootPackage());
    		
    		try {
    			return GeneratorJavaFile.generatorJava("vo.ftl", param);
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
    		name+="VO";
    		
    		FullyQualifiedJavaType java=new FullyQualifiedJavaType(getTargetVOPackage()+"."+name);
    		
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
