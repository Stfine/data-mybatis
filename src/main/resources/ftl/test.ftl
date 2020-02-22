package ${packagePath};

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import ${packageRootPath}.model.${name};
import ${packageRootPath}.vo.${name}VO;
import ${packageRootPath}.dao.${name}Dao;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:applicationContext.xml" })
@TransactionConfiguration(defaultRollback = true)
@Transactional(readOnly = false)
public class ${javaName} {

	@Autowired
	private ${name}Dao dao;
	
	@Test
	public void insert(){
		${name} model=new ${name}();
		int result=dao.insert(model);
		Assert.assertEquals(1, result);
	}
	
	@Test
	public void deleteById(){
		Long id=1L;
		int result=dao.deleteById(id);
		Assert.assertEquals(1, result);
	}
	
	@Test
	public void findById(){
		Long id=1L;
		${name} model=dao.findById(id);
	}
	
	@Test
	public void updateById(){
		${name} model=new ${name}();
		int result=dao.updateById(model);
		Assert.assertEquals(1, result);
	}
	
	public void findByVO(){
		${name}VO vo=new ${name}VO();
		List<${name}> list=dao.findByVO(vo);
	}
}