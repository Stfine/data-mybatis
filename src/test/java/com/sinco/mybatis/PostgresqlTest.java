package com.sinco.mybatis;

import java.util.List;

import com.google.common.collect.Lists;
import com.sinco.mybatis.generator.GeneratorTable;
import com.sinco.mybatis.generator.MyBatisGeneratorTool2;
import com.sinco.mybatis.generator.config.JdbcConfig;

public class PostgresqlTest {

	public static void main(String[] args) {
		// 数据连接配制
		JdbcConfig jdbc = new JdbcConfig(
				"jdbc:postgresql://localhost:5432/james-test", "james",
				"admin123", "org.postgresql.Driver");

		// 代码生成的文件目录
		String itemPath = "/Users/james/git/data-mybatis/data-mybatis/src/test/java";
		// 主包名
		String rootPackage = "com.sinco.mybatis";

		MyBatisGeneratorTool2 tool = new MyBatisGeneratorTool2(jdbc, itemPath, rootPackage);
		// 是否生成 bo
		tool.setGeneratorBO(true);
		// 是否生成 dao
		tool.setGeneratorDao(true);
		// 是否重新生成 mapper
		tool.setGeneratorMapperJava(true);

		List<GeneratorTable> tableList = Lists.newArrayList(
				new GeneratorTable("public", "Member", "Member")
		);

		tool.generator(tableList);

		System.out.println("生成成功");
	}

}
