package ${package.Service};

import com.alibaba.fastjson.JSONObject;
import ${package.Entity}.${entity};
import ${package.Entity}.Page;
import ${package.Parent}.dao.DaoSupport;
import ${package.Parent}.utils.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
  * <p> 业务接口 </p>
  *
  * @author daizhao ${currentTime}
  */
@Service("${entity?uncap_first}Service")
@Slf4j
public class ${entity}Service{

	@Autowired
	private ${table.mapperName} ${table.mapperName?cap_first};

}