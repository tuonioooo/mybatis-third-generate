package ${package.Mapper};

import ${package.Entity}.${entity};
import com.hacker.mybatis.mapper.AutoMapper;

/**
* <p> Mapper接口 </p>
*
* @author daizhao ${currentTime}
*/
public interface ${table.mapperName} extends Mapper<${entity}Entity>, MySqlMapper<${entity}Entity> {

}