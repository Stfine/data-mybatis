package ${packagePath};


import ${packageRootPath}.model.${name};
<#if isApi='true' >
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.varicom.api.annotation.DomainMapping;
</#if>
/**
 * 
 * BO 用于返回数据
 *
 */
<#if isApi='true' >
@JsonAutoDetect(getterVisibility=Visibility.NONE)
@JsonFilter(${javaName}.FILTER_KEY)
@DomainMapping(domainName = "${name}",desc = "", anotherName="${name}")
</#if>
public class ${javaName} extends ${superClassName}{

	<#if isApi='true' >
		public static final String FILTER_KEY="${name}Filter";
	</#if>
	
}