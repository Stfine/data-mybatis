package com.sinco.mybatis.generator;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.FileTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;

public class GeneratorJavaFile {
	static Configuration cfg;
	static{
		cfg = new Configuration();
		// 指定模板文件从何处加载的数据源，这里设置成一个文件目录。
//		try {
			
			//FileTemplateLoader ftl=new FileTemplateLoader(new File(GeneratorJavaFile.class.getResource("/ftl").getPath()));
			ClassTemplateLoader ctl = new ClassTemplateLoader(GeneratorJavaFile.class, "/ftl");
			TemplateLoader[] loaders = new TemplateLoader[] {ctl };
			//TemplateLoader[] loaders = new TemplateLoader[] { ftl, ctl };
			MultiTemplateLoader mtl = new MultiTemplateLoader(loaders);
			cfg.setTemplateLoader(mtl);

//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		// 指定模板如何检索数据模型，这是一个高级的主题了…
		// 但先可以这么来用：
		cfg.setObjectWrapper(new DefaultObjectWrapper());
	}
	
	public static String generatorJava(String ftlName,Map<String, Object> param) throws Exception{
			Template temp = cfg.getTemplate(ftlName);
			StringWriter writer=new StringWriter();
			temp.process(param, writer);
			return writer.toString();
	}
	
}
