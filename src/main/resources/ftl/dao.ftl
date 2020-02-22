package ${packagePath};

import java.util.List;

import ${packageRootPath}.model.${name};
import ${packageRootPath}.vo.${name}VO;

/**
 * 
 * 数据访问接口
 *
 */
public interface ${javaName} {
	
    int deleteById(Long id);

    int insert(${name} record);

    ${name} findById(Long id);
    
    List<${name}> findByVO(${name}VO vo);
    
    int updateById(${name} record);
}
