package ${packagePath};

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.google.common.collect.Lists;

import ${packageRootPath}.mapper.${name}Mapper;
import ${packageRootPath}.model.${name};
import ${packageRootPath}.bo.${name}BO;
import ${packageRootPath}.model.${name}Example;

@Repository
public class ${javaName} {

	@Autowired
	private ${name}Mapper mapper;


   /** generate code begin**/
	public List<${name}BO> findAll(){
		${name}Example example=new ${name}Example();
		return mapper.selectByExample(example);
	}
	<#if !pks?? >
	List<${name}BO> findAll(Iterable<${idType}> ids){
		${name}Example example=new ${name}Example();
		example.createCriteria().and${idName2}In(Lists.newArrayList(ids));
		return mapper.selectByExample(example);
	}
	</#if>
	
	public long count(){
		${name}Example example=new ${name}Example();
		return mapper.countByExample(example);
	}

	public List<${name}> save(Iterable<${name}> entities){
		List<${name}> list=new ArrayList<${name}>();
		for (${name} ${name} : entities) {
			list.add(save(${name}));
		}
		return list;
	}
	
	<#if pks?? >
	public ${name} save(${name} record){
		
		mapper.insertSelective(record);
		
		return record;
	}
	<#else>
	public ${name} save(${name} record){
		<#if idInc >
		if(record.get${idName2}() == null){
		<#else>
		if(!exists(record.get${idName2}())){
		</#if>
			mapper.insertSelective(record);
		}else{
			mapper.updateByPrimaryKeySelective(record);
		}
		return record;
	}
	</#if>
	

	<#if pks?? >
		<#list pks as pk>
	public List<${name}BO> findBy${pk.javaProperty?cap_first}(${pk.fullyQualifiedJavaType.fullyQualifiedName} ${pk.javaProperty}){
		${name}Example example=new ${name}Example();
		example.createCriteria().and${pk.javaProperty?cap_first}EqualTo(${pk.javaProperty});
		return mapper.selectByExample(example);
	}
	
	public int deleteBy${pk.javaProperty?cap_first}(${pk.fullyQualifiedJavaType.fullyQualifiedName} ${pk.javaProperty}){
		${name}Example example=new ${name}Example();
		example.createCriteria().and${pk.javaProperty?cap_first}EqualTo(${pk.javaProperty});
		return mapper.deleteByExample(example);
	}
		</#list>

	public int delete(<#list pks as pk> ${pk.fullyQualifiedJavaType.fullyQualifiedName} ${pk.javaProperty}<#if pk_has_next>,</#if></#list>){
		return mapper.deleteByPrimaryKey(<#list pks as pk>${pk.javaProperty}<#if pk_has_next>,</#if></#list>);
	}
	<#if isFindOne>
	public ${name}BO findOne(<#list pks as pk> ${pk.fullyQualifiedJavaType.fullyQualifiedName} ${pk.javaProperty}<#if pk_has_next>,</#if></#list>){
		return mapper.selectByPrimaryKey(<#list pks as pk>${pk.javaProperty}<#if pk_has_next>,</#if></#list>);
	}
	</#if>
	<#else>
	public void update(${name} record) {
		mapper.updateByPrimaryKeySelective(record);
	}
	
	public ${name}BO findOne(${idType} id){
		return mapper.selectByPrimaryKey(id);
	}

	public boolean exists(${idType} id){
		if(id == null){
			return false;
		}
		${name}Example example=new ${name}Example();
		example.createCriteria().and${idName2}EqualTo(id);
		return mapper.countByExample(example) > 0;
	}
	
	 public void delete(${idType} id){
		 mapper.deleteByPrimaryKey(id);
	 }
	 
	 public void remove(${idType} id){
		 mapper.deleteByPrimaryKey(id);
	 }

	public void delete(${name} entity){
		 mapper.deleteByPrimaryKey(entity.get${idName2}());
	}

	public void delete(Iterable<${name}> entities){
		List<${idType}> ids=Lists.newArrayList();
		for (${name}  entity: entities) {
			ids.add(entity.get${idName2}());
		}
		deleteByIds(ids);
	}
	
	public void deleteByIds(Iterable<${idType}> ids){
		${name}Example example=new ${name}Example();
		example.createCriteria().and${idName2}In(Lists.newArrayList(ids));
		 mapper.deleteByExample(example);
	}

	public void deleteAll(){
		${name}Example example=new ${name}Example();
		mapper.deleteByExample(example);
	}
	</#if>
	/** generate code end**/
}
