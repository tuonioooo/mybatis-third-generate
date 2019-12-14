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
	private ${table.mapperName} ${table.mapperName?uncap_first};

    public ResultData<?> findPageList(${table.qoName} ${table.qoName?uncap_first}) {
        PageHelper.startPage(${table.qoName?uncap_first}.getPage(), ${table.qoName?uncap_first}.getSize());
        Example example = new Example(${entity}Entity.class);
        Example.Criteria criteria = example.createCriteria();
        List<${entity}Entity> entities = ${table.mapperName?uncap_first}.selectByExample(example);
            PageInfo<${entity}Entity> pageInfo = new PageInfo<>(entities);
                long total = pageInfo.getTotal();
                if (total != 0) {
                List<${table.voName}> result = pageInfo.getList().stream().map(entity ->
                    ${table.voName}.builder()
                    <#list table.fields as field>
                    <#if field.propertyType == "Date">
                    .${field.propertyName}Str(DateUtils.localDateTimeFormat(entity.get${field.propertyName?cap_first}(), "yyyy-MM-dd HH:mm:ss"))
                    <#elseif field.propertyName == "remove">
                    <#else>
                    .${field.propertyName}(entity.get${field.propertyName?cap_first}())
                    </#if>
                    </#list>
                    .build()).collect(Collectors.toList());
                return ResultData.success(ListViewData.<${table.voName}>builder().total(total).list(result).build());
             }
        return ResultData.success(ListViewData.<${table.voName}>builder().total(0).list(Lists.newArrayList()).build());
    }


    @Transactional(rollbackFor = Exception.class)
    public ResultData saveOrUpdate(${table.qoName} ${table.qoName?uncap_first}) {
        ${entity}Entity ${entity?uncap_first}Entity;
        if (StringUtils.isBlank(${table.qoName?uncap_first}.getId())) {
            //新增
            ${entity?uncap_first}Entity = new ${entity}Entity();
            this.setAddOrUpdateAttributes(${entity?uncap_first}Entity, ${table.qoName?uncap_first});
            ${table.mapperName?uncap_first}.insert(${entity?uncap_first}Entity);
        } else {
            //修改
            ${entity?uncap_first}Entity = ${table.mapperName?uncap_first}.selectByPrimaryKey(${table.qoName?uncap_first}.getId());
            if(Objects.isNull(${entity?uncap_first}Entity)){
                return ResultData.failMsg("【" + ${table.qoName?uncap_first}.getId() + "】不存在");
            }
            this.setAddOrUpdateAttributes(${entity?uncap_first}Entity, ${table.qoName?uncap_first});
            ${table.mapperName?uncap_first}.updateByPrimaryKey(${entity?uncap_first}Entity);
        }
        return ResultData.success("保存成功");
    }

    private void setAddOrUpdateAttributes(${entity}Entity ${entity?uncap_first}Entity, ${table.qoName} ${table.qoName?uncap_first}) {
        <#list table.fields as field>
        <#if field.propertyName == "remove" || field.propertyType == "Date" || field.propertyName == "id">
        <#else>
            ${entity?uncap_first}Entity.set${field.propertyName?cap_first}(${table.qoName?uncap_first}.get${field.propertyName?cap_first}());
        </#if>
        </#list>
    }

    @Transactional(rollbackFor = Exception.class)
    public ResultData deleteById(Integer id) {
        try {
            if(Objects.isNull(id)){
                return ResultData.failMsg("id不能为空");
            }
            ${table.mapperName?uncap_first}.deleteByPrimaryKey(id);
            return ResultData.success("删除成功");
        } catch (Exception e) {
            log.error("删除异常", e);
            return ResultData.failMsg("删除异常");
        }
    }

    public ResultData getDetailById(Integer id) {
        if(Objects.isNull(id)){
            return ResultData.fail("Id不能为空");
        }
        ${entity}Entity entity = ${table.mapperName?uncap_first}.selectByPrimaryKey(id);
        return ResultData.success(${entity}Response.builder()
                <#list table.fields as field>
                <#if field.propertyType == "Date">
                .${field.propertyName}Str(DateUtils.localDateTimeFormat(entity.get${field.propertyName?cap_first}(), "yyyy-MM-dd HH:mm:ss"))
                <#elseif field.propertyName == "remove">
                <#else>
                .${field.propertyName}(entity.get${field.propertyName?cap_first}())
                </#if>
                </#list>
                .build());
    }

    public ResultData exist(String name, Integer id){
        if(StringUtils.isBlank(name)){
            return ResultData.failMsg("名称不能为空");
        }
        Example example = new Example(${entity}Entity.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("name", name);
        if(Objects.nonNull(id)){
            criteria.andNotEqualTo("id", id);
        }
        List<${entity}Entity> entities = ${table.mapperName?uncap_first}.selectByExample(example);
            return entities.size() > 0
            ? ResultData.failMsg("名称已存在")
            : ResultData.success("名称可用");
    }

}
