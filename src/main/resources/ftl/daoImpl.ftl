package ${packagePath};

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ${packageRootPath}.mapper.${name}Mapper;
import ${packageRootPath}.model.${name};
import ${packageRootPath}.bo.${name}BO;
import ${packageRootPath}.model.${name}Example;
import ${packageRootPath}.vo.${name}VO;

@Repository
public class ${javaName} {

	@Autowired
	private ${name}Mapper mapper;

	public int deleteById(Long id) {
		return mapper.deleteByPrimaryKey(id);
	}

	public int insert(${name} record) {
		return mapper.insert(record);
	}

	public ${name}BO findById(Long id) {
		return mapper.selectByPrimaryKey(id);
	}

	public int updateById(${name} record) {
		return mapper.updateByPrimaryKey(record);
	}

	public List<${name}BO> findByVO(${name}VO vo) {
		${name}Example example=new ${name}Example();
		return mapper.selectByExample(example);
	}

}
