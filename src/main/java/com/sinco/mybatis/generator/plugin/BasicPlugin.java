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
import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;

import com.sinco.mybatis.generator.GeneratorJavaFile;

public abstract class BasicPlugin extends PluginAdapter {

	public String getTargetProject() {
		return this.getProperties().getProperty("targetProject",null);
	}
	
	public String getTargetRootPackage() {
		return this.getProperties().getProperty("targetRootPackage",null);
	}
	
	protected String makeJavaName(String name){
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
}
